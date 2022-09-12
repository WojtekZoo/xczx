package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author： WojtekZoo
 * @date： 2020/7/27 10:48
 * @modifiedBy：
 */
@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    /**
     * 页面查询
     *
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    @ApiOperation("新增页面")
    CmsPageResult add(CmsPage cmsPage);

    /**
     * 根据编号查询页面详情
     *
     * @param pageId
     * @return
     */
    @ApiOperation("根据编号查询页面详情")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    CmsPageResult findById(String pageId);

    /**
     * 根据编号修改页面
     *
     * @param pageId
     * @param cmsPage
     * @return
     */
    @ApiOperation("根据编号修改页面")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    CmsPageResult editById(String pageId, CmsPage cmsPage);

    /**
     * 根据编号删除页面
     *
     * @param pageId
     * @return
     */
    @ApiOperation("根据编号删除页面")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    ResponseResult delById(String pageId);

    /**
     * 根据编号发布页面
     *
     * @param pageId
     * @return
     */
    @ApiOperation("根据编号发布页面")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    ResponseResult post(String pageId);
}
