package org.azeroth.nalu;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseSqlContext {
   int tableIndex=0;
   int nextTableIndex(){
       return this.tableIndex++;
   }
   ArrayList<DbSet> lstTable=new ArrayList<>();
   WhereNode whereNode;
   DbSet fromTable;
   ArrayList<SelectNode> lstSelectNode=new ArrayList<>();
    int colindex=0;
    int nextColIndex() {
        return colindex++;
    }

    ArrayList<JoinNode> lstJoin=new ArrayList<>();

    int parameterIndex=0;
    String nextParameterName(){
        return "?";
    }
    ArrayList<Tuple.Tuple2<String,Object>> dictParameter=new ArrayList<>();
}
