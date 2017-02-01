package com.dump129.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dump129.liveat500px.dao.PhotoItemCollectionDao;
import com.dump129.liveat500px.dao.PhotoItemDao;
import com.dump129.liveat500px.manager.PhotoListManager;
import com.dump129.liveat500px.view.PhotoListItem;

/**
 * Created by Dump129 on 1/30/2017.
 */

public class PhotoListAdapter extends BaseAdapter {
    private PhotoItemCollectionDao itemCollectionDao;
    @Override
    public int getCount() {
        if (itemCollectionDao == null || itemCollectionDao.getPhotoItemDaoList() == null) {
            return 0;
        }
        return itemCollectionDao.getPhotoItemDaoList().size();
    }

    @Override
    public Object getItem(int position) {
        return itemCollectionDao.getPhotoItemDaoList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoListItem item;
        if (convertView == null) {
            item = new PhotoListItem(parent.getContext());
        } else {
            item = (PhotoListItem) convertView;
        }

        // Set Data to ListView
        PhotoItemDao dao = (PhotoItemDao) getItem(position);
        item.setNameText(dao.getCaption());
        item.setDescription(dao.getUserName() + "\n" + dao.getCamera());
        item.setImageUrl(dao.getImageUrl());

        return item;
    }

    public void setItemCollectionDao(PhotoItemCollectionDao itemCollectionDao) {
        this.itemCollectionDao = itemCollectionDao;
    }
}
