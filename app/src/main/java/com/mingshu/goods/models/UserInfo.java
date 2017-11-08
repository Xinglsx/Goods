package com.mingshu.goods.models;

import java.io.Serializable;

/**
 * Created by Lisx on 2017-06-27.
 */

public class UserInfo implements Serializable {
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        this.ID = id;
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

    public String getRegistertime() {
        return registertime;
    }

    public void setRegistertime(String registertime) {
        this.registertime = registertime;
    }

    private String ID;

    private String CreateTime;

    private String registertime;

    private String id;

    private String userid;

    private String password;

    private String nickname ;

    private String idcard ;

    private String realname;

//    public DateTime? birth ;
    private String phonenumber ;

    private String avatar ;

    private short usertype ;

    private short userlevel;

    private String usersignature ;

    private String wechat ;

    private String qq;

    private String sina;

    private String taobao;
}
