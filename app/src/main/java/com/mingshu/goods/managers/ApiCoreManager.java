package com.mingshu.goods.managers;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.mingshu.goods.models.GoodsInfo;

import java.util.HashMap;
import java.util.Map;

import winning.framework.BaseActivity;
import winning.framework.managers.ApiManager;

/**
 * Created by Lisx on 2017-06-27.
 */

public class ApiCoreManager extends ApiManager {

    private String baseURL = "";

    public ApiCoreManager(Context context){
        super((BaseActivity)context);
//        baseURL = "http://100.91.28.177:8890/GoodsService.svc";
        baseURL = "http://192.168.10.61:8890/GoodsService.svc";
//        baseURL = "http://192.168.2.108:8890/GoodsService.svc";
    }

    public Api validateUserInfo(String strCode, String password){
        JSONObject param = new JSONObject();
        param.put("strCode",strCode);
        param.put("password",password);
        return createAPI(Request.Method.POST,baseURL+"/UserInfo/ValidateUserInfo",param);
    }

    public Api registerUserInfo(String strCode, String password){
        JSONObject param = new JSONObject();
        param.put("strCode",strCode);
        param.put("password",password);
        return createAPI(Request.Method.POST,baseURL+"/UserInfo/RegisterUserInfo",param);
    }

    public Api getGoodsList(int curPage, int pageSize){
        Map param = new HashMap<>();
        param.put("curPage",String.valueOf(curPage));
        param.put("pageSize",String.valueOf(pageSize));
        return createAPI(Request.Method.GET,baseURL+"/Goods/GetGoodsList",param);
    }

    public Api clickCounIncrement(String goodsId){
        JSONObject param = new JSONObject();
        param.put("goodsId",goodsId);
        return createAPI(Request.Method.POST,baseURL+"/Goods/ClickCounIncrement",param);
    }

    public Api saveGoodsInfo(GoodsInfo goodsInfo){
        JSONObject param = new JSONObject();
        param.put("goodsInfo",goodsInfo);
        //Log.e("param",param.toString());
        return createAPI(Request.Method.POST,baseURL+"/Goods/SaveGoodsInfo",param);
    }
}
