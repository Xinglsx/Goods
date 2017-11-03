package com.mingshu.goods;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.mingshu.goods.views.MessageEvent;
import com.mingshu.goods.views.adapters.BaseFragment;
import com.mingshu.goods.views.adapters.DataBindingAdapterCoupon;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    TextView textView;
    private SharedPreferences sp;
    private  String filter;

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
        sp = this.getActivity().getSharedPreferences("couponFilter", Context.MODE_PRIVATE);
        initView();
        EventBus.getDefault().register(this);
        return view;
    }

    private void initView() {
        listViewCoupons = (PullToRefreshListView) view.findViewById(R.id.listView_coupons);
        textView = (TextView) view.findViewById(R.id.txt_q);
        filter = sp.getString(Constant.COUPON_FILTER,textView.getText().toString());
        getcoupons(1, Constant.PAGESIZE,filter);
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
                filter = textView.getText().toString();
                getcoupons(pageNo,Constant.PAGESIZE,filter);
            }
        });

        listViewCoupons.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if(dataBindingAdapterCoupon.getCount() ==  pageNo* Constant.PAGESIZE){
                    getmorecoupons(pageNo+1,Constant.PAGESIZE,filter);
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
                //缓存最后的查询关键字
                filter = textView.getText().toString();
                sp.edit().putString(Constant.COUPON_FILTER,filter).commit();
                getcoupons(pageNo,Constant.PAGESIZE,filter);
            }
        });
    }

    public void SearchText(String text){
        getcoupons(1, Constant.PAGESIZE,text);
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
    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        CommonUtil.ShowMsg(event.message,getActivity());
//        Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
