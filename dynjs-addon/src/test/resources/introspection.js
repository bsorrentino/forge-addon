/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module.exports = {

    printC:function( name, o ) {
        if( o ) {
            print( name + ": " + o.class.name );
        }
    },
    _printM:function( methods ) {
      for( var i = 0; i< methods.length; ++i ) {
          print( "\t" + methods[i].name );
      }
    },
    printM:function( name, o ) {
        if( o ) {
            print( "Method of " +  name + " " + o.class.name);
            this._printM( o.class.methods );
        }
    },
    printDM:function( name, o ) {
        if( o ) {
            print( "Method of " +  name + " " + o.class.name);
            this._printM( o.class.declaredMethods );
        }
    },
    printSM:function( name, clazz ) {
        if( clazz ) {
            print( "Method of " +  name + " " + clazz.name);
            this._printM(clazz.methods );
        }
    }
};
