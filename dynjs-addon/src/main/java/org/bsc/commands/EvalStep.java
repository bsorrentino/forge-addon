package org.bsc.commands;

import static org.bsc.commands.AbstractBaseDynjsUICommand.getAttribute;
import static org.bsc.commands.AbstractBaseDynjsUICommand.getOut;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.Reference;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UINavigationContext;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;

/**
 * 
 * @author bsorrentino
 *
 */
public class EvalStep extends AbstractUICommand implements UIWizardStep {

	@Override
	public NavigationResult next(UINavigationContext context) throws Exception {
		getOut(context).out().println("EvalStep.next");
		return null;
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		getOut(context).out().println("EvalStep.execute");

		DynJS dynjs = getAttribute(context, DynJS.class.getName());

		if (dynjs != null) {

			Reference ref = dynjs.getExecutionContext().resolve("execute");

			if (ref != null) {

				Object fn = ref.getValue(dynjs.getExecutionContext());
				if (fn instanceof JSFunction) {

					Object result = dynjs.getExecutionContext().call(
							(JSFunction) fn, null, new Object[] { context });

					return Results.success(String.valueOf(result));
				}
			}
		}

		return Results.success();
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {

		DynJS dynjs = getAttribute(builder, DynJS.class.getName());

		if (dynjs != null) {
			getOut(builder).out().println("EvalStep.initializeUI");

			Reference ref = dynjs.getExecutionContext().resolve("initializeUI");

			if (ref != null) {

				Object fn = ref.getValue(dynjs.getExecutionContext());
				if (fn instanceof JSFunction) {

					dynjs.getExecutionContext().call((JSFunction) fn, null,
							new Object[] { builder });

				}
			}
		}

	}

}
