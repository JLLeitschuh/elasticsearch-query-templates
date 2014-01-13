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

import java.io.StringReader;
import java.io.StringWriter;

import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * Main entry point handling template registration, compilation and
 * execution.
 * 
 * TODO Major refactoring needed as approach to tackling template queries completely changed underway
 * */
public class TemplateEngine {

    public Object compile(String template) {
        MustacheFactory f = new DefaultMustacheFactory();
        return f.compile(new StringReader(template), "query-template");
    }

    public Object execute(Object template, Map<String, Object> vars) {
        StringWriter result = new StringWriter();
        ((Mustache) template).execute(result, vars);
        return result.toString();
    }
}
