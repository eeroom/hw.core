package org.azeroth.nalu;

public class Tuple {
    public static <T,B>  Tuple2<T, B> create(T t,B b){
        var tmp=new Tuple2<T, B>();
        tmp.item1=t;
        tmp.item2=b;
        return tmp;
    }

    public static class Tuple2<T,B>{
        T item1;
        B item2;
    }
}
