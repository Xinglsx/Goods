package com.mingshu.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.ApplicationUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.views.adapters.BaseFragment;


/**
 * Created by Lisx on 2017-06-29.
 */

public class FragmentFind extends BaseFragment {
    View view;
    Context context;
    UserInfo curUser;

    @SuppressLint({"NewApi", "ValidFragment"})
    public FragmentFind(Context context) {
        this.context = context;
    }
    public FragmentFind(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_find,null);
        curUser = (UserInfo) ApplicationUtil.get(this.getActivity(), Constant.USERINFO);
        initView();
        return view;
    }

    private void initView() {
        WebView webView_Welfare = (WebView) view.findViewById(R.id.webView_Welfare);
        webView_Welfare.loadUrl("http://www.mingshukeji.com.cn/WelfareFlash");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }



}

