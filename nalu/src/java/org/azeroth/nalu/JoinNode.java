package org.azeroth.nalu;

public class JoinNode implements IParseSql {

    DbSet rightDbSet;
    WhereNode jw;
    JoinOpt joinOpt;

    public JoinNode(DbSet rightDbSet,WhereNode jw,JoinOpt joinOpt){
        this.rightDbSet=rightDbSet;
        this.jw=jw;
        this.joinOpt=joinOpt;
    }

    @Override
    public String parse(ParseSqlContext context) {
        var sql=String.format("%s %s AS %s ON %s",this.joinOpt.getSql()
                ,this.rightDbSet.tableName
                ,this.rightDbSet.tableAlias
                ,this.jw.parse(context));
        return sql;
    }
}
