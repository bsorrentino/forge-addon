package org.bsc.commands;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.ui.input.InputComponentFactory;


public abstract class AbstractDynjsUICommand extends AbstractBaseDynjsUICommand {
	
	@Inject
	private InputComponentFactory componentFactory;
	
	@Override
	protected final boolean isProjectRequired() {
		return false;
	}

	@Override
	protected final ProjectFactory getProjectFactory() {
		return null;
	}

	public final InputComponentFactory getComponentFactory() {
		return componentFactory;
	}
	
}
