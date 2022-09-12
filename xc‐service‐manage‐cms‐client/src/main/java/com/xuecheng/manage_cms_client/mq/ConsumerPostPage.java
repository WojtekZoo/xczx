package com.xuecheng.manage_cms_client.mq;

import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 消息消费
 *
 * @author： WojtekZoo
 * @date： 2022/8/31 15:00
 * @modifiedBy：
 */
@Component
public class ConsumerPostPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {
        LOGGER.info("receive cms post page:{}", msg);
        //解析消息
        JSONObject jsonObject = JSONObject.parseObject(msg);
        //取出页面ID
        String pageId = jsonObject.getString("pageId");
        //查询页面
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            LOGGER.error("receive cms post page,cmsPage is null:{}", msg);
            return;
        }
        //将页面保存到服务器物理路径
        pageService.savePageToServerPath(pageId);

    }
}
