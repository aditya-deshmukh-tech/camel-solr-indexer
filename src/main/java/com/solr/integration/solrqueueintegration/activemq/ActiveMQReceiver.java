package com.solr.integration.solrqueueintegration.activemq;

import com.solr.integration.solrqueueintegration.activemq.processors.ActiveMQJsonValidateAndConvertToSid;
import com.solr.integration.solrqueueintegration.common.AbstractQueueConsumerRoute;
import com.solr.integration.solrqueueintegration.common.models.AbstractQueueConsumerParams;
import com.solr.integration.solrqueueintegration.common.processors.AbstractErrMsgProcessor;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Component
public class ActiveMQReceiver extends AbstractQueueConsumerRoute {

    @Autowired
    private ActiveMQJsonValidateAndConvertToSid activeMQJsonValidateAndConvertToSid;

    @Autowired
    private AbstractErrMsgProcessor abstractErrMsgProcessor;

    @Override
    public AbstractQueueConsumerParams getAbstractQueueConsumerParams() {
        return new AbstractQueueConsumerParams("direct:exceptionLog", "activemq:medicines", "activeMQ", true);
    }

    @Override
    public Class<? extends Throwable>[] getArrayOfRecoverableExceptions() {
        return new Class[]{TimeoutException.class};
    }

    @Override
    public Class<? extends Throwable>[] getArrayOfIrrecoverableExceptions() {
        return new Class[]{IllegalArgumentException.class};
    }

    @Override
    public Processor getValidationAndConvertToSidProcessor() {
        return activeMQJsonValidateAndConvertToSid;
    }

    @Override
    public Processor getErrorMsgToDeadLetterMsgProcessor() {
        return abstractErrMsgProcessor;
    }
}
