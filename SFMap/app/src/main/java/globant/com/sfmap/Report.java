package globant.com.sfmap;


import org.json.JSONObject;

/**
 * Created by efren.campillo.
 */
public class Report {

    District mDistrict;

    String mCategory;
    String mTime;
    String mDate;
    String mDayOfWeek;
    String mAddress;
    String mDescription;
    String mResolution;
    String mId;
    String mLat;
    String mLon;


    public Report(JSONObject info, District district) {
        try {
            mCategory = info.getString("category");
            mTime = info.getString("time");
            mDate = info.getString("date");
            mAddress = info.getString("address");
            mDescription = info.getString("descript");
            mDayOfWeek = info.getString("dayofweek");
            mResolution = info.getString("resolution");
            mId = info.getString("incidntnum");
            mLat = info.getString("y");
            mLon = info.getString("x");
            mDistrict = district;
            mDistrict.incrementReportCounter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
