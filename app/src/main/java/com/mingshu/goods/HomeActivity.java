package com.mingshu.goods;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingshu.goods.databinding.ActivityHomeBinding;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.ApplicationUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.views.adapters.MyFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import winning.framework.ScanBaseActivity;

public class HomeActivity extends ScanBaseActivity {

    private ActivityHomeBinding binding;
    private UserInfo user;

    // 四个Tab，每个Tab包含一个按钮
    private LinearLayout mTabGoods;
//    private LinearLayout mTabConcerns;
//    private LinearLayout mTabFind;
    private LinearLayout mTabMy;

    // 四个按钮
    private ImageButton mImgGoods;
//    private ImageButton mImgConcerns;
//    private ImageButton mImgFind;
    private ImageButton mImgMy;

    private TextView txtGoods;
    private TextView txtMy;

    //Fragment相关
    private List<Fragment> articleInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        user =  (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        articleInfos = new ArrayList<>();
        initView();
        initEvent();
        initViewPage();
    }

    private void initView(){
        // 初始化四个LinearLayout
        mTabGoods = (LinearLayout)findViewById(R.id.line_bottom_goods);
//        mTabConcerns = (LinearLayout)findViewById(R.id.line_bottom_concerns);
//        mTabFind = (LinearLayout)findViewById(R.id.line_bottom_find);
        mTabMy = (LinearLayout)findViewById(R.id.line_bottom_my);
        // 初始化四个按钮
        mImgGoods = (ImageButton)findViewById(R.id.image_btn_goods);
//        mImgConcerns = (ImageButton)findViewById(R.id.image_btn_concerns);
//        mImgFind = (ImageButton)findViewById(R.id.image_btn_find);
        mImgMy = (ImageButton)findViewById(R.id.image_btn_my);

        txtGoods = (TextView) findViewById(R.id.txt_goods);
        txtMy = (TextView) findViewById(R.id.txt_my);
    }

    private void initEvent(){
        binding.mainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int currentItem = binding.mainViewpager.getCurrentItem();
                switch (currentItem){
                    case 0:
                        binding.mainViewpager.setCurrentItem(0);
                        resetLayout();
                        mImgGoods.setImageResource(R.drawable.image_btn_goods_green);
                        txtGoods.setTextColor(Color.GREEN);
                        break;
//                    case 1:
//                        resetLayout();
//                        mImgConcerns.setImageResource(R.drawable.image_btn_concerns_green);
//                        break;
//                    case 2:
//                        resetLayout();
//                        mImgFind.setImageResource(R.drawable.image_btn_find_green);
//                        break;
                    case 1:
                        resetLayout();
                        mImgMy.setImageResource(R.drawable.image_btn_my_green);
                        txtMy.setTextColor(Color.GREEN);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void initViewPage(){
        //将Fragment加入
        articleInfos.add(new FragmentGoods(this));
//        articleInfos.add(new FragmentConcerns(this));
//        articleInfos.add(new FragmentFind(this));
        articleInfos.add(new FragmentMy(this));

        MyFragmentAdapter fragmentAdapter = new MyFragmentAdapter(getFragmentManager(),articleInfos,this);

        binding.mainViewpager.setAdapter(fragmentAdapter);
        binding.mainViewpager.setCurrentItem(0);

    }

    //初始化图标
    private void resetLayout(){
//        mImgFind.setImageResource(R.drawable.image_btn_find);
//        mImgConcerns.setImageResource(R.drawable.image_btn_concerns);
        mImgGoods.setImageResource(R.drawable.image_btn_goods);
        mImgMy.setImageResource(R.drawable.image_btn_my);

        txtGoods.setTextColor(Color.BLACK);
        txtMy.setTextColor(Color.BLACK);
    }

    public void imageBtnClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.line_bottom_goods:
                binding.mainViewpager.setCurrentItem(0);
                resetLayout();
                mImgGoods.setImageResource(R.drawable.image_btn_goods_green);
                txtGoods.setTextColor(Color.GREEN);
                break;
//            case R.id.line_bottom_concerns:
//                binding.mainViewpager.setCurrentItem(1);
//                resetLayout();
//                mImgConcerns.setImageResource(R.drawable.image_btn_concerns_green);
//                break;
//            case R.id.line_bottom_find:
//                binding.mainViewpager.setCurrentItem(2);
//                resetLayout();
//                mImgFind.setImageResource(R.drawable.image_btn_find_green);
//                break;
            case R.id.line_bottom_my:
                binding.mainViewpager.setCurrentItem(1);
                resetLayout();
                mImgMy.setImageResource(R.drawable.image_btn_my_green);
                txtMy.setTextColor(Color.GREEN);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccessScan(String s) {

    }

    private long mkeyTime;
    //两次返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > 1500) {
                mkeyTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
