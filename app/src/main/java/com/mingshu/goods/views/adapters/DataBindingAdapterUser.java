package com.mingshu.goods.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingshu.goods.models.UserInfo;
import com.mingshu.pmp.goods.R;

/**
 * Created by Lisx on 2017-07-03.
 */

public class DataBindingAdapterUser extends BaseAdapter {

    UserInfo user;
    Context context;

    public DataBindingAdapterUser(UserInfo user, Context context){
        this.user = user;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return user;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(this.context, R.layout.fragment_goods_item,null);
            viewHolder.txtUserNickname = (TextView) view.findViewById(R.id.txt_user_nickname);
            viewHolder.txtUserId= (TextView) view.findViewById(R.id.txt_user_id);
            viewHolder.imgUserHead = (ImageView) view.findViewById(R.id.image_user_head);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if(user != null){
            viewHolder.txtUserNickname.setText( user.getNickname());
            viewHolder.txtUserId.setText(user.userid);
//            Glide.with(context).load(goodsInfos.get(i).getImages()).dontAnimate().into(viewHolder.imageGoods);
//            viewHolder.txtReason.setText(goodsInfos.get(i).getReason());
        }

        return view;
    }

    public static class ViewHolder{
        ImageView imgUserHead;//用户头像
        TextView txtUserNickname;//用户昵称
        TextView txtUserId;//用户账号
    }
}
