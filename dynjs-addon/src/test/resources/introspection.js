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
    printM:function( name, o ) {
        if( o ) { 
            var methods = o.class.declaredMethods;
            //var methods = o.getClass().getMethods();
            print( "Method of " +  name);
            for( var i = 0; i< methods.length; ++i ) {
                print( "\t" + methods[i].name );
            }  
        }
    }
};