package org.bsc.commands;

import java.io.PrintStream;
import java.util.jar.Manifest;

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
import org.jboss.forge.addon.ui.util.Metadata;

public class Eval extends AbstractDynjsUICommand {

	@Inject
	@WithAttributes(label = "Script", required = true, type = InputType.FILE_PICKER)
	private UIInput<FileResource<?>> script;

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(Eval.class).name("Eval")
				.category(CATEGORY);
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {

		builder.add(script);
	}

	@Override
	public Result execute(final UIExecutionContext context) {
		
		final PrintStream out = context.getUIContext().getProvider().getOutput().out();
		
		final FileResource<?> js = script.getValue();
		
		final GlobalObjectFactory factory = new GlobalObjectFactory() {
			
			@Override
			public GlobalObject newGlobalObject(DynJS runtime) {
				return new GlobalObject(runtime) {{
					
					defineReadOnlyGlobalProperty("command", Eval.this);
					defineReadOnlyGlobalProperty("context", context);
					
				}};
			}
		};
				
		try {
			final Manifest mf = getManifest();
			
			out.printf( "VERSION: [%s]\n", getVersion(mf));
			
			final Object result = super.executeFromFile(context.getUIContext(), js, factory, mf);
			
			return Results.success(String.valueOf(result));

		} catch (Exception e) {

			return Results.fail("error evaluating script file", e);
		}

	}
		
}
