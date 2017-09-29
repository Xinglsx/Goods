package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mingshu.goods.databinding.ActivityUserManagerBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.PagedData;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.goods.views.adapters.DataBindingAdapterUser;

import java.util.List;
import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;
import winning.framework.utils.ApplicationUtil;


public class UserManagerActivity extends ScanBaseActivity {
    private ApiCoreManager apiCoreManager;
    private ActivityUserManagerBinding binding;
    private DataBindingAdapterUser bindingAdapterUser;
    private PullToRefreshListView listViewUserInfos;
    private int pageNumber = 0;
    private final int pageSize = 30;
    private List<UserInfo> userInfos;
    private UserInfo curUser;
    private Boolean isFirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_manager);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_manager);
        apiCoreManager = new ApiCoreManager(this);
        curUser = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        binding.setTitle("用户管理");
        initView();
        getUserInfos(pageNumber,pageSize,9,"");
    }

    private void initView() {
//        binding.listviewUsers.setMode(PullToRefreshBase.Mode.BOTH);
        binding.listviewUsers.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getUserInfos(pageNumber,pageSize,9,"");
            }
        });
        binding.listviewUsers.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if(bindingAdapterUser.getCount() ==  (pageNumber+1) * pageSize){
                    getMoreUserInfos(pageNumber+1,pageSize,9,"");
                }
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtil.DisplayToast("亲，没有更多注册用户了！",UserManagerActivity.this);
                            binding.listviewUsers.onRefreshComplete();
                        }
                    }).start();

                }
            }
        });

        binding.listviewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(curUser.getUsertype() != 5 && userInfos.get(position - 1).getUsertype() == 4){
                    CommonUtil.DisplayToast("您无权限修改此用户!",UserManagerActivity.this);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("user",userInfos.get(position-1));
                intent.setClass(UserManagerActivity.this,UserTypeEditActivity.class);
                startActivity(intent);
            }
        });

        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagerActivity.this.finish();
            }
        });
    }


    @Override
    public void onSuccessScan(String s) {

    }

    private void getUserInfos(int curPage, int pageSize, int type,String filter){
        ApiManager.Api api = apiCoreManager.getUserInfos(curPage,pageSize,type,filter);
        api.invoke(new NetworkEngine.Success<PagedData<UserInfo>>(){
            @Override
            public void callback(PagedData<UserInfo> result){
                userInfos = result.getDataList();
                PrompUtil.stopProgessDialog();
                bindingAdapterUser = new DataBindingAdapterUser(result.getDataList(),UserManagerActivity.this);
                binding.listviewUsers.setAdapter(bindingAdapterUser);
                pageNumber = 0;
                binding.listviewUsers.onRefreshComplete();
                if(isFirst){
                    isFirst = false;
                }else{
                    CommonUtil.DisplayToast("用户信息已刷新！",UserManagerActivity.this);
                }
            }
        },new NetworkEngine.Failure(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UserManagerActivity.this);
                binding.listviewUsers.onRefreshComplete();
            }
        },new NetworkEngine.Error(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UserManagerActivity.this);
                binding.listviewUsers.onRefreshComplete();
            }
        });
    }

    private void getMoreUserInfos(int curPage, int pageSize, int type,String filter){
        ApiManager.Api api = apiCoreManager.getUserInfos(curPage,pageSize,type,filter);
        api.invoke(new NetworkEngine.Success<PagedData<UserInfo>>(){
            @Override
            public void callback(PagedData<UserInfo> result){
                userInfos.removeAll(result.getDataList());
                userInfos.addAll(result.getDataList());
                bindingAdapterUser.AddItem(result.getDataList());
                bindingAdapterUser.notifyDataSetChanged();
                pageNumber++;
                binding.listviewUsers.onRefreshComplete();
                PrompUtil.stopProgessDialog();

            }
        },new NetworkEngine.Failure(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UserManagerActivity.this);
                binding.listviewUsers.onRefreshComplete();
            }
        },new NetworkEngine.Error(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UserManagerActivity.this);
                binding.listviewUsers.onRefreshComplete();
            }
        });
    }
}
