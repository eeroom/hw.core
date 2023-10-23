package io.github.eeroom.springboot1x.基本使用.dao;

import io.github.eeroom.springboot1x.基本使用.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStudent {
    public List<Student> getAll();
}
