package com.mingshu.goods.utils;

import android.content.Context;

import java.util.Map;

import winning.framework.BaseApplication;

/**
 * Created by Lisx on 2017-06-28.
 */

public class ApplicationUtil {
    public ApplicationUtil() {}

    public static Map getData(Context context){
        BaseApplication baseApplication = (BaseApplication)context.getApplicationContext();
        return baseApplication.getData();
    }

    public static Map put(Context context, String key, Object value){
        Map map = getData(context);
        map.put(key,value);
        return map;
    }

    public static Object get(Context context, String key){
        Map map = getData(context);
        return map.get(key);
    }
}
