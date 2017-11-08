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
import com.mingshu.goods.models.AdInfo;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.models.PagedData;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
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
    Context context;
    private int pageNumber = 1;
    private boolean isLogin = true;
    private int TotalPages;
    //轮播图片
    private String[] images = new String[]{
            "http://www.mingshukeji.com.cn/images/carousels/1.jpg",//
            "http://www.mingshukeji.com.cn/images/carousels/2.jpg",//
            "http://www.mingshukeji.com.cn/images/carousels/3.jpg",
            "http://www.mingshukeji.com.cn/images/carousels/4.jpg"
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

        final int pageSize = images.length;
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

//        PrompUtil.startProgressDialog(this.getActivity(),"获取中，请稍等。。。");
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
                getGoodsInfos(1,Constant.PAGESIZE,2);
            }
        });

        listViewGoods.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if(pageNumber < TotalPages){
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
        ApiCoreManager.Api api = apiCoreManager.getGoodsList(curPage,pageSize,type,"");
        api.invoke(new NetworkEngine.Success<PagedData<GoodsInfo>>() {
            @Override
            public void callback(PagedData<GoodsInfo> data) {
                goodsInfos = data.getDataList();
                bindingAdapterArticle = new DataBindingAdapterGoods(FragmentGoods.this.goodsInfos,FragmentGoods.this.getActivity());
                listViewGoods.setAdapter(bindingAdapterArticle);
//                PrompUtil.stopProgessDialog();
                pageNumber = 1;
                TotalPages = data.getTotalPages();
                listViewGoods.onRefreshComplete();
                if(!isLogin){
                    //CommonUtil.DisplayToast("亲，商品已经刷新成功！",FragmentGoods.this.getActivity());
                    listViewGoods.onRefreshComplete();
                }else{
                    isLogin = false;
                }

            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
//                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
                listViewGoods.onRefreshComplete();
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
//                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
                listViewGoods.onRefreshComplete();
            }
        });
    }

    private void getMoreGoodsInfos(int curPage, int pageSize,int type){
        ApiCoreManager.Api api = apiCoreManager.getGoodsList(curPage,pageSize,type,"");
        api.invoke(new NetworkEngine.Success<PagedData<GoodsInfo>>() {
            @Override
            public void callback(PagedData<GoodsInfo> data) {
                if(data != null && data.getDataList() != null && !data.getDataList().isEmpty()) {
                    goodsInfos.removeAll(data.getDataList());
                    goodsInfos.addAll(data.getDataList());
                    bindingAdapterArticle.AddItem(data.getDataList());
                    bindingAdapterArticle.notifyDataSetChanged();
                }
//                PrompUtil.stopProgessDialog();
                pageNumber++;
                listViewGoods.onRefreshComplete();
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
//                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
                listViewGoods.onRefreshComplete();
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
//                PrompUtil.stopProgessDialog();
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
        public Object instantiateItem(final ViewGroup container, final int position) {
            ImageView iv = imageViews.get(position);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        case 0 :
                            skipToAd("carousel0");
                            break;
                        case 1:
                            skipToAd("carousel1");
                            break;
                        case 2:
                            skipToAd("carousel2");
                            break;
                        case 3:
                            skipToAd("carousel3");
                            break;
                        default:
                            break;
                    }
                }
            });
            container.addView(iv);
            return iv;
        }
        //获取广告
        public void skipToAd(String key){
            final ApiCoreManager.Api api = apiCoreManager.getAdvertisement(key);
            api.invoke(new NetworkEngine.Success<AdInfo>() {
                @Override
                public void callback(AdInfo data) {
                    int type = data.getAdInfo().getType();
                    switch (type){
                        case 0:
                            //关于我们
                            Intent intent = new Intent(FragmentGoods.this.getActivity(),TextViewActivity.class);
                            intent.putExtra("title","招募特约用户");
                            String content = "<h3>手机APP刚刚上线，现招募20位淘宝客加盟，1元成为特约用户，可以上传自己分享的商品信息。</h3>\n";
                            content += "<h4>基本要求：</h4>\n";
                            content += "<p>1、对分享购物不感冒</p>\n";
                            content += "<p>2、对业余赚钱有渴望</p>\n";
                            content += "<p>3、肯专研，爱逛淘宝</p>\n";
                            content += "<p>4、每天至少上传一件商品</p>\n";
                            content += "<p>5、用Android手机的（没办法，不会开发苹果APP）</p>\n";
                            content += "<p>6、电话、微信、姓名实名认证。</p>\n";
                            content += "<p>7、熟悉淘宝联盟，清楚淘宝客的基本操作和分享规则的优先考虑。（不了解的，这有教程）</p>\n";
                            content += "<h4>加盟流程：</h4>\n";
                            content += "下载闪荐 -> 注册闪荐账号 -> 完善个人资料 -> 加微信(ydxc608) -> ";
                            content += "出示闪荐账号 -> 成为特约用户 -> 上传一个商品成功 -> 支付1元费用\n";
                            intent.putExtra("content",content);//html类型
                            startActivity(intent);
                            break;
                        case 1:
                            //文字广告
                            Intent intent1 = new Intent(FragmentGoods.this.getActivity(),TextViewActivity.class);
                            intent1.putExtra("title","招募特约用户");
                            intent1.putExtra("content",data.getAdInfo().getContent());//html类型
                            startActivity(intent1);
                            break;
                        case 2:
                            //商品广告
                            Intent intent2 = new Intent(FragmentGoods.this.getActivity(),GoodsActivity.class);
                            intent2.putExtra("goods",data.getGoodsInfo());
                            startActivity(intent2);
                            break;
                        default:
                            break;
                    }
                }
            }, new NetworkEngine.Failure() {
                @Override
                public void callback(int code, String message, Map rawData) {
//                    PrompUtil.stopProgessDialog();
//                    CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
//                    listViewGoods.onRefreshComplete();
                    //如果失败，刚进行招募特约用户界面
                    Intent intent = new Intent(FragmentGoods.this.getActivity(), TextViewActivity.class);
                    intent.putExtra("title", "招募特约用户");
                    String content = "<h3>手机APP刚刚上线，现招募20位淘宝客加盟，1元成为特约用户，可以上传自己分享的商品信息。</h3>\n";
                    content += "<h4>基本要求：</h4>\n";
                    content += "<p>1、对分享购物不感冒</p>\n";
                    content += "<p>2、对业余赚钱有渴望</p>\n";
                    content += "<p>3、肯专研，爱逛淘宝</p>\n";
                    content += "<p>4、每天至少上传一件商品</p>\n";
                    content += "<p>5、用Android手机的（没办法，不会开发苹果APP）</p>\n";
                    content += "<p>6、电话、微信、姓名实名认证。</p>\n";
                    content += "<p>7、熟悉淘宝联盟，清楚淘宝客的基本操作和分享规则的优先考虑。（不了解的，这有教程）</p>\n";
                    content += "<h4>加盟流程：</h4>\n";
                    content += "下载闪荐 -> 注册闪荐账号 -> 完善个人资料 -> 加微信(ydxc608) -> ";
                    content += "出示闪荐账号 -> 成为特约用户 -> 上传一个商品成功 -> 支付1元费用\n";
                    intent.putExtra("content", content);//html类型
                    startActivity(intent);
                }
            }, new NetworkEngine.Error() {
                @Override
                public void callback(int code, String message, Map rawData) {
//                    PrompUtil.stopProgessDialog();
                    CommonUtil.ShowMsg(message,FragmentGoods.this.getActivity());
                    listViewGoods.onRefreshComplete();
                }
            });
        }

        //移除页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
