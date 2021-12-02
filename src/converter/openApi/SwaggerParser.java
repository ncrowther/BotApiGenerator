package converter.openApi;
/*
import java.util.HashSet;
import java.util.Map;

import com.fasterxml.jackson.databind.module.SimpleModule;

import io.swagger.inflector.examples.*;
import io.swagger.inflector.examples.models.Example;
import io.swagger.inflector.processors.JsonNodeExampleSerializer;
import io.swagger.models.*;
import io.swagger.util.Json;
import io.swagger.util.Yaml;

public class SwaggerParser {

	public static void main(String args[]) {
		// Load your OpenAPI/Swagger definition
		Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");

		// Create an Example object for the Pet model
		Map<String, Model> definitions = swagger.getDefinitions();
		Model pet = definitions.get("Pet");
		Example example = ExampleBuilder.fromModel("Pet", pet, definitions, new HashSet<String>());
		// Another way:
		// Example example = ExampleBuilder.fromProperty(new RefProperty("Pet"),
		// swagger.getDefinitions());

		// Configure example serializers
		SimpleModule simpleModule = new SimpleModule().addSerializer(new JsonNodeExampleSerializer());
		Json.mapper().registerModule(simpleModule);
		Yaml.mapper().registerModule(simpleModule);

		// Convert the Example object to string

		// JSON example
		String jsonExample = Json.pretty(example);
		System.out.println(jsonExample);

		// YAML example
		String yamlExample = Yaml.pretty().writeValueAsString(example);
		System.out.println(yamlExample);

		// XML example (TODO: pretty-print it)
		String xmlExample = new XmlExampleSerializer().serialize(example);
		System.out.println(xmlExample);

	}
}
*/