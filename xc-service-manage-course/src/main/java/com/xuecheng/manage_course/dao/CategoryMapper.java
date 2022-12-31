package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author： WojtekZoo
 * @date： 2022/12/30 16:53
 * @modifiedBy：
 */
@Mapper
public interface CategoryMapper {
    public CategoryNode selectList();
}
