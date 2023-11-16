package io.github.eeroom.springcore.mybatis整合springmybatis.dao.hzoa;

import io.github.eeroom.springcore.mybatis整合springmybatis.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITeacher {

    public List<Teacher> getAll();
}
