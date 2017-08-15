package com.mingshu.goods;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.pmp.goods.R;
import com.mingshu.pmp.goods.databinding.ActivityGoodsBinding;


public class GoodsActivity extends AppCompatActivity {

    private ActivityGoodsBinding binding;
    GoodsInfo goodsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods);

        Intent intent = getIntent();
        goodsInfo = (GoodsInfo)intent.getSerializableExtra("goods");

        Glide.with(this).load(goodsInfo.getImages()).dontAnimate().into(binding.imageGoods);
        binding.txtGoodDescription.setText(goodsInfo.getDescription());
        binding.txtReason.setText(goodsInfo.getReason());
        binding.txtGoodsPrice.setText(goodsInfo.getPrice());
        binding.txtClickcount.setText(String.valueOf(goodsInfo.getClickcount()));

        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", goodsInfo.getCommand());
                cm.setPrimaryClip(mClipData);
                PackageManager packageManager = getPackageManager();
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage("com.taobao.taobao");
                startActivity(intent);
            }
        });
    }
}
