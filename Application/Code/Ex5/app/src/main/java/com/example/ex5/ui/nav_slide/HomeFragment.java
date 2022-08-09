package com.example.ex5.ui.nav_slide;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.example.ex5.MainActivity;
import com.example.ex5.R;
import com.example.ex5.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentHomeBinding binding;
    private Spinner spinner;
    private TextView mText, date_line,time_line, mKV;
    private TextView link_line;
    private Button mButton;
    private Button ok_Button;
    private List<String> ARR = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //link_line = root.findViewById(R.id.link);
        spinner = root.findViewById(R.id.spinner);
        mText = root.findViewById(R.id.ID);
        mKV = root.findViewById(R.id.KV);
        mButton = root.findViewById(R.id.add_remove);
        ok_Button = root.findViewById(R.id.ok);
        date_line = root.findViewById(R.id.date);
        time_line =root.findViewById(R.id.time);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, ARR);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        DatabaseReference referenceKV = FirebaseDatabase.getInstance().getReference().child("ThongSo");
        referenceKV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ARR.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String name = postSnapshot.getKey();
                    ARR.add(name);
                    spinner.setAdapter(adapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //link_line.setMovementMethod(LinkMovementMethod.getInstance());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_add_remove);
            }
        });
        ok_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mText.equals(""))
                    Toast.makeText(getActivity(), "No device found", Toast.LENGTH_SHORT).show();
                else {
                    ((MainActivity) getActivity()).UnHideItem();
                    ((MainActivity) getActivity()).SetText(mText.getText().toString());
                    ((MainActivity) getActivity()).NameItem("ID: " + mText.getText().toString());
                    ((MainActivity) getActivity()).save();
                    Toast.makeText(getActivity(), "Connect to ID Device: " + mText.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        CountDownTimer cdt = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {
                Date Date_time = Calendar.getInstance().getTime();
                String format_date = DateFormat.getDateInstance(DateFormat.FULL).format(Date_time);
                String format_time = DateFormat.getTimeInstance(DateFormat.DEFAULT).format(Date_time);
                date_line.setText(format_date);
                time_line.setText(format_time);
            }

            @Override
            public void onFinish() {
                this.start();
            }
        };
        cdt.start();
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String itemKV = spinner.getSelectedItem().toString();

        mText.setText(itemKV);
        DatabaseReference referenceKV = FirebaseDatabase.getInstance().getReference().child("ThongSo");
        referenceKV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String name = postSnapshot.getKey();
                    if (name.equals(itemKV)) {
                        DatabaseReference referenceKV = FirebaseDatabase.getInstance().getReference().child("ThongSo").child(name);
                        referenceKV.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    String name = postSnapshot.getKey();
                                    if (name.equals("Area")) {
                                        String KhuVuc = dataSnapshot.child("Area").getValue().toString();;
                                        mKV.setText(KhuVuc);
                                        break;
                                    }
                                    else
                                        mKV.setText("");
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}