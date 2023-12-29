package com.solr.integration.solrqueueintegration.common.components;

import com.solr.integration.solrqueueintegration.common.enumfields.JsonInputDataEnum;
import com.solr.integration.solrqueueintegration.common.enumfields.JsonTransformEnum;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConvertToSolrInputDocument {

    public SolrInputDocument getSid(Map<String, Object> data) {
        SolrInputDocument sid = new SolrInputDocument();
        sid.addField(JsonTransformEnum.ID.field, getUniqueId(data, JsonInputDataEnum.TITLE_MED.field));
        sid.addField(JsonTransformEnum.MED_NAME.field, getString(data, JsonInputDataEnum.TITLE_MED.field));
        sid.addField(JsonTransformEnum.MED_DESC.field, getString(data, JsonInputDataEnum.DESC_MED.field));
        sid.addField(JsonTransformEnum.MED_CONTENTS.field, getStringsArray(data, JsonInputDataEnum.COMP_MED.field));
        sid.addField(JsonTransformEnum.MED_IMG.field, getString(data, JsonInputDataEnum.IMG_URL_MED.field));
        sid.addField(JsonTransformEnum.MED_SYMPTOMS.field, getStringsArray(data, JsonInputDataEnum.SYMPTOMS_MED.field));
        sid.addField(JsonTransformEnum.MED_URL.field, getString(data, JsonInputDataEnum.URL_MED.field));
        return sid;
    }

    private String getString(Map<String, Object> data, String inputString) {
        if (data.containsKey(inputString)) {
            return (String) data.get(inputString);
        } else {
            throw new IllegalArgumentException("no valid [" + inputString + "] key present in input data");
        }
    }

    private String[] getStringsArray(Map<String, Object> data, String inputString) {
        if (data.containsKey(inputString)) {
            if (data.get(inputString).toString().contains("+")) {
                return data.get(inputString).toString().split("\\+");
            } else {
                return data.get(inputString).toString().split(",");
            }
        } else {
            throw new IllegalArgumentException("no valid [" + inputString + "] key present in input data");
        }
    }

    private String getUniqueId(Map<String, Object> data, String medName) {
        return data.get(medName).toString();
    }
}
