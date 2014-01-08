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

import java.io.StringReader;
import java.io.StringWriter;

import java.util.Map;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.ScriptEngineService;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * Main entry point handling template registration, compilation and
 * execution.
 * 
 * TODO Major refactoring needed as approach to tackling template queries completely changed underway
 * */
public class QueryTemplateEngineService extends AbstractComponent implements ScriptEngineService {

    public QueryTemplateEngineService(Settings settings) {
        super(settings);
    }

    @Override
    public void close() {
        // Nothing to do here.
    }

    @Override
    public Object compile(String template) {
        MustacheFactory f = new DefaultMustacheFactory();
        return f.compile(new StringReader(template), "query-template");
    }

    @Override
    public ExecutableScript executable(Object mustache, @Nullable Map<String, Object> vars) {
        return new MustacheExecutableScript((Mustache) mustache, vars);
    }

    @Override
    public Object execute(Object mustache, Map<String, Object> vars) {
        StringWriter result = new StringWriter();
        ((Mustache) mustache).execute(result, vars);
        return result.toString();
    }

    @Override
    public String[] extensions() {
        return new String[]{"mustache"};
    }

    @Override
    public SearchScript search(Object arg0, SearchLookup arg1, @Nullable Map<String, Object> arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] types() {
        return new String[]{"mustache", "mustache"};
    }

    @Override
    public Object unwrap(Object arg) {
        return arg;
    }

    private class MustacheExecutableScript implements ExecutableScript {
        private Mustache mustache;
        private Map<String, Object> vars;

        public MustacheExecutableScript(Mustache mustache, Map<String, Object> vars) {
            this.mustache = mustache;
            this.vars = vars;
        }

        @Override
        public void setNextVar(String name, Object value) {
            this.vars.put(name, value);
        }

        @Override
        public Object run() {
            StringWriter result = new StringWriter();
            ((Mustache) mustache).execute(result, vars);
            return result.toString();
        }

        @Override
        public Object unwrap(Object value) {
            return value;
        }
    }
}
