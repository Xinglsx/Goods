package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mingshu.goods.databinding.ActivityGoodsListBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.goods.views.adapters.DataBindingAdapterGoods;

import java.util.List;
import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.network.NetworkEngine;


public class GoodsListActivity extends ScanBaseActivity {

    private int curType;//0-用户上传商品列表   1-管理员待审核商品列表
    private int nextType;//0-用户查看草稿商品   1-用户查看提交商品  2-管理员查看待审核商品
    private int state;//获取商品状态
    private ActivityGoodsListBinding binding;
    private ApiCoreManager apiCoreManager;
    private List<GoodsInfo> goodsInfos;
    private DataBindingAdapterGoods bindingAdapterArticle;
    private PullToRefreshListView listViewGoodsList;
    private int pageNumber = 0;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_goods_list);
        Intent intent = this.getIntent();
        curType = intent.getIntExtra("type",0);
        if(curType == 0){
            state = 310;
        }else{
            state = 1;
        }

        binding.setTitle(intent.getStringExtra("title"));
        apiCoreManager = new ApiCoreManager(this);
        initView();
    }

    private void initView() {
        listViewGoodsList = binding.listviewGoodsList;
//        listViewGoodsList.setMode(PullToRefreshBase.Mode.BOTH);
        getGoodsInfos(pageNumber,10,state);

        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsListActivity.this.finish();
            }
        });

        listViewGoodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转商品详细信息
                Intent intent = new Intent();
                GoodsInfo temp = goodsInfos.get(i-1);//选中的商品
                intent.putExtra("goods",temp);
                if(curType == 0 ){
                    nextType = 1;
                }else {
                    nextType = 2;
                }
                intent.putExtra("type",nextType);
                intent.setClass(GoodsListActivity.this,GoodsActivity.class);
                startActivity(intent);
            }
        });
        listViewGoodsList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGoodsInfos(0, Constant.PAGESIZE,state);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        listViewGoodsList.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if(bindingAdapterArticle.getCount() ==  (pageNumber+1) * Constant.PAGESIZE){
                    getMoreGoodsInfos(pageNumber+1,Constant.PAGESIZE,state);
                }
                else{
                    CommonUtil.DisplayToast("亲，没有更多商品了",GoodsListActivity.this);
                }
            }
        });

    }
    //获取文章列表
    private void getGoodsInfos(int curPage, int pageSize,int state){
        ApiCoreManager.Api api = apiCoreManager.getGoodsList(curPage,pageSize,state);
        api.invoke(new NetworkEngine.Success<List<GoodsInfo>>() {
            @Override
            public void callback(List<GoodsInfo> data) {
                goodsInfos = data;
                bindingAdapterArticle = new DataBindingAdapterGoods(GoodsListActivity.this.goodsInfos,GoodsListActivity.this);
                listViewGoodsList.setAdapter(bindingAdapterArticle);
                pageNumber = 0;
                PrompUtil.stopProgessDialog();
                listViewGoodsList.onRefreshComplete();
                if(!isFirst){
                    CommonUtil.DisplayToast("亲，商品已经刷新成功！",GoodsListActivity.this);
                }else{
                    isFirst = false;
                }

            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,GoodsListActivity.this);
                listViewGoodsList.onRefreshComplete();
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,GoodsListActivity.this);
                listViewGoodsList.onRefreshComplete();
            }
        });
    }

    private void getMoreGoodsInfos(int curPage, int pageSize,int state){
        ApiCoreManager.Api api = apiCoreManager.getGoodsList(curPage,pageSize,state);
        api.invoke(new NetworkEngine.Success<List<GoodsInfo>>() {
            @Override
            public void callback(List<GoodsInfo> data) {
                if(data != null && !data.isEmpty()) {
                    goodsInfos.removeAll(data);
                    goodsInfos.addAll(data);
                    bindingAdapterArticle.AddItem(data);
                    bindingAdapterArticle.notifyDataSetChanged();
                    pageNumber++;
                }
                PrompUtil.stopProgessDialog();
                listViewGoodsList.onRefreshComplete();
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,GoodsListActivity.this);
                listViewGoodsList.onRefreshComplete();
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,GoodsListActivity.this);
                listViewGoodsList.onRefreshComplete();
            }
        });
    }
    @Override
    public void onSuccessScan(String s) {

    }
}
