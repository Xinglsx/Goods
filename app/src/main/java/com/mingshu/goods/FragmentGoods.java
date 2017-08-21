package com.mingshu.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.goods.views.adapters.BaseFragment;
import com.mingshu.goods.views.adapters.DataBindingAdapterGoods;
import com.mingshu.pmp.goods.R;

import java.util.List;
import java.util.Map;

import winning.framework.network.NetworkEngine;

/**
 * Created by Lisx on 2017-06-29.
 */

public class FragmentGoods extends BaseFragment {

    private ApiCoreManager apiCoreManager;

    private List<GoodsInfo> goodsInfos;
    private View view;
    private PullToRefreshListView listViewGoods;
    private DataBindingAdapterGoods bindingAdapterArticle;
    private  Context context;
    private int pageNumber = 0;
    private boolean isLogin = true;

    @SuppressLint({"NewApi", "ValidFragment"})
    public FragmentGoods(Context context) {
        this.context = context;
    }
    public FragmentGoods(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods,null); //View.inflate(getActivity(),R.layout.fragment_goods,null);
        apiCoreManager = new ApiCoreManager(this.getActivity());
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    private void initView(){
        PrompUtil.startProgressDialog(this.getActivity(),"获取中，请稍等。。。");
        getArticleInfos(pageNumber, Constant.PAGESIZE);

        listViewGoods = (PullToRefreshListView)view.findViewById(R.id.listView_Goods);
//        listViewGoods.setMode(PullToRefreshBase.Mode.BOTH);
        listViewGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                articleTitleClick(i-1);//更新阅读量

                //跳转文章详细信息
                Intent intent = new Intent();
                intent.putExtra("goods",goodsInfos.get(i-1));
                intent.setClass(FragmentGoods.this.getActivity(),GoodsActivity.class);
                startActivity(intent);
            }
        });
        listViewGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getArticleInfos(0,Constant.PAGESIZE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        listViewGoods.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if(bindingAdapterArticle.getCount() ==  (pageNumber+1) * Constant.PAGESIZE){
                    getMoreArticleInfos(pageNumber+1,Constant.PAGESIZE);
                }
                else{
                    CommonUtil.DisplayToast("亲，没有更多商品了",FragmentGoods.this.getActivity());
                }
            }
        });
    }

    //获取文章列表
    private void getArticleInfos(int curPage, int pageSize){
        ApiCoreManager.Api api = apiCoreManager.getGoodsList(curPage,pageSize);
        api.invoke(new NetworkEngine.Success<List<GoodsInfo>>() {
            @Override
            public void callback(List<GoodsInfo> data) {
                goodsInfos = data;
                bindingAdapterArticle = new DataBindingAdapterGoods(FragmentGoods.this.goodsInfos,FragmentGoods.this.getActivity());
                listViewGoods.setAdapter(bindingAdapterArticle);
                pageNumber = 0;
                PrompUtil.stopProgessDialog();
                listViewGoods.onRefreshComplete();
                if(!isLogin){
                    CommonUtil.DisplayToast("亲，商品已经刷新成功！",FragmentGoods.this.getActivity());
                }else{
                    isLogin = false;
                }

            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
                listViewGoods.onRefreshComplete();
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
                listViewGoods.onRefreshComplete();
            }
        });
    }

    private void getMoreArticleInfos(int curPage, int pageSize){
        ApiCoreManager.Api api = apiCoreManager.getGoodsList(curPage,pageSize);
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
                listViewGoods.onRefreshComplete();
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
                listViewGoods.onRefreshComplete();
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
                listViewGoods.onRefreshComplete();
            }
        });
    }

    //更新阅读量
    private void articleTitleClick(final int i){
        //获取文章列表
        ApiCoreManager.Api api = apiCoreManager.clickCounIncrement(goodsInfos.get(i).getId());
        api.invoke(new NetworkEngine.Success<Boolean>() {
            @Override
            public void callback(Boolean data) {
                FragmentGoods.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goodsInfos.get(i).setClickcount(goodsInfos.get(i).getClickcount() + 1);
                        bindingAdapterArticle.notifyDataSetChanged();
                    }
                });
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {}
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {}
        });
    }
}
