package globant.com.sfmap;

/**
 * Created by efren.campillo.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int mPreviousTotal = 0; // The total number of items in the dataset after the last load
    private boolean mIsLoading = true; // True if we are still waiting for the last set of data to load.
    private int mVisibleThreshold = 5; // The minimum amount of items to have below your current scroll position before mIsLoading more.
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    private int mCurrentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mVisibleItemCount = recyclerView.getChildCount();
        mTotalItemCount = mLinearLayoutManager.getItemCount();
        mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (mIsLoading) {
            if (mTotalItemCount > mPreviousTotal) {
                mIsLoading = false;
                mPreviousTotal = mTotalItemCount;
            }
        }
        if (!mIsLoading && (mTotalItemCount - mVisibleItemCount)
                <= (mFirstVisibleItem + mVisibleThreshold)) {
            // End has been reached
            mCurrentPage++;

            onLoadMore(mCurrentPage);

            mIsLoading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);

}
