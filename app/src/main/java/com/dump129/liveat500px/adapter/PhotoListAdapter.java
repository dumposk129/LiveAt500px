package com.dump129.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.dump129.liveat500px.R;
import com.dump129.liveat500px.dao.PhotoItemCollectionDao;
import com.dump129.liveat500px.dao.PhotoItemDao;
import com.dump129.liveat500px.view.PhotoListItem;

/**
 * Created by Dump129 on 1/30/2017.
 */

public class PhotoListAdapter extends BaseAdapter {
    private PhotoItemCollectionDao itemCollectionDao;
    private int lastPosition = -1;

    @Override
    public int getCount() {
        if (itemCollectionDao == null) {
            return 0;
        }
        if (itemCollectionDao.getPhotoItemDaoList() == null) {
            return 1;
        }
        return itemCollectionDao.getPhotoItemDaoList().size() + 1;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() - 1 ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {
            ProgressBar progressBar;
            if (convertView != null) {
                progressBar = (ProgressBar) convertView;
            } else {
                progressBar = new ProgressBar(parent.getContext());
            }
            return progressBar;
        }
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

        if (position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(), R.anim.up_from_bottom);
            item.startAnimation(anim);
            lastPosition = position;
        }

        return item;
    }

    public void increaseLastPosition(int amount) {
        lastPosition += amount;
    }

    public void setItemCollectionDao(PhotoItemCollectionDao itemCollectionDao) {
        this.itemCollectionDao = itemCollectionDao;
    }
}
