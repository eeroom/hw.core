package io.github.eeroom.springcore.整合mybatis.dao.hzoa;

import io.github.eeroom.springcore.整合mybatis.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITeacher {

    public List<Teacher> getAll();
}
