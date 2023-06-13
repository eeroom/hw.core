package io.github.eeroom.springcore.mybatis.dao.hzoa;

import io.github.eeroom.springcore.mybatis.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITeacher {

    public List<Teacher> getAll();
}
