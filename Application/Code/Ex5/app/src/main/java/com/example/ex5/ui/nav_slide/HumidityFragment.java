package com.example.ex5.ui.nav_slide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ex5.MainActivity;
import com.example.ex5.R;
import com.example.ex5.databinding.FragmentHumidityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HumidityFragment extends Fragment {
    private TextView mText;
    private DatabaseReference reference;
    private FragmentHumidityBinding binding;
    private ImageView civ;
    private int old=0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHumidityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mText = root.findViewById(R.id.Humidity_Key);
        civ = root.findViewById(R.id.arrow);
        String text = ((MainActivity)getActivity()).GetText();
        if (!(text.equals("00000"))) {
            reference = FirebaseDatabase.getInstance().getReference().child("ThongSo").child(text);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Temp = dataSnapshot.child("Humidity").getValue().toString();
                    mText.setText(Temp);
                    float left = (float) ((12.6 * (Integer.parseInt(Temp))) - 10);
                    int left_r = Math.round(left);
                    LeftRight(old);
                    MoveImg(left_r - old);
                    old = left_r;

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

    //Animation Img
    public void LeftRight(int left){
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) civ.getLayoutParams();
        marginParams.setMargins(left, 550, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
        civ.setLayoutParams(layoutParams);
    }
    public void MoveImg(int left){
        Animation img = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,left,Animation.RELATIVE_TO_PARENT,Animation.RELATIVE_TO_PARENT);
        img.setDuration(2000);
        img.setFillAfter(true);
        civ.startAnimation(img);
    }
}