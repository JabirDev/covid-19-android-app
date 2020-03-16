package com.jabirdeveloper.covid_19.model;

public class CountriesCase {
    private String country;
    private int cases, todayCases, deaths, todayDeaths, recovered, active, critical;

    public CountriesCase(String country, int cases, int todayCases, int deaths, int todayDeaths, int recovered, int active, int critical) {
        this.country = country;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.active = active;
        this.critical = critical;
    }

    public String getCountry() {
        return country;
    }

    public int getCases() {
        return cases;
    }

    public int getTodayCases() {
        return todayCases;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getTodayDeaths() {
        return todayDeaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getActive() {
        return active;
    }

    public int getCritical() {
        return critical;
    }
}
