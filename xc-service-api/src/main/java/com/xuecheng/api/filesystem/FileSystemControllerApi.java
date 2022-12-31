package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author： WojtekZoo
 * @date： 2022/12/31 15:55
 * @modifiedBy：
 */
@Api(value = "文件管理接口", description = "文件管理接口，提供文件的处理功能")
public interface FileSystemControllerApi {

    @ApiOperation("文件上传")
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata);
}
