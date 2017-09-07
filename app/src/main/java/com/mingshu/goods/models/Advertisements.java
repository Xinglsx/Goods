package com.mingshu.goods.models;

import java.io.Serializable;

/**
 * Created by Lisx on 2017-09-06.
 */

public class Advertisements implements Serializable {

    public String id ;

    public String key;

    public short type;

    public String img;

    public String content;

    public String goodskey;

    public String begintime;

    public String endtime;

    public int state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGoodskey() {
        return goodskey;
    }

    public void setGoodskey(String goodskey) {
        this.goodskey = goodskey;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
