package org.bsc;

import static org.bsc.commands.AddonUtils.getAttribute;
import static org.bsc.commands.AddonUtils.getManifest;
import static org.bsc.commands.AddonUtils.getOut;
import static org.bsc.commands.AddonUtils.putAttribute;

import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bsc.commands.AbstractDynjsProjectCommand;
import org.bsc.commands.EvalStep;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.GlobalObjectFactory;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.xml.resources.XMLResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UINavigationContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizard;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.xml.sax.helpers.XMLReaderFactory;


public class JQM4GWTAddPage extends AbstractDynjsProjectCommand implements UIWizard
{

  @Override
  public UICommandMetadata getMetadata(UIContext context)
  {
    return Metadata.forCommand(JQM4GWTAddPage.class)
    .name("jqm4gwt-add-page")
    .category(Categories.create("JQM4GWT"));
  }

  @Override
  public void initializeUI(UIBuilder builder) throws Exception
  {
  }

  @Override
  public Result execute(UIExecutionContext context) throws Exception
  {
    return Results
    .success("Command 'jqm4gwt-add-page' successfully executed!");
  }

  @Override
  public NavigationResult next(UINavigationContext context) throws Exception
  {
    DynJS dynjs = getAttribute(context, DynJS.class.getName());
    if (dynjs == null)
    {
      final Project project = super.getSelectedProject(context);
      final GlobalObjectFactory factory = new GlobalObjectFactory()
      {
        @Override
        public GlobalObject newGlobalObject(DynJS runtime)
        {
          return new GlobalObject(runtime)
          {
            {
              defineReadOnlyGlobalProperty("self",
              JQM4GWTAddPage.this, true);
              defineReadOnlyGlobalProperty("project", project,
              true);
            }
          };
        }
      };
      dynjs = newDynJS(context, factory);
      final Manifest mf = getManifest();
      try
      {
        runnerFromClasspath(dynjs, "javasource.js", mf).evaluate();
      }
      catch (Exception e)
      {
        throw e;
      }
      putAttribute(context, DynJS.class.getName(), dynjs);
    }
    return Results.navigateTo(EvalStep.class);
  }

  /**
  *
  */
  public final JavaClassSource createJavaPage( UIExecutionContext context, Project project, String className ) {
	final JavaSourceFacet jsf = project.getFacet(JavaSourceFacet.class);

    final JavaClassSource jcs = Roaster.create(  JavaClassSource.class );

    jcs.setPublic()
    .setPackage( jsf.getBasePackage().concat(".client") )
    .setName( className )
    .addInterface("JQMPageEvent.Handler")
    .addField( String.format("public static final UiBinder BINDER = GWT.create(%s.UiBinder.class);", className))
    .getOrigin()
    .addField( String.format("public final static JQMPage INSTANCE = new %s().page;", className))
    .getOrigin()
    .addField( String.format("private final JQMPage page = %s.BINDER.createAndBindUi(this);", className))
    .getOrigin()
    .addMethod().setConstructor(true).setBody("page.addPageHandler(this);")
    .getOrigin()
    ;
    {
      MethodSource<?> m = jcs.addMethod()
      .setName("onInit")
      .setReturnTypeVoid()
      .setPublic()
      .setBody("");
      m.addParameter("com.sksamuel.jqm4gwt.JQMPageEvent", "event");
      m.addAnnotation(Override.class);
    }

    {
      MethodSource<?> m = jcs.addMethod()
      .setName("onBeforeShow")
      .setReturnTypeVoid()
      .setPublic()
      .setBody("");

      m.addParameter("com.sksamuel.jqm4gwt.JQMPageEvent", "event");
      m.addAnnotation(Override.class);
    }
    {
      MethodSource<?> m = jcs.addMethod()
      .setName("onBeforeHide")
      .setReturnTypeVoid()
      .setPublic()
      .setBody("");

      m.addParameter("com.sksamuel.jqm4gwt.JQMPageEvent", "event");
      m.addAnnotation(Override.class);
    }
    {
      MethodSource<?> m = jcs.addMethod()
      .setName("onShow")
      .setReturnTypeVoid()
      .setPublic()
      .setBody("");

      m.addParameter("com.sksamuel.jqm4gwt.JQMPageEvent", "event");
      m.addAnnotation(Override.class);
    }
    {
      MethodSource<?> m = jcs.addMethod()
      .setName("onHide")
      .setReturnTypeVoid()
      .setPublic()
      .setBody("");

      m.addParameter("com.sksamuel.jqm4gwt.JQMPageEvent", "event");
      m.addAnnotation(Override.class);
    }


    jcs.addImport("com.sksamuel.jqm4gwt.JQMPage");
    jcs.addImport("com.sksamuel.jqm4gwt.JQMPageEvent");
    jcs.addNestedType(String.format("interface UiBinder extends com.google.gwt.uibinder.client.UiBinder<JQMPage, %s> { }", className));

    return jcs;
  }
  
  /**
   * 
   * @return
   */
  public final void createUIBinder( UIExecutionContext context, Project project, String className  ) throws Exception {
	
	  		final JavaSourceFacet jsf = project.getFacet(JavaSourceFacet.class);
	  		
			final DirectoryResource uibinderDir = jsf.getBasePackageDirectory().getChildDirectory("client");

			if (!uibinderDir.exists()) {

				if (!uibinderDir.mkdirs()) {
					getOut(context).err().printf("ERROR CREATING FOLDER: [%s]\n", uibinderDir);
					return;
				}
			}
			
			final java.io.InputStream is = getClass().getClassLoader()
					.getResourceAsStream("pageTemplate.xml");

			java.io.File uibinderFile = new java.io.File( uibinderDir.getUnderlyingResourceObject(), String.format("%s.ui.xml", className));
			FileUtils.copyInputStreamToFile(is,  uibinderFile);
  }

}
