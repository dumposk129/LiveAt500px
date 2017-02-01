package com.dump129.liveat500px.manager.network;

import com.dump129.liveat500px.dao.PhotoItemCollectionDao;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Dump129 on 1/31/2017.
 */

public interface ApiService {
    @POST("list")
    Call<PhotoItemCollectionDao> loadPhotoList();
}
