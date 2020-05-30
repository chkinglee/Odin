package org.chkinglee.norn.odin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用工具
 *
 * @Author: lilinzhen
 * @Version: 2020/5/30
 **/
public class Util {


    public static String getCurrentDateStr() {
        Date d = new Date(System.currentTimeMillis());
        String date = new SimpleDateFormat("yyyyMMdd").format(d);
        return date;
    }
}
