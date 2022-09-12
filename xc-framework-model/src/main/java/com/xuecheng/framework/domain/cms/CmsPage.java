package com.xuecheng.framework.domain.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 10:04.
 * @Modified By:
 */
@Data
@ToString
@Document(collection = "cms_page")
@ApiModel(value="cms页面对象", description="cms页面对象")
public class CmsPage {
    /**
     * 页面名称、别名、访问地址、类型（静态/动态）、页面模版、状态
     */
    @ApiModelProperty("站点id")
    private String siteId;

    @Id
    @ApiModelProperty("页面ID")
    private String pageId;

    @ApiModelProperty("页面名称")
    private String pageName;

    @ApiModelProperty("别名")
    private String pageAliase;

    @ApiModelProperty("访问地址")
    private String pageWebPath;

    @ApiModelProperty("参数")
    private String pageParameter;

    @ApiModelProperty("物理路径")
    private String pagePhysicalPath;

    @ApiModelProperty("类型（静态/动态）")
    private String pageType;

    @ApiModelProperty("页面模版")
    private String pageTemplate;

    @ApiModelProperty("页面静态化内容")
    private String pageHtml;

    @ApiModelProperty("状态")
    private String pageStatus;

    @ApiModelProperty("创建时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date pageCreateTime;

    @ApiModelProperty("模版id")
    private String templateId;

    @ApiModelProperty("参数列表")
    private List<CmsPageParam> pageParams;

    //    @ApiModelProperty("模版文件Id")
//    private String templateFileId;

    @ApiModelProperty("静态文件Id")
    private String htmlFileId;

    @ApiModelProperty("数据Url")
    private String dataUrl;
}
