package org.azeroth.nalu;

public enum ColOperator {
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
    private ColOperator(String name){
        this.name=name;
    }
    String name;

    public String  getName(){
        return this.name;
    }
}
