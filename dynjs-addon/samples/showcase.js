/**
 * New node file
 */


function initializeUI( builder ) {
}

function execute( context ) {

	var name = context.prompt.prompt( "give me name");

	print( "prompt: " + name );

	print( "BeanManager: " + self.beanManager );

	return "OK ";
}
