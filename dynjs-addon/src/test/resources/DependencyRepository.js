var Class = java.lang.Class;
var String = java.lang.String;


function createRepository( id, url ) {

var c = Class.forName('org.jboss.forge.addon.dependencies.DependencyRepository');

var cc = c.getConstructor( [String, String] );

if( id==='local') {
  var home = new java.io.File(java.lang.System.getProperty( "user.home" ));
  var r = new java.io.File( home, ".m2/repository");

  return cc.newInstance( [id,  r.toURI().toURL().toString()] );

}

return cc.newInstance( [id,  url.toString()] );

}

module.exports = createRepository;
