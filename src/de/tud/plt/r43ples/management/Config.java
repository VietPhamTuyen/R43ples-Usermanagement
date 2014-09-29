package de.tud.plt.r43ples.management;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class Config {
	
	public static String service_uri;
	public static int service_port;
	public static String sparql_endpoint;
	public static String sparql_user;
	public static String sparql_password;
	public static String yed_filepath;
	public static String visualisation_path;
	public static String revision_graph;
	
	private static Logger logger = Logger.getLogger(Config.class);

	
	public static void readDefaultConfig() throws ConfigurationException {
		readConfig("r43ples.conf");
	}
	
	/**
	 * read the configuration information from local file
	 * @param configFilePath path to config file
	 * @throws ConfigurationException
	 */
	public static void readConfig(final String configFilePath) throws ConfigurationException{
		
		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration(configFilePath);
			service_uri = config.getString("service.uri");
			service_port = config.getInt("service.port");
			
			sparql_endpoint = config.getString("sparql.endpoint");
			sparql_user = config.getString("sparql.username");
			sparql_password = config.getString("sparql.password");
			
			revision_graph = config.getString("revision.graph");
			
			yed_filepath = config.getString("yEd.filePath");
			visualisation_path = config.getString("visualisation.path");
		} catch (ConfigurationException e) {
			logger.warn("Could not read configuration file '" + configFilePath + "'. Switch to 'r43ples.dist.conf'.");
			readConfig("r43ples.dist.conf");
		}
		
	}

}