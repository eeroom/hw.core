package io.github.eeroom.springcore.整合mybatis.dao.hzkd;

import io.github.eeroom.springcore.整合mybatis.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStudent {
    public List<Student> getAll();
}