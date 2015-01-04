
var InputComponents = org.jboss.forge.addon.ui.util.InputComponents;
var String = java.lang.String;

var attrs = {};

attrs.ic = self.componentFactory.createInput("ClassName", String );
attrs.ic.label = "Class Name";
attrs.ic.required = true;


function initializeUI( builder ) {

	print( "initialize UI");
	for( m in attrs ) {
		builder.add( attrs[m] );
	}
	print( "UI initialized!")

}

function execute( context ) {
	var facets = require("facets")();

	//var JavaSourceFacet =  org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
	
	var c = self.createJavaPage( context, project, "" + attrs.ic.value );
	
	print( "Class " + c + " facets.MavenJavaSourceFacet " + facets.MavenJavaSourceFacet);
	
	facets.MavenJavaSourceFacet.saveJavaSource( c );
	
	self.createUIBinder( context, project, "" + attrs.ic.value );
	
	return "OK";

}
