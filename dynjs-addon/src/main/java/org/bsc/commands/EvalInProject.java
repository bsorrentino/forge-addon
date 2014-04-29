package org.bsc.commands;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.GlobalObjectFactory;
import org.dynjs.runtime.Runner;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.Projects;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFilter;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UICompleter;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

/**
 * 
 * @author softphone
 * 
 */
public class EvalInProject extends AbstractProjectCommand {
	@Inject
	@WithAttributes(label = "Script", required = true, type = InputType.FILE_PICKER)
	private UIInput<FileResource<?>> script;

	@Inject
	ProjectFactory projectFactory;

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(EvalInProject.class).name("EvalInProject")
				.category(Categories.create("Utility", "Dynjs"));
	}

	PrintStream out(UIContext ctx) {
		return ctx.getProvider().getOutput().out();
	}

	@SuppressWarnings("unchecked")
	private List<Resource<?>> listResources(Resource<?> res,
			final List<Resource<?>> result) {

		List<?> r = res.listResources(new ResourceFilter() {

			@Override
			public boolean accept(Resource<?> res) {

				if (res instanceof FileResource) {

					FileResource<?> file = (FileResource<?>) res;

					return file.getName().endsWith(".js");
				} else if (res instanceof DirectoryResource) {

					listResources(res, result);
				}

				return false;
			}
		});

		result.addAll((Collection<? extends Resource<?>>) r);

		return result;
	}

	@Override
	public void initializeUI(final UIBuilder builder) throws Exception {

		final Project project = Projects.getSelectedProject(projectFactory,
				builder.getUIContext());

		out(builder.getUIContext()).printf("root [%s]\n", project.getRoot());

		script.setCompleter(new UICompleter<FileResource<?>>() {

			@SuppressWarnings("unchecked")
			@Override
			public Iterable<FileResource<?>> getCompletionProposals(
					UIContext context,
					InputComponent<?, FileResource<?>> input, String value) {

				final List<Resource<?>> result = listResources(
						project.getRoot(), new ArrayList<Resource<?>>());

				Collections.sort(result, new Comparator<Resource<?>>() {

					@Override
					public int compare(Resource<?> o1, Resource<?> o2) {
						return o1.getFullyQualifiedName().compareTo(
								o2.getFullyQualifiedName());
					}
				});

				return (Iterable<FileResource<?>>) (List<?>) result;
			}
		});
		builder.add(script);

	}

	/**
	 * 
	 * @param js
	 * @return
	 * @throws Exception
	 */
	private Object execute(UIContext ctx, final FileResource<?> js, GlobalObjectFactory factory)
			throws Exception {

		final Config config = new Config();

		config.setGlobalObjectFactory(factory);
		config.setOutputStream(ctx.getProvider().getOutput().out());
		config.setErrorStream(ctx.getProvider().getOutput().err());

		final DynJS dynjs = new DynJS(config);

		final Runner runner = dynjs.newRunner();

		final File file = js.getUnderlyingResourceObject();

		final String folder = file.getParent();
		if (folder != null) {
			dynjs.execute(String.format("require.addLoadPath('%s')", folder));
		}

		final Object result = runner.withSource(file).execute();

		return result;

	}

	@Override
	public Result execute(UIExecutionContext context) {

		final Project project = super.getSelectedProject(context);

		out(context.getUIContext()).printf("script [%s]\n", script.getValue());

		final GlobalObjectFactory factory = new GlobalObjectFactory() {

			@Override
			public GlobalObject newGlobalObject(DynJS runtime) {
				return new GlobalObject(runtime) {
					{

						defineReadOnlyGlobalProperty("command",
								EvalInProject.this);
						defineReadOnlyGlobalProperty("project", project);
					}
				};
			}
		};

		try {

			final Object result = execute(context.getUIContext(), script.getValue(), factory);

			return Results.success(String.valueOf(result));

		} catch (Exception e) {

			return Results.fail("error evaluating script file", e);
		}

	}

	@Override
	protected ProjectFactory getProjectFactory() {
		return projectFactory;
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}
}