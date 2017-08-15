package com.mingshu.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
    private ListView listViewArticle;
    private DataBindingAdapterGoods bindingAdapterArticle;
    private  Context context;

    public FragmentGoods(Context context) {
        this.context = context;
    }

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
        getArticleInfos(0, Constant.PAGESIZE);

        listViewArticle = (ListView)view.findViewById(R.id.listView_Article);
        listViewArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                articleTitleClick(i);//更新阅读量

                //跳转文章详细信息
                Intent intent = new Intent();
                intent.putExtra("goods",goodsInfos.get(i));
                intent.setClass(FragmentGoods.this.getActivity(),GoodsActivity.class);
                startActivity(intent);


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
                listViewArticle = (ListView)view.findViewById(R.id.listView_Article);
                bindingAdapterArticle = new DataBindingAdapterGoods(FragmentGoods.this.goodsInfos,FragmentGoods.this.getActivity());
                listViewArticle.setAdapter(bindingAdapterArticle);
                PrompUtil.stopProgessDialog();
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
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
