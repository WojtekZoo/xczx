package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 页面控制器
 *
 * @author： WojtekZoo
 * @date： 2020/12/28 12:19
 * @modifiedBy：
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    PageService pageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return pageService.findList(page, size, queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    @Override
    @GetMapping("/findById/{pageId}")
    public CmsPageResult findById(@PathVariable("pageId") String pageId) {
        return pageService.findById(pageId);
    }

    @Override
    @PutMapping("/editById/{pageId}")
    public CmsPageResult editById(@PathVariable("pageId") String pageId, @RequestBody CmsPage cmsPage) {
        return pageService.editById(pageId, cmsPage);
    }

    @Override
    @DeleteMapping("delById/{pageId}")
    public ResponseResult delById(@PathVariable("pageId") String pageId) {
        return pageService.delById(pageId);
    }

    @Override
    @PostMapping("/post/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.post(pageId);
    }
}
