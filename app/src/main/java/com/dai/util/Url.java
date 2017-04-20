package com.dai.util;

/**
 * Created by Administrator on 2017/4/9 0009.
 */

public class Url {


    public static String getRegisterUrl() {
        return registerUrl;
    }
//    private static final String registerUrl = "http://8.34.216.83:8080/register";
//    private static final String registerUrl = "http://47.93.47.206:8080/mobile/register";
    private static final String registerUrl = "http://10.0.3.2:8080/mobile/register";

    public static String getLoginUrl() {
        return loginUrl;
    }

    private static final String loginUrl = "http://10.0.3.2:8080/mobile/login";
}
