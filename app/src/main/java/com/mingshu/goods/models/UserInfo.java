package com.mingshu.goods.models;

import java.io.Serializable;

/**
 * Created by Lisx on 2017-06-27.
 */

public class UserInfo implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public short getUsertype() {
        return usertype;
    }

    public void setUsertype(short usertype) {
        this.usertype = usertype;
    }

    public short getUserlevel() {
        return userlevel;
    }

    public void setUserlevel(short userlevel) {
        this.userlevel = userlevel;
    }

    public String getUsersignature() {
        return usersignature;
    }

    public void setUsersignature(String usersignature) {
        this.usersignature = usersignature;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSina() {
        return sina;
    }

    public void setSina(String sina) {
        this.sina = sina;
    }

    public String getTaobao() {
        return taobao;
    }

    public void setTaobao(String taobao) {
        this.taobao = taobao;
    }

    public String id;

    public String userid;

    public String password;

    public String nickname ;

    public String idcard ;

    public String realname;

//    public DateTime? birth ;
    public String phonenumber ;

    public String avatar ;

    public short usertype ;

    public short userlevel;

    public String usersignature ;

    public String wechat ;

    public String qq;

    public String sina;

    public String taobao;
}
