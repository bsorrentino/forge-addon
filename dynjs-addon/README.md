## dynjs-addon

Addon that allow to develop a [forge addon]() using javascript. The javascript engine used is [dynjs](http://dynjs.org/)


### Commands

| Command        | Description |
| ------------- |:-------------|
| eval          | *evaluate script* |
| evalinproject | *evaluate script in project's scope* |  
| installmodule | *install a new common module* |

### Getting started

#### Javascript basic template
```javascript
var input = {}; // object containing the UIInput(s)

function initializeUI( builder ) { // Initialize UI & fill input object

  print( "initialize UI");

}

function execute( context ) { // perform task using the input values

  print( "executeJS " );
}

```

### Examples

#### Print the values of required inputs
```javascript
var String = java.lang.String;

print( "addon loaded!");

var input = {};

// initialize an UIInput
input.ic = self.componentFactory.createInput("test", String );
input.ic.label = "Test";
input.ic.required = true;

// initialize an UIInput
input.ic2 = self.componentFactory.createInput("test2", String );
input.ic2.label = "Test2";
input.ic2.required = true;

function initializeUI( builder ) {

	print( "initialize UI");
	for( m in input ) {
		builder.add( input[m] );
	}
	print( "UI initialized!")

}

function execute( context ) {

	print( "executeJS " );

	return "OK " +  
         input.ic.value +
         " - " +
         input.ic2.value;
}
```
