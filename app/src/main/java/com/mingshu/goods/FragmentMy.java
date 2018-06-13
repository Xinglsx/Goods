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

/**
 * Created by Lisx on 2017-06-29.
 */

public class FragmentMy extends BaseFragment {
    View view;

    Context context;
    UserInfo curUser;
    int type;

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
        if(curUser == null){
            type = 0;
        }else {
            type= curUser.getUsertype();
        }
        switch (type){
            default:
            case 0:
                //未注册
                view.findViewById(R.id.linLayout_RegisterOrLogin).setVisibility(View.VISIBLE);
            case 1:
            case 2:
                //待审商品
                view.findViewById(R.id.linlayout_pending).setVisibility(View.GONE);
                view.findViewById(R.id.txt_pending).setVisibility(View.GONE);
                //上传商品
                view.findViewById(R.id.linlayout_upload_goods).setVisibility(View.GONE);
            case 3:
                //审核商品
                view.findViewById(R.id.linlayout_audit_goods).setVisibility(View.GONE);
                view.findViewById(R.id.txt_audit_goods).setVisibility(View.GONE);
                //用户管理
                view.findViewById(R.id.txt_user_manager).setVisibility(View.GONE);
                view.findViewById(R.id.linlayout_user_manager).setVisibility(View.GONE);
            case 4:
            case 5:
                break;
        }
        if(curUser != null && curUser.getNickname() != null){
            ((TextView)view.findViewById(R.id.txt_user_nickname)).setText(curUser.getNickname());
        }
        if(curUser != null && curUser.getUsersignature() != null){
            ((TextView)view.findViewById(R.id.txt_user_signature)).setText(curUser.getUsersignature());
        }

        (view.findViewById(R.id.linlayout_change_user)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),LoginActivity.class);
                intent.putExtra("isLogout",true);
                startActivity(intent);
//                FragmentMy.this.getActivity().finish();
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
                intent.setClass(FragmentMy.this.getActivity(),GoodsListActivity.class);
                intent.putExtra("type",1);//管理员
                intent.putExtra("title","全部待审商品");
                startActivity(intent);
            }
        });

        view.findViewById(R.id.linlayout_pending).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),GoodsListActivity.class);
                intent.putExtra("type",0);//用户
                intent.putExtra("title","个人待审商品");
                startActivity(intent);

            }
        });
        view.findViewById(R.id.linlayout_user_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),UserManagerActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.linlayout_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),SettingsActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.linlayout_help_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),HelpActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.linlayout_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),HelpActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btn_Register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btn_Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FragmentMy.this.getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
