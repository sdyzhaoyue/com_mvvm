/*
 * ************************************************************
 * 文件：DomainBean.java  模块：http-compiler  项目：component
 * 当前修改时间：2019年04月23日 18:23:20
 * 上次修改时间：2019年04月13日 08:44:03
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：http-compiler
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.http.compiler.bean;

/**
 * Created by xu.yi. on 2019/4/2.
 * 解析出来的生成类注解信息
 */
public class DomainBean {
    private String host = "";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
