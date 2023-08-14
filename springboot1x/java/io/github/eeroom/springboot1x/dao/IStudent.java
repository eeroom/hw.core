package io.github.eeroom.springboot1x.dao;

import io.github.eeroom.springboot1x.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStudent {
    public List<Student> getAll();
}
