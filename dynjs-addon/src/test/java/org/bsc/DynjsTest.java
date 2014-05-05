package org.bsc;

import java.io.File;
import java.io.FileNotFoundException;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.GlobalObjectFactory;
import org.dynjs.runtime.Runner;
import org.junit.Before;
import org.junit.Test;

public class DynjsTest {

	  private  DynJS dynjs;
	  private Config config;

	  @Before
	  public void initialize() {
	    config = new Config();
	    
	    
		config.setGlobalObjectFactory( new GlobalObjectFactory() {
			
			@Override
			public GlobalObject newGlobalObject(DynJS runtime) {
				return new GlobalObject(runtime) {{
					
					defineReadOnlyGlobalProperty("command", DynjsTest.this);
				}};
			}
		});

	    config.setDebug(true);
	    dynjs  = new DynJS(config);
	   
	    
	  }

	  private Object runScript(String fileName) throws FileNotFoundException {
		  
		final File baseDir =  new File("src/test/resources");
		final File file = new File( baseDir, fileName);

		dynjs.execute( String.format("require.addLoadPath('%s')", baseDir.getPath()) );
	    
		
	    final Runner runner = dynjs.newRunner();

	    return runner.withSource(file).execute();
	    
	    
	  }

	  public Object eval(String code) {
	    return dynjs.evaluate(code);
	  }

	  
	  @Test
	  public void testRunScript() throws Exception {
	      
	      runScript( "dynjsapp.js");
	            
	  }

}