package org.azeroth.nalu;

import org.azeroth.nalu.model.ScoreInfo;
import org.azeroth.nalu.model.StudentInfo;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Throwable {
        System.out.println( "Hello World!" );
        DbContext dbContext=new DbContext();
        var lst= dbContext.DbSet(StudentInfo.class)
                .select(x->Columns.of(x.getAge(),x.getName()))
                .where(x->x.col(a->a.getAge()).lt(3))
                .join(dbContext.DbSet(ScoreInfo.class),(x,y)->x.col(a->a.getName()).eq("zhang你"))
                .where(x->x.col(a->a.item2.getScore()).gt(80).or(x.col(a->a.item1.getAge()).in(33,44)))
                .toList();
    }
}
