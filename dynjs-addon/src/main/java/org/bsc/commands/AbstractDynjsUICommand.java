package org.bsc.commands;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.DependencyResolver;
import org.jboss.forge.addon.projects.ProjectFactory;


public abstract class AbstractDynjsUICommand extends AbstractBaseDynjsUICommand {

	@Inject
	protected DependencyResolver dependencyResolver;
	
	@Override
	protected final boolean isProjectRequired() {
		return false;
	}

	@Override
	protected final ProjectFactory getProjectFactory() {
		return null;
	}


	
}
