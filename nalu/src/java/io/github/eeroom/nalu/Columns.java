package io.github.eeroom.nalu;

public class Columns {
    private Columns(){

    }
    public static Columns of(Object... cols){
        return new Columns();
    }
}
