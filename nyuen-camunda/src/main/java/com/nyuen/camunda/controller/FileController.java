package com.nyuen.camunda.controller;

import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLDecoder;
import java.util.Date;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/25
 */
@Api(tags = "文件操作控制类")
@RestController
@RequestMapping("/file")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.saveRootPath}")
    private String saveRootPath;
    @Value("${file.readRootPath}")
    private String readRootPath;

    @ApiOperation(value = "上传文件,返回文件路径url", httpMethod = "POST")
    @PostMapping("/uploadFile")
    public Result uploadFile(@RequestBody MultipartFile multipartFile) {
        // 上传文件到服务器
        String filePath = null;
        String originalFilename;
        File file;
        String readPath = "";
        try {
            originalFilename = multipartFile.getOriginalFilename();
            String now = DateUtil.DateToString(new Date(), "yyyyMMdd");
            String rootPath = saveRootPath + now + "/";
            File rootFile = new File(rootPath);
            if (!rootFile.isDirectory()) {
                rootFile.mkdirs();
            }
            filePath = rootPath + originalFilename;
            file = new File(filePath);
            multipartFile.transferTo(file);
            readPath = readRootPath + now + "/"+ originalFilename;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ResultFactory.buildSuccessResult(readPath);
    }



}
