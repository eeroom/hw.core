package io.github.eeroom.nalu;

public class SelectNode implements IParseSql {
    Column<?> column;

    public SelectNode(Column column){
        this.column=column;
    }

    public String nameNick;
    public int index;

    @Override
    public String parse(ParseSqlContext context) {
        if(this.nameNick==null ||this.nameNick.length()<1)
            return this.column.parse(context);
        return this.column.parse(context)+" as "+this.nameNick;
    }
}
