package com.dump129.liveat500px.manager;

import android.content.Context;
import android.os.Bundle;

import com.dump129.liveat500px.dao.PhotoItemCollectionDao;
import com.dump129.liveat500px.dao.PhotoItemDao;

import java.util.ArrayList;

/**
 * Created by Dump129 on 1/30/2017.
 */
public class PhotoListManager {
    private PhotoItemCollectionDao dao;

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

    public PhotoItemCollectionDao getDao() {
        return dao;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
    }

    // Insert Data at Top
    public void insertDaoAtTopPosition(PhotoItemCollectionDao newDao) {
        if (dao == null) {
            dao = new PhotoItemCollectionDao();
        }
        if (dao.getPhotoItemDaoList() == null) {
            dao.setPhotoItemDaoList(new ArrayList<PhotoItemDao>());
        }
        dao.getPhotoItemDaoList().addAll(0, newDao.getPhotoItemDaoList());
    }

    public int getMaximumId() {
        if (dao == null) {
            return 0;
        }
        if (dao.getPhotoItemDaoList() == null) {
            return 0;
        }

        if (dao.getPhotoItemDaoList().size() == 0) {
            return 0;
        }

        int maxId = dao.getPhotoItemDaoList().get(0).getId();
        for (int i = 1; i < dao.getPhotoItemDaoList().size(); i++) {
            maxId = Math.max(maxId, dao.getPhotoItemDaoList().get(i).getId());
        }
        return maxId;
    }

    public int getMinimumId() {
        if (dao == null) {
            return 0;
        }
        if (dao.getPhotoItemDaoList() == null) {
            return 0;
        }

        if (dao.getPhotoItemDaoList().size() == 0) {
            return 0;
        }

        int minId = dao.getPhotoItemDaoList().get(0).getId();
        for (int i = 1; i < dao.getPhotoItemDaoList().size(); i++) {
            minId = Math.min(minId, dao.getPhotoItemDaoList().get(i).getId());
        }
        return minId;
    }

    public int getCount() {
        if (dao == null) {
            return 0;
        }
        if (dao.getPhotoItemDaoList() == null) {
            return 0;
        }

       return dao.getPhotoItemDaoList().size();
    }

    // Insert Data at Bottom
    public void appendDaoAtBottomPosition(PhotoItemCollectionDao newDao) {
        if (dao == null) {
            dao = new PhotoItemCollectionDao();
        }
        if (dao.getPhotoItemDaoList() == null) {
            dao.setPhotoItemDaoList(new ArrayList<PhotoItemDao>());
        }
        dao.getPhotoItemDaoList().addAll(dao.getPhotoItemDaoList().size(), newDao.getPhotoItemDaoList());
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("dao", dao);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        dao = savedInstanceState.getParcelable("dao");
    }
}
