package org.bsc.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObjectFactory;
import org.dynjs.runtime.Runner;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.furnace.util.OperatingSystemUtils;

/**
 * 
 * @author softphone
 *
 */
public abstract class AbstractBaseDynjsUICommand extends AbstractProjectCommand {
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	protected Manifest getManifest() throws IOException {
		return new Manifest(getClass().getClassLoader().getResourceAsStream("MANIFEST.MF"));
	}
	
	/**
	 * 
	 * @param mf
	 * @return
	 * @throws IOException
	 */
	protected String getVersion( final Manifest mf ) throws IOException {

		final String version = mf.getMainAttributes().getValue("version");

		return version;
	}

	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	protected final java.io.File getAssetDir( final Manifest mf ) throws IOException {
		
		final java.io.File forgeDir =  OperatingSystemUtils.getUserForgeDir();
		
		final java.io.File result = new java.io.File( forgeDir, String.format("dynjs/%s", getVersion(mf)));
		
		if( !result.exists() ) {
			if( !result.mkdirs() ) {
			
				throw new IOException( String.format("error creating dynjs asset dir [%s]", result));
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param w
	 * @throws IOException 
	 */
	protected final void copyResourceToAssetDir( final String resourceName, final Manifest mf  ) throws IOException {
		
		final java.net.URL source = getClass().getClassLoader().getResource(resourceName);
		if( source == null ) {
			throw new FileNotFoundException( String.format("resource [%s] not found in classloader!", source));
		}
		
		final java.io.File target = new java.io.File( getAssetDir(mf), resourceName );

		if( !target.exists() || getVersion(mf).endsWith("SNAPSHOT")) {
			FileUtils.copyURLToFile(source, target);
		}
	}
	
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

		final java.io.File assetDir = getAssetDir(getManifest());

		dynjs.execute( new StringBuilder()
			.append("require.addLoadPath('").append(assetDir.getPath()).append("');")
			.toString()	
			);
		
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

		final java.io.File assetDir = getAssetDir(getManifest());
		final String folder = file.getParent();
		if (folder != null) {
			dynjs.execute( new StringBuilder()
								.append("__basedir = '").append(folder).append("';")
								.append("require.addLoadPath(__basedir);")
								.append("require.addLoadPath('").append(assetDir.getPath()).append("');")
								.toString()
								);
		}

		final Object result = runner.withSource(file).execute();

		return result;

	}

}
