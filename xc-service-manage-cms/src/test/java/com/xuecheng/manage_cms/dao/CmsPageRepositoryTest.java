package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import feign.RequestTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author： WojtekZoo
 * @date： 2020/12/31 9:54
 * @modifiedBy：
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void testFindPage() {
        //分页参数
        int page = 0;//从0开始
        int size = 10;//共10页
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);

    }

    @Test
    public void testUpdate() {
        Optional<CmsPage> optional = cmsPageRepository.findById("5abefd525b05aa293098fca6");
        //optional: jdk1.8中的容器对象，将对象的非空判断标准化了
        //判断是否为空
        if (optional.isPresent()) {
            //optional.get()返回对象
            CmsPage cmsPage = optional.get();
            cmsPage.setPageAliase("ccc1");
            //save保存后会返回修改后的结果
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }
    }

    @Test
    public void testFindByPageName() {
        CmsPage cmsPage = cmsPageRepository.findByPageName("10101.html");
        CmsPage byPageNameAndSiteIdAndPageWebPath = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath("preview_4028e58161bd3b380161bd3bcd2f0000.html", "5a751fab6abb5044e0d19ea1", "/coursepre/");
        System.out.println(byPageNameAndSiteIdAndPageWebPath);
        System.out.println("1111111111");
        System.out.println(cmsPage);
    }

    @Test
    public void testFindByCondition() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        //条件值存放
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("cc");
        //条件筛选策略(条件匹配器)
        ExampleMatcher matching = ExampleMatcher.matching()
                //模糊匹配
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage, matching);
        Page<CmsPage> res = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> pages = res.getContent();
        long totalPages = res.getTotalElements();
    }

    @Test
    public void testRequestTemplate(){
        ResponseEntity<Map> entity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f", Map.class);
        Map body = entity.getBody();
        System.out.println(body);
    }
}
