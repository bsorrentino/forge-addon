
var reflection = require('introspection');
var deps = require( 'DependencyQueryBuilder');
var localRepo = require( 'DependencyRepository')( "local");
var depbuilder = require( 'DependencyBuilder');

try {

print( command );
//print( command.getDependencyResolver() );

reflection.printM( 'command', command);

print(deps.name);


var query1 =  deps.create( "org.robovm:robovm-objc" );
query1.setRepositories( [localRepo ]);

var rr = query1.getDependencyRepositories();

print( rr.size() );

reflection.printC( 'query1', query1);

var dr = command.getDependencyResolver();

print( dr );

reflection.printM( 'dr', dr);

//var result =  dr.resolveDependencies( query1 );
var result =  dr.resolveVersions( query1 );

print( result.size() );

print( depbuilder.name ); 

var ii = result.iterator();

while( ii.hasNext() ) {

  var cc = ii.next();

  print( cc );

  //var dep = depbuilder.create();
  //dep.setCoordinate( cc );


  //print( dep.getArtifact() );

  /*
  reflection.printC( "cc", cc );

  var query2 = deps.create(cc); query2.setRepositories( [localRepo ]);
  //var dep = dr.resolveDependencies( query2 );
  var dd = dr.resolveArtifact( query2 );
  print( dd );
  dd.getArtifact().delete();
  */
}

"END";

}
catch( e ) {

  e.printStackTrace();
  print( "ERROR " + e);
}
