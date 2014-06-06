package org.bsc.commands;

import org.jboss.forge.addon.projects.ProjectFactory;


public abstract class AbstractDynjsUICommand extends AbstractBaseDynjsUICommand {
	

	@Override
	protected final boolean isProjectRequired() {
		return false;
	}

	@Override
	protected final ProjectFactory getProjectFactory() {
		return null;
	}
	
}
