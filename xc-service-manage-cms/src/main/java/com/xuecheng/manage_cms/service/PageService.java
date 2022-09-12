package com.xuecheng.manage_cms.service;

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @author： WojtekZoo
 * @date： 2020/12/31 10:46
 * @modifiedBy：
 */
@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 页面查询
     *
     * @param page             页码从1开始计数
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;

        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        //拼装查询条件
        CmsPage cmsPage = new CmsPage();
        if (Validator.isNotEmpty(queryPageRequest)) {
            //站点ID
            if (Validator.isNotEmpty(queryPageRequest.getSiteId())) {
                cmsPage.setSiteId(queryPageRequest.getSiteId());
            }
            //模板ID
            if (Validator.isNotEmpty(queryPageRequest.getTemplateId())) {
                cmsPage.setTemplateId(queryPageRequest.getTemplateId());
            }
            //页面别名
            if (Validator.isNotEmpty(queryPageRequest.getPageAliase())) {
                cmsPage.setPageAliase(queryPageRequest.getPageAliase());
            }
        }
        //条件筛选策略(条件匹配器)
        ExampleMatcher matching = ExampleMatcher.matching()
                //页面别名模糊匹配
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains())
                //站点ID精确匹配
                .withMatcher("siteId", ExampleMatcher.GenericPropertyMatchers.exact())
                //模板ID精确匹配
                .withMatcher("templateId", ExampleMatcher.GenericPropertyMatchers.exact());
        //条件对象
        Example<CmsPage> example = Example.of(cmsPage, matching);

        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult<CmsPage> result = new QueryResult<>();
        result.setList(all.getContent());//数据列表
        result.setTotal(all.getTotalElements());//总记录数
        return new QueryResponseResult(CommonCode.SUCCESS, result);
    }

    /**
     * 页面新增
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        //页面已经存在
        if (Validator.isNotEmpty(cmsPage1)) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTS);
        }
        cmsPage.setPageId(null);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPageRepository.save(cmsPage));
    }

    /**
     * 根据ID查询详情
     *
     * @param pageId
     * @return
     */
    public CmsPageResult findById(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        return optional.map(cmsPage -> new CmsPageResult(CommonCode.SUCCESS, cmsPage)).orElse(new CmsPageResult(CommonCode.FAIL, null));
    }

    /**
     * 根据ID修改
     *
     * @param pageId
     * @param cmsPage
     * @return
     */
    public CmsPageResult editById(String pageId, CmsPage cmsPage) {
        CmsPage one = null;
        CmsPageResult cpr = this.findById(pageId);
        if (cpr.isSuccess()) {
            one = cpr.getCmsPage();
        }
        if (Validator.isNotEmpty(one)) {
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新数据url
            one.setDataUrl(cmsPage.getDataUrl());
            //执行更新
            CmsPage save = cmsPageRepository.save(one);
            //返回成功
            return new CmsPageResult(CommonCode.SUCCESS, save);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 根据页面ID删除
     *
     * @param pageId
     * @return
     */
    public ResponseResult delById(String pageId) {
        CmsPageResult cpr = this.findById(pageId);
        if (cpr.isSuccess()) {
            cmsPageRepository.deleteById(pageId);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 页面静态化方法
     * <p>
     * 静态化程序获取页面的DataUrl
     * <p>
     * 静态化程序远程请求DataUrl获取数据模型。
     * <p>
     * 静态化程序获取页面的模板信息
     * <p>
     * 执行页面静态化
     */
    public String getPageHtml(String pageId) {

        //获取数据模型
        Map model = getModelByPageId(pageId);
        if (model == null) {
            //数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //获取页面的模板信息
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //执行静态化
        return generateHtml(template, model);

    }

    //执行静态化
    private String generateHtml(String templateContent, Map model) {
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateContent);
        //向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            Template template = configuration.getTemplate("template");
            //调用api进行静态化
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取页面的模板信息
    private String getTemplateByPageId(String pageId) {
        //取出页面的信息
        CmsPage cmsPage = null;
        CmsPageResult cpr = this.findById(pageId);
        if (cpr.isSuccess()) {
            cmsPage = cpr.getCmsPage();
        }
        if (cmsPage == null) {
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取页面的模板id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //从GridFS中取模板文件内容
            //根据文件id查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            //从流中取数据
            try {
                return IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    //获取数据模型
    private Map getModelByPageId(String pageId) {
        //取出页面的信息
        CmsPage cmsPage = null;
        CmsPageResult cpr = this.findById(pageId);
        if (cpr.isSuccess()) {
            cmsPage = cpr.getCmsPage();
        }
        if (cmsPage == null) {
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出页面的dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            //页面dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //通过restTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        HashMap<String, Object> res = new HashMap<>();
        res.put("model", body);
        return res;
    }

    /**
     * 根据页面ID发布页面
     *
     * @param pageId
     * @return
     */
    public ResponseResult post(String pageId) {
        //执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        if (Validator.isEmpty(pageHtml)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //保存静态化文件到gridFS
        CmsPage cmsPage = this.saveHtml(pageId, pageHtml);
        //发送MQ消息
        this.sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private void sendPostPage(String pageId) {
        //校验页面是否存在
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();
        //封装消息内容
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageId", pageId);
        //站点id作为routingKey, 发布消息
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, cmsPage.getSiteId(), jsonObject.toJSONString());
    }

    /**
     * 将静态化文件保存到gridFS
     *
     * @param pageId
     * @param pageHtml
     * @return
     */
    private CmsPage saveHtml(String pageId, String pageHtml) {
        //校验页面是否存在
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();
        //删除之前的内容
        String htmlFileId = cmsPage.getHtmlFileId();
        if (Validator.isNotEmpty(htmlFileId)) {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        //保存html文件到GridFS
        InputStream is = IOUtils.toInputStream(pageHtml, StandardCharsets.UTF_8);
        String id = gridFsTemplate.store(is, cmsPage.getPageName()).toString();
        //将文件ID保存到cmsPage中
        cmsPage.setHtmlFileId(id);
        return cmsPageRepository.save(cmsPage);
    }

}
