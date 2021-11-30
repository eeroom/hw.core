package org.azeroth.nalu.node;

import org.azeroth.nalu.DbSet;
import org.azeroth.nalu.IParseSql;
import org.azeroth.nalu.JoinOpt;
import org.azeroth.nalu.ParseSqlContext;

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
        var sql=String.format("%s %s AS %s ON %s",this.joinOpt.getName()
                ,this.rightDbSet.tableName
                ,this.rightDbSet.tableAlias
                ,this.jw.parse(context));
        return sql;
    }
}
