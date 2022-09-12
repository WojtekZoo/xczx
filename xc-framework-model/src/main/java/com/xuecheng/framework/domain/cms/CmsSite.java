package com.xuecheng.framework.domain.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.List;

/**
 * 页面站点对象
 *
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 9:46.
 * @Modified By:
 */
@Data
@ToString
@Document(collection = "cms_site")
@ApiModel(value = "cms站点对象", description = "cms站点对象")
public class CmsSite {

    //站点ID
    @Id
    private String siteId;
    //站点名称
    private String siteName;
    //站点名称
    private String siteDomain;
    //站点端口
    private String sitePort;
    //站点访问地址
    private String siteWebPath;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date siteCreateTime;
    //站点物理路径
    private String sitePhysicalPath;
}
