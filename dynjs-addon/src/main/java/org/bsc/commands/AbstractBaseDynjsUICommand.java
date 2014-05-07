package org.bsc.commands;

import java.io.File;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObjectFactory;
import org.dynjs.runtime.Runner;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIContext;

public abstract class AbstractBaseDynjsUICommand extends AbstractProjectCommand {
	/**
	 * 
	 * @param js
	 * @return
	 * @throws Exception
	 */
	protected Object executeFromClasspath(UIContext ctx, final String resourceName, GlobalObjectFactory factory)
			throws Exception {

		final Config config = new Config();

		config.setGlobalObjectFactory(factory);
		config.setOutputStream(ctx.getProvider().getOutput().out());
		config.setErrorStream(ctx.getProvider().getOutput().err());

		final DynJS dynjs = new DynJS(config);

		final Runner runner = dynjs.newRunner();

		java.io.InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);

		if( is == null ) {
			throw new IllegalArgumentException( String.format("resource [%s] not found in classpath!", resourceName));
		}
		
		final Object result = runner.withSource( new java.io.InputStreamReader(is)).execute();

		return result;


	}
	
	/**
	 * 
	 * @param js
	 * @return
	 * @throws Exception
	 */
	protected Object executeFromFile(UIContext ctx, final FileResource<?> js, GlobalObjectFactory factory)
			throws Exception {

		final Config config = new Config();

		config.setGlobalObjectFactory(factory);
		config.setOutputStream(ctx.getProvider().getOutput().out());
		config.setErrorStream(ctx.getProvider().getOutput().err());

		final DynJS dynjs = new DynJS(config);

		final Runner runner = dynjs.newRunner();

		final File file = js.getUnderlyingResourceObject();

		final String folder = file.getParent();
		if (folder != null) {
			dynjs.execute(String.format("__basedir = '%s'; require.addLoadPath(__basedir);", folder));
		}

		final Object result = runner.withSource(file).execute();

		return result;

	}

}
