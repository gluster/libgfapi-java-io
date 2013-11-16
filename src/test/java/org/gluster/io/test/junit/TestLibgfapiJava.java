package org.gluster.io.test.junit;

import org.gluster.fs.GlusterClient;
import org.gluster.fs.GlusterFile;
import org.gluster.fs.GlusterVolume;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public class TestLibgfapiJava {
	
	protected static GlusterVolume vol = null;
	
	@BeforeClass
	 public static void setUp() throws Exception {
		GlusterClient cl = new GlusterClient();
		vol = cl.connect(System.getProperty("GLUSTER_TEST_VOLUME","gv0"));
		vol.setLogging("/dev/stdout", 7);
	  }
	  
	@Before
	  public void init() {
		 getTestBase().mkdirs();
	  }
	
	  
	  @After
	  public void tearDown() throws Exception {
		 getTestBase().delete();
	  }

	  
	  public GlusterFile getTestBase(){
		  return vol.open("/test");
	  }
}
