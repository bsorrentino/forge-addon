package org.bsc;

import javax.inject.Inject;
import org.bsc.commands.JSEval;
import org.hamcrest.core.IsNull;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.junit.Test;

@RunWith(Arquillian.class)
public class JSEvalTest {

	@Deployment
	@AddonDependencies({
			@AddonDependency(name = "org.jboss.forge.furnace.container:cdi"),
			@AddonDependency(name = "org.jboss.forge.addon:core"),
			@AddonDependency(name = "org.bsc:dynjs-addon")
        })
	public static AddonArchive getDeployment() {
		return ShrinkWrap.create(AddonArchive.class).addBeansXML()
                        .addAsAddonDependencies( 
                                AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi"), 
                                AddonDependencyEntry.create("org.jboss.forge.addon:core"), 
                                AddonDependencyEntry.create("org.bsc:dynjs-addon") 
                        )
                        ;
	}

        @Inject
        JSEval evalCommand;
        
	@Test
	public void testAddon() {
            
		Assert.assertThat( evalCommand, IsNull.notNullValue());
                
                
	}
}