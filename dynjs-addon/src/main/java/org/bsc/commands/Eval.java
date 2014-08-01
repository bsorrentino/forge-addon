package org.bsc.commands;

import java.util.jar.Manifest;

import javax.inject.Inject;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.GlobalObjectFactory;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.Reference;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UINavigationContext;
import org.jboss.forge.addon.ui.context.UIValidationContext;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.result.navigation.NavigationResultBuilder;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizard;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;

public class Eval extends AbstractDynjsUICommand implements UIWizard {

	
	private DynJS dynjs;
	
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
		getOut( builder ).out().println( "Eval.initializeUI");
		
		builder.add(script);
	}

	@Override
	public Result execute(final UIExecutionContext context) {
		getOut( context ).out().println( "Eval.execute");
		return Results.success();
	}
	
	@Override
	public NavigationResult next(UINavigationContext context) throws Exception {
		
		getOut(context).out().println( "Eval.next" );
		
		if( dynjs == null ) {
			final GlobalObjectFactory factory = new GlobalObjectFactory() {
				
				@Override
				public GlobalObject newGlobalObject(DynJS runtime) {
					return new GlobalObject(runtime) {{
						
						defineReadOnlyGlobalProperty("command", Eval.this);
						//defineReadOnlyGlobalProperty("context", context);
						
					}};
				}
			};
	
			dynjs = newDynJS(context, factory);
	
			final FileResource<?> js = script.getValue();
	
			final Manifest mf = getManifest();
	
			runnerFromFile(dynjs, js, mf).evaluate();
	
		}
		
		return NavigationResultBuilder.create()
				.add( new EvalStep() )
				.build();
	}
		
	
	class EvalStep implements UICommand, UIWizardStep {

		@Override
		public NavigationResult next(UINavigationContext context) throws Exception {
			getOut(context).out().println( "EvalStep.next");
			return null;
		}

		@Override
		public Result execute(UIExecutionContext context) throws Exception {
			getOut(context).out().println( "EvalStep.execute");
			return Results.success();
		}

		@Override
		public UICommandMetadata getMetadata(UIContext context) {
			return null;
		}

		@Override
		public void initializeUI(UIBuilder builder) throws Exception {
			getOut(builder).out().printf( "EvalStep.initializeUI( dynjs=[%s] )\n", dynjs );

			if( dynjs!= null ) {
				Reference ref = dynjs.getExecutionContext().resolve("initializeUI");
				
				if( ref!=null ) {
					
					Object fn = ref.getValue(dynjs.getExecutionContext());
					if( fn instanceof JSFunction ) {
						
						dynjs.getExecutionContext().call( (JSFunction) fn, null, new Object[]{builder} );
						
					}
				}
			}
			
		}

		@Override
		public boolean isEnabled(UIContext context) {
			return true;
		}

		@Override
		public void validate(UIValidationContext context) {
			
		}
		
	}
}
