package top.nones.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
    private int lastPosition = -1;

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

            holder.numberText.setText("#" + (index + 1));
            holder.nameText.setText(name);
            holder.pinyinText.setText(pinyin);
            holder.hexagramView.setGua(gua);

            // 设置进入动画
            setAnimation(holder.itemView, position);

            // 设置点击动画
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    // 添加点击反馈
                    v.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .start();
                            listener.onItemClick(index);
                        })
                        .start();
                }
            });
        } catch (Exception e) {
            // 静默处理
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            viewToAnimate.startAnimation(AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.card_enter));
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return hexagrams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final HexagramView hexagramView;
        final TextView numberText;
        final TextView nameText;
        final TextView pinyinText;

        ViewHolder(View view) {
            super(view);
            hexagramView = view.findViewById(R.id.hexagramView);
            numberText = view.findViewById(R.id.numberText);
            nameText = view.findViewById(R.id.nameText);
            pinyinText = view.findViewById(R.id.pinyinText);
        }
    }
}
