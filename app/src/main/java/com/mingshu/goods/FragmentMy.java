package com.mingshu.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.ApplicationUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.views.adapters.BaseFragment;
import com.mingshu.pmp.goods.R;

/**
 * Created by Lisx on 2017-06-29.
 */

public class FragmentMy extends BaseFragment {
    View view;
    Context context;
    UserInfo curUser;

    @SuppressLint({"NewApi", "ValidFragment"})
    public FragmentMy(Context context) {
        this.context = context;
    }
    public FragmentMy(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_my,null);
        curUser = (UserInfo) ApplicationUtil.get(this.getActivity(), Constant.USERINFO);
        initView();
        return view;
    }

    public void initView() {
        ((TextView)view.findViewById(R.id.txt_user_nickname)).setText(curUser.getNickname());
        ((TextView)view.findViewById(R.id.txt_user_id)).setText("账号："+curUser.getUserid());

        (view.findViewById(R.id.linlayout_change_user)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),LoginActivity.class);
                intent.putExtra("isLogout",true);
                winning.framework.utils.ApplicationUtil.put(FragmentMy.this.getActivity(),Constant.USERINFO,"");
                startActivity(intent);
            }
        });

        (view.findViewById(R.id.linlayout_upload_goods)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),UploadGoodsActivity.class);
                startActivity(intent);
            }
        });

        (view.findViewById(R.id.linlayout_audit_goods)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),AuditGoodsActivity.class);
                startActivity(intent);
            }
        });

        (view.findViewById(R.id.btn_logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),LoginActivity.class);
                intent.putExtra("isLogout",true);
                winning.framework.utils.ApplicationUtil.put(FragmentMy.this.getActivity(),Constant.USERINFO,"");
                startActivity(intent);
            }
        });
    }
}
