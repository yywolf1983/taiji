package top.nones.app;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 六十四卦数据缓存单例
 * 避免重复从 assets 加载和解析 JSON
 */
public class HexagramDataStore {
    private static volatile HexagramDataStore instance;
    private JSONArray hexagramsArray;
    private final List<JSONObject> hexagrams = new ArrayList<>();

    private HexagramDataStore() {}

    public static HexagramDataStore getInstance() {
        if (instance == null) {
            synchronized (HexagramDataStore.class) {
                if (instance == null) {
                    instance = new HexagramDataStore();
                }
            }
        }
        return instance;
    }

    /**
     * 从 assets 加载数据，仅首次调用时读取文件
     */
    public void load(Context context) {
        if (!hexagrams.isEmpty()) return;
        try {
            InputStream is = context.getAssets().open("hexagrams.json");
            byte[] buffer = new byte[is.available()];
            int bytesRead = 0;
            while (bytesRead < buffer.length) {
                int read = is.read(buffer, bytesRead, buffer.length - bytesRead);
                if (read == -1) break;
                bytesRead += read;
            }
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject root = new JSONObject(json);
            hexagramsArray = root.getJSONArray("hexagrams");
            hexagrams.clear();
            for (int i = 0; i < hexagramsArray.length(); i++) {
                hexagrams.add(hexagramsArray.getJSONObject(i));
            }
        } catch (Exception e) {
            hexagrams.clear();
            hexagramsArray = null;
        }
    }

    /** 获取总数 */
    public int size() {
        return hexagrams.size();
    }

    /** 按索引获取卦象 */
    public JSONObject get(int index) {
        if (index < 0 || index >= hexagrams.size()) return null;
        return hexagrams.get(index);
    }

    /** 获取完整数组 */
    public List<JSONObject> getAll() {
        return hexagrams;
    }

    /** 获取 JSONArray（兼容旧接口） */
    public JSONArray getJsonArray() {
        return hexagramsArray;
    }
}
