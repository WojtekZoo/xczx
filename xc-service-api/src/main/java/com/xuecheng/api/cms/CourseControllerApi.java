package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author： WojtekZoo
 * @date： 2020/7/27 10:48
 * @modifiedBy：
 */
@Api(value = "课程管理接口", description = "课程管理接口，提供数据模型的管理、查询接口")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    @ApiImplicitParam(name = "courseId", value = "课程ID", required = true, paramType = "path", dataType = "String")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);
}
