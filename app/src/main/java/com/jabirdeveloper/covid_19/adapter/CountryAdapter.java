package com.jabirdeveloper.covid_19.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jabirdeveloper.covid_19.R;
import com.jabirdeveloper.covid_19.model.CountriesCase;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private List<CountriesCase> items;
    private List<CountriesCase> itemsFull;

    public CountryAdapter(Context context, List<CountriesCase> items) {
        this.context = context;
        this.items = items;
        itemsFull = new ArrayList<>(items);
    }

    class CountryHolder extends RecyclerView.ViewHolder {
        TextView tvCountry, tvCases, tvDeaths, tvRecovered, tvDeathsPercentage, tvRecoveredPercentage;

        CountryHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tv_country);
            tvCases = itemView.findViewById(R.id.tv_cases);
            tvDeaths = itemView.findViewById(R.id.tv_deaths);
            tvRecovered = itemView.findViewById(R.id.tv_recovered);
            tvDeathsPercentage = itemView.findViewById(R.id.tv_deaths_percentage);
            tvRecoveredPercentage = itemView.findViewById(R.id.tv_recovered_percentage);
        }

        void setData(final CountriesCase data) {
            tvCountry.setText(data.getCountry());
            float deathPercent = (data.getDeaths() * 100.0f) / data.getCases();
            float recoveredPercent = (data.getRecovered() * 100.0f) / data.getCases();
            String deathP = round(deathPercent, 1) + "%";
            String recoveredP = round(recoveredPercent, 1)+ "%";
            tvDeathsPercentage.setText(deathP);
            tvRecoveredPercentage.setText(recoveredP);
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

        String round(double value, int digit){
            BigDecimal bigDecimal = new BigDecimal(value);
            bigDecimal = bigDecimal.setScale(digit, BigDecimal.ROUND_HALF_UP);
            return bigDecimal.toString();
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

    @Override
    public Filter getFilter() {
        return countriesFilter;
    }

    private Filter countriesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CountriesCase> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(itemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CountriesCase country : itemsFull) {
                    if (country.getCountry().toLowerCase().contains(filterPattern)){
                        filteredList.add(country);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
