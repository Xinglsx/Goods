package com.mingshu.goods.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mingshu.goods.models.UserInfo;
import com.mingshu.pmp.goods.R;

import java.util.List;

/**
 * Created by Lisx on 2017-07-03.
 */

public class DataBindingAdapterUser extends BaseAdapter {

    List<UserInfo> users;
    Context context;

    public DataBindingAdapterUser(List<UserInfo> user, Context context){
        this.users = user;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public Boolean AddItem(List<UserInfo> addUsers){
        this.users.removeAll(addUsers);
        this.users.addAll(addUsers);
        return true;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(this.context, R.layout.item_user,null);
            viewHolder.txtUserNickname = (TextView) view.findViewById(R.id.txt_user_nickname);
            viewHolder.txtUserId= (TextView) view.findViewById(R.id.txt_user_id);
            viewHolder.txtUserType = (TextView) view.findViewById(R.id.txt_user_type);
//            viewHolder.imgUserHead = (ImageView) view.findViewById(R.id.image_user_head);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if(users != null && users.size() > i){
            viewHolder.txtUserNickname.setText( users.get(i).getNickname());
            viewHolder.txtUserId.setText(users.get(i).getUserid());
            viewHolder.txtUserType.setText(String.valueOf(users.get(i).getUsertype()));
        }

        return view;
    }

    public static class ViewHolder{
        TextView txtUserNickname;//用户昵称
        TextView txtUserId;//用户账号
        TextView txtUserType;//用户类型
        // ImageView imgUserHead;//用户头像

    }
}
