package org.bsc.functional;

/**
 * Created by bsorrentino on 15/07/14.
 */
public interface Functional {

    public interface F<P> {

        void f( P param );
    }
    public interface F2<P1,P2> {

        void f( P1 param1, P2 param2 );
    }

    public interface Fn<P,R> {

        R f( P param );
    }
    public interface Fn2<P1,P2,R> {

        R f( P1 param1, P2 param2 );
    }

}
