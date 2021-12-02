package org.azeroth.nalu;

public enum ColOpt {
    lt("<"),
    gt(">"),
    eq("="),
    noteq("!="),
    lteq("<="),
    gteq(">="),
    like("like"),
    in("in"),
    notin("not in"),
    notlike("not like"),
    between("between"),
    notbetween("not between"),
    eqnull("is null"),
    noteqnull("is not null"),
    exists("exists"),
    notexists("not exists");
    private ColOpt(String sql){
        this.sql=sql;
    }
    String sql;

    public String getSql(){
        return this.sql;
    }
}
