package org.bsc.commands;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.ProjectFactory;

public abstract class AbstractDynjsProjectCommand extends AbstractBaseDynjsUICommand {
	
	@Inject
	private ProjectFactory projectFactory;

	@Override
	protected final ProjectFactory getProjectFactory() {
		return projectFactory;
	}

	@Override
	protected final boolean isProjectRequired() {
		return true;
	}


}
