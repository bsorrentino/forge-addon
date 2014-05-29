package org.bsc;

import javax.inject.Inject;

import org.bsc.commands.Eval;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

//@Ignore
@RunWith(Arquillian.class)
public class DynjsAddonTest
{

   @Deployment
   @Dependencies({ 
       //@AddonDependency(name = "org.jboss.forge.addon:addon-manager", version="2.4.1.Final"),
       //@AddonDependency(name = "org.jboss.forge.addon:addons", version="2.4.1.Final"),
       //@AddonDependency(name = "org.jboss.forge.addon:core", version="2.4.1.Final"),
       //@AddonDependency(name = "org.jboss.forge.furnace.container:cdi", version="2.4.1.Final")
       //@AddonDependency(name = "org.jboss.forge.addon:maven"),
	   @AddonDependency(name = "org.bsc:dynjs-addon", version="1.0.0-SNAPSHOT") 
	   })
   public static ForgeArchive getDeployment()
   {
      ForgeArchive archive = ShrinkWrap
            .create(ForgeArchive.class)
            .addBeansXML()
            .addAsAddonDependencies(
            		
                    AddonDependencyEntry.create("org.jboss.forge.addon:addon-manager", "2.4.1.Final"),
                    AddonDependencyEntry.create("org.jboss.forge.addon:addons", "2.4.1.Final"),
                    AddonDependencyEntry.create("org.jboss.forge.addon:core", "2.4.1.Final"),
                    AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi", "2.4.1.Final"),
                    
            		AddonDependencyEntry.create("org.bsc:dynjs-addon", "1.0.0-SNAPSHOT")
            		)
            ;
      return archive;
   }
   
   
   @Inject
   private Eval evalCommand;

   @Test
   public void testCommandInjection() throws Exception
   {
      Assert.assertNotNull(evalCommand);
      Assert.assertNotNull(evalCommand.getDependencyResolver());
      
      System.out.println( String.valueOf(evalCommand.getDependencyResolver()));
   }
   
}