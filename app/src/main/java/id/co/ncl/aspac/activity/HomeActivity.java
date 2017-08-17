package id.co.ncl.aspac.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.fragment.AttendanceFragment;
import id.co.ncl.aspac.fragment.CreateFragment;
import id.co.ncl.aspac.fragment.HomeFragment;
import id.co.ncl.aspac.fragment.LeaveFragment;
import id.co.ncl.aspac.fragment.MachineFragment;
import id.co.ncl.aspac.fragment.SparepartFragment;
import id.co.ncl.aspac.fragment.WorkFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setFirstFragment();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //get the fragment manager here!
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new Fragment();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_attendance) {
            fragment = new AttendanceFragment();
        } else if (id == R.id.nav_leave) {
            fragment = new LeaveFragment();
        } else if (id == R.id.nav_work) {
            fragment = new WorkFragment();
        } else if (id == R.id.nav_sparepart) {
            fragment = new SparepartFragment();
        } else if (id == R.id.nav_machine) {
            fragment = new MachineFragment();
        } else if (id == R.id.nav_create) {
            fragment = new CreateFragment();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        //finish the fragment transaction
        transaction.replace(R.id.home_main_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.home_main_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setFirstFragment() {
        boolean boolIntent = checkforBundle();
        Fragment fragment = new HomeFragment();
        //Fragment fragment = new InputTaskFragment();
        Log.d("boolIntent", String.valueOf(boolIntent));

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.home_main_frame, fragment);
        transaction.commit();
    }

    private boolean checkforBundle() {
        boolean value = false;
        // You can be pretty confident that the intent will not be null here.
        Intent intent = getIntent();

        // Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("fragment")) {
                String fragment = extras.getString("fragment");
                if(fragment.equals("forum")) {
                    value = true;
                    Log.d("checkNotif", "IT IS TRUE");
                }
                else {
                    Log.d("checkNotif", "IT IS FALSE");
                }
            }
        }

        return value;
    }

    private void logout() {
        //clear all shared preferences first
        SharedPreferences sharedPref = getSharedPreferences("userCred", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        //start intent, and kill the previous activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        //add some Toast to help undertand better
        Toast.makeText(this, "Kamu telah berhasil keluar", Toast.LENGTH_SHORT).show();
    }
}
