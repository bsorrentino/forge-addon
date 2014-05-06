/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var reflection = require('introspection');

print(  java.lang.Class.forName("com.google.common.base.Absent") );
print(  com.google.common.base.Absent.class );
print(  com.google.common.base.Absent.class.name );
print(  Packages.com.google.common.base.Absent);
print(  Packages.org.dynjs.Clock );

reflection.printC( "dynjs ", dynjs );
reflection.printC( "dynjs.classpath ", dynjs.classpath );

reflection.printM( "dynjs", dynjs );
reflection.printM( "dynjs.classpath", dynjs.classpath );

"end";