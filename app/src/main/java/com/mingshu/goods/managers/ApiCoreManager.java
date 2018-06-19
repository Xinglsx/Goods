package com.mingshu.goods.managers;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.models.Questions;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.MyLogUtil;

import java.util.HashMap;
import java.util.Map;

import winning.framework.BaseActivity;
import winning.framework.managers.ApiManager;

/**
 * Created by Lisx on 2017-06-27.
 */

public class ApiCoreManager extends ApiManager {

//    public static String baseURL = "http://www.mingshukeji.com.cn:8080/GoodsService.svc";
    public static String baseApiURL = "http://www.mingshukeji.com.cn/api";

    public ApiCoreManager(Context context){
        super((BaseActivity)context);
    }

    public Api getVersionInfo(){
        return createAPI(Request.Method.GET,baseApiURL+"/Version/GetVersionInfo",null);
    }

    public Api validateUserInfo(String strCode, String password){
        JSONObject param = new JSONObject();
        param.put("strCode",strCode);
        param.put("password",password);
        return createAPI(Request.Method.POST,baseApiURL+"/Users/ValidateUserInfo",param);
    }

    public Api registerUserInfo(String strCode, String password){
        JSONObject param = new JSONObject();
        param.put("strCode",strCode);
        param.put("password",password);
        return createAPI(Request.Method.POST,baseApiURL+"/Users/RegisterUserInfo",param);
    }

    public Api getUserInfos(int curPage, int pageSize, int type,String filter){
        Map param = new HashMap<>();
        param.put("curPage",String.valueOf(curPage));
        param.put("pageSize",String.valueOf(pageSize));
        param.put("type",String.valueOf(type));
        param.put("filter",filter);
        return createAPI(Request.Method.GET,baseApiURL+"/Users/GetUserInfos",param);
    }

    public Api saveUserInfo(UserInfo userInfo){
        JSONObject param = new JSONObject();
        param.put("userInfo",userInfo);
        return createAPI(Request.Method.POST,baseApiURL+"/Users/SaveUserInfo",param);
    }

    public Api changePassword(String id, String oldPassword, String newPassword){
        JSONObject param = new JSONObject();
        param.put("id",id);
        param.put("oldPassword",oldPassword);
        param.put("newPassword",newPassword);
        return createAPI(Request.Method.POST,baseApiURL+"/Users/ChangePassword",param);
    }

    public Api saveQuestion(Questions questionInfo){
        JSONObject param = new JSONObject();
        param.put("questionInfo",questionInfo);
        return createAPI(Request.Method.POST,baseApiURL+"/Question/AddQuestion",param);
    }

    public Api getGoodsList(int curPage, int pageSize,int type,String filter){
        Map param = new HashMap<>();
        param.put("curPage",String.valueOf(curPage));
        param.put("pageSize",String.valueOf(pageSize));
        param.put("type",String.valueOf(type));
        param.put("filter",filter);
        return createAPI(Request.Method.GET,baseApiURL+"/RecommandGoods/GetRecommandGoodsList",param);
    }

    public Api clickCounIncrement(String goodsId){
        Map param = new HashMap<>();
        param.put("id",goodsId);
        return createAPI(Request.Method.GET,baseApiURL+"/RecommandGoods/ClickCounIncrement",param);
    }

    public Api saveGoodsInfo(GoodsInfo goodsInfo){
        JSONObject param = new JSONObject();
        param.put("goodsInfo",goodsInfo);
        MyLogUtil.LogShitou("lisx",param.toString ());
        return createAPI(Request.Method.POST,baseApiURL+"/RecommandGoods/SaveGoodsInfo",param);
    }

    public Api auditGoodsInfo(GoodsInfo goodsInfo){
        JSONObject param = new JSONObject();
        param.put("goodsInfo",goodsInfo);
        return createAPI(Request.Method.POST,baseApiURL+"/RecommandGoods/AuditGoodsInfo",param);
    }

    public Api getAdvertisement(String key){
        Map param = new HashMap<>();
        param.put("key",key);
        return createAPI(Request.Method.GET,baseApiURL+"/Advertisement/GetAdvertisement",param);
    }

    public Api getCouponList(long pageNo, long pageSize, String q) {
        Map param = new HashMap<>();
        param.put("pageNo", String.valueOf(pageNo));
        param.put("pageSize", String.valueOf(pageSize));
        param.put("q", q);
        return createAPI(Request.Method.GET, baseApiURL + "/Tbk/GetCouponList", param);
    }

    public Api getRedPaperCommand(String commandKey) {
        Map param = new HashMap<>();
        param.put("commandKey", commandKey);
        return createAPI(Request.Method.GET,baseApiURL+"/Tbk/GetRedPaperCommand",param);
    }

    public Api getGoodsInfo(String id){
        Map param = new HashMap<>();
        param.put("id",id);
        return createAPI(Request.Method.GET,baseApiURL+"/RecommandGoods/GetGoodsInfo",param);
    }

    public Api analysisTbkStr(String tbkStr){
        JSONObject param = new JSONObject();
        param.put("tbkStr",tbkStr);
        return createAPI(Request.Method.POST,baseApiURL+"/RecommandGoods/AnalysisTbkStr",param);
    }
}
