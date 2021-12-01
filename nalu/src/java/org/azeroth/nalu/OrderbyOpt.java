package org.azeroth.nalu;

public enum  OrderbyOpt {
    asc("asc"),
    desc("desc");
    private String sql;
    private OrderbyOpt(String sql){
        this.sql=sql;
    }
    public String getSql(){
        return this.sql;
    }
}
