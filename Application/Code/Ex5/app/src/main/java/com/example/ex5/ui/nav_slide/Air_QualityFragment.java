package com.example.ex5.ui.nav_slide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ex5.MainActivity;
import com.example.ex5.R;
import com.example.ex5.databinding.FragmentAirQualityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Air_QualityFragment extends Fragment {
    private TextView mText;
    private DatabaseReference reference;
    private FragmentAirQualityBinding binding;
    private ImageView civ;
    private int old;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAirQualityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mText = root.findViewById(R.id.Air_Key);
        civ = root.findViewById(R.id.arrow);
        String text = ((MainActivity)getActivity()).GetText();
        if (!(text.equals("00000"))) {
            reference = FirebaseDatabase.getInstance().getReference().child("ThongSo").child(text);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    float top = 0;
                    String Temp = dataSnapshot.child("Air_Quality").getValue().toString();
                    mText.setText(Temp);
//              float angle = (float) ((0.3*(Integer.parseInt(Temp)))-295);
                    if (Temp.equals("0")) top = 990;
                    else
                        top = (float) (-0.57 * (Float.parseFloat(Temp))) + 1220;
                    int updown = Math.round(top);
                    UpDown(old);
                    MoveImg(updown - old, 0);
                    old = updown;

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
    public void Rolate(float angle){
        //civ.setRotation(0);
        civ.animate().rotation(angle).setDuration(2000).start();
    }
    public void UpDown(int top){
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) civ.getLayoutParams();
        marginParams.setMargins(0, top, 0, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginParams);
        civ.setLayoutParams(layoutParams);
    }
    public void MoveImg(int top,int bot){
        Animation img = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,Animation.RELATIVE_TO_PARENT,Animation.RELATIVE_TO_PARENT,top);
        img.setDuration(2000);
        img.setFillAfter(true);
        civ.startAnimation(img);
    }
}