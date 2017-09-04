package com.mingshu.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.goods.views.adapters.BaseFragment;
import com.mingshu.goods.views.adapters.DataBindingAdapterGoods;

import java.util.ArrayList;
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
    //轮播图片
    private String[] images = new String[]{
            "http://139.224.129.220:8080/images/carousels/1.jpg",//
            "http://139.224.129.220:8080/images/carousels/2.jpg",//
            "http://139.224.129.220:8080/images/carousels/3.jpg",
            "http://139.224.129.220:8080/images/carousels/4.jpg"
    };
    //用来存放导航图片实例（保证唯一性，滑动的时候不重复创建）
    private List<ImageView> imageViews;
    //圆点与圆点之间的边距
    private int left;
    //存放三个灰色圆点的线性布局
    private LinearLayout ll;
    //用来存放红色圆点和灰色圆点的相对布局
    private RelativeLayout rl;
    //红色圆点ImageView
    private ImageView red_Iv;
    android.support.v4.view.ViewPager viewPager;


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
        imageViews = new ArrayList<>();
        ll = (LinearLayout) view.findViewById(R.id.ll);
        rl = (RelativeLayout) view.findViewById(R.id.rl);
        viewPager = (ViewPager) view.findViewById(R.id.carousel_viewpager);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    private void initView(){

        int pageSize = images.length;
        for (int i = 0; i < pageSize; i++) {
            ImageView iv = new ImageView(FragmentGoods.this.getActivity());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(images[i]).dontAnimate().into(iv);
//            iv.setImageResource(images[i]);
            imageViews.add(iv);
            //动态加载灰色圆点
            ImageView gray_Iv = new ImageView(this.getActivity());
            gray_Iv.setImageResource(R.drawable.gray_point);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(24,24);
            //从第二个开始有边距
            if (i > 0) {
                layoutParams.leftMargin = 70;  //注意单位是px
            }
            gray_Iv.setLayoutParams(layoutParams);
            ll.addView(gray_Iv);
        }
        //添加红色圆点
        red_Iv = new ImageView(this.getActivity());
        red_Iv.setImageResource(R.drawable.red_point);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(24,24);
        red_Iv.setLayoutParams(layoutParams);
        rl.addView(red_Iv);
        //任何一个组件都可以得到视图树
        red_Iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //视图完成绘制的时候调用
            @Override
            public void onGlobalLayout() {
                left = ll.getChildAt(1).getLeft() - ll.getChildAt(0).getLeft();
                System.out.println(left);
                //移除视图树的监听
                red_Iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        viewPager.setAdapter(new MyAdapter());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //导航页被选择的时候调用
            @Override
            public void onPageSelected(int position) {

            }
            //导航页滑动的时候调用
            //positionOffset:滑动的百分比（[0,1}）
            @Override
            public void onPageScrolled(int position, float positionOffset, int arg2) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) red_Iv.getLayoutParams();
                layoutParams.leftMargin = (int) (left * positionOffset + position * left);
                red_Iv.setLayoutParams(layoutParams);
            }
            //导航页滑动的状态改变的时候调用
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        PrompUtil.startProgressDialog(this.getActivity(),"获取中，请稍等。。。");
        getGoodsInfos(pageNumber, Constant.PAGESIZE,2);

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
        listViewGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getGoodsInfos(0,Constant.PAGESIZE,2);
            }
        });

        listViewGoods.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if(bindingAdapterArticle.getCount() ==  (pageNumber+1) * Constant.PAGESIZE){
                    getMoreGoodsInfos(pageNumber+1,Constant.PAGESIZE,2);
                }
                else{
                    CommonUtil.DisplayToast("亲，没有更多商品了",FragmentGoods.this.getActivity());
                    listViewGoods.onRefreshComplete();
                }
            }
        });
    }

    //获取文章列表
    private void getGoodsInfos(int curPage, int pageSize,int type){
        ApiCoreManager.Api api = apiCoreManager.getGoodsList(curPage,pageSize,type);
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
                    listViewGoods.onRefreshComplete();
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

    private void getMoreGoodsInfos(int curPage, int pageSize,int type){
        ApiCoreManager.Api api = apiCoreManager.getGoodsList(curPage,pageSize,type);
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

    //PagerAdapter有四个方法
    class MyAdapter extends PagerAdapter {
        //返回导航页的个数
        @Override
        public int getCount() {
            return images.length;
        }
        //判断是否由对象生成
        @Override
        public boolean isViewFromObject(View view,Object object) {
            return view == object;
        }
        //加载页面
        //ViewGroup:父控件指ViewPager
        //position:当前子控件在父控件中的位置
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = imageViews.get(position);
            container.addView(iv);
            return iv;
        }
        //移除页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
