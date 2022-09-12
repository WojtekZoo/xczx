package com.xuecheng.manage_cms_client.service;

import cn.hutool.core.lang.Validator;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.Optional;

/**
 * @author： WojtekZoo
 * @date： 2022/8/31 10:22
 * @modifiedBy：
 */
@Service
public class PageService {
    @Resource
    CmsPageRepository cmsPageRepository;

    @Resource
    CmsSiteRepository cmsSiteRepository;

    @Resource
    GridFsTemplate gridFsTemplate;

    @Resource
    GridFSBucket gridFSBucket;

    /**
     * 将页面html保存到页面物理路径
     *
     * @param pageId
     */
    public void savePageToServerPath(String pageId) {
        //根据页面ID获取页面信息
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();
        //获取站点信息
        CmsSite cmsSite = this.getCmsSiteById(cmsPage.getSiteId());
        //整理页面物理路径
        String dir = cmsSite.getSitePhysicalPath() + cmsPage.getPagePhysicalPath();
        String pagePath =  dir + cmsPage.getPageName();
        File file = new File(dir);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }

        //获取页面静态文件
        InputStream is = this.getFileById(cmsPage.getHtmlFileId());
        if (Validator.isEmpty(is)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //文件内容序列化到页面物理路径
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pagePath);
            IOUtils.copy(is, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    public InputStream getFileById(String fileId) {
        try {
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CmsSite getCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_SITE_NOTEXISTS);
        }
        return optional.get();
    }
}
