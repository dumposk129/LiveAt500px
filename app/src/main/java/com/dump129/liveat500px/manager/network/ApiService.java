package com.dump129.liveat500px.manager.network;

import com.dump129.liveat500px.dao.PhotoItemCollectionDao;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Dump129 on 1/31/2017.
 */

public interface ApiService {
    @POST("list")
    Call<PhotoItemCollectionDao> loadPhotoList();

    @POST("list/after/{id}")
    Call<PhotoItemCollectionDao> loadPhotoListAfterId(@Path("id") int id);
}
