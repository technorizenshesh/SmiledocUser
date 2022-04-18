package com.smiledocuser.model;

public class GetFilterCategoryModal {
    String lastname,name,category_name,fees,idd,imageDr,subcategory,doctorName;

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
}
