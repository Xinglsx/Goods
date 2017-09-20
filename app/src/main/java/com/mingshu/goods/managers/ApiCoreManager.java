package com.mingshu.goods.managers;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.models.Questions;
import com.mingshu.goods.models.UserInfo;

import java.util.HashMap;
import java.util.Map;

import winning.framework.BaseActivity;
import winning.framework.managers.ApiManager;

/**
 * Created by Lisx on 2017-06-27.
 */

public class ApiCoreManager extends ApiManager {

    public static String baseURL = "http://www.mingshukeji.com.cn:8080/GoodsService.svc";

    public ApiCoreManager(Context context){
        super((BaseActivity)context);
//        baseURL = "http://192.168.10.61:8890/GoodsService.svc";//公司内网服务器
//        baseURL = "http://182.61.58.192/GoodsService.svc";//百度外网服务器
//        baseURL = "";//阿里外网服务器
    }

    public Api getVersionInfo(){
        return createAPI(Request.Method.GET,baseURL+"/GetVersionInfo",null);
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

    public Api getUserInfos(int curPage, int pageSize, int type,String filter){
        Map param = new HashMap<>();
        param.put("curPage",String.valueOf(curPage));
        param.put("pageSize",String.valueOf(pageSize));
        param.put("type",String.valueOf(type));
        param.put("filter",filter);
        return createAPI(Request.Method.GET,baseURL+"/UserInfo/GetUserInfos",param);
    }

    public Api saveUserInfo(UserInfo userInfo){
        JSONObject param = new JSONObject();
        param.put("userInfo",userInfo);
        return createAPI(Request.Method.POST,baseURL+"/UserInfo/SaveUserInfo",param);
    }

    public Api changePassword(String id, String oldPassword, String newPassword){
        JSONObject param = new JSONObject();
        param.put("id",id);
        param.put("oldPassword",oldPassword);
        param.put("newPassword",newPassword);
//        MyLogUtil.LogShitou("param",param.toString());
        return createAPI(Request.Method.POST,baseURL+"/UserInfo/ChangePassword",param);
    }

    public Api saveQuestion(Questions questionInfo){
        JSONObject param = new JSONObject();
        param.put("questionInfo",questionInfo);
        return createAPI(Request.Method.POST,baseURL+"/UserInfo/SaveQuestion",param);
    }

    public Api getGoodsList(int curPage, int pageSize,int type){
        Map param = new HashMap<>();
        param.put("curPage",String.valueOf(curPage));
        param.put("pageSize",String.valueOf(pageSize));
        param.put("type",String.valueOf(type));
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
//        MyLogUtil.LogShitou("param",param.toString());
        return createAPI(Request.Method.POST,baseURL+"/Goods/SaveGoodsInfo",param);
    }

    public Api updatePictrue(String strBase64){
        JSONObject param = new JSONObject();
        param.put("strBase64",strBase64);
//        MyLogUtil.LogShitou("param",param.toString());
        return createAPI(Request.Method.POST,baseURL+"/Goods/UpdatePictrue",param);
    }

    public Api getAdvertisement(String key){
        Map param = new HashMap<>();
        param.put("key",key);
        return createAPI(Request.Method.GET,baseURL+"/Advertisement/GetAdvertisement",param);
    }

    public Api getCouponList(long pageNo, long pageSize, String q){
        Map param = new HashMap<>();
        param.put("pageNo",String.valueOf(pageNo));
        param.put("pageSize",String.valueOf(pageSize));
        param.put("q",q);
        return createAPI(Request.Method.GET,baseURL+"/Tbk/GetCouponList",param);
    }
}
