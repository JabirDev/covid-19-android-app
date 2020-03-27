package com.jabirdeveloper.covid_19.fragment;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jabirdeveloper.covid_19.R;
import com.jabirdeveloper.covid_19.model.GlobalCase;
import com.jabirdeveloper.covid_19.network.Koneksi;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalFragment extends Fragment {

    private static final String TAG = GlobalFragment.class.getSimpleName();
    private TextView tvCases, tvDeaths, tvRecovered, tvDeathsPercentage, tvRecoveredPercentage;
    private SharedPreferences dataGlobal;
    private ValueAnimator animCases = new ValueAnimator();
    private ValueAnimator animDetahs = new ValueAnimator();
    private ValueAnimator animRecovered = new ValueAnimator();
    private SwipeRefreshLayout refreshLayout;

    public GlobalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_global, container, false);
        // Inflate the layout for this fragment
        init(view);
        return view;
    }

    private void init(View view) {
        dataGlobal = getContext().getSharedPreferences("global", Context.MODE_PRIVATE);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        tvCases = view.findViewById(R.id.tv_cases);
        tvDeaths = view.findViewById(R.id.tv_deaths);
        tvRecovered = view.findViewById(R.id.tv_recovered);
        tvDeathsPercentage = view.findViewById(R.id.tv_deaths_percentage);
        tvRecoveredPercentage = view.findViewById(R.id.tv_recovered_percentage);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        getData();

    }

    private void getData() {
        final Call<GlobalCase> data = Koneksi.getCovidService().getGlobal(Koneksi.URL_GLOBAL);
        data.enqueue(new Callback<GlobalCase>() {
            @Override
            public void onResponse(Call<GlobalCase> call, Response<GlobalCase> response) {
                refreshLayout.setRefreshing(false);
                GlobalCase gs = response.body();
                if (gs != null) {
                    SharedPreferences.Editor editor = dataGlobal.edit();
                    int cases = gs.getCases();
                    int deaths = gs.getDeaths();
                    int recovered = gs.getRecovered();
                    editor.putInt("cases", cases);
                    editor.putInt("deaths", deaths);
                    editor.putInt("recovered", recovered);
                    editor.apply();

                    float deathPercent = (deaths * 100.0f) / cases;
                    float recoveredPercent = (recovered * 100.0f) / cases;
                    String deathP = round(deathPercent, 1) + "%";
                    String recoveredP = round(recoveredPercent, 1)+ "%";

                    tvDeathsPercentage.setText(deathP);
                    tvRecoveredPercentage.setText(recoveredP);

                    animCases.setObjectValues(0, cases);
                    animCases.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tvCases.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(animation.getAnimatedValue()));
                        }
                    });
                    animDetahs.setObjectValues(0, deaths);
                    animDetahs.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tvDeaths.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(animation.getAnimatedValue()));
                        }
                    });
                    animRecovered.setObjectValues(0, recovered);
                    animRecovered.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tvRecovered.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(animation.getAnimatedValue()));
                        }
                    });

                    animCases.setDuration(300);
                    animDetahs.setDuration(400);
                    animRecovered.setDuration(500);
                    animCases.start();
                    animDetahs.start();
                    animRecovered.start();

                }
            }

            @Override
            public void onFailure(Call<GlobalCase> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(getContext(), "Failed to update data", Toast.LENGTH_SHORT).show();

                int cases = dataGlobal.getInt("cases",0);
                int deaths = dataGlobal.getInt("deaths",0);
                int recovered = dataGlobal.getInt("recovered",0);

                float deathPercent = (deaths * 100.0f) / cases;
                float recoveredPercent = (recovered * 100.0f) / cases;
                String deathP = round(deathPercent, 1) + "%";
                String recoveredP = round(recoveredPercent, 1)+ "%";

                tvDeathsPercentage.setText(deathP);
                tvRecoveredPercentage.setText(recoveredP);

                animCases.setObjectValues(0, cases);
                animCases.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tvCases.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(animation.getAnimatedValue()));
                    }
                });
                animDetahs.setObjectValues(0, deaths);
                animDetahs.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tvDeaths.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(animation.getAnimatedValue()));
                    }
                });
                animRecovered.setObjectValues(0, recovered);
                animRecovered.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tvRecovered.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(animation.getAnimatedValue()));
                    }
                });

                animCases.setDuration(1000);
                animDetahs.setDuration(1400);
                animRecovered.setDuration(1500);
                animCases.start();
                animDetahs.start();
                animRecovered.start();
            }
        });
    }

    private String round(double value, int digit){
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(digit, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.toString();
    }

}
