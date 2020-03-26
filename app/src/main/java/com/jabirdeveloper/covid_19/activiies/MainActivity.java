package com.jabirdeveloper.covid_19.activiies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.jabirdeveloper.covid_19.R;
import com.jabirdeveloper.covid_19.adapter.TabAdapter;
import com.jabirdeveloper.covid_19.fragment.AboutFragment;
import com.jabirdeveloper.covid_19.fragment.CountriesFragment;
import com.jabirdeveloper.covid_19.fragment.GlobalFragment;

public class MainActivity extends AppCompatActivity {

    private GlobalFragment globalFragment = new GlobalFragment();
    private CountriesFragment countriesFragment = new CountriesFragment();
    private AboutFragment aboutFragment = new AboutFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        TabLayout tabLayout = findViewById(R.id.main_tablayout);
        ViewPager viewPager = findViewById(R.id.main_viewpager);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabAdapter.tambahFragment(globalFragment, "Global");
        tabAdapter.tambahFragment(countriesFragment, "Countries");
        tabAdapter.tambahFragment(aboutFragment, "About");
        tabAdapter.notifyDataSetChanged();

    }

}
