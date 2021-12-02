package org.azeroth.nalu;

public enum FunctionOpt {
    sum("sum"),
    avg("avg"),
    count("count"),
    max("max"),
    min("min"),
    lower("lower"),
    upper("upper");
    private  String sql;
    private FunctionOpt(String sql){
        this.sql=sql;
    }
    public  String getSql(){
        return this.sql;
    }
}
