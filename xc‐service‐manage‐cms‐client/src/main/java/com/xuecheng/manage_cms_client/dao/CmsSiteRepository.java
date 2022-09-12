package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author： WojtekZoo
 * @date： 2022/8/31 10:19
 * @modifiedBy：
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {
}
