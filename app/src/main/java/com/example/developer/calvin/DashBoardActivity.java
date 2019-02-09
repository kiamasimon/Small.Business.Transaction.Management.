package com.example.developer.calvin;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.developer.calvin.Fragments.AddSale;
import com.example.developer.calvin.Fragments.CartFragment;
import com.example.developer.calvin.Fragments.HomeFragment;
import com.example.developer.calvin.Fragments.ProductFragment;
import com.example.developer.calvin.Fragments.ProfitFragment;
import com.example.developer.calvin.Fragments.PurchaseFragment;


public class DashBoardActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String TAG = "HomeActivity";
    private TextView fullName, email;
    private Menu optionsMenu;
    private Class currentFragment;
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Log.e(TAG, " home activity started");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);
        final NavigationView navView = (NavigationView) findViewById(R.id.navView);
        View headerView = navView.getHeaderView(0);

        fullName = (TextView) headerView.findViewById(R.id.tvFullName);
        email = (TextView) headerView.findViewById(R.id.tvEmail);

        fullName.setText(sharedPreferences.getString("user_name",""));
        email.setText(sharedPreferences.getString("email",""));
        navView.setCheckedItem(R.id.navHome);
        showFragment(HomeFragment.class);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Class fragment = null;

                if (id == R.id.navHome) {
                    fragment = HomeFragment.class;
                    showFragment(fragment);
                }else if (id == R.id.navProduct){
                    fragment = ProductFragment.class;
                    showFragment(fragment);
                }else if (id == R.id.navPurchase){
                    fragment = PurchaseFragment.class;
                    showFragment(fragment);
                }else if (id == R.id.navLogout){
                    Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPref",MODE_PRIVATE);
                    sharedPreferences.edit().clear();
                    startActivity(intent);
                }else if (id == R.id.navMyAccount){
                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(intent);
                }else if (id == R.id.navInventory){
                    fragment = AddSale.class;
                    showFragment(fragment);
                }else if (id == R.id.navCart){
                    fragment = CartFragment.class;
                    showFragment(fragment);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void showFragment(Class fragmentClass) {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        currentFragment = fragmentClass;
        hideSearch();
    }

    private void hideSearch() {
        if (currentFragment.getName().equals(HomeFragment.class.getName()) || currentFragment.getName().equals(HomeFragment.class.getName())) {
            if (optionsMenu != null) {
                optionsMenu.findItem(R.id.search).setVisible(true);
            }
        } else {
            if (optionsMenu != null) {
                optionsMenu.findItem(R.id.search).setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        optionsMenu = menu;
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(DashBoardActivity.this, SearchResultsActivity.class);
                intent.putExtra("QUERY", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

}

