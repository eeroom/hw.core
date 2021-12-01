package org.azeroth.nalu;

import java.util.ArrayList;

public class WhereNodeLeaf<C> extends WhereNode {
    Column<C> col;
    ColOpt opt;
    Object value;
    public WhereNodeLeaf(Column<C> col, ColOpt opt, Object value){
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
        context.dictParameter.add(Tuple.create(pname,this.value));
        var sql= String.format("%s %s %s",this.col.parse(context),this.opt.getSql(),pname);
        return sql;
    }

    String toSqlWithIn(ParseSqlContext context) {
        var lstValue = (C[]) this.value;
        ArrayList<String> lstpName=new ArrayList<>();
        for (C obj : lstValue) {
            var pname=context.nextParameterName();
            lstpName.add(pname);
            context.dictParameter.add(Tuple.create(pname,obj));
        }
        var sql= String.format("%s %s (%s)",this.col.parse(context),this.opt.getSql(),String.join(",",lstpName));
        return sql;
    }
}
