/**
 * New node file
 */

require.addLoadPath(__basedir + '/src/main/resources');

var facets = require("facets")();

print( facets.mavenfacet.localRepositoryDirectory );

facets.mavenfacet.executeMaven( ["clean"] );
