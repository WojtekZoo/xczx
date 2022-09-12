package com.xuecheng.framework.domain.cms;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Created by admin on 2018/2/6.
 */
@Data
@ToString
public class CmsConfigModel {
    private String key;
    private String nagroup1me;
    private String url;
    private Map mapValue;
    private String value;

}
