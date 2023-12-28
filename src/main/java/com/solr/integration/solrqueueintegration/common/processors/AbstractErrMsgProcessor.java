package com.solr.integration.solrqueueintegration.common.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AbstractErrMsgProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> inputMsg = (Map<String, Object>) exchange.getIn().getBody();
        Map<String, Object> transformedErrMsg = new HashMap<>();
        transformedErrMsg.put("errorType", exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class).getClass().toString());
        transformedErrMsg.put("errorMsg", exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class).getMessage());
        transformedErrMsg.put("errorQueue", exchange.getIn().getHeader("queueType").toString());
        transformedErrMsg.put("errorDoc", inputMsg);
        exchange.getIn().setBody(transformedErrMsg);
    }
}
