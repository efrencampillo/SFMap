package globant.com.sfmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ReportsActivity extends AppCompatActivity implements
        ReportsRetriever.ReportsRetrieverListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ReportsAdapter mReportsAdapter;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ReportsRetriever.getInstance().clear();
                ReportsRetriever.getInstance().retrieveMoreReports();
                mReportsAdapter.notifyDataSetChanged();
                setRecyclerViewEndlessScroller();
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mReportsAdapter = new ReportsAdapter();
        mRecyclerView.setAdapter(mReportsAdapter);
        setRecyclerViewEndlessScroller();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.view_map) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReportsRetriever.getInstance().registerListener(this);
        if (ReportsRetriever.getInstance().getTotalReportLoaded() < 1) {
            ReportsRetriever.getInstance().retrieveMoreReports();
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ReportsRetriever.getInstance().unregisterListener();
    }

    @Override
    public void onLocationsAvailable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                mReportsAdapter.notifyDataSetChanged();
                updateTitle();
            }
        });
    }

    @Override
    public void onNetWorkError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                String messageToShow = error;
                if (messageToShow == null) {
                    messageToShow = getString(R.string.network_error_message);
                }
                Toast.makeText(getBaseContext(), messageToShow, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerViewEndlessScroller() {
        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                mSwipeRefreshLayout.setRefreshing(true);
                ReportsRetriever.getInstance().retrieveMoreReports();
            }
        });
    }

    private void updateTitle() {
        mToolbar.setTitle(getString(R.string.reports, ReportsRetriever.getInstance().getTotalReportLoaded()));
    }

}
