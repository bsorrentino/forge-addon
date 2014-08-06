## DynJS addon

> Addon that allow to develop a [forge addon](http://forge.jboss.org/addons) using [DynJS javascript engine](http://dynjs.org/).

### Available Commands

| Command       | Description  |
| ------------- |:-------------|
| eval          | *Evaluate a script* |
| evalinproject | *Evaluate a script in project's scope* |  
| installmodule | *Install a new common module* |

### > eval
>  Evaluate a script
#### Parameters
| param        | Description |
| ------------- |:-------------|
| --script      | *script's full path* |
#### Implicit Object(s)
Object(s) Available in the script

>| object        | Description |
| ------------- |:-------------|
| self          | the current [command](src/main/java/org/bsc/commands/Eval.java) instance |


### > evalinproject
>  Evaluate a script in project's scope
#### Parameters
| param        | Description |
| ------------- |:-------------|
| --script      | *script's full path* |
#### Implicit Object(s)
Object(s) Available in the script

>| object       | Description |
| ------------- |:-------------|
| self          | the current [command](src/main/java/org/bsc/commands/Eval.java) instance |
| project       | the current [project](http://docs.jboss.org/forge/javadoc/2.6.1-SNAPSHOT/) instance |

### > installmodule
>  Install a new common module. After installation such module will be available to script using **require** command
#### Parameters
| param        | Description |
| ------------- |:-------------|
| --script      | *script's full path* |

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
