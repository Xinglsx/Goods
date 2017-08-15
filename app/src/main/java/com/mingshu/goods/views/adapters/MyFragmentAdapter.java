package com.mingshu.goods.views.adapters;

import android.content.Context;
import android.app.Fragment;
import android.app.FragmentManager;


import java.util.List;

/**
 * Created by Lisx on 2017-06-29.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> articleInfos;
    private Context context;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> articleInfos, Context context) {
        super(fm);
        this.articleInfos = articleInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return articleInfos == null ? 0 : articleInfos.size();
    }

    @Override
    public Fragment getItem(int position) {
        return articleInfos.get(position);
    }
}
