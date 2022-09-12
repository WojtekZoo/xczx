package com.xuecheng.manage_cms.dao;


import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author： WojtekZoo
 * @date： 2020/12/31 9:52
 * @modifiedBy：
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {

}
