package org.elasticsearch.script.querytemplate;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.script.querytemplate.QueryTemplateEngineService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QueryTemplateEngineServiceTest {
    private QueryTemplateEngineService qe;
    
    private static String TEMPLATE = "GET _search {\"query\": " + "{\"boosting\": {" + "\"positive\": {\"match\": {\"body\": \"gift\"}},"
            + "\"negative\": {\"term\": {\"body\": {\"value\": \"solr\"}" + "}}, \"negative_boost\": {{boost_val}} } }}";

    @Before
    public void setup() {
        qe = new QueryTemplateEngineService(ImmutableSettings.Builder.EMPTY_SETTINGS);
    }

    @After
    public void close() {
        qe.close();
    }

    @Test
    public void testSimpleParameterReplace() {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("boost_val", "0.3");
        Object o = qe.execute(qe.compile(TEMPLATE), vars);
        assertEquals("GET _search {\"query\": {\"boosting\": {\"positive\": {\"match\": {\"body\": \"gift\"}},"
                + "\"negative\": {\"term\": {\"body\": {\"value\": \"solr\"}}}, \"negative_boost\": 0.3 } }}", ((String) o));
    }

}
