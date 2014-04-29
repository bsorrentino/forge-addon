package org.bsc.commands;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Logger;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

public class Setup extends AbstractUICommand {
	
	private static Logger log = Logger.getLogger(Setup.class.toString());

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(Setup.class).name("Setup")
				.category(Categories.create("Utility", "Cordova-Poc"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {

	}

	@Override
	public Result execute(UIExecutionContext context) {
		
		final StringBuilder out = new StringBuilder();
		
		final String urlString = context.getPrompt().prompt("site url: ");

		java.net.URL url;

		try {
			url = new java.net.URL(urlString);
		} catch (MalformedURLException e) {

			return Results.fail("provided url is not valid!", e);
		}

		final CleanerProperties props = new CleanerProperties() {
			{
				setAddNewlineToHeadAndBody(true);
			}
		};

		final HtmlCleaner htmlParser = new HtmlCleaner(props);

		final TagNode rootNode;
		try {
			rootNode = htmlParser.clean(url);

		} catch (IOException e) {
			return Results.fail("error parsing url!", e);
		}

		final List<? extends TagNode> scriptList = rootNode.getElementListByName(
				"script", true /* isRecursive */);

		for (TagNode script : scriptList) {

			final String v = script.getAttributeByName("src");
			
			log.info( v );
			out.append(v).append('\n');
		}

		final List<? extends TagNode> linkList = rootNode.getElementListByName(
				"link", true /* isRecursive */);

		for (TagNode link : linkList) {

			final String v = link.getAttributeByName("href");
			
			log.info( v );
			out.append(v).append('\n');

		}

		final List<? extends TagNode> images = rootNode.getElementListByName("img",
				true /* isRecursive */);

		for (TagNode img : images) {

			final String v = img.getAttributeByName("href");
			
			log.info( v );
			out.append(v).append('\n');

		}

		context.getUIContext().getProvider().getOutput().out().println( out.toString() );
		
		return Results.success(url.toString());
	}
}