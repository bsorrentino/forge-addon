package org.bsc.commands;

import javax.inject.Inject;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.GlobalObjectFactory;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

public class Eval extends AbstractDynjsUICommand {

	@Inject
	@WithAttributes(label = "Script", required = true, type = InputType.FILE_PICKER)
	private UIInput<FileResource<?>> script;

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(Eval.class).name("Eval")
				.category(Categories.create("Utility", "Dynjs"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {

		builder.add(script);
	}

	@Override
	public Result execute(UIExecutionContext context) {

		final FileResource<?> js = script.getValue();
		
		final GlobalObjectFactory factory = new GlobalObjectFactory() {
			
			@Override
			public GlobalObject newGlobalObject(DynJS runtime) {
				return new GlobalObject(runtime) {{
					
					defineReadOnlyGlobalProperty("command", Eval.this);
				}};
			}
		};
				
		try {

			final Object result = super.executeFromFile(context.getUIContext(), js, factory, getManifest());
			
			return Results.success(String.valueOf(result));

		} catch (Exception e) {

			return Results.fail("error evaluating script file", e);
		}

	}
}
