package com.sampleassignment.developertext.Model;

public class ResultModel {
    private String query;

    private String status;

    private String country;

    private String countryCode;

    private String region;

    private String regionName;

    private String city;

    private String zip;

    private double lat;

    private double lon;

    private String timezone;

    private String isp;

    private String org;

    private String as;

    public void setQuery(String query){
        this.query = query;
    }
    public String getQuery(){
        return this.query;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return this.country;
    }
    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }
    public String getCountryCode(){
        return this.countryCode;
    }
    public void setRegion(String region){
        this.region = region;
    }
    public String getRegion(){
        return this.region;
    }
    public void setRegionName(String regionName){
        this.regionName = regionName;
    }
    public String getRegionName(){
        return this.regionName;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setZip(String zip){
        this.zip = zip;
    }
    public String getZip(){
        return this.zip;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public double getLat(){
        return this.lat;
    }
    public void setLon(double lon){
        this.lon = lon;
    }
    public double getLon(){
        return this.lon;
    }
    public void setTimezone(String timezone){
        this.timezone = timezone;
    }
    public String getTimezone(){
        return this.timezone;
    }
    public void setIsp(String isp){
        this.isp = isp;
    }
    public String getIsp(){
        return this.isp;
    }
    public void setOrg(String org){
        this.org = org;
    }
    public String getOrg(){
        return this.org;
    }
    public void setAs(String as){
        this.as = as;
    }
    public String getAs(){
        return this.as;
    }
}
