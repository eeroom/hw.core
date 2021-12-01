package org.azeroth.nalu.model;

import org.azeroth.nalu.Columns;
import org.azeroth.nalu.DbContext;

public class T4 {

    public static void test() throws Throwable {
        DbContext dbContext=new DbContext();
        var lst= dbContext.DbSet(StudentInfo.class)
                .select(x-> Columns.of(x.getAge(),x.getName()))
                .where(x->x.col(a->a.getAge()).lt(3))
                .join(dbContext.DbSet(ScoreInfo.class),(x,y)->x.col(a->a.getId()).eq(y.col(b->b.getStudentId())))
                .where(x->x.col(a->a.item2.getScore()).gt(80).or(x.col(a->a.item1.getAge()).in(33,44)))
                .toList();

    }
}
