package com.jabirdeveloper.covid_19.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.jabirdeveloper.covid_19.R;
import com.jabirdeveloper.covid_19.activiies.MainActivity;
import com.jabirdeveloper.covid_19.adapter.CountryAdapter;
import com.jabirdeveloper.covid_19.model.CountriesCase;
import com.jabirdeveloper.covid_19.network.Koneksi;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountriesFragment extends Fragment {

    private static final String TAG = CountriesFragment.class.getSimpleName();
    private List<CountriesCase> items = new ArrayList<>();
    private CountryAdapter adapater;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rv;

    public CountriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        rv = view.findViewById(R.id.recycler_view);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                setupRecyclerView();
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
                if (cc != null) {
                    items.addAll(cc);
                    setupRecyclerView();
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

    private void setupRecyclerView() {
        adapater = new CountryAdapter(getContext(), items);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adapater);
        rv.setLayoutManager(manager);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapater.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
