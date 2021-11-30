package org.azeroth.nalu;

import org.azeroth.nalu.node.JoinNode;
import org.azeroth.nalu.node.SelectNode;
import org.azeroth.nalu.node.WhereNode;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseSqlContext {
   int tableIndex=0;
   public int nextTableIndex(){
       return this.tableIndex++;
   }
   ArrayList<DbSet> lstTable=new ArrayList<>();
   WhereNode whereNode;
   DbSet fromTable;
   ArrayList<SelectNode> lstSelectNode=new ArrayList<>();
    int colindex=0;
    public int nextColIndex() {
        return colindex++;
    }

    ArrayList<JoinNode> lstJoin=new ArrayList<>();

    int parameterIndex=0;
    public String nextParameterName(){
        return "p_"+(this.parameterIndex++)+"_p";
    }
    public HashMap<String,Object> dictParameter=new HashMap<>();
}
