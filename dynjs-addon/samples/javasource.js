

function initializeUI( builder ) {

}

function execute( context ) {

var facets = require("facets")();


for( m in facets ) {

  print( "facet: " + m );

}

var Roaster = org.jboss.forge.roaster.Roaster;
var JavaSourceFacet =  org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;

var JavaClassSource = org.jboss.forge.roaster.model.source.JavaClassSource;


print( "Class " + Roaster + " jcs " + JavaClassSource );


var c = Roaster.create( JavaClassSource )
                    .setName("Test")
                    .setPublic()
                    ;


print( "Class " + c );

facets.mavenjavasourcefacet.saveJavaSource( c );

return "OK";

}
