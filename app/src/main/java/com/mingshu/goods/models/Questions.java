package com.mingshu.goods.models;

/**
 * Created by Lisx on 2017-09-01.
 * 用户反馈信息
 */

public class Questions {
    private String id ;
    private String feedbackuserid;
    private String feedbackusernickname;
    private String content;
    private String feedbacktime;
    private String contact;//联系方式

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedbackuserid() {
        return feedbackuserid;
    }

    public void setFeedbackuserid(String feedbackuserid) {
        this.feedbackuserid = feedbackuserid;
    }

    public String getFeedbackusernickname() {
        return feedbackusernickname;
    }

    public void setFeedbackusernickname(String feedbackusernickname) {
        this.feedbackusernickname = feedbackusernickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFeedbacktime() {
        return feedbacktime;
    }

    public void setFeedbacktime(String feedbacktime) {
        this.feedbacktime = feedbacktime;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
