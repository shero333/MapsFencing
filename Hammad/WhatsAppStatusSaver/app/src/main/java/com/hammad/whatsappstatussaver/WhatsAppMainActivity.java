package com.hammad.whatsappstatussaver;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hammad.whatsappstatussaver.adapter.ViewPagerAdapter;

public class WhatsAppMainActivity extends AppCompatActivity {

    Activity activity;

    //shortcut Alt+Shift+F
    private int REQUEST_CODE=1;

    TabLayout tabLayout;
    ViewPager viewPager;

    //states for Color State List
    int[][] states = new int[][] {
            new int[] { android.R.attr.state_selected}, // selected
            new int[] {-android.R.attr.state_selected}, // unselected
    };

    //colors for Color State List
    int[] colors=new int[]{Color.WHITE,Color.BLACK};

    //color state list
    ColorStateList colorStateList;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whatsapp_activity_main);

        //initializing views
        initializeViews();

        //tab layout and view pager settings
        tabAndViewPager();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            Intent intent=new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent,1000);
        }
        //checking permissions
        checkPermissions();
    }

    private void checkPermissions() {
        String[] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(activity,permission[0]) != PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(activity, permission[1]) != PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(activity,permission[2]) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,permission,REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE && grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(activity, "Permission granted!", Toast.LENGTH_SHORT).show();
            recreate();
        }
        else
        {
            Toast.makeText(activity, "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }

    //this handles the back button pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews()
    {
        //initializing toolbar
        toolbar=findViewById(R.id.toolbar_whatsapp);
        setSupportActionBar(toolbar);

        //initializing the activity reference
        activity=this;

        //initializing tab layout and view pager
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);

        //setting the toolbar back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //color state list for selected & unselected tab text color (White text color when Tab is selected & Black text color when Tab is unselected)
        colorStateList=new ColorStateList(states,colors);
    }

    private void tabAndViewPager()
    {
        //adding tabs in tab layout
        tabLayout.addTab(tabLayout.newTab().setText("Images"));
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Saved Files"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //setting the tab text color which accept a color state list
        tabLayout.setTabTextColors(colorStateList);

        //setting the ViewPager adapter
        final ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),activity,tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //this is used to navigate tabs based on user clicks
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}