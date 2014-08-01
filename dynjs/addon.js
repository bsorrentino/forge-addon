/**
 * New node file
 */

print( "addon loaded!");

var attrs = {
	
	ic:null
};

function initializeUI( builder ) {

	print( "initialize UI")

	attrs.ic = self.componentFactory.createInput("test", "t", java.lang.String);
	attrs.ic.setLabel( "Test");
	attrs.ic.setRequired( true );
	//attrs.ic.setDefaultValue( "OK");
	attrs.ic.setValue("X");
	builder.add( attrs.ic );

	print( "UI initialized!")
}

function execute( context ) {

	print( "executeJS " + context.getUIContext());
	
	return "OK " +  attrs.ic.getValue();
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