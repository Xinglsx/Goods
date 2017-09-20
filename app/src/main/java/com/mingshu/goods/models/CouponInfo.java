package com.mingshu.goods.models;

import java.io.Serializable;

/**
 * Created by Lisx on 2017-09-15.
 */

public class CouponInfo implements Serializable {
    /// <summary>
    /// 后台一级类目
    /// </summary>
    private String category;
    /// <summary>
    /// 佣金比率(%)
    /// </summary>
    private String commission_rate;
    /// <summary>
    /// 商品优惠券推广链接
    /// </summary>
    private String coupon_click_url;
    /// <summary>
    /// 优惠券结束时间
    /// </summary>
    private String coupon_end_time ;
    /// <summary>
    /// 优惠券面额
    /// </summary>
    private String coupon_info;
    /// <summary>
    /// 优惠券剩余量
    /// </summary>
    private String coupon_remain_count;
    /// <summary>
    /// 优惠券开始时间
    /// </summary>
    private String coupon_start_time;
    /// <summary>
    /// 优惠券总量
    /// </summary>
    private String coupon_total_count;
    /// <summary>
    /// 推荐理由
    /// </summary>
    private String item_description;
    /// <summary>
    /// 商品详情页链接地址
    /// </summary>
    private String item_url;
    /// <summary>
    /// 卖家昵称
    /// </summary>
    private String nick;
    /// <summary>
    /// itemId
    /// </summary>
    private String num_iid ;
    /// <summary>
    /// 商品主图
    /// </summary>
    private String pict_url;
    /// <summary>
    /// 卖家id
    /// </summary>
    private String seller_id;
    /// <summary>
    /// 店铺名称
    /// </summary>
    private String shop_title;
    /// <summary>
    /// 商品标题
    /// </summary>
    private String title;
    /// <summary>
    /// 卖家类型，0表示集市，1表示商城
    /// </summary>
    private String user_type;
    /// <summary>
    /// 30天销量
    /// </summary>
    private String volume;
    /// <summary>
    /// 折扣价
    /// </summary>
    private String zk_final_price;
    /// <summary>
    /// 商品小图列表
    /// </summary>
    //public List<string> small_images;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCommission_rate() {
        return commission_rate;
    }

    public void setCommission_rate(String commission_rate) {
        this.commission_rate = commission_rate;
    }

    public String getCoupon_click_url() {
        return coupon_click_url;
    }

    public void setCoupon_click_url(String coupon_click_url) {
        this.coupon_click_url = coupon_click_url;
    }

    public String getCoupon_end_time() {
        return coupon_end_time;
    }

    public void setCoupon_end_time(String coupon_end_time) {
        this.coupon_end_time = coupon_end_time;
    }

    public String getCoupon_info() {
        return coupon_info;
    }

    public void setCoupon_info(String coupon_info) {
        this.coupon_info = coupon_info;
    }

    public String getCoupon_remain_count() {
        return coupon_remain_count;
    }

    public void setCoupon_remain_count(String coupon_remain_count) {
        this.coupon_remain_count = coupon_remain_count;
    }

    public String getCoupon_start_time() {
        return coupon_start_time;
    }

    public void setCoupon_start_time(String coupon_start_time) {
        this.coupon_start_time = coupon_start_time;
    }

    public String getCoupon_total_count() {
        return coupon_total_count;
    }

    public void setCoupon_total_count(String coupon_total_count) {
        this.coupon_total_count = coupon_total_count;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_url() {
        return item_url;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getPict_url() {
        return pict_url;
    }

    public void setPict_url(String pict_url) {
        this.pict_url = pict_url;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getShop_title() {
        return shop_title;
    }

    public void setShop_title(String shop_title) {
        this.shop_title = shop_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getZk_final_price() {
        return zk_final_price;
    }

    public void setZk_final_price(String zk_final_price) {
        this.zk_final_price = zk_final_price;
    }
}
