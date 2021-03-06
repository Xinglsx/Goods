package com.mingshu.goods;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mingshu.goods.databinding.ActivityHomeBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.ApplicationUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.views.adapters.MyFragmentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;

public class HomeActivity extends ScanBaseActivity {

    private ActivityHomeBinding binding;
    private SharedPreferences sp;
    private ApiCoreManager apiCoreManager;
    UserInfo user;

    // 四个按钮
    private ImageView mImgGoods;//推荐商品
    private ImageView mImgCoupon;//粉线福利券
    private ImageView mImgFind;//福利闪达
    private ImageView mImgMy;//我的

    private TextView txtGoods;
    private TextView txtCoupon;
    private TextView txtFind;
    private TextView txtMy;

    //Fragment相关
    private List<Fragment> articleInfos;

    //选中的菜单颜色
    private int selectColor = Color.parseColor("#FF9900") ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        apiCoreManager = new ApiCoreManager(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sp.getBoolean(Constant.AUTOLOGIN_ISCHECK, false)) {
            Intent intent = this.getIntent();
            if (!intent.getBooleanExtra("isLogout", false)) {
                GetUserInfo();
            }
        }

        user =  (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        articleInfos = new ArrayList<>();
        initView();
        initEvent();
        initViewPage();

        Intent intent = getIntent();
        Boolean isVoice = intent.getBooleanExtra("isVoice",false);
        if(isVoice){
            int fragmentIndex = intent.getIntExtra("fragmentIndex",0);
            String searchContext = intent.getStringExtra("searchContext");
            binding.mainViewpager.setCurrentItem(fragmentIndex);
            resetLayout();
            mImgCoupon.setImageResource(R.drawable.image_btn_coupon_select);
            txtCoupon.setTextColor(Color.GREEN);
//            FragmentCoupon couponFragment = (FragmentCoupon) articleInfos.get(2);
//            couponFragment.SearchText(searchContext);
        }
    }

    private void initView(){
        // 初始化四个按钮
        mImgGoods = (ImageView)findViewById(R.id.image_btn_goods);
        mImgCoupon = (ImageView)findViewById(R.id.image_btn_coupon);
        mImgFind = (ImageView)findViewById(R.id.image_btn_find);
        mImgMy = (ImageView)findViewById(R.id.image_btn_my);

        txtGoods = (TextView) findViewById(R.id.txt_goods);
        txtCoupon = (TextView) findViewById(R.id.txt_coupon);
        txtFind = (TextView) findViewById(R.id.txt_find);
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
                        mImgGoods.setImageResource(R.drawable.image_btn_goods_select);
                        txtGoods.setTextColor(selectColor);
                        break;
                    case 1:
                        resetLayout();
                        mImgFind.setImageResource(R.drawable.image_btn_welfareflash_select);
                        txtFind.setTextColor(selectColor);
                        break;
                    case 2:
                        resetLayout();
                        mImgCoupon.setImageResource(R.drawable.image_btn_coupon_select);
                        txtCoupon.setTextColor(selectColor);
                        break;
                    case 3:
                        resetLayout();
                        mImgMy.setImageResource(R.drawable.image_btn_my_select);
                        txtMy.setTextColor(selectColor);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewById(R.id.image_btn_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,VoiceSearchActivity.class));
            }
        });
    }

    public void initViewPage(){
        //将Fragment加入
        articleInfos.add(new FragmentGoods(this));
        articleInfos.add(new FragmentFind(this));
        articleInfos.add(new FragmentCoupon(this));
        articleInfos.add(new FragmentMy(this));

        MyFragmentAdapter fragmentAdapter = new MyFragmentAdapter(getFragmentManager(),articleInfos,this);

        binding.mainViewpager.setAdapter(fragmentAdapter);
        binding.mainViewpager.setCurrentItem(0);

    }
    //初始化图标
    private void resetLayout(){
        mImgCoupon.setImageResource(R.drawable.image_btn_coupon_unselect);
        mImgGoods.setImageResource(R.drawable.image_btn_goods_unselect);
        mImgFind.setImageResource(R.drawable.image_btn_welfareflash_unselect);
        mImgMy.setImageResource(R.drawable.image_btn_my_unselect);

        txtGoods.setTextColor(Color.BLACK);
        txtMy.setTextColor(Color.BLACK);
        txtFind.setTextColor(Color.BLACK);
        txtCoupon.setTextColor(Color.BLACK);
    }

    public void imageBtnClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.line_bottom_goods:
                binding.mainViewpager.setCurrentItem(0);
                resetLayout();
                mImgGoods.setImageResource(R.drawable.image_btn_goods_select);
                txtGoods.setTextColor(selectColor);
                break;
            case R.id.line_bottom_find:
                binding.mainViewpager.setCurrentItem(1);
                resetLayout();
                mImgFind.setImageResource(R.drawable.image_btn_welfareflash_select);
                txtFind.setTextColor(selectColor);
                break;
            case R.id.line_bottom_coupon:
                binding.mainViewpager.setCurrentItem(2);
                resetLayout();
                mImgCoupon.setImageResource(R.drawable.image_btn_coupon_select);
                txtCoupon.setTextColor(selectColor);
                break;
            case R.id.line_bottom_my:
                binding.mainViewpager.setCurrentItem(3);
                resetLayout();
                mImgMy.setImageResource(R.drawable.image_btn_my_select);
                txtMy.setTextColor(selectColor);
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

    public void GetUserInfo() {
        //网络访问成功不成功，均不处理
        ApiManager.Api api = apiCoreManager.validateUserInfo(sp.getString(Constant.USERCODE,""),
                sp.getString(Constant.PASSWORD,""));
        api.invoke(new NetworkEngine.Success<UserInfo>() {
            @Override
            public void callback(UserInfo data) {
                //保存全局用户信息
                ApplicationUtil.put(HomeActivity.this,Constant.USERINFO,data);
            }

        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
            }
        }, new NetworkEngine.Error() {

            @Override
            public void callback(int code, String message, Map rawData) {
            }
        });
    }
}
