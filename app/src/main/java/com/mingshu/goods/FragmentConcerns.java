package com.mingshu.goods;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mingshu.goods.views.adapters.BaseFragment;

/**
 * Created by Lisx on 2017-06-29.
 */

public class FragmentConcerns extends BaseFragment {
    View view;
    ListView listViewArticle;
    Context context;

    @SuppressLint({"NewApi", "ValidFragment"})
    public FragmentConcerns(Context context) {
        this.context = context;
    }

    public FragmentConcerns(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_concerns,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listViewArticle = (ListView)view.findViewById(R.id.listView_Concerns);

    }
}
