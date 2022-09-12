package com.xuecheng.framework.domain.cms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 10:04.
 * @Modified By:
 */
@Data
@ToString
public class CmsPageParam {
    @ApiModelProperty("参数名称")
    private String pageParamName;

    @ApiModelProperty("参数值")
    private String pageParamValue;

}
