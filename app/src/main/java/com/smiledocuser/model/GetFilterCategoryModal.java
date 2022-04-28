package com.smiledocuser.model;

public class GetFilterCategoryModal {
    String lastname,name,category_name,fees,idd,imageDr,subcategory,doctorName,set_filter;


    public String getDrId() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }

    public String getDrname() {
        return doctorName;
    }

    public void setDrname1(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSet_filter() {
        return set_filter;
    }

    public void setSet_filter(String set_filter) {
        this.set_filter = set_filter;
    }
}
