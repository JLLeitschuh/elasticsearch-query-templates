/**
 *
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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
package org.elasticsearch.script.querytemplate;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BaseQueryBuilder;

public class TemplateQueryBuilder extends BaseQueryBuilder {

    private Map<String, Object> vars;
    private String template;
    
    public TemplateQueryBuilder(String template, Map<String, Object> vars) {
        this.template = template;
        this.vars = vars;
    }

    @Override
    protected void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.startObject(TemplateQueryParser.NAME);
        builder.field("template_string", template);
        builder.field("template_vars", vars);
        builder.endObject();
        builder.endObject();
        builder.close();
    }

}
