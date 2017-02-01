package com.dump129.liveat500px.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dump129 on 1/31/2017.
 */

public class PhotoItemCollectionDao {
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private List<PhotoItemDao> photoItemDaoList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<PhotoItemDao> getPhotoItemDaoList() {
        return photoItemDaoList;
    }

    public void setPhotoItemDaoList(List<PhotoItemDao> photoItemDaoList) {
        this.photoItemDaoList = photoItemDaoList;
    }
}
