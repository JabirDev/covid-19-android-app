package com.jabirdeveloper.covid_19.network;

import com.jabirdeveloper.covid_19.model.CountriesCase;
import com.jabirdeveloper.covid_19.model.GlobalCase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class Koneksi {

    public static final String BASE_URL = "https://coronavirus-19-api.herokuapp.com/";
    public static final String URL_GLOBAL = BASE_URL + "all";
    public static final String URL_COUNTRIES = BASE_URL + "countries";

    public interface CovidService {
        @GET
        Call<GlobalCase> getGlobal(@Url String url);
        @GET
        Call<List<CountriesCase>> getCountries(@Url String url);
    }

    private static CovidService covidService = null;

    public static CovidService getCovidService(){
        if (covidService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            covidService = retrofit.create(CovidService.class);
        }
        return covidService;
    }

}
