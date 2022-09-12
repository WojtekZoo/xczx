package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author： WojtekZoo
 * @date： 2020/7/27 10:48
 * @modifiedBy：
 */
@Api(value = "cms模板管理接口", description = "cms模板管理接口")
public interface CmsTemplateControllerApi {
    @ApiOperation("查询模板名称编号")
    QueryResponseResult listIdName();

}
