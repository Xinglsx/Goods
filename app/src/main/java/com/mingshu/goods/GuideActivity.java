package com.mingshu.goods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mingshu.goods.databinding.ActivityGuideBinding;
import com.mingshu.goods.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import winning.framework.ScanBaseActivity;

public class GuideActivity extends ScanBaseActivity {
    private ActivityGuideBinding binding;

    //导航页资源
    private int[] images = new int[]{
            R.drawable.image_guide_1,//http://192.224.129.220:8080/images/guides/image_guide_1.png
            R.drawable.image_guide_2,//http://192.224.129.220:8080/images/guides/image_guide_2.png
            R.drawable.image_guide_3,//http://192.224.129.220:8080/images/guides/image_guide_3.png
            R.drawable.image_guide_4//http://192.224.129.220:8080/images/guides/image_guide_4.png
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
    //首次开启缓存修改
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_guide);
        imageViews = new ArrayList<>();
        ll = binding.ll;
        rl = binding.rl;
        sp = GuideActivity.this.getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        int pageSize = images.length;
        for (int i = 0; i < pageSize; i++) {
            ImageView iv = new ImageView(GuideActivity.this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(images[i]).dontAnimate().into(iv);
//            iv.setImageResource(images[i]);
            imageViews.add(iv);
            //动态加载灰色圆点
            ImageView gray_Iv = new ImageView(this);
            gray_Iv.setImageResource(R.drawable.gray_point);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(32,32);
            //从第二个开始有边距
            if (i > 0) {
                layoutParams.leftMargin = 100;  //注意单位是px
            }
            gray_Iv.setLayoutParams(layoutParams);
            ll.addView(gray_Iv);
        }
        //添加红色圆点
        red_Iv = new ImageView(this);
        red_Iv.setImageResource(R.drawable.red_point);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(32,32);
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

        binding.guideViewpager.setAdapter(new MyAdapter());
        binding.guideViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //导航页被选择的时候调用
            @Override
            public void onPageSelected(int position) {
                if (position == images.length - 1) {
                    binding.btn.setVisibility(View.VISIBLE);
                }else {
                    binding.btn.setVisibility(View.GONE);
                }
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

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getBoolean(Constant.IS_FIRST_IN,true)){
                    sp.edit().putBoolean(Constant.IS_FIRST_IN,false).commit();
                    //跳转登录页面
                    Intent it =new Intent(GuideActivity.this,LoginActivity.class);
                    startActivity(it);
                    GuideActivity.this.finish();
                }else{
                    GuideActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void onSuccessScan(String s) {

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
