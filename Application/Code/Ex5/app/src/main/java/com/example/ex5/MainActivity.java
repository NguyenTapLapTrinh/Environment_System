package com.example.ex5;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.Menu;


import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ex5.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final String FILE_NAME = "data.txt";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private List<String> Device =new ArrayList<>();
    private String text = "00000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_heat, R.id.nav_humidity,R.id.nav_temp,R.id.nav_air)
                .setOpenableLayout(drawer)
                .build();
        load();
        if (!(text.equals("00000"))) {
            UnHideItem();
            NameItem("ID: " + text);
        }
        else
            HideItem();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void SaveData() {
        DatabaseReference referenceKV = FirebaseDatabase.getInstance().getReference().child("ThongSo");
        referenceKV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Device.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.getKey();
                    Device.add(name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Store IDDevice
    public void removeDevice(int i){ Device.remove(i) ; }
    public String GetDevice(int i){
        return Device.get(i);
    }
    public int sizeDevice(){
        return Device.size();
    }

    //Navigation bar
    public void UnHideItem() {
        NavigationView menu = findViewById(R.id.nav_view);
        Menu nav_menu = menu.getMenu();
        nav_menu.findItem(R.id.nav_temp).setVisible(true);
        nav_menu.findItem(R.id.nav_air).setVisible(true);
        nav_menu.findItem(R.id.nav_humidity).setVisible(true);
        nav_menu.findItem(R.id.nav_heat).setVisible(true);
    }
    public void HideItem() {
        NavigationView menu = findViewById(R.id.nav_view);
        Menu nav_menu = menu.getMenu();
        nav_menu.findItem(R.id.nav_temp).setVisible(false);
        nav_menu.findItem(R.id.nav_air).setVisible(false);
        nav_menu.findItem(R.id.nav_humidity).setVisible(false);
        nav_menu.findItem(R.id.nav_heat).setVisible(false);
    }
    public void NameItem(String temp){
        NavigationView menu = findViewById(R.id.nav_view);
        MenuItem tools = menu.getMenu().findItem(R.id.nav_title);
        SpannableString s = new SpannableString(temp);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.5f), 0, s.length(), 0);
        tools.setTitle(s);

    }

    //transfer data
    public void SetText(String cText){
        text = cText;
    }
    public String GetText(){
        return text;
    }


    //Save ID to txt
    public void save(){
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(text.getBytes());
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //Load ID from txt
    public void load(){
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String data;
            while((data = br.readLine()) != null){
                text = data;
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}