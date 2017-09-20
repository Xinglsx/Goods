package com.mingshu.goods.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingshu.goods.R;
import com.mingshu.goods.models.CouponInfo;

import java.util.List;

/**
 * Created by Lisx on 2017-07-03.
 */

public class DataBindingAdapterCoupon extends BaseAdapter {

    List<CouponInfo> couponInfos;
    Context context;

    public DataBindingAdapterCoupon(List<CouponInfo> couponInfos, Context context){
        this.couponInfos = couponInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return couponInfos == null ? 0 : couponInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return couponInfos == null ? null : couponInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(this.context, R.layout.item_coupon,null);
            viewHolder.txtCouponTitle = (TextView) view.findViewById(R.id.txt_coupon_title_item);
            viewHolder.txtCouponEndTime= (TextView) view.findViewById(R.id.txt_coupon_endtime_item);
            viewHolder.txtCouponCount = (TextView) view.findViewById(R.id.txt_coupon_count_item);
            viewHolder.txtCouponInfo = (TextView) view.findViewById(R.id.txt_coupon_info_item);
            viewHolder.imageCoupon = (ImageView) view.findViewById(R.id.image_coupon_item);
            viewHolder.txtCouponVolume = (TextView) view.findViewById(R.id.txt_coupon_volume);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if(couponInfos != null){
            viewHolder.txtCouponTitle.setText( couponInfos.get(i).getTitle());
            viewHolder.txtCouponEndTime.setText(couponInfos.get(i).getCoupon_end_time());
            viewHolder.txtCouponCount.setText(couponInfos.get(i).getCoupon_remain_count()+"/"
                    +couponInfos.get(i).getCoupon_total_count());
            viewHolder.txtCouponInfo.setText(couponInfos.get(i).getCoupon_info());
            viewHolder.txtCouponVolume.setText("月销"+couponInfos.get(i).getVolume());
            Glide.with(context).load(couponInfos.get(i).getPict_url()).dontAnimate().into(viewHolder.imageCoupon);
        }

        return view;
    }

    public Boolean AddItem(List<CouponInfo> addGoods){
        this.couponInfos.removeAll(addGoods);
        this.couponInfos.addAll(addGoods);
        return true;
    }

    public static class ViewHolder{
        TextView txtCouponTitle;//标题
        ImageView imageCoupon;//图片
        TextView txtCouponEndTime;//结束时间
        TextView txtCouponInfo;//优惠券面值
        TextView txtCouponCount;//优惠券数量
        TextView txtCouponVolume;//月销量
    }

}
