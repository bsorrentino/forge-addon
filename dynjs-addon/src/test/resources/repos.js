var rr = require('introspection');

var RepositoryUtils = org.jboss.forge.addon.maven.projects.util.RepositoryUtils;
var MavenRepositorySystemUtils = org.apache.maven.repository.internal.MavenRepositorySystemUtils;
var DefaultRepositorySystemSession = org.eclipse.aether.DefaultRepositorySystemSession;
var LocalRepository = org.eclipse.aether.repository.LocalRepository;
var RemoteRepository = org.eclipse.aether.repository.RemoteRepository;
var DefaultArtifact = org.eclipse.aether.artifact.DefaultArtifact;
var LocalArtifactRequest = org.eclipse.aether.repository.LocalArtifactRequest;
var DefaultMetadata = org.eclipse.aether.metadata.DefaultMetadata;
var LocalMetadataRequest = org.eclipse.aether.repository.LocalMetadataRequest;
var File = java.io.File;
var VersionRangeRequest = org.eclipse.aether.resolution.VersionRangeRequest;
var ArtifactDescriptorRequest = org.eclipse.aether.resolution.ArtifactDescriptorRequest;


var defaultRepo =
  new RemoteRepository.classes[0]( "central", "default", "http://central.maven.org/maven2/" ).build();

function getLocalArtifact( localRepoManager, repositorySession, artifact )
{

  var request = new LocalArtifactRequest();

  request.setArtifact( artifact );

  var result = localRepoManager.find(repositorySession, request);

  return result;

}

function getArtifactDescriptor( repositorySession, artifact )
{

  var request = new ArtifactDescriptorRequest();

  request.setArtifact( artifact );

  var result = command.
                container.
                repositorySystem.
                readArtifactDescriptor(repositorySession, request);
  return result;

}

function getMetadata() {
  /*
  rr.printM( "localRepoManager", localRepoManager );

  rr.printC( org.eclipse.aether.metadata.Metadata );

  var request = new LocalMetadataRequest();

  var metadata = new DefaultMetadata("org.robovm",
                                      "robovm-objc",
                                      RELEASE_OR_SNAPSHOT);

  request.setMetadata( metadata );

  var result = localRepoManager.find(repositorySession, request);

  print( result );
  */

}
try {

rr.printC( "MavenRepositorySystemUtils", MavenRepositorySystemUtils );
rr.printC( "RepositoryUtils", RepositoryUtils );
rr.printM( "command", command.container.repositorySystem );

var settings = command.container.getSettings();

rr.printC( "settings", settings );

//var localRepo = RepositoryUtils.toArtifactRepository("local",
//                  new File(settings.localRepository).toURI().toURL().toString(), null, true, true);

var localRepo = new LocalRepository(settings.localRepository);

rr.printC( "localRepo", localRepo );

var repositorySession = MavenRepositorySystemUtils.newSession();

var localRepoManager = command.
                        container.
                        repositorySystem.
                        newLocalRepositoryManager(repositorySession, localRepo);

repositorySession.setLocalRepositoryManager( localRepoManager );




{
var artifact = new DefaultArtifact("org.robovm", "robovm-dist", "", "[0,)");

var repositories =  new java.util.ArrayList();
repositories.add( defaultRepo );

var request = new VersionRangeRequest();
request.setArtifact( artifact ) ;
request.setRepositories( repositories );

var result = command.
              container.
              repositorySystem.
              resolveVersionRange(repositorySession,request );

result.versions.forEach(  function( e,i ) {
  print(e);

  var a = new DefaultArtifact(artifact.getGroupId(),
                                artifact.getArtifactId(),
                                "nocompiler", "tar.gz", e.toString());

  var aa = getLocalArtifact( localRepoManager, repositorySession, a );

  print( aa );


});

}

"END";

}
catch( e ) {

  e.printStackTrace();
  print( "ERROR " + e);
}
