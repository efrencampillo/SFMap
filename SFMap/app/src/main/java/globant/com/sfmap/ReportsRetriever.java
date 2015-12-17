package globant.com.sfmap;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by efren.campillo.
 */
public class ReportsRetriever {

    private static ReportsRetriever sInstance;
    private ReportsRetrieverListener mListener;
    private ArrayList<Report> mReports;
    private ArrayList<District> mDistrict;
    OkHttpClient mClient;

    private int REQUEST_LIMIT = 50;

    private ReportsRetriever() {
        mReports = new ArrayList<>();
        mDistrict = new ArrayList<>();
        mClient = new OkHttpClient();
    }

    public static ReportsRetriever getInstance() {
        if (sInstance == null) {
            sInstance = new ReportsRetriever();
        }
        return sInstance;
    }

    public void registerListener(ReportsRetrieverListener listener) {
        mListener = listener;
    }

    public void unregisterListener() {
        mListener = null;
    }

    public interface ReportsRetrieverListener {
        void onLocationsAvailable();

        void onNetWorkError(String error);
    }

    public void retrieveMoreReports() {
        int offset = mReports.size();
        retrieveReportsFromServer(offset);
    }

    private void retrieveReportsFromServer(final int offset) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, -1);
                    Date date = cal.getTime();
                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("https")
                            .host("data.sfgov.org")
                            .addPathSegment("resource")
                            .addPathSegment("ritf-b9ki.json")
                            .addEncodedQueryParameter("$limit", "" + REQUEST_LIMIT)
                            .addEncodedQueryParameter("$offset", "" + offset)
                            .addEncodedQueryParameter("$order", "date%20DESC")
                            .addQueryParameter("$where", "date>'" + dateFormat.format(date) + "'")
                            .build();
                    Request request = new Request.Builder().url(url).build();
                    Response response = mClient.newCall(request).execute();
                    JSONArray array = new JSONArray(response.body().string());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String districtName = obj.getString("pddistrict");
                        District district;
                        if (existDistrict(districtName)) {
                            district = getDistrict(districtName);
                        } else {
                            district = new District(districtName, obj.getJSONObject("location"));
                            mDistrict.add(district);
                        }
                        Report report = new Report(obj, district);
                        mReports.add(report);
                    }
                    if (mListener != null) {
                        mListener.onLocationsAvailable();//TODO send paserd info
                    }
                } catch (Exception e) {
                    if (mListener != null) {
                        mListener.onNetWorkError(e.getMessage());
                    }
                }
            }
        }).start();
    }

    private boolean existDistrict(String name) {
        for (District district : mDistrict) {
            if (district.mName.equals(name)) return true;
        }
        return false;
    }

    private District getDistrict(String name) {
        for (District district : mDistrict) {
            if (district.mName.equals(name)) return district;
        }
        return null;
    }

    public Report get(int index) {
        if (mReports.size() < index) {
            return null;
        }
        return mReports.get(index);
    }

    public int getTotalReportLoaded() {
        return mReports.size();
    }

    public ArrayList<District> getDistricts() {
        return mDistrict;
    }

    public void clear() {
        mReports.clear();
        mDistrict.clear();
    }
}
