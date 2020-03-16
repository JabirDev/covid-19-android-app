package com.jabirdeveloper.covid_19.model;

public class GlobalCase {
    private int cases, deaths, recovered;

    public GlobalCase(int cases, int deaths, int recovered) {
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public int getCases() {
        return cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getRecovered() {
        return recovered;
    }
}
