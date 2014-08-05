
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