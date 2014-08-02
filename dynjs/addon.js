/**
 * New node file
 */

var InputComponents = org.jboss.forge.addon.ui.util.InputComponents

print( "addon loaded!");

var attrs = {};

attrs.ic = self.createInput("test");
attrs.ic.setLabel( "Test");
attrs.ic.setRequired( true );
//attrs.ic.setDefaultValue( "OK");
//attrs.ic.setValue("X");		

attrs.ic2 = self.createInput("test2");
attrs.ic2.setLabel( "Test2");
attrs.ic2.setRequired( true );
//attrs.ic.setDefaultValue( "OK");
//attrs.ic.setValue("X");		

function initializeUI( builder ) {

	print( "initialize UI");
	builder.add( attrs.ic ).add( attrs.ic2 );
	print( "UI initialized!")

}

function execute( context ) {

	print( "executeJS " + context.getUIContext());
	print( "InputComponents " + InputComponents);
	
	return "OK " +  attrs.ic.value;
}

/*
{
		
ic:null,


initializeUI:function( builder ) {

	print( "initialize UI")

	this.ic = self.componentFactory.createInput("test", "test", java.lang.String);
	this..ic.setLabel( "Test:");
	this.ic.setRequired( true );
	builder.add( attrs.ic );

	print( "UI initialized!")
},

execute:function( context ) {

	print( "executeJS " + context.getUIContext());
	
	return "OK " +  attrs.ic;
}

}
*/