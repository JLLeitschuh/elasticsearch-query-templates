/**
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.index.query.template;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.network.NetworkUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.TemplateQueryBuilder;
import org.elasticsearch.index.query.TemplateQueryParser;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Integration test for template based queries: Go from templated search request to search response.
 * */
public class QueryTemplateTest {

    protected final ESLogger logger = Loggers.getLogger(getClass());

    private Node node;

    private Client client;

    @Before
    public void createNodes() throws Exception {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("index.queryparser.query.template.type", TemplateQueryParser.class)
                .put("index.cache.filter.type", "none")
                .put("cluster.name", "test-cluster-" + NetworkUtils.getLocalAddress())
                .put("gateway.type", "none")
                .put("number_of_shards", 1)
                .build();


        node = NodeBuilder.nodeBuilder().settings(settings).node();
        client = node.client();
    }

    @After
    public void closeNodes() {
        if (client != null) client.close();
        if (node != null) node.close();
    }

    @Test
    //TODO disabled for now - does not work yet
    public void testTemplateQueryExecution() throws Exception {
        client.admin().indices().prepareCreate("test").execute().actionGet();
        client.prepareIndex("test", "type1", "1")
                .setSource(jsonBuilder().startObject().field("test", "value beck").field("num1", 1.0f).endObject())
                .execute().actionGet();
        client.admin().indices().prepareFlush().execute().actionGet();
        client.prepareIndex("test", "type1", "2")
                .setSource(jsonBuilder().startObject().field("test", "value beck").field("num1", 2.0f).endObject())
                .execute().actionGet();
        client.admin().indices().prepareFlush().execute().actionGet();
        client.prepareIndex("test", "type1", "3")
                .setSource(jsonBuilder().startObject().field("test", "value beck").field("num1", 3.0f).endObject())
                .execute().actionGet();
        
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("template", "beck");
        
        TemplateQueryBuilder builder = new TemplateQueryBuilder("{\"match_{{template}}\": {}}\"", vars);
        SearchResponse sr = client.prepareSearch()
                .setQuery(builder)
                .execute()
                .actionGet();
        // TODO check search response
    }
}
