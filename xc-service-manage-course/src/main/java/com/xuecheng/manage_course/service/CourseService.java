package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.TeachplanNodeMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author： WojtekZoo
 * @date： 2022/9/1 14:58
 * @modifiedBy：
 */
@Service
public class CourseService {
    @Autowired
    TeachplanNodeMapper teachplanNodeMapper;

    public TeachplanNode findTeachplanList(String courseId){
        return teachplanNodeMapper.selectList(courseId);
    }
}
