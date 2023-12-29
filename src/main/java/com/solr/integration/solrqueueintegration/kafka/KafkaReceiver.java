package com.solr.integration.solrqueueintegration.kafka;

import com.solr.integration.solrqueueintegration.common.AbstractQueueConsumerRoute;
import com.solr.integration.solrqueueintegration.common.models.AbstractQueueConsumerParams;
import com.solr.integration.solrqueueintegration.common.processors.AbstractErrMsgProcessor;
import com.solr.integration.solrqueueintegration.kafka.processors.KafkaJsonValidateAndConvertToSid;
import org.apache.camel.Processor;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class KafkaReceiver extends AbstractQueueConsumerRoute {

    @Autowired
    private AbstractErrMsgProcessor abstractErrMsgProcessor;

    @Autowired
    private KafkaJsonValidateAndConvertToSid kafkaJsonValidateAndConvertToSid;

    @Value("${kafka.url}")
    private String kafkaUrl;

    @Override
    public Class<? extends Throwable>[] getArrayOfRecoverableExceptions() {
        return new Class[]{KafkaException.class};
    }

    @Override
    public Class<? extends Throwable>[] getArrayOfIrrecoverableExceptions() {
        return new Class[]{IllegalArgumentException.class};
    }

    @Override
    public AbstractQueueConsumerParams getAbstractQueueConsumerParams() {
        return new AbstractQueueConsumerParams("direct:exceptionLog", "kafka:medicines?brokers=" + kafkaUrl, "kafka", false);
    }

    @Override
    public Processor getValidationAndConvertToSidProcessor() {
        return kafkaJsonValidateAndConvertToSid;
    }

    @Override
    public Processor getErrorMsgToDeadLetterMsgProcessor() {
        return abstractErrMsgProcessor;
    }
}
