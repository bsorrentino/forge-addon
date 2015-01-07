package org.bsc.jqm4gwt;

import javax.inject.Inject;

import org.bsc.JQM4GWTAddPage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Ignore
@RunWith(Arquillian.class)
public class JQM4GWTAddPageTest
{

   @Deployment
   @Dependencies({
         @AddonDependency(name = "org.jboss.forge.furnace.container:cdi"),
         @AddonDependency(name = "org.jboss.forge.addon:core"),
         @AddonDependency(name = "org.bsc:dynjs-addon"), 
   		 @AddonDependency(name = "org.bsc.jqm4gwt:jqm4gwt-addon") 
   })
   public static ForgeArchive getDeployment()
   {
 
	   ForgeArchive archive = ShrinkWrap
            .create(ForgeArchive.class)
            .addBeansXML()
            .addAsAddonDependencies(
                  AddonDependencyEntry
                        .create("org.jboss.forge.furnace.container:cdi"),
                  AddonDependencyEntry
                        .create("org.jboss.forge.addon:core"),
                  AddonDependencyEntry.create("org.bsc:dynjs-addon"),
                  AddonDependencyEntry.create("org.bsc.jqm4gwt:jqm4gwt-addon")
            	);
      return archive;
   }
   
   @Inject JQM4GWTAddPage addon;
   
   @Test
   public void generate() {
	   
   }
}