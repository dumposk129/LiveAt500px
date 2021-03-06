package com.dump129.liveat500px.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dump129.liveat500px.R;
import com.dump129.liveat500px.activity.MoreInfoActivity;
import com.dump129.liveat500px.adapter.PhotoListAdapter;
import com.dump129.liveat500px.dao.PhotoItemCollectionDao;
import com.dump129.liveat500px.dao.PhotoItemDao;
import com.dump129.liveat500px.datatype.MutableInteger;
import com.dump129.liveat500px.manager.Contextor;
import com.dump129.liveat500px.manager.PhotoListManager;
import com.dump129.liveat500px.manager.network.HttpManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Dump129 on 1/30/2017.
 */
public class MainFragment extends Fragment {
    /*****************
     * Variables Zone
     *****************/
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnNewPhoto;
    boolean isLoadingMore = false;

    private PhotoListAdapter photoListAdapter;
    private PhotoListManager photoListManager;

    private MutableInteger lastPositionInteger;
    /*****************
     * Function Zone
     *****************/
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
    public void onCreate(Bundle savedInstanceState) {
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
        photoListManager = new PhotoListManager();
        lastPositionInteger = new MutableInteger(-1);
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState
        photoListAdapter = new PhotoListAdapter(lastPositionInteger);

        // FindViewById
        listView = (ListView) rootView.findViewById(R.id.listView);
        btnNewPhoto = (Button) rootView.findViewById(R.id.btnNewPhoto);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        photoListAdapter.setDao(photoListManager.getDao());
        listView.setAdapter(photoListAdapter);

        // Listener
        swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener);
        listView.setOnScrollListener(listViewScrollListener);
        listView.setOnItemClickListener(listViewItemClickListener);
        btnNewPhoto.setOnClickListener(buttonClickListener);

        if (savedInstanceState == null)
            refreshData();
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

    private void loadMoreData() {
        if (isLoadingMore) {
            return;
        }
        isLoadingMore = true;
        int minId = photoListManager.getMinimumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoListBeforeId(minId);
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_LOAD_MORE));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
        outState.putBundle("photoListManager", photoListManager.onSaveInstanceState());
        outState.putBundle("lastPosition", lastPositionInteger.onSaveInstanceState());
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
        photoListManager.onRestoreInstanceState(savedInstanceState.getBundle("photoListManager"));
        lastPositionInteger.onRestoreInstanceState(savedInstanceState.getBundle("lastPosition"));
    }

    private void showButtonNewPhoto() {
        btnNewPhoto.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_in);
        btnNewPhoto.startAnimation(anim);
    }

    private void hideButtonNewPhoto() {
        btnNewPhoto.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_out);
        btnNewPhoto.startAnimation(anim);
    }

    private void showToast(String toast) {
        Toast.makeText(Contextor.getInstance().getContext(), toast, Toast.LENGTH_SHORT).show();
    }


    /*****************
     * Listener Zone
     *****************/

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnNewPhoto) {
                listView.smoothScrollToPosition(0);
                hideButtonNewPhoto();
            }
        }
    };
    SwipeRefreshLayout.OnRefreshListener swipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };
    AbsListView.OnScrollListener listViewScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (view == listView) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);

                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    if (photoListManager.getCount() > 0) {
                        loadMoreData();
                    }
                }
            }
        }
    };
    AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < photoListManager.getCount()) {
                PhotoItemDao dao = photoListManager.getDao().getPhotoItemDaoList().get(position);
                FragmentListener listener = (FragmentListener) getActivity();
                if (listener != null) {
                    listener.onPhotoClicked(dao);
                }
            }
        }
    };


    /*****************
     * Inner Class
     *****************/
    class PhotoListLoadCallback implements Callback<PhotoItemCollectionDao> {
        private static final int MODE_RELOAD = 1;
        private static final int MODE_RELOAD_NEWER = 2;
        private static final int MODE_LOAD_MORE = 3;
        private int mode;

        public PhotoListLoadCallback(int mode) {
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {
            if (response.isSuccessful()) {
                PhotoItemCollectionDao dao = response.body();

                int firstVisiblePosition = listView.getFirstVisiblePosition();
                View c = listView.getChildAt(0);
                int top = c == null ? 0 : c.getTop();

                if (mode == MODE_RELOAD_NEWER) {
                    photoListManager.insertDaoAtTopPosition(dao);
                } else if (mode == MODE_LOAD_MORE) {
                    photoListManager.appendDaoAtBottomPosition(dao);
                } else {
                    photoListManager.setDao(dao);
                }
                hideSwipeRefreshLayoutIfLoadingCompleted();
                clearLoadingMoreFlagIfCapable(mode);
                photoListAdapter.setDao(photoListManager.getDao());
                photoListAdapter.notifyDataSetChanged();

                if (mode == MODE_RELOAD_NEWER) {
                    int additionalSize = (dao != null && dao.getPhotoItemDaoList() != null) ? dao.getPhotoItemDaoList().size() : 0;
                    photoListAdapter.increaseLastPosition(additionalSize);
                    listView.setSelectionFromTop(firstVisiblePosition + additionalSize, top);

                    if (additionalSize > 0) {
                        showButtonNewPhoto();
                    }
                }
                showToast("Load Completed");
            } else {
                clearLoadingMoreFlagIfCapable(mode);
                try {
                    showToast(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {
            clearLoadingMoreFlagIfCapable(mode);
            hideSwipeRefreshLayoutIfLoadingCompleted();
            showToast(t.toString());
        }

        private void clearLoadingMoreFlagIfCapable(int mode) {
            if (mode == MODE_LOAD_MORE) {
                isLoadingMore = false;
            }
        }

        private void hideSwipeRefreshLayoutIfLoadingCompleted() {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    /*****************
     * Interface
     *****************/
    public interface FragmentListener {
        void onPhotoClicked(PhotoItemDao photoItemDao);
    }
}
