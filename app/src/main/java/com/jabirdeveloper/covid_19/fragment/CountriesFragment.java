package com.jabirdeveloper.covid_19.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jabirdeveloper.covid_19.R;
import com.jabirdeveloper.covid_19.adapter.CountryAdapater;
import com.jabirdeveloper.covid_19.model.CountriesCase;
import com.jabirdeveloper.covid_19.network.Koneksi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountriesFragment extends Fragment {

    private static final String TAG = CountriesFragment.class.getSimpleName();
    private List<CountriesCase> items = new ArrayList<>();
    private CountryAdapater adapater;
    private LinearLayoutManager manager;
    private SwipeRefreshLayout refreshLayout;

    public CountriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countries, container, false);
        // Inflate the layout for this fragment
        init(view);
        return view;
    }

    private void init(View view) {
        refreshLayout = view.findViewById(R.id.refresh_layout);
        RecyclerView rv = view.findViewById(R.id.recycler_view);

        refreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        adapater = new CountryAdapater(getContext(), items);
        manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adapater);
        rv.setLayoutManager(manager);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                adapater.notifyDataSetChanged();
                getData();
            }
        });

        getData();
    }

    private void getData() {
        refreshLayout.setRefreshing(true);
        Call<List<CountriesCase>> data = Koneksi.getCovidService().getCountries(Koneksi.URL_COUNTRIES);
        data.enqueue(new Callback<List<CountriesCase>>() {
            @Override
            public void onResponse(Call<List<CountriesCase>> call, Response<List<CountriesCase>> response) {
                List<CountriesCase> cc = response.body();
                if (cc != null){
                    items.addAll(cc);
                    adapater.notifyDataSetChanged();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<CountriesCase>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(getContext(), "Failed to update data", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

}
