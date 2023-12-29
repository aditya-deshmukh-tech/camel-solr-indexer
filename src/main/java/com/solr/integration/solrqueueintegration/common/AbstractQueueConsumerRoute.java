package com.solr.integration.solrqueueintegration.common;

import com.solr.integration.solrqueueintegration.common.models.AbstractQueueConsumerParams;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.solr.SolrConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;


public abstract class AbstractQueueConsumerRoute extends RouteBuilder {

    public abstract Class<? extends Throwable>[] getArrayOfRecoverableExceptions();

    public abstract Class<? extends Throwable>[] getArrayOfIrrecoverableExceptions();

    public abstract AbstractQueueConsumerParams getAbstractQueueConsumerParams();

    public abstract Processor getValidationAndConvertToSidProcessor();

    public abstract Processor getErrorMsgToDeadLetterMsgProcessor();

    @Value("${solr.url}")
    private String solrUrl;

    @Override
    public void configure() throws Exception {

        onException(getArrayOfIrrecoverableExceptions())// can add multiple exceptions which cannot be recover
                .handled(true)
                .useOriginalMessage()
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader("queueType").constant(getAbstractQueueConsumerParams().queueType())
                .process(getErrorMsgToDeadLetterMsgProcessor())
                .to(getAbstractQueueConsumerParams().deadLetterRouteString());  // can be modified to pass to dead letter database

        onException(getArrayOfRecoverableExceptions())
                .handled(true)
                .maximumRedeliveries(3)
                .redeliveryDelay(3000)
                .logRetryAttempted(true)
                .log(LoggingLevel.ERROR, "retry attempts exhausted")
                .useOriginalMessage()
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader("queueType").constant(getAbstractQueueConsumerParams().queueType())
                .process(getErrorMsgToDeadLetterMsgProcessor())
                .to(getAbstractQueueConsumerParams().deadLetterRouteString());

        from(getAbstractQueueConsumerParams().queueString()).autoStartup(getAbstractQueueConsumerParams().autoStartState())
                .unmarshal().json(JsonLibrary.Jackson)
                .log("message before updating = ${body}")
                .process(exchange -> {
                    if (exchange.getIn().getBody() instanceof ArrayList) {
                        exchange.getIn().setHeader("list", true);
                    } else {
                        exchange.getIn().setHeader("list", false);
                    }
                })
                .to("direct:conditionalRoute")
                .log("all records indexed succesfully");


        from("direct:exceptionLog")
                .log("error body from exception = ${body}");

        from("direct:conditionalRoute")
                .choice()
                .when(simple("${header.list} == 'true'"))
                    .log("message is Objects list")
                    .to("direct:splitNIndex")
                .otherwise()
                    .to("direct:index")
                    .log("message is object")
                .end();

        from("direct:splitNIndex")
                .split(body())
                .to("direct:index")
                .end();

        from("direct:index")
                .process(getValidationAndConvertToSidProcessor())
                .setHeader(SolrConstants.COLLECTION, constant("medicines"))
                .setHeader(SolrConstants.OPERATION, constant(SolrConstants.OPERATION_INSERT))
                .to(solrUrl);

    }
}
