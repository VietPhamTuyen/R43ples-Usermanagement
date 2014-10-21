package de.tud.plt.r43ples.develop;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.http.HttpException;

import de.tud.plt.r43ples.management.Config;
import de.tud.plt.r43ples.management.SampleDataSet;
import de.tud.plt.r43ples.management.TripleStoreInterface;

/**
 * Create an example graph of the following structure:
 * 
 *                  ADD: D,E              ADD: G
 *               +-----X---------------------X--------- (Branch B1)
 *               |  DEL: A                DEL: D
 * ADD: A,B,C    |
 * ---X----------+ (Master)
 * DEL: -        |
 *               |  ADD: D,H              ADD: I
 *               +-----X---------------------X--------- (Branch B2)
 *                  DEL: C                DEL: -
 * 
 * 
 * @author Stephan Hensel
 *
 */
public class CreateExampleGraph {

	/** The graph name. **/
	private static String graphName = "http://exampleGraph";
	
	
	/**
	 * Main entry point. Create the example graph.
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws ConfigurationException 
	 * @throws HttpException 
	 */
	public static void main(String[] args) throws IOException, ConfigurationException, HttpException {
		
		Config.readConfig("r43ples.conf");
		TripleStoreInterface.init(Config.sparql_endpoint, Config.sparql_user, Config.sparql_password);
		
		SampleDataSet.createSampleDataSetMerging(graphName);
	}

}
