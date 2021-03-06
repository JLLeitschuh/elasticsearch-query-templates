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
package org.elasticsearch.index.query;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.search.Query;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.ScriptService;

/**
 * In the simplest case, parse template string and variables from the request, compile the template and
 * execute the template against the given variables.
 *
 * TODO support named templates
 * */
public class TemplateQueryParser implements QueryParser {

    /** Name to reference this type of query. */
    public static final String NAME = "template";
    /** Name of query parameter containing the template string. */
    public static final String STRING = "template_string";
    /** Name of query parameter containing the template parameters. */
    public static final String VARS = "template_vars";
    /** This is what we are registered with for query executions. */
    private final ScriptService scriptService;

    /**
     * @param scriptService will automatically be wired by Guice
     * */
    @Inject
    public TemplateQueryParser(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    /**
     * @return a list of names this query is registered under.
     * */
    @Override
    public String[] names() {
        return new String[] {NAME};
    }

    @Override
    @Nullable
    public Query parse(QueryParseContext parseContext) throws IOException {
        XContentParser parser = parseContext.parser();

        String template = "";
        Map<String, Object> vars = new HashMap<String, Object>();

        String currentFieldName = null;
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (STRING.equals(currentFieldName)) {
                template = (String) parser.objectText();
            } else if (VARS.equals(currentFieldName)) {
                XContentParser.Token innerToken;
                String key = "";
                while ((innerToken = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                    // parsing template parameter map
                    if (innerToken == XContentParser.Token.FIELD_NAME) {
                        key = parser.currentName();
                    } else {
                        vars.put(key, parser.objectText());
                        key = "";
                    }
                }
            }
        }

        ExecutableScript executable = this.scriptService.executable("mustache", template, vars);
        String querySource = (String) executable.run();

        XContentParser qSourceParser = XContentFactory.xContent(querySource).createParser(querySource);
        try {
            final QueryParseContext context = new QueryParseContext(parseContext.index(), parseContext.indexQueryParser);
            context.reset(qSourceParser);
            Query result = context.parseInnerQuery();
            parser.nextToken();
            return result;
        } finally {
            qSourceParser.close();
        }
    }
}
