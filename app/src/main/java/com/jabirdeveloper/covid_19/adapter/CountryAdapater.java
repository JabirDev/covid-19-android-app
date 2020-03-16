package com.jabirdeveloper.covid_19.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jabirdeveloper.covid_19.R;
import com.jabirdeveloper.covid_19.model.CountriesCase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryAdapater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CountriesCase> items;

    public CountryAdapater(Context context, List<CountriesCase> items) {
        this.context = context;
        this.items = items;
    }

    class CountryHolder extends RecyclerView.ViewHolder {
        TextView tvCountry, tvCases, tvDeaths, tvRecovered;

        CountryHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tv_country);
            tvCases = itemView.findViewById(R.id.tv_cases);
            tvDeaths = itemView.findViewById(R.id.tv_deaths);
            tvRecovered = itemView.findViewById(R.id.tv_recovered);
        }

        void setData(final CountriesCase data) {
            tvCountry.setText(data.getCountry());
            String cases = "Cases: " +
                    NumberFormat.getNumberInstance(Locale.getDefault()).format(data.getCases()) +
                    " | Today: " +
                    NumberFormat.getNumberInstance(Locale.getDefault()).format(data.getTodayCases()) +
                    " | Active: " +
                    NumberFormat.getNumberInstance(Locale.getDefault()).format(data.getActive());
            String deaths = "Deaths: " +
                    NumberFormat.getNumberInstance(Locale.getDefault()).format(data.getDeaths()) +
                    " | Today: " +
                    NumberFormat.getNumberInstance(Locale.getDefault()).format(data.getTodayDeaths());
            String recovered = "Recovered: " +
                    NumberFormat.getNumberInstance(Locale.getDefault()).format(data.getRecovered()) +
                    " | Critical: " +
                    NumberFormat.getNumberInstance(Locale.getDefault()).format(data.getCritical());
            tvCases.setText(cases);
            tvDeaths.setText(deaths);
            tvRecovered.setText(recovered);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_countries, parent, false);
        return new CountryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CountryHolder ch = (CountryHolder) holder;
        ch.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
