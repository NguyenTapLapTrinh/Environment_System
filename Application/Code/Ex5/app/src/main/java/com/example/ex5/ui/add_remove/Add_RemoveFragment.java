package com.example.ex5.ui.add_remove;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ex5.MainActivity;
import com.example.ex5.R;
import com.example.ex5.databinding.FragmentAddRemoveBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_RemoveFragment extends Fragment{

    private FragmentAddRemoveBinding binding;
    private Button add;
    private Button remove;
    private EditText mKV,rID;
    private int number;
    private DatabaseReference mDatabase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddRemoveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        add = root.findViewById(R.id.add);
        remove= root.findViewById(R.id.remove);

        mKV = root.findViewById(R.id.lineKV);
        rID = root.findViewById(R.id.removeID);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                ((MainActivity) getActivity()).SaveData();
                String lineID;
                String lineKV = mKV.getText().toString();
                while(true) {
                    boolean check = true;
                    lineID = Integer.toString((int)Math.floor(Math.random()*(99999-10000+1)+10000));
                    for (int i = 0; i < ((MainActivity) getActivity()).sizeDevice(); i++) {
                        if (((MainActivity) getActivity()).GetDevice(i).equals(lineID)) {
                            check = false;
                        }
                    }
                    if(check) break;
                }
                if (!(lineKV.equals(""))) {
                    mDatabase.child("ThongSo").child(lineID).child("Temp").setValue(0);
                    mDatabase.child("ThongSo").child(lineID).child("Humidity").setValue(0);
                    mDatabase.child("ThongSo").child(lineID).child("Air_Quality").setValue(0);
                    mDatabase.child("ThongSo").child(lineID).child("Heat_index").setValue(0);
                    mDatabase.child("ThongSo").child(lineID).child("Area").setValue(lineKV);
                    mKV.getText().clear();
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Creating");
                    alert.setMessage("ID: " + lineID+"\nKhu Vuc: "+lineKV);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.create().show();
                }
                else Toast.makeText(getActivity(), "Enter Khu Vuc to create", Toast.LENGTH_SHORT).show();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String lineID = rID.getText().toString();
                rID.getText().clear();
                ((MainActivity) getActivity()).SaveData();
                if (!(lineID.equals(""))) {
                    boolean ck = false;
                    for (int i =0; i < ((MainActivity) getActivity()).sizeDevice(); i++ ){
                        if (((MainActivity) getActivity()).GetDevice(i).equals(lineID)) {
                            ck = true;
                            number = i;
                        }
                    }
                    if (ck) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("Alert");
                        alert.setMessage("Do you want to remove");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (((MainActivity) getActivity()).GetText().equals(lineID)) {
                                    AlertDialog.Builder Nalert = new AlertDialog.Builder(getActivity());
                                    Nalert.setTitle("Warnning");
                                    Nalert.setMessage("App is connected with this Device\nChange to another device to remove");
                                    Nalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            int hh =2;
                                        }
                                    });
                                    Nalert.create().show();
                                }
                                else {
                                    ((MainActivity) getActivity()).removeDevice(number);
                                    mDatabase.child("ThongSo").child(lineID).removeValue();
                                    Toast.makeText(getActivity(), "Remove ID", Toast.LENGTH_SHORT).show();
                                    ((MainActivity) getActivity()).save();
                                }
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int hh =2;
                            }
                        });
                        alert.create().show();
                    }
                    else Toast.makeText(getActivity(), "Not found ID", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getActivity(), "Enter ID to remove", Toast.LENGTH_SHORT).show();

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}