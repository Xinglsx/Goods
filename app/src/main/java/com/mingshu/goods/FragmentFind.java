package com.mingshu.goods;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mingshu.goods.views.adapters.BaseFragment;
import com.mingshu.pmp.goods.R;


/**
 * Created by Lisx on 2017-06-29.
 */

public class FragmentFind extends BaseFragment {
    View view;
    ListView listViewArticle;

    Context context;

    @SuppressLint({"NewApi", "ValidFragment"})
    public FragmentFind(Context context) {
        this.context = context;
    }
    public FragmentFind(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_find,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((ImageButton)view.findViewById(R.id.image_btn_football)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.putExtra("uri","https://s.click.taobao.com/zAc4mgw");
//                intent.setClass(FragmentFind.this.getActivity(),WebViewActivity.class);
//                startActivity(intent);
//                Intent intent = new Intent();
//                intent.setAction("Android.intent.action.VIEW");
//                Uri uri = Uri.parse("https://s.click.taobao.com/zAc4mgw"); // 商品地址
//                intent.setData(uri);
//                intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
//                startActivity(intent);
                //手动将选择内容放置在剪切板
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", "复制这条信息，￥caEO0YqRUdf￥ ，打开【手机淘宝】即可查看");
                cm.setPrimaryClip(mClipData);
                PackageManager packageManager = context.getPackageManager();
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage("com.taobao.taobao");
                startActivity(intent);

            }
        });
    }
}
