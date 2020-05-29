package com.example.coursehelper.View;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.coursehelper.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fragmentManager;
    private CoursesScheduleTabFragment homePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        homePage = new CoursesScheduleTabFragment();

        setupToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        setupDrawerContent(navView);

        drawerToggle = setupDrawerToggle();
        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        loadFragment(homePage, CoursesScheduleTabFragment.FRAG_TAG);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView homeLogo = toolbar.findViewById(R.id.homelogo);
        homeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursesScheduleTabFragment myFragment = (CoursesScheduleTabFragment) fragmentManager.findFragmentByTag(CoursesScheduleTabFragment.FRAG_TAG);
                if(myFragment != null ){
                    if(myFragment.isVisible()){
                        Toast.makeText(MainActivity.this, "You are already in Home Page", Toast.LENGTH_SHORT).show();
                    } else {
                        returnToHomePage();
                    }
                }
            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name,  R.string.app_name);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
        navigationView.getHeaderView(0).findViewById(R.id.loginTextView).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadFragment(new LoginFragment(), LoginFragment.TAG_FRAG);
                drawerLayout.closeDrawers();
            }
        });
    }

    private void returnToHomePage() {
        int entryCount = fragmentManager.getBackStackEntryCount();
        for(int i=0; i<entryCount-1; i++){
            fragmentManager.popBackStackImmediate();
        }

        String rootFragmentTag  = fragmentManager.getBackStackEntryAt(0).getName();
        fragmentManager.findFragmentByTag(rootFragmentTag).getView().setVisibility(View.VISIBLE);
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        //when loading new fragment, make current top stack fragment hide
        int top = fragmentManager.getBackStackEntryCount()-1;
        if(top >= 0){
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(top);
            Fragment currentFragment = fragmentManager.findFragmentByTag(backStackEntry.getName());
            currentFragment.getView().setVisibility(View.GONE);
        }

        fragmentTransaction.add(R.id.frameLayout, fragment, tag);

        if(tag.equals(CoursesScheduleTabFragment.FRAG_TAG)){
            fragmentTransaction.addToBackStack(tag);
        }else {
            fragmentTransaction.addToBackStack(tag);
        }

        fragmentTransaction.commit();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        String tag = "";
        switch(menuItem.getItemId()) {
            case R.id.nav_schedule:
                break;
            case R.id.nav_feedback:
                fragmentClass = FeedbackFragment.class;
                tag = FeedbackFragment.FRAG_TAG;
                break;
            case R.id.nav_ratemyprofessor:
//                fragmentClass = ThirdFragment.class;
                break;
            default:
                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        }

        try{
            fragment = (Fragment)fragmentClass.newInstance();
            loadFragment(fragment, tag);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // The action bar home/up action should open or close the drawer.
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
