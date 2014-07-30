package org.bsc.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.jar.Manifest;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bsc.functional.Functional.Fn;
import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObjectFactory;
import org.dynjs.runtime.Runner;
import org.jboss.forge.addon.dependencies.DependencyResolver;
import org.jboss.forge.addon.environment.Environment;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.furnace.manager.maven.MavenContainer;
import org.jboss.forge.furnace.util.OperatingSystemUtils;

/**
 * 
 * @author softphone
 *
 */
public abstract class AbstractBaseDynjsUICommand extends AbstractProjectCommand implements AddonConstants {
	
	@Inject
	private MavenContainer container;

	@Inject
	private Environment environment;

	@Inject
	protected DependencyResolver dependencyResolver;
	
	public MavenContainer getContainer() {
		return container;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public DependencyResolver getDependencyResolver() {
		return dependencyResolver;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @see http://stackoverflow.com/questions/1272648/reading-my-own-jars-manifest
	 */
	protected Manifest getManifest() throws IOException {
		Class<?> clazz = getClass();
		String className = clazz.getSimpleName() + ".class";
		String classPath = clazz.getResource(className).toString();
		
		if (!classPath.startsWith("jar")) {
		  // Class not from JAR
		  throw new IOException( "MANIFEST NOT FOUND!");
		}
		
		final String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1).concat("/META-INF/MANIFEST.MF");
		
		Manifest manifest = new Manifest(new java.net.URL(manifestPath).openStream());

		return manifest;
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
	 * @param w
	 * @throws IOException 
	 */
	protected final <T>  T copyFileToAssetDir( final java.io.File resource, final Manifest mf, boolean overwrite, 
					Fn<Void,T> onSuccess, 
					Fn<Exception,T> onError  )  
	{
		
		if( resource == null ) {
			throw new IllegalArgumentException("resource parameter is null!");
		}
		
		
		try {
			final java.io.File assetDir = getAssetDir(mf);
			final String resourceName = FilenameUtils.getName( resource.getName() );
			final java.io.File target = new java.io.File( assetDir, resourceName );

			if( target.exists() && !overwrite ) {
				return onError.f(new IllegalStateException(String.format("resource [%s] already exists!", resourceName)) );
			}
			FileUtils.copyFileToDirectory(resource, assetDir);

		} catch (IOException e) {
			return onError.f(e);
		}
		
		return onSuccess.f(null);
		
	}
	
	/**
	 * 
	 * @return
	 */
	private Config newConfig() {
		//final Config config = new Config(getClass().getClassLoader().getParent());
		final Config config = new Config(getClass().getClassLoader());

		return config;
	}
	
	protected Runner runnerFromClasspath(UIContext ctx, final String resourceName, GlobalObjectFactory factory, Manifest mf)
			throws Exception {

		final Config config = newConfig();
		
		config.setGlobalObjectFactory(factory);
		config.setOutputStream(ctx.getProvider().getOutput().out());
		config.setErrorStream(ctx.getProvider().getOutput().err());
		
		final DynJS dynjs = new DynJS(config);

		final Runner runner = dynjs.newRunner();

		java.io.InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);

		if( is == null ) {
			throw new IllegalArgumentException( String.format("resource [%s] not found in classpath!", resourceName));
		}

		final java.io.File assetDir = getAssetDir(mf);

		dynjs.evaluate( new StringBuilder()
			.append("require.addLoadPath('").append(assetDir.getPath()).append("');")
			.toString()	
			);
		
		return runner.withSource( new java.io.InputStreamReader(is));
	}

	/**
	 * 
	 * @param ctx
	 * @param factory
	 * @return
	 */
	protected DynJS newDynJS( UIContext ctx, GlobalObjectFactory factory) {
		
		final Config config = newConfig();

		config.setGlobalObjectFactory(factory);
		config.setOutputStream(ctx.getProvider().getOutput().out());
		config.setErrorStream(ctx.getProvider().getOutput().err());

		final DynJS dynjs = new DynJS(config);

		return dynjs;
	}
	
	/**
	 * 
	 * @param ctx
	 * @param js
	 * @param factory
	 * @param mf
	 * @return
	 * @throws Exception
	 */
	protected Runner runnerFromFile( DynJS dynjs, final FileResource<?> js,  Manifest mf)  throws Exception {

		final File file = js.getUnderlyingResourceObject();

		final java.io.File assetDir = getAssetDir(mf);
		final String folder = file.getParent();
		if (folder != null) {
			
			final String header = new StringBuilder()
				.append("__basedir = '").append(folder).append("';")
				.append("require.addLoadPath(__basedir);")
				.append("require.addLoadPath('").append(assetDir.getPath()).append("');")
				.toString();
			
			dynjs.evaluate(header);
		}

		return dynjs.newRunner().withSource(file);

	}
	
	/**
	 * 
	 * @param js
	 * @return
	 * @throws Exception
	 */
	protected Object executeFromClasspath(UIContext ctx, final String resourceName, GlobalObjectFactory factory, Manifest mf)
			throws Exception {
		
		final Object result = runnerFromClasspath(ctx, resourceName, factory, mf).execute();

		return result;


	}
	
	/**
	 * 
	 * @param js
	 * @return
	 * @throws Exception
	 */
	protected Object executeFromFile(UIContext ctx, final FileResource<?> js, GlobalObjectFactory factory, Manifest mf)
			throws Exception {

		final DynJS dynjs = newDynJS(ctx, factory);

		final Object result = runnerFromFile(dynjs, js, mf).execute();

		return result;

	}

	
}
