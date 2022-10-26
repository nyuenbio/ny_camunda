package com.nyuen.camunda.vo;

import lombok.Data;

import java.io.InputStream;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/25
 */
@Data
public class AttachmentVo {
    private String name;
    private String type;
    private String description;
    private String url;
    private InputStream is;
}
