package com.example.ex5.ui.nav_slide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ex5.MainActivity;
import com.example.ex5.R;
import com.example.ex5.databinding.FragmentHeatIndexBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Heat_IndexFragment extends Fragment {
    private TextView mText;
    private DatabaseReference reference;
    private FragmentHeatIndexBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHeatIndexBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mText = root.findViewById(R.id.Heat_Key);
        String text = ((MainActivity)getActivity()).GetText();
        if (!(text.equals("00000"))) {
            reference = FirebaseDatabase.getInstance().getReference().child("ThongSo").child(text);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Temp = dataSnapshot.child("Heat_index").getValue().toString();
                    mText.setText(Temp);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}