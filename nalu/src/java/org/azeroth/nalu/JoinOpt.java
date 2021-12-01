package org.azeroth.nalu;

enum JoinOpt {
    inner("inner join",1),
    left("left join",2),
    right("right join",3),
    none("none",4);
    private String name;
    private int index;
    private JoinOpt(String name,int index){
        this.name=name;
        this.index=index;
    }

    String getName(){
        return  this.name;
    }

}
