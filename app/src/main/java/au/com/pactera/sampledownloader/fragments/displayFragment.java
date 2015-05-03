package au.com.pactera.sampledownloader.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import au.com.pactera.sampledownloader.R;
import au.com.pactera.sampledownloader.adapters.DownloadItemAdapter;
import au.com.pactera.sampledownloader.dto.JSONResponse;
import au.com.pactera.sampledownloader.network.DownloadHandler;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by priju.jacobpaul on 3/05/15.
 */
public class displayFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener{

    private ActionBar mActionbar;
    private DownloadHandler mDownloadHandler;
    private DownloadItemAdapter mAdapter;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        mActionbar.setTitle(getString(R.string.app_name));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displaylistlayout,container,false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startTimerToShowRefreshView();
        onRefresh();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mSwipeLayout.setRefreshing(true);
        }
    };

    // Workaround to fix android bug
    // https://code.google.com/p/android/issues/detail?id=77712
    private void startTimerToShowRefreshView(){
        handler.sendEmptyMessageDelayed(0,100);
    }

    /**
     * OnRefresh
     */
    @Override public void onRefresh() {
        fetchDetails();
    }


    /**
     * Gather the details from the network
     */
    private void fetchDetails(){
        mDownloadHandler = new DownloadHandler();
        mDownloadHandler.downloadJSON(new DownloadHandler.DownloadHandlerCallback() {
            @Override
            public void onResponse(JSONResponse response) {
                mSwipeLayout.setRefreshing(false);
                mActionbar.setTitle(response.getTitle());
                if(mAdapter == null) {
                    mAdapter = new DownloadItemAdapter(getActivity(), response.getRows());
                    mAdapter.parseAndRemoveItems();
                    setListAdapter(mAdapter);
                }
                else {
                    mAdapter.setRows(response.getRows());
                    mAdapter.parseAndRemoveItems();
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(au.com.pactera.sampledownloader.dto.Error error) {
                mSwipeLayout.setRefreshing(false);
                String message = getString(R.string.download_error);
                if(error.getErrorDescription() != null);{
                    message = error.getErrorDescription();
                }
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        // Cancel all network operation.
        if(mDownloadHandler != null) {
            mDownloadHandler.cancelDownload();
        }
    }
}
