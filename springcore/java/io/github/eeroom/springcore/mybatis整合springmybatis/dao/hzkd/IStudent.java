package io.github.eeroom.springcore.mybatis整合springmybatis.dao.hzkd;

import io.github.eeroom.springcore.mybatis整合springmybatis.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStudent {
    public List<Student> getAll();
}