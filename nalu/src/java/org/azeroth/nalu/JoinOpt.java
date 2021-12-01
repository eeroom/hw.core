package org.azeroth.nalu;

enum JoinOpt {
    inner("inner join"),
    left("left join"),
    right("right join"),
    none("none");
    private String sql;
    private JoinOpt(String sql){
        this.sql=sql;
    }

    String getSql(){
        return  this.sql;
    }

}
