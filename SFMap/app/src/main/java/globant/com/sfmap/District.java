package globant.com.sfmap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by efren.campillo.
 */
public class District {

    float lat;
    float lon;
    String mName;
    int reports = 0;

    public District(String name, JSONObject obj) {
        mName = name;
        try {
            lat = Float.parseFloat(obj.getString("latitude"));
            lon = Float.parseFloat(obj.getString("longitude"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incrementCounter() {
        reports++;
    }

    public static ArrayList<String> colors = new ArrayList<String>() {{
        add("#FF0000");
        add("#EB3600");
        add("#E54800");
        add("#D86D00");
        add("#D27F00");
        add("#C5A300");
        add("#B9C800");
        add("#A6FF00");
        add("#A6FF00");
        add("#A6FF00");
    }};

    public static class ReportComparator implements Comparator<District> {
        @Override
        public int compare(District district1, District district2) {
            return district2.reports - district1.reports;
        }
    }
}
