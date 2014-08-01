package de.tud.plt.r43ples.test;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.http.HttpException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.tud.plt.r43ples.management.Config;
import de.tud.plt.r43ples.management.ResourceManagement;
import de.tud.plt.r43ples.management.RevisionManagement;
import de.tud.plt.r43ples.management.TripleStoreInterface;


public class TestRevisionManagment {

	@Before
	public void setUp() throws HttpException, IOException, ConfigurationException{
		Config.readConfig("r43ples.conf");
		TripleStoreInterface.init(Config.sparql_endpoint, Config.sparql_user, Config.sparql_password);
		RevisionManagement.putGraphUnderVersionControl("test1234");
		RevisionManagement.putGraphUnderVersionControl("test_dataset_user");
	}
	
	@After
	public void tearDown() throws HttpException, IOException{
		RevisionManagement.purgeGraph("test1234");
		RevisionManagement.purgeGraph("test_dataset_user");
	}
	
	
	@Test
	public void testR43ples() throws HttpException, IOException {
		String revNumberMaster = RevisionManagement.getMasterRevisionNumber("test1234");
		Assert.assertEquals("0", revNumberMaster);
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("0");
		
		RevisionManagement.createNewRevision("test1234", "<a> <b> <c>.", "", "test", "test commit message", list);
		
		revNumberMaster = RevisionManagement.getMasterRevisionNumber("test1234");
		Assert.assertEquals("1", revNumberMaster);
		
	}
	
	@Test
	public void testR43ples_user() throws HttpException, IOException {
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("0");
		RevisionManagement.createNewRevision("test_dataset_user", 
				ResourceManagement.getContentFromResource("test-delta-added-1.nt"), 
				ResourceManagement.getContentFromResource("test-delta-removed-1.nt"),
				"test_user", "test commit message 1", list);

		String revNumberMaster = RevisionManagement.getMasterRevisionNumber("test_dataset_user");
		Assert.assertEquals("1", revNumberMaster);
		
		
		list.remove("0");
		list.add("1");
		RevisionManagement.createNewRevision("test_dataset_user", 
				ResourceManagement.getContentFromResource("test-delta-added-2.nt"), 
				ResourceManagement.getContentFromResource("test-delta-removed-2.nt"),
				"test_user", "test commit message 2", list);		
		revNumberMaster = RevisionManagement.getMasterRevisionNumber("test_dataset_user");
		Assert.assertEquals("2", revNumberMaster);
		
		
		list.remove("1");
		list.add("2");
		RevisionManagement.createNewRevision("test_dataset_user", 
				ResourceManagement.getContentFromResource("test-delta-added-3.nt"), 
				ResourceManagement.getContentFromResource("test-delta-removed-3.nt"),
				"test_user", "test commit message 3", list);		
		revNumberMaster = RevisionManagement.getMasterRevisionNumber("test_dataset_user");
		Assert.assertEquals("3", revNumberMaster);
		
		
		list.remove("2");
		list.add("3");
		RevisionManagement.createNewRevision("test_dataset_user", 
				ResourceManagement.getContentFromResource("test-delta-added-4.nt"), 
				ResourceManagement.getContentFromResource("test-delta-removed-4.nt"),
				"test_user", "test commit message 4", list);
		revNumberMaster = RevisionManagement.getMasterRevisionNumber("test_dataset_user");
		Assert.assertEquals("4", revNumberMaster);
	}
	
}
