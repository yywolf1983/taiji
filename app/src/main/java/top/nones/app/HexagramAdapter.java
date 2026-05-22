package top.nones.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;
import java.util.List;

/**
 * 六十四卦 RecyclerView 适配器
 */
public class HexagramAdapter extends RecyclerView.Adapter<HexagramAdapter.ViewHolder> {
    private final List<JSONObject> hexagrams;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int index);
    }

    public HexagramAdapter(List<JSONObject> hexagrams) {
        this.hexagrams = hexagrams;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hexagram_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject hexagram = hexagrams.get(position);
        try {
            String name = hexagram.getString("name");
            String gua = hexagram.getString("gua");
            String pinyin = hexagram.getString("pinyin");
            int index = hexagram.getInt("index");

            holder.nameText.setText(name);
            holder.pinyinText.setText(pinyin);
            holder.hexagramView.setGua(gua);

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(index);
                }
            });
        } catch (Exception e) {
            // 静默处理
        }
    }

    @Override
    public int getItemCount() {
        return hexagrams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final HexagramView hexagramView;
        final TextView nameText;
        final TextView pinyinText;

        ViewHolder(View view) {
            super(view);
            hexagramView = view.findViewById(R.id.hexagramView);
            nameText = view.findViewById(R.id.nameText);
            pinyinText = view.findViewById(R.id.pinyinText);
        }
    }
}
