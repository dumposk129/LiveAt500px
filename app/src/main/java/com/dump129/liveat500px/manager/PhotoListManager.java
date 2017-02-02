package com.dump129.liveat500px.manager;

import android.content.Context;

import com.dump129.liveat500px.dao.PhotoItemCollectionDao;
import com.dump129.liveat500px.dao.PhotoItemDao;

import java.util.ArrayList;

/**
 * Created by Dump129 on 1/30/2017.
 */
public class PhotoListManager {
    private PhotoItemCollectionDao collectionDao;

   /* private static PhotoListManager instance;

    public static PhotoListManager getInstance() {
        if (instance == null)
            instance = new PhotoListManager();
        return instance;
    }*/

    private Context mContext;

    public PhotoListManager() {
        mContext = Contextor.getInstance().getContext();
    }

    public PhotoItemCollectionDao getCollectionDao() {
        return collectionDao;
    }

    public void setCollectionDao(PhotoItemCollectionDao collectionDao) {
        this.collectionDao = collectionDao;
    }

    // Insert Data at Top
    public void insertDaoAtTopPosition(PhotoItemCollectionDao newDao) {
        if (collectionDao == null) {
            collectionDao = new PhotoItemCollectionDao();
        }
        if (collectionDao.getPhotoItemDaoList() == null) {
            collectionDao.setPhotoItemDaoList(new ArrayList<PhotoItemDao>());
        }
        collectionDao.getPhotoItemDaoList().addAll(0, newDao.getPhotoItemDaoList());
    }

    public int getMaximumId() {
        if (collectionDao == null) {
            return 0;
        }
        if (collectionDao.getPhotoItemDaoList() == null) {
            return 0;
        }

        if (collectionDao.getPhotoItemDaoList().size() == 0) {
            return 0;
        }

        int maxId = collectionDao.getPhotoItemDaoList().get(0).getId();
        for (int i = 1; i < collectionDao.getPhotoItemDaoList().size(); i++) {
            maxId = Math.max(maxId, collectionDao.getPhotoItemDaoList().get(i).getId());
        }
        return maxId;
    }

    public int getMinimumId() {
        if (collectionDao == null) {
            return 0;
        }
        if (collectionDao.getPhotoItemDaoList() == null) {
            return 0;
        }

        if (collectionDao.getPhotoItemDaoList().size() == 0) {
            return 0;
        }

        int minId = collectionDao.getPhotoItemDaoList().get(0).getId();
        for (int i = 1; i < collectionDao.getPhotoItemDaoList().size(); i++) {
            minId = Math.min(minId, collectionDao.getPhotoItemDaoList().get(i).getId());
        }
        return minId;
    }

    public int getCount() {
        if (collectionDao == null) {
            return 0;
        }
        if (collectionDao.getPhotoItemDaoList() == null) {
            return 0;
        }

       return collectionDao.getPhotoItemDaoList().size();
    }

    // Insert Data at Bottom
    public void appendDaoAtBottomPosition(PhotoItemCollectionDao newDao) {
        if (collectionDao == null) {
            collectionDao = new PhotoItemCollectionDao();
        }
        if (collectionDao.getPhotoItemDaoList() == null) {
            collectionDao.setPhotoItemDaoList(new ArrayList<PhotoItemDao>());
        }
        collectionDao.getPhotoItemDaoList().addAll(collectionDao.getPhotoItemDaoList().size(), newDao.getPhotoItemDaoList());
    }
}
