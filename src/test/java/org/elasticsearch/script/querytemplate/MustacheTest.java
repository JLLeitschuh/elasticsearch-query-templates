package org.elasticsearch.script.querytemplate;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Test;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class MustacheTest {

    @Test
    public void test() {
        HashMap<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("boost_val", "0.2");

        String template = "GET _search {\"query\": " +
        		    "{\"boosting\": {" +
        		    "\"positive\": {\"match\": {\"body\": \"gift\"}}," +
        		    "\"negative\": {\"term\": {\"body\": {\"value\": \"solr\"}" +
        		    "}}, \"negative_boost\": {{boost_val}} } }}";
        MustacheFactory f = new DefaultMustacheFactory();
        Mustache mustache = f.compile(new StringReader(template), "example");
        StringWriter writer = new StringWriter();
        mustache.execute(writer, scopes);
        writer.flush();
        assertEquals(
                "Mustache templating broken", 
                "GET _search {\"query\": {\"boosting\": {\"positive\": {\"match\": {\"body\": \"gift\"}}," +
                "\"negative\": {\"term\": {\"body\": {\"value\": \"solr\"}}}, \"negative_boost\": 0.2 } }}",
                writer.toString());
    }
}
