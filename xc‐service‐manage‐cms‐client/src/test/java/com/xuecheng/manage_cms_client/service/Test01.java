package com.xuecheng.manage_cms_client.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author： WojtekZoo
 * @date： 2022/8/31 11:30
 * @modifiedBy：
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test01 {
    @Autowired
    PageService pageService;

    @Test
    public void savePageToServerPathTest(){
        pageService.savePageToServerPath("5a795ac7dd573c04508f3a56");
        System.out.println("success");
    }
}
