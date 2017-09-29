package com.mingshu.goods.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lisx on 2017-09-28.
 * 分页类型
 */

public class PagedData<T> implements Serializable {
    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public int getTotalPages() {
        return TotalPages;
    }

    public void setTotalPages(int totalPages) {
        TotalPages = totalPages;
    }

    public List<T> getDataList() {
        return DataList;
    }

    public void setDataList(List<T> dataList) {
        DataList = dataList;
    }

    private int TotalCount;
    private int TotalPages;
    private List<T> DataList;
}
