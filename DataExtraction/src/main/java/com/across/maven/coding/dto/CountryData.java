

package com.across.maven.coding.dto;

import com.across.maven.coding.utililty.StringUtil;

public class CountryData extends UserAgentData {
    private String countryId;
    private String country;
    private String field1 = StringUtil.EMPTY_STRING;
    private String field2 = StringUtil.EMPTY_STRING;
    private String field3 = StringUtil.EMPTY_STRING;

    public CountryData() {
    }

    public CountryData(String countryId, String country, String userAgent, String field1, String field2, String field3) {
        super(userAgent);
        this.countryId = countryId;
        this.country = country;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = new StringBuffer(StringUtil.QUOTE).append(countryId).append(StringUtil.QUOTE).toString();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    @Override
    public String toString() {
        return " " + countryId + "\t" + country + "\t" + super.getUserAgent() + "\t" + field1 + "\t" + field2 + "\t" + field3 + "\n";
    }



}
