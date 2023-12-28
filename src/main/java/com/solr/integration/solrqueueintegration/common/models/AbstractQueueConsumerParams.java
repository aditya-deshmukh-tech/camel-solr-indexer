package com.solr.integration.solrqueueintegration.common.models;

import java.util.Objects;

public record AbstractQueueConsumerParams(String deadLetterRouteString, String queueString, String queueType, boolean autoStartState) {

    public AbstractQueueConsumerParams {
        Objects.requireNonNull(deadLetterRouteString);
        Objects.requireNonNull(queueString);
        Objects.requireNonNull(queueType);
        Objects.requireNonNull(autoStartState);
    }
}
