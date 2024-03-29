package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author： WojtekZoo
 * @date： 2022/8/31 10:18
 * @modifiedBy：
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
}
