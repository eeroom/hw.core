package org.azeroth.nalu;

public class Tuple {
    public static <T,B>  Tuple2<T, B> create(T t,B b){
        var tmp=new Tuple2<T, B>();
        tmp.item1=t;
        tmp.item2=b;
        return tmp;
    }
    public static <T,B,X>  Tuple3<T,B,X> create(T t,B b,X x){
        var tmp=new Tuple3<T,B,X>();
        tmp.item1=t;
        tmp.item2=b;
        tmp.item3=x;
        return tmp;
    }
    public static <T,B,X,Y>  Tuple4<T,B,X,Y> create(T t,B b,X x,Y y){
        var tmp=new Tuple4<T,B,X,Y>();
        tmp.item1=t;
        tmp.item2=b;
        tmp.item3=x;
        tmp.item4=y;
        return tmp;
    }
    public static <T,B,X,Y,Z>  Tuple5<T,B,X,Y,Z> create(T t,B b,X x,Y y,Z z){
        var tmp=new Tuple5<T,B,X,Y,Z>();
        tmp.item1=t;
        tmp.item2=b;
        tmp.item3=x;
        tmp.item4=y;
        tmp.item5=z;
        return tmp;
    }

    public static class Tuple2<T,B>{
       public T item1;
       public B item2;
    }
    public static class Tuple3<T,B,X>{
        public T item1;
        public B item2;
        public X item3;
    }
    public static class Tuple4<T,B,X,Y>{
        public T item1;
        public B item2;
        public X item3;
        public Y item4;
    }
    public static class Tuple5<T,B,X,Y,Z>{
        public T item1;
        public B item2;
        public X item3;
        public Y item4;
        public Z item5;
    }
}
