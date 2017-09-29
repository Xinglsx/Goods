package com.mingshu.goods.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingshu.goods.R;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;

import java.util.List;

/**
 * Created by Lisx on 2017-07-03.
 */

public class DataBindingAdapterGoods extends BaseAdapter {

    List<GoodsInfo> goodsInfos;
    Context context;
    ViewHolder viewHolder;

    public DataBindingAdapterGoods(List<GoodsInfo> goodsInfos, Context context){
        this.goodsInfos = goodsInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return goodsInfos == null ? 0 : goodsInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return goodsInfos == null ? null : goodsInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(this.context, R.layout.item_goods,null);
            viewHolder.txtGoodsDescription = (TextView) view.findViewById(R.id.txt_goods_description_item);
            viewHolder.txtRecommendtime= (TextView) view.findViewById(R.id.txt_goods_recommandtime_item);
            viewHolder.txtRecommendname = (TextView) view.findViewById(R.id.txt_goods_recommandname_item);
            viewHolder.txtGoodsOldPrice = (TextView) view.findViewById(R.id.txt_goods_oldprice_item);
            viewHolder.txtGoodsPrice = (TextView) view.findViewById(R.id.txt_goods_price_item);
            viewHolder.imageGoods = (ImageView) view.findViewById(R.id.image_goods_item);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if(goodsInfos != null){
            viewHolder.txtGoodsDescription.setText( goodsInfos.get(i).getDescription());

            viewHolder.txtRecommendtime.setText(CommonUtil.stampToDate(goodsInfos.get(i).getRecommendtime(),
                    Constant.DATEFORMAT_DATETIME_SS) );
            viewHolder.txtRecommendname.setText(goodsInfos.get(i).getRecommendname());
            if(goodsInfos.get(i).getOldprice().contains("￥")){
                viewHolder.txtGoodsOldPrice.setText("原价:"+goodsInfos.get(i).getOldprice());
            }else{
                viewHolder.txtGoodsOldPrice.setText("原价:￥" + goodsInfos.get(i).getOldprice());
            }
            if(goodsInfos.get(i).getPrice().contains("￥")){
                viewHolder.txtGoodsPrice.setText("【券后】"+goodsInfos.get(i).getPrice());
            }else{
                viewHolder.txtGoodsPrice.setText("【券后】￥" + goodsInfos.get(i).getPrice());
            }

            Glide.with(context).load(goodsInfos.get(i).getImage()).dontAnimate().into(viewHolder.imageGoods);
        }

        return view;
    }

    public Boolean AddItem(List<GoodsInfo> addGoods){
        this.goodsInfos.removeAll(addGoods);
        this.goodsInfos.addAll(addGoods);
        return true;
    }

    public static class ViewHolder{
        TextView txtGoodsDescription;//描述
        ImageView imageGoods;//图片
        TextView txtRecommendname;//推荐人姓名
        TextView txtRecommendtime;//推荐时间
        TextView txtGoodsPrice;//价格
        TextView txtGoodsOldPrice;//原价
    }

}
