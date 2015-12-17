package globant.com.sfmap;

import android.content.Context;
import android.content.Intent;
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
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Report report = ReportsRetriever.getInstance().get(position);
        holder.mTitleTextView.setText(report.category + "\n" + report.address);
        holder.mDescriptionTextView.setText(report.date + " " + report.description + " \n" + report.resolution);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCrimeInMap(v.getContext(), report);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ReportsRetriever.getInstance().getTotalReportLoaded();
    }

    private void showCrimeInMap(Context context, Report report) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(MapActivity.CRIME_TITLE, report.category);
        intent.putExtra(MapActivity.CRIME_SNIPPET, report.description + "\n" + report.address);
        intent.putExtra(MapActivity.CRIME_LAT, report.latitud);
        intent.putExtra(MapActivity.CRIME_LON, report.longitud);
        intent.putExtra(MapActivity.MAP_FLAG, true);
        context.startActivity(intent);
    }
}