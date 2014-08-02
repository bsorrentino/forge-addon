/**
 * New node file
 */

var InputComponents = org.jboss.forge.addon.ui.util.InputComponents
var String = java.lang.String;

print( "addon loaded!");

var attrs = {};

attrs.ic = self.componentFactory.createInput("test", String );
attrs.ic.label = "Test";
attrs.ic.required = true;

attrs.ic2 = self.componentFactory.createInput("test2", String );
attrs.ic2.label = "Test2";
attrs.ic2.required = true;

function initializeUI( builder ) {

	print( "initialize UI");
	for( m in attrs ) {
		builder.add( attrs[m] );
	}
	print( "UI initialized!")

}

function execute( context ) {

	print( "executeJS " );
	
	return "OK " +  attrs.ic.value;
}

