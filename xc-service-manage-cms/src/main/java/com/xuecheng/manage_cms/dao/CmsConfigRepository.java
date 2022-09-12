package com.xuecheng.manage_cms.dao;


import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author： WojtekZoo
 * @date： 2020/12/31 9:52
 * @modifiedBy：
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig, String> {

}
