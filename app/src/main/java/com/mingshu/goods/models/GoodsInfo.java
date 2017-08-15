package com.mingshu.goods.models;

import java.io.Serializable;

/**
 * Created by Lisx on 2017-06-29.
 */

public class GoodsInfo implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getIsbuy() {
        return isbuy;
    }

    public void setIsbuy(Boolean isbuy) {
        this.isbuy = isbuy;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Boolean getRecommendflag() {
        return recommendflag;
    }

    public void setRecommendflag(Boolean recommendflag) {
        this.recommendflag = recommendflag;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBuyimages() {
        return buyimages;
    }

    public void setBuyimages(String buyimages) {
        this.buyimages = buyimages;
    }

    public String getRecommender() {
        return recommender;
    }

    public void setRecommender(String recommender) {
        this.recommender = recommender;
    }

    public String getRecommendname() {
        return recommendname;
    }

    public void setRecommendname(String recommendname) {
        this.recommendname = recommendname;
    }

    public String getAuditopinion() {
        return auditopinion;
    }

    public void setAuditopinion(String auditopinion) {
        this.auditopinion = auditopinion;
    }

    public String getAudituser() {
        return audituser;
    }

    public void setAudituser(String audituser) {
        this.audituser = audituser;
    }

    public String getAuditname() {
        return auditname;
    }

    public void setAuditname(String auditname) {
        this.auditname = auditname;
    }

    public long getDealcount() {
        return dealcount;
    }

    public void setDealcount(long dealcount) {
        this.dealcount = dealcount;
    }

    public long getClickcount() {
        return clickcount;
    }

    public void setClickcount(long clickcount) {
        this.clickcount = clickcount;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getRecommendtime() {
        return recommendtime;
    }

    public void setRecommendtime(String recommendtime) {
        this.recommendtime = recommendtime;
    }

    public String getAudittime() {
        return audittime;
    }

    public void setAudittime(String audittime) {
        this.audittime = audittime;
    }

    public String id;

    public String description;
    public String reason;

    public String link ;

    public String command ;

    public String price ;

    public String expirydate;

    public Boolean isbuy;

    public short state;

    public Boolean recommendflag ;

    public String images ;

    public String buyimages;

    public String recommender ;

    public String recommendname;

    public String recommendtime;

    public String auditopinion ;

    public String audittime;

    public String audituser ;

    public String auditname;

    public long dealcount;

    public long clickcount;
}
