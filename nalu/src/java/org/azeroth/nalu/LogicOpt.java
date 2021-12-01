package org.azeroth.nalu;

enum LogicOpt {
    and("and"),
    or("or");
    private  String sql;
    private LogicOpt(String sql){
        this.sql=sql;
    }
    public String getSql(){
        return this.sql;
    }
}
