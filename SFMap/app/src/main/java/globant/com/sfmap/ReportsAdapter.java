package globant.com.sfmap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by efren.campillo.
 */
public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitleTextView;
        public TextView mDescriptionTextView;
        public LinearLayout mLayout;

        public ViewHolder(View v) {
            super(v);
            mTitleTextView = (TextView) v.findViewById(R.id.titleText);
            mDescriptionTextView = (TextView) v.findViewById(R.id.description);
            mLayout = (LinearLayout) v.findViewById(R.id.layout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReportsAdapter() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Report report = ReportsRetriever.getInstance().get(position);
        holder.mTitleTextView.setText(report.mCategory + "\n" + report.mAddress);
        holder.mDescriptionTextView.setText(report.mDate + " " + report.mDescription + " \n" + report.mResolution);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(position);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ReportsRetriever.getInstance().getTotalReportsLoaded();
    }

    public interface AdapterClickListener {
        void onClick(int position);
    }

    private AdapterClickListener mClickListener;

    public void registerReportClickListener(AdapterClickListener listener) {
        mClickListener = listener;
    }

    public void unregisterReportClickListener() {
        mClickListener = null;
    }
}