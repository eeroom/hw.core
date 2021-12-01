package org.azeroth.nalu;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseSqlContext {
   private int tableIndex=0;
   int nextTableIndex(){
       return this.tableIndex++;
   }
   public ArrayList<DbSet> lstTable=new ArrayList<>();
   public WhereNode whereNode;
   public WhereNode havingNode;
   public DbSet fromTable;
   public ArrayList<SelectNode> lstSelectNode=new ArrayList<>();
    private int colindex=0;
    int nextColIndex() {
        return colindex++;
    }

    public ArrayList<JoinNode> lstJoinNode =new ArrayList<>();

    private int parameterIndex=0;
    protected String nextParameterName(){
        return "?";
    }
    public ArrayList<Tuple.Tuple2<String,Object>> lstDbParameter =new ArrayList<>();
    public ArrayList<OrderbyNode> lstOrderByNode=new ArrayList<>();
    public ArrayList<Column> lstGroupByNode=new ArrayList<>();
    public  int takerows;
    public  int skiprows;
}
