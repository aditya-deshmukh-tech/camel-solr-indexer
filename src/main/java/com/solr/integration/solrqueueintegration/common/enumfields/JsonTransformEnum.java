package com.solr.integration.solrqueueintegration.common.enumfields;

public enum JsonTransformEnum {
    ID("id"),
    MED_NAME("med_name"),
    MED_DESC("med_desc"),
    MED_CONTENTS("med_contents"),
    MED_IMG("med_img"),
    MED_SYMPTOMS("med_symptoms"),
    MED_URL("med_url");

    public String field;

    JsonTransformEnum(String field) {
        this.field = field;
    }
}
