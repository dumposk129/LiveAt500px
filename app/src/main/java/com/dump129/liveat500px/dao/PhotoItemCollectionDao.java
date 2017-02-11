package com.dump129.liveat500px.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dump129 on 1/31/2017.
 */

public class PhotoItemCollectionDao implements Parcelable{
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private List<PhotoItemDao> photoItemDaoList;

    public PhotoItemCollectionDao() {
    }

    protected PhotoItemCollectionDao(Parcel in) {
        success = in.readByte() != 0;
        photoItemDaoList = in.createTypedArrayList(PhotoItemDao.CREATOR);
    }

    public static final Creator<PhotoItemCollectionDao> CREATOR = new Creator<PhotoItemCollectionDao>() {
        @Override
        public PhotoItemCollectionDao createFromParcel(Parcel in) {
            return new PhotoItemCollectionDao(in);
        }

        @Override
        public PhotoItemCollectionDao[] newArray(int size) {
            return new PhotoItemCollectionDao[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeTypedList(photoItemDaoList);
    }
}
