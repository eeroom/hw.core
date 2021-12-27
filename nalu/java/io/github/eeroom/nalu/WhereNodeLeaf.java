package io.github.eeroom.nalu;

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
            case notin:
                return this.toSqlWithIn(context);
            case eqnull:
            case noteqnull:
                return this.toSqlWithNull(context);
            case between:
            case notbetween:
                return this.toSqlWithBetween(context);
            case exists:
            case notexists:
                return this.toSqlWithExists(context);
            default:
                break;
        }
        var pname=context.nextParameterName();
        if(this.value instanceof Enum){
            context.lstDbParameter.add(Tuple.create(pname,this.value.toString()));
        }else {
            context.lstDbParameter.add(Tuple.create(pname,this.value));
        }
        var sql= String.format("%s %s %s",this.col.parse(context),this.opt.getSql(),pname);
        return sql;
    }

    String toSqlWithIn(ParseSqlContext context) {
        var lstValue = (C[]) this.value;
        ArrayList<String> lstpName=new ArrayList<>();
        for (C obj : lstValue) {
            var pname=context.nextParameterName();
            lstpName.add(pname);
            if(obj instanceof Enum){
                context.lstDbParameter.add(Tuple.create(pname,obj.toString()));
            }else {
                context.lstDbParameter.add(Tuple.create(pname,obj));
            }
        }
        var sql= String.format("%s %s (%s)",this.col.parse(context),this.opt.getSql(),String.join(",",lstpName));
        return sql;
    }

     String toSqlWithNull(ParseSqlContext context){
        var sql=String.format("%s %s",this.col.parse(context),this.opt.getSql());
        return sql;
     }

    String toSqlWithBetween(ParseSqlContext context){
        var rangeValue=(C[])this.value;
        var pname1=context.nextParameterName();
        context.lstDbParameter.add(Tuple.create(pname1,rangeValue[0]));
        var pname2=context.nextParameterName();
        context.lstDbParameter.add(Tuple.create(pname1,rangeValue[1]));
        var sql=String.format("%s %s %s and %s",this.col.parse(context),this.opt.getSql(),pname1,pname2);
        return  sql;
    }

     String toSqlWithExists(ParseSqlContext context){
        throw  new IllegalArgumentException("方法未完成");
     }
}
