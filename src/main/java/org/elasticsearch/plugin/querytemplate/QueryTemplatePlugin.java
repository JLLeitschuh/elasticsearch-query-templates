package org.elasticsearch.plugin.querytemplate;

import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.script.ScriptModule;
import org.elasticsearch.script.querytemplate.QueryTemplateEngineService;

/**
 * Plugin to enable referencing query templates and parameters.
 * 
 * This class is also referenced in the plugin configuration - make sure
 * you change the class name in src/main/resources/es.plugin.properties
 * when refactoring this class to a different name.
 */
public class QueryTemplatePlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "query-template";
    }

    @Override
    public String description() {
        return "Query template plugin allowing to add reference queries by template name";
    }

    public void onModule(ScriptModule module) {
        module.addScriptEngine(QueryTemplateEngineService.class);
    }
}
