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
   //static final String version = "2.4.1.Final";
   //static final String version = "2.6.0.Final";
   static final String version = "2.7.2.Final";

   @Deployment
   @Dependencies({ 
	  @AddonDependency(name = "org.bsc:dynjs-addon", version="2.7.2-SNAPSHOT") 
	   })
   public static ForgeArchive getDeployment()
   {
	  
      ForgeArchive archive = ShrinkWrap
            .create(ForgeArchive.class)
            .addBeansXML()
            .addAsAddonDependencies(
            		
                    AddonDependencyEntry.create("org.jboss.forge.addon:addon-manager", version),
                    AddonDependencyEntry.create("org.jboss.forge.addon:addons", version),
                    AddonDependencyEntry.create("org.jboss.forge.addon:core", version),
                    AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi", version),
                    
            		AddonDependencyEntry.create("org.bsc:dynjs-addon", "2.7.2-SNAPSHOT")
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