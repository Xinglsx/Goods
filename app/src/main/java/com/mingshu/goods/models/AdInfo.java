package com.mingshu.goods.models;


import java.io.Serializable;

/**
 * Created by Lisx on 2017-09-06.
 */

public class AdInfo implements Serializable {
    public Advertisements adInfo;
    public GoodsInfo goodsInfo;

    public Advertisements getAdInfo() {
        return adInfo;
    }

    public void setAdInfo(Advertisements adInfo) {
        this.adInfo = adInfo;
    }

    public GoodsInfo getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
}
