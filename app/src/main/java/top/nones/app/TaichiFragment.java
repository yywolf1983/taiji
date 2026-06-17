package top.nones.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TaichiFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taichi, container, false);
        
        view.findViewById(R.id.btnCoinDivination).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CoinDivinationActivity.class);
            startActivity(intent);
        });
        
        view.findViewById(R.id.btnYarrowDivination).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), YarrowDivinationActivity.class);
            startActivity(intent);
        });
        
        return view;
    }
}
