/**
 * New node file
 */

print( "addon loaded!");

{
		
ic:null


initializeUI:function( builder ) {

	print( "initialize UI")

	attrs.ic = self.componentFactory.createInput("test", "test", java.lang.String);
	attrs.ic.setLabel( "Test:");
	attrs.ic.setRequired( true );
	builder.add( attrs.ic );

	print( "UI initialized!")
}

execute:function( context ) {

	print( "executeJS " + context.getUIContext());

	var attrs = context.getUIContext().getAttributeMap();
	
	return "OK " +  attrs.ic;
}