package com.mingshu.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        view.findViewById(R.id.linlayout_redpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FragmentFind.this.getActivity(),RedPaperActivity.class));
            }
        });
        view.findViewById(R.id.linlayout_redpaper_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FragmentFind.this.getActivity(),RedPaperAlipayActivity.class));
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }



}

