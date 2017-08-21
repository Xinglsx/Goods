package com.mingshu.goods.models;

import java.io.Serializable;

/**
 * Created by Lisx on 2017-08-21.
 */

public class VersionInfo implements Serializable {
    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    /// <summary>
    /// 版本号
    /// </summary>
    public int versionNumber;
    /// <summary>
    /// 版本编号
    /// </summary>
    public String version;
    /// <summary>
    /// 下载地址
    /// </summary>
    public String downloadAddress;
    /// <summary>
    /// 更新内容
    /// </summary>
    public String updateContent;
}
