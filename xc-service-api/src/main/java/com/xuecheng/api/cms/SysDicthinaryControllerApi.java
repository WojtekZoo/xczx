package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author： WojtekZoo
 * @date： 2022/12/30 17:12
 * @modifiedBy：
 */
@Api(value = "数据字典接口", description = "提供数据字典接口的管理、查询功能")
public interface SysDicthinaryControllerApi {
    //数据字典
    @ApiOperation(value = "数据字典查询接口")
    public SysDictionary getByType(String type);
}
