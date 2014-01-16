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
package org.elasticsearch.plugin.querytemplate;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.TemplateQueryParser;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.indices.query.IndicesQueriesRegistry;

/**
 * Make sure to register the query. Index, Settings and IndicesQueriesRegistry are injected automaticalls.
 * */
public class RegisterTemplateQueryParser extends AbstractIndexComponent {

    /**
     * @param index wired by Guice
     * @param indexSettings wired by Guice
     * @param indicesQueriesRegistry wired by Guice, needed to register the query parser
     * @param parser wired by Guice, this is the query parser to register
     * */
    @Inject
    public RegisterTemplateQueryParser(Index index,
            @IndexSettings Settings indexSettings,
            IndicesQueriesRegistry indicesQueriesRegistry,
            TemplateQueryParser parser) {
        super(index, indexSettings);
        indicesQueriesRegistry.addQueryParser(parser);
    }
}
