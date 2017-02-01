package com.dump129.liveat500px.manager;

import android.content.Context;

import com.dump129.liveat500px.dao.PhotoItemCollectionDao;

/**
 * Created by Dump129 on 1/30/2017.
 */
public class PhotoListManager {
    private PhotoItemCollectionDao collectionDao;

    private static PhotoListManager instance;

    public static PhotoListManager getInstance() {
        if (instance == null)
            instance = new PhotoListManager();
        return instance;
    }

    private Context mContext;

    private PhotoListManager() {
        mContext = Contextor.getInstance().getContext();
    }

    public PhotoItemCollectionDao getCollectionDao() {
        return collectionDao;
    }

    public void setCollectionDao(PhotoItemCollectionDao collectionDao) {
        this.collectionDao = collectionDao;
    }
}
