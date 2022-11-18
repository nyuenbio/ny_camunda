package com.nyuen.camunda.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/18
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String uploadFile(MultipartFile multipartFile, String saveRootPath, String readRootPath) {
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
        return readPath;
    }
}
