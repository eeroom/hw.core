package org.azeroth.nalu.node;

import org.azeroth.nalu.ColOperator;
import org.azeroth.nalu.Column;
import org.azeroth.nalu.ParseSqlContext;

import java.util.ArrayList;
import java.util.Arrays;

public class WhereNodeLeaf<C> extends WhereNode {
    Column<C> col;
    ColOperator opt;
    Object value;
    public WhereNodeLeaf(Column<C> col, ColOperator opt, Object value){
        this.col=col;
        this.opt=opt;
        this.value=value;
    }


    @Override
    public String parse(ParseSqlContext context) {
        switch (this.opt){
            case in:
                return this.toSqlWithIn(context);
            case notin:
                return this.toSqlWithIn(context);
            default:
                break;
        }
        var pname=context.nextParameterName();
        context.dictParameter.put(pname,this.value);
        var sql= String.format("%s %s %s",this.col.parse(context),this.opt.getName(),pname);
        return sql;
    }

    String toSqlWithIn(ParseSqlContext context) {
        var lstValue = (C[]) this.value;
        ArrayList<String> lstpName=new ArrayList<>();
        for (C obj : lstValue) {
            var pname=context.nextParameterName();
            lstpName.add(pname);
            context.dictParameter.put(pname,obj);
        }
        var sql= String.format("%s %s (%s)",this.col.parse(context),this.opt.getName(),String.join(",",lstpName));
        return sql;
    }
}
