package org.azeroth.nalu;

public enum ColOpt {
    lt("<"),
    gt(">"),
    eq("="),
    lteq("<="),
    gteq(">="),
    like("like"),
    in("in"),
    notin("not in"),
    notlike("not like"),
    between("between"),
    notbetween("not between");
    private ColOpt(String sql){
        this.sql=sql;
    }
    String sql;

    public String getSql(){
        return this.sql;
    }
}
