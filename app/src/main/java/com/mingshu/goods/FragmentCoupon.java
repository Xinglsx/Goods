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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.CouponInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.views.adapters.BaseFragment;
import com.mingshu.goods.views.adapters.DataBindingAdapterCoupon;

import java.util.List;
import java.util.Map;

import winning.framework.network.NetworkEngine;

/**
 * Created by Lisx on 2017-06-29.
 */

public class FragmentCoupon  extends BaseFragment  {
    View view;
    PullToRefreshListView listViewCoupons;
    Context context;
    ApiCoreManager apiCoreManager;
    List<CouponInfo> couponInfos;
    private int pageNo = 1;
    DataBindingAdapterCoupon dataBindingAdapterCoupon;
    TextView q;

    @SuppressLint({"NewApi", "ValidFragment"})
    public FragmentCoupon(Context context) {
        this.context = context;
    }

    public FragmentCoupon(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_coupon,null);
        apiCoreManager = new ApiCoreManager(this.getActivity());
        initView();
        return view;
    }

    private void initView() {
        listViewCoupons = (PullToRefreshListView) view.findViewById(R.id.listView_coupons);
        q = (TextView) view.findViewById(R.id.txt_q);
        getcoupons(1, Constant.PAGESIZE,q.getText().toString());
        listViewCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转文章详细信息
                Intent intent = new Intent();
                intent.putExtra("coupon",couponInfos.get(i-1));
                intent.setClass(FragmentCoupon.this.getActivity(),CouponActivity.class);
                startActivity(intent);
            }
        });
        listViewCoupons.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo = 1;
                getcoupons(pageNo,Constant.PAGESIZE,q.getText().toString());
            }
        });

        listViewCoupons.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if(dataBindingAdapterCoupon.getCount() ==  pageNo* Constant.PAGESIZE){
                    getmorecoupons(pageNo+1,Constant.PAGESIZE,q.getText().toString());
                }
                else{
                    CommonUtil.DisplayToast("亲，没有更多粉丝福利券了，看看其他的吧！",FragmentCoupon.this.getActivity());
                    listViewCoupons.onRefreshComplete();
                }
            }
        });

        view.findViewById(R.id.btn_find_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo = 1;
                getcoupons(pageNo,Constant.PAGESIZE,q.getText().toString());
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listViewCoupons = (PullToRefreshListView)view.findViewById(R.id.listView_coupons);

    }

    private void getcoupons(long pageNo, long pageSize, String q){
        ApiCoreManager.Api api = apiCoreManager.getCouponList(pageNo,pageSize,q);
        api.invoke(new NetworkEngine.Success<List<CouponInfo>>() {
            @Override
            public void callback(List<CouponInfo> data) {
                couponInfos = data;
                dataBindingAdapterCoupon = new DataBindingAdapterCoupon(couponInfos,FragmentCoupon.this.getActivity());
                listViewCoupons.setAdapter(dataBindingAdapterCoupon);
                FragmentCoupon.this.pageNo = 1;
                listViewCoupons.onRefreshComplete();
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                CommonUtil.ShowMsg(message,FragmentCoupon.this.getActivity());
                listViewCoupons.onRefreshComplete();
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                CommonUtil.ShowMsg(message,FragmentCoupon.this.getActivity());
                listViewCoupons.onRefreshComplete();
            }
        });
    }

    private void getmorecoupons(long pageNo, long pageSize, String q){
        ApiCoreManager.Api api = apiCoreManager.getCouponList(pageNo,pageSize,q);
        api.invoke(new NetworkEngine.Success<List<CouponInfo>>() {
            @Override
            public void callback(List<CouponInfo> data) {
                couponInfos.removeAll(data);
                couponInfos.addAll(data);
                dataBindingAdapterCoupon.AddItem(data);
                dataBindingAdapterCoupon.notifyDataSetChanged();
                FragmentCoupon.this.pageNo++;
                listViewCoupons.onRefreshComplete();
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                CommonUtil.ShowMsg(message,FragmentCoupon.this.getActivity());
                listViewCoupons.onRefreshComplete();
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                CommonUtil.ShowMsg(message,FragmentCoupon.this.getActivity());
                listViewCoupons.onRefreshComplete();
            }
        });
    }
}
