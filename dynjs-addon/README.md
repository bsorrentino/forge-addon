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
| self          | the current [command](http://bsorrentino.github.io/forge-addon/dynjs-addon/apidocs/org/bsc/commands/Eval.html) instance |


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
| self          | the current [command](http://bsorrentino.github.io/forge-addon/dynjs-addon/apidocs/org/bsc/commands/EvalInProject.html) instance |
| project       | the current [project](http://docs.jboss.org/forge/javadoc/2.6.1-SNAPSHOT/org/jboss/forge/addon/projects/Project.html) instance |

### > installmodule
>  Install a new common module. After installation such module will be available to script using **require** command
#### Parameters
| param        | Description |
| ------------- |:-------------|
| --script      | *script's full path* |

### Getting started

#### Install

`> forge --install org.bsc:dynjs-addon,2.6.0.Final`

All available version [here](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.bsc%22%20AND%20a%3A%22dynjs-addon%22)

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
var Boolean = java.lang.Boolean;

print( "addon loaded!");

var input = {};

// initialize an UIInput (String)
input.string0 = self.componentFactory.createInput("string0", String );
input.string0.label = "Give me a string";
input.string0.required = true;

// initialize an UIInput (Boolean)
input.bool0 = self.componentFactory.createInput("bool0", Boolean );
input.bool0.label = "Give me boolean";
input.bool0.DefaultValue = true;

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
         input.string0.value +
         " - " +
         input.bool0.value;
}
```

#### Require module(s)
```javascript


var facets = require("facets")(); // facets is a built-in module
// Other modules can be shared using 'installModule' command

print( "addon loaded!");


function initializeUI( builder ) {

}

function execute( context ) {

  // Perform clean,package on current open project
  facets.mavenfacet.executeMaven( ["clean",  "package"] );
}
```

#### Develop a Module
```javascript

/**
Simple module that provide pwd & cd functions

install from cli:
=================

> installmodule --script <full script path>

Usage within script:
====================

var shell = require("shell");

*/

var OSUtils = org.jboss.forge.furnace.util.OperatingSystemUtils;
var System = java.lang.System;

module.exports = {

		pwd:function() {
			return OSUtils.getWorkingDir();
		},
		cd:function( dir ) {
			return System.setProperty("user.dir", dir);
		}


}

```

### Example
> Checkout more examples from [here](samples)
