package com.dump129.liveat500px.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dump129.liveat500px.dao.PhotoItemCollectionDao;
import com.dump129.liveat500px.dao.PhotoItemDao;
import com.google.gson.Gson;

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
        // Load cache with Persistence Storage
        loadCache();
    }

    public PhotoItemCollectionDao getDao() {
        return dao;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
        // Save cache with Persistence Storage
        saveCache();
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
        // Save cache with Persistence Storage
        saveCache();
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
        // Save cache with Persistence Storage
        saveCache();
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("dao", dao);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        dao = savedInstanceState.getParcelable("dao");
    }

    private void saveCache() {
        PhotoItemCollectionDao cacheDao = new PhotoItemCollectionDao();
        if (dao != null && dao.getPhotoItemDaoList() != null)
            cacheDao.setPhotoItemDaoList(dao.getPhotoItemDaoList().subList(0, Math.min(20, dao.getPhotoItemDaoList().size())));
        String json = new Gson().toJson(cacheDao);

        SharedPreferences prefs = mContext.getSharedPreferences("photos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //Add/Edit/Delete
        editor.putString("json", json);
        editor.apply();
    }

    private void loadCache() {
        SharedPreferences prefs = mContext.getSharedPreferences("photos", Context.MODE_PRIVATE);
        String json = prefs.getString("json", null);
        if (json == null)
            return;

        dao = new Gson().fromJson(json, PhotoItemCollectionDao.class);
    }
}
