package yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.gozo.funkysanta.rest.client.model.WSConfiguration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlResolver {

	public void whenLoadYAMLDocumentWithTopLevelClass_thenLoadCorrectJavaObjectWithNestedObjects() {

		Yaml yaml = new Yaml(new Constructor(Customer.class));
		String content = read("/config.yaml");
		//InputStream inputStream = this.getClass().getResourceAsStream("/customer_details.yaml");
		// Customer customer = yaml.load(inputStream);

		WSConfiguration instance = new Yaml().loadAs(content, WSConfiguration.class);

		System.out.println(instance.getMeta().getLocator());
	}
	
	public static void main(String[] args) {
		YamlResolver yamlResolver = new YamlResolver();
		yamlResolver.whenLoadYAMLDocumentWithTopLevelClass_thenLoadCorrectJavaObjectWithNestedObjects();
	}
	
	public String read(String location) {
		try {
			InputStream is;
			URL url = getClass().getResource(location);
			is = url.openConnection().getInputStream();
			return readContent(is);
		} catch (IOException e) {
			// put a warning here
			// log the warning
			// stop the processing
			// service cannot be written without its configuration
			// ignore this configuration
			e.printStackTrace();
		}
		// throw an exception here
		return null;
	}
	
	public String readContent(InputStream is) {
		String content = null;
		try {
			content = IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}
}
