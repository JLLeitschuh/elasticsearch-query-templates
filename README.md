Query templating plugin for ElasticSearch
==================================

Installation
------------

For latest master installation clone the repository, run a simple 

    mvn package

    mkdir $ES_DIR/plugins/elasticsearch-query-templates
    cp target/releases/elasticsearch-query-templates-1.0-SNAPSHOT.zip $ES_DIR/plugins/elasticsearch-query-templates

    unzip $ES_DIR/plugins/elasticsearch-query-templates/elasticsearch-query-templates.zip


Usage
-----

In the simplest case, submit both, template_string and template_vars as part of
your search request:


```json
GET _search
{
    "query": {
        "template": {
            "template_string": "{\"match_{{template}}\": {}}\"",
            "template_vars" : {
                "template" : "all"
            }
        }
    }
}
```

You register a template by name like so:

TBD


You execute a registered template with custom parameters like so:

TBD


Template language
-----------------

Templating is based on Mustache. Some simple usage examples:

Substitution of tokens:

```json
            "template_string": "{\"match_{{template}}\": {}}\"",
            "template_vars" : {
                "template" : "all"
``` 




License
-------

Licensed to Elasticsearch under one or more contributor
license agreements. See the NOTICE file distributed with
this work for additional information regarding copyright
ownership. Elasticsearch licenses this file to you under
the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.



