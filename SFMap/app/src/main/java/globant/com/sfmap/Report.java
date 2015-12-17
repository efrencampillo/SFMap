package globant.com.sfmap;


import org.json.JSONObject;

/**
 * Created by efren.campillo.
 */
public class Report {

    District mDistrict;

    String category;
    String time;
    String date;
    String dayOfWeek;
    String address;
    String description;
    String resolution;
    String id;
    String latitud;
    String longitud;


    public Report(JSONObject info, District district) {
        try {
            category = info.getString("category");
            time = info.getString("time");
            date = info.getString("date");
            address = info.getString("address");
            description = info.getString("descript");
            dayOfWeek = info.getString("dayofweek");
            resolution = info.getString("resolution");
            id = info.getString("incidntnum");
            latitud = info.getString("y");
            longitud = info.getString("x");
            mDistrict = district;
            mDistrict.incrementCounter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
