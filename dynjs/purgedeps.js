/*
clear
echo "clean project targets"
mvn $@ -o clean
echo -n "delete project's artifacts from local repo [y/N]"
read -n 1 del

#echo "$del"

if [ "$del" == "y" ] ; then
	echo -e "\nperform delete "
	
	mvn  $@ -fn -T2 -o \
	 org.codehaus.mojo:build-helper-maven-plugin:1.8:remove-project-artifact \
	 -Dbuildhelper.failOnError=false \
	 -Dbuildhelper.removeAll=true
	 
else
	echo -e "\nskip delete "
fi

echo -n "delete project's dependencies from local repo [y/N] "
read -n 1 del

#echo "$del"

if [ "$del" == "y" ] ; then
	echo -e "\nperform delete "
	
	mvn  $@ -fn -T2 -o \
	 org.apache.maven.plugins:maven-dependency-plugin:2.8:purge-local-repository \
	 -DreResolve=false \
	 -DsnapshotsOnly=false \
	 -Dverbose=true
	 
else
	echo -e "\nskip delete"
fi


*/

var facets = require("facets")();

var String = java.lang.String;
var Boolean = java.lang.Boolean;
var System = java.lang.System;

var OSUtils = org.jboss.forge.furnace.util.OperatingSystemUtils;

print( "getWorkingDir " + OSUtils.getWorkingDir() );
print( "getUserDir " + System.getProperty("user.dir") );

System.setProperty("user.dir", project.root.fullyQualifiedName);

print( "getWorkingDir " + OSUtils.getWorkingDir() );
print( "getUserDir " + System.getProperty("user.dir") );

var input = {};

input.params = self.componentFactory.createInput("params", String );
input.params.label = "mvn parameters";
input.params.defaultValue ="-e";

input.dlr = self.componentFactory.createInput("dlr", Boolean );
input.dlr.label = "Delete project's artifacts from local repo?";
input.dlr.defaultValue = true;

input.dld = self.componentFactory.createInput("dld", Boolean );
input.dld.label = "Delete project's dependencies from local repo ?";
input.dld.defaultValue = true;

function initializeUI( builder ) {
	
	for( m in input ) {
		builder.add( input[m] );
	}
}

function execute( context ) {

	facets.mavenfacet.executeMaven( ["clean",  "-o", input.params.value ] );
	
	if( input.dlr.value == true ) {
		print( "deleting project's artifact");
		facets.mavenfacet.executeMaven( 
				 ["org.codehaus.mojo:build-helper-maven-plugin:1.8:remove-project-artifact", 
				 "-Dbuildhelper.failOnError=false",
				 "-Dbuildhelper.removeAll=true",
				 "-fn", "-T2", "-o",  
				 input.params.value
				  ] );		 
	}
	if( input.dld.value == true ) {
		print( "deleting project's dependencies");
		facets.mavenfacet.executeMaven( 
				["org.apache.maven.plugins:maven-dependency-plugin:2.8:purge-local-repository", 
				 "-DreResolve=false",
				 "-DsnapshotsOnly=false",
				 "-Dverbose=false",
				 "-fn", "-T2", "-o",
				 input.params.value
				  ] );		 
	}
	return "OK ";
}


