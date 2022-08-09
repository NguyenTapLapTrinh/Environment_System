package com.example.ex5.ui.nav_slide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ex5.MainActivity;
import com.example.ex5.R;
import com.example.ex5.databinding.FragmentTempBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TempFragment extends Fragment {
    private TextView mText;
    private DatabaseReference reference;
    private FragmentTempBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTempBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mText = root.findViewById(R.id.Temp_Key);
        String text = ((MainActivity)getActivity()).GetText();
        if (!(text.equals("00000"))) {
            reference = FirebaseDatabase.getInstance().getReference().child("ThongSo").child(text);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Temp = dataSnapshot.child("Temp").getValue().toString();
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