package com.king.crm.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/16
 */
public class EmailUtil {

    public static  boolean isMobile(String email){
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        // 验证手机号
        String s2="^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$";
        if(StringUtils.isNotBlank(email)){
            p = Pattern.compile(s2);
            m = p.matcher(email);
            b = m.matches();
        }
        return b;
    }

    public static void main(String[] args) {
        System.out.println(isMobile("132@qq.com"));
    }
}

