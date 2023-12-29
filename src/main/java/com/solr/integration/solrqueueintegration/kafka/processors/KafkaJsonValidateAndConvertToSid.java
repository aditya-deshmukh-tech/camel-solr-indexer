package com.solr.integration.solrqueueintegration.kafka.processors;

import com.solr.integration.solrqueueintegration.common.components.ConvertToSolrInputDocument;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaJsonValidateAndConvertToSid implements Processor {

    @Autowired
    private ConvertToSolrInputDocument convertToSolrInputDocument;

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> inputData = (Map<String, Object>) exchange.getIn().getBody();
        SolrInputDocument sid = convertToSolrInputDocument.getSid(inputData);
        exchange.getIn().setBody(sid);
    }
}
