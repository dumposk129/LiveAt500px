package com.dump129.liveat500px.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dump129.liveat500px.R;
import com.dump129.liveat500px.adapter.PhotoListAdapter;
import com.dump129.liveat500px.dao.PhotoItemCollectionDao;
import com.dump129.liveat500px.manager.Contextor;
import com.dump129.liveat500px.manager.PhotoListManager;
import com.dump129.liveat500px.manager.network.HttpManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Dump129 on 1/30/2017.
 */
public class MainFragment extends Fragment {
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnNewPhoto;

    private PhotoListAdapter photoListAdapter;
    private PhotoListManager photoListManager;

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState

        photoListManager = new PhotoListManager();

        listView = (ListView) rootView.findViewById(R.id.listView);
        photoListAdapter = new PhotoListAdapter();
        listView.setAdapter(photoListAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });

        refreshData();

        btnNewPhoto = (Button) rootView.findViewById(R.id.btnNewPhoto);
        btnNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
                hideButtonNewPhoto();
            }
        });
    }

    private void refreshData() {
        if (photoListManager.getCount() == 0) {
            reloadData();
        } else {
            reloadDataNewer();
        }
    }

    private void reloadData() {
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD));
    }

    private void reloadDataNewer() {
        int maxId = photoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoListAfterId(maxId);
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD_NEWER));
    }

    class PhotoListLoadCallback implements Callback<PhotoItemCollectionDao> {
        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;
        int mode;

        public PhotoListLoadCallback(int mode) {
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {
            swipeRefreshLayout.setRefreshing(false);
            if (response.isSuccessful()) {
                PhotoItemCollectionDao dao = response.body();

                int firstVisiblePosition = listView.getFirstVisiblePosition();
                View c = listView.getChildAt(0);
                int top = c == null ? 0 : c.getTop();

                if (mode == MODE_RELOAD_NEWER) {
                    photoListManager.insertDaoAtTopPosition(dao);
                } else {
                    photoListManager.setCollectionDao(dao);
                }
                photoListAdapter.setItemCollectionDao(photoListManager.getCollectionDao());
                photoListAdapter.notifyDataSetChanged();
                if (mode == MODE_RELOAD_NEWER) {
                    int additionalSize = (dao != null && dao.getPhotoItemDaoList() != null) ? dao.getPhotoItemDaoList().size() : 0;
                    photoListAdapter.increaseLastPosition(additionalSize);
                    listView.setSelectionFromTop(firstVisiblePosition + additionalSize, top);

                    if (additionalSize > 0) {
                        showButtonNewPhoto();
                    }
                } else {
                }

                Toast.makeText(Contextor.getInstance().getContext(), "Load Completed", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Toast.makeText(Contextor.getInstance().getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(Contextor.getInstance().getContext(), t.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

    public void showButtonNewPhoto() {
        btnNewPhoto.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_in);
        btnNewPhoto.startAnimation(anim);
    }

    public void hideButtonNewPhoto() {
        btnNewPhoto.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_out);
        btnNewPhoto.startAnimation(anim);
    }
}
