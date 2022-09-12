package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author： WojtekZoo
 * @date： 2022/9/1 14:40
 * @modifiedBy：
 */
@Mapper
public interface TeachplanNodeMapper {
    TeachplanNode selectList(String courseId);
}
