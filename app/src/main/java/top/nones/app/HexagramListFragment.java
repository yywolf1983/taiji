package top.nones.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import org.json.JSONObject;

public class HexagramListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hexagram_list, container, false);
        recyclerView = view.findViewById(R.id.hexagramRecyclerView);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setHasFixedSize(true);

        loadHexagrams();
    }

    private void loadHexagrams() {
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        new Thread(() -> {
            HexagramDataStore store = HexagramDataStore.getInstance();
            store.load(requireContext());
            List<JSONObject> hexagrams = store.getAll();

            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                loadingIndicator.setVisibility(View.GONE);

                if (hexagrams.isEmpty()) {
                    Toast.makeText(getContext(), "加载卦象数据失败", Toast.LENGTH_LONG).show();
                    return;
                }

                recyclerView.setVisibility(View.VISIBLE);
                HexagramAdapter adapter = new HexagramAdapter(hexagrams);
                adapter.setOnItemClickListener(index ->
                        HexagramDetailActivity.start(requireContext(), index));
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}
