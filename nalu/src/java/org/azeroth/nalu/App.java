package org.azeroth.nalu;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Throwable {
        DbContext dbContext=new MyDbContext();
        var lst= dbContext.dbSet(StudentInfo.class)
                .select(x-> Columns.of(x.getAge(),x.getName()))
                .where(x->x.col(a->a.getAge()).lt(3))
                .join(dbContext.dbSet(ScoreInfo.class),(x,y)->x.col(a->a.getId()).eq(y.col(b->b.getStudentId())))
                .where(x->x.col(a->a.item2.getScore()).gt(80).or(x.col(a->a.item1.getAge()).in(33,44)))
                .toList();
        int a=3;
    }
    static class MyDbContext extends DbContext {
        @Override
        protected Connection getConnection() throws SQLException {
            String cnnstr="jdbc:sqlserver://127.0.0.1\\sqlexpress:1433;DatabaseName=hw";
            var cnn= java.sql.DriverManager.getConnection(cnnstr,"sa","123456");
            return cnn;
        }

        @Override
        protected ParseSqlContext getParseSqlContext() {
            return new ParseSqlContext();
        }
    }
    static class ScoreInfo {
        int id;
        int studentId;
        int score;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        int projectId;
    }

    static class StudentInfo {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        int age;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        int id;
    }
}
