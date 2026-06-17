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

    public static String getBaihua(JSONObject hexagram) {
        return hexagram.optString("baihua");
    }

    public static String getModern(JSONObject hexagram) {
        return hexagram.optString("modern");
    }

    public static String getWuxing(JSONObject hexagram) {
        return hexagram.optString("wuxing");
    }

    public static String getNature(JSONObject hexagram) {
        return hexagram.optString("nature");
    }

    public static String getFamily(JSONObject hexagram) {
        return hexagram.optString("family");
    }

    public static String getSeason(JSONObject hexagram) {
        return hexagram.optString("season");
    }

    public static String getDirection(JSONObject hexagram) {
        return hexagram.optString("direction");
    }

    public static JSONObject getRelationships(JSONObject hexagram) {
        try {
            return hexagram.getJSONObject("relationships");
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static int getOppositeIndex(JSONObject hexagram) {
        try {
            JSONObject relationships = getRelationships(hexagram);
            return relationships.optInt("opposite", -1);
        } catch (Exception e) {
            return -1;
        }
    }

    public static int getInverseIndex(JSONObject hexagram) {
        try {
            JSONObject relationships = getRelationships(hexagram);
            return relationships.optInt("inverse", -1);
        } catch (Exception e) {
            return -1;
        }
    }

    public static int findHexagramIndexByGua(String guaStr) {
        try {
            org.json.JSONArray hexagrams = HexagramDataStore.getInstance().getJsonArray();
            if (hexagrams == null) return -1;
            for (int i = 0; i < hexagrams.length(); i++) {
                JSONObject hexagram = hexagrams.getJSONObject(i);
                if (guaStr.equals(hexagram.optString("gua"))) {
                    return i;
                }
            }
        } catch (Exception e) {
        }
        return -1;
    }
}