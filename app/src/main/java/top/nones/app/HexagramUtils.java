package top.nones.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HexagramUtils {
    public static String getHexagramName(JSONObject hexagram) {
        return hexagram.optString("name");
    }

    public static String getPinyin(JSONObject hexagram) {
        return hexagram.optString("pinyin");
    }

    public static String getGua(JSONObject hexagram) {
        return hexagram.optString("gua");
    }

    public static String getDescription(JSONObject hexagram) {
        return hexagram.optString("description");
    }

    public static JSONObject getInterpretation(JSONObject hexagram) {
        try {
            return hexagram.getJSONObject("interpretation");
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static JSONArray getLines(JSONObject hexagram) {
        try {
            return hexagram.getJSONArray("lines");
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
}