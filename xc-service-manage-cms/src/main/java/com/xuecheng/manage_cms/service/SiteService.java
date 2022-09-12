package com.xuecheng.manage_cms.service;

import cn.hutool.core.lang.Validator;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 站点服务
 *
 * @author： WojtekZoo
 * @date： 2020/12/31 10:46
 * @modifiedBy：
 */
@Service
public class SiteService {
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    public QueryResponseResult findAll() {
        List<CmsSite> all = cmsSiteRepository.findAll();
        QueryResult<CmsSite> res = new QueryResult<>();
        res.setList(all);
        return new QueryResponseResult(CommonCode.SUCCESS, res);
    }
}
