package com.mingshu.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingshu.goods.views.adapters.BaseFragment;
import com.mingshu.goods.wxapi.WXEntryActivity;


/**
 * Created by Lisx on 2017-06-29.
 */

public class FragmentFind extends BaseFragment {
    View view;
//    ListView listViewArticle;

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
        view.findViewById(R.id.linlayout_make_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FragmentFind.this.getActivity(),MakeMoneyActivity.class));
            }
        });

        view.findViewById(R.id.btn_wx_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FragmentFind.this.getActivity(), WXEntryActivity.class);
                startActivity(intent);
            }
        });
    }
}
