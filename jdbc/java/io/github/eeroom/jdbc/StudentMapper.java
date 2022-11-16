package io.github.eeroom.jdbc;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {

    @Select("select * from Student")
    public List<Student> getAll();
}
