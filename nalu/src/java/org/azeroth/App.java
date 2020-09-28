package org.azeroth;

import org.azeroth.model.StudentInfo;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Query query=new Query();
        var tbStudentInfo= query.DbSet(StudentInfo.class);
        var col= tbStudentInfo.col(x->x.getAge()).lt(3);
        var col2= tbStudentInfo.col(x->x.getName());

        var lstStudent= query.Where(tbStudentInfo.col(x->x.getName()).like("刘%").or(tbStudentInfo.col(x->x.getName()).eq("张山")))
                             .Where(tbStudentInfo.col(x->x.getAge()).in(18,22,43))
                             .<StudentInfo>toList();
        ;
    }
}
