package org.bsc;

import java.io.File;
import java.io.FileNotFoundException;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.GlobalObjectFactory;
import org.dynjs.runtime.Runner;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DynjsTest {

	  private  DynJS dynjs;
	  private Config config;

	  @Before
	  public void initialize() {
	    config = new Config();
	    
	    config.setInvokeDynamicEnabled(true);
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

	  @Test
	  public void runScriptFromClasspath() throws Exception {
		
		java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("dynjsapp.js");
		
	    final Runner runner = dynjs.newRunner();

	    Object result = runner.withSource( new java.io.InputStreamReader(is)).execute();
	    
	    Assert.assertThat( String.valueOf(result), IsEqual.equalTo("end"));
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

	  @Test
	  public void testOverride() throws Exception {
	
		  Object impl = dynjs.evaluate(  
				  "new org.bsc.AbstractFoo( {",
				  " implementMe: function(msg) { print( msg ); return msg; }",
				  "} );"
				  );
	      Assert.assertThat( impl, IsNull.notNullValue());
	      Assert.assertThat( impl, IsInstanceOf.instanceOf(AbstractFoo.class));
	      
	      String result = ((AbstractFoo) impl).implementMe("test");    
	      Assert.assertThat(result, IsEqual.equalTo("test"));
	  }

}
