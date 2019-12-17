package com.sunmi.sunmistatisticslib.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SMStaticsBean implements Serializable {

    private List<SMStaticsInfo> data = new ArrayList<>();

    public List<SMStaticsInfo> getData() {
        return data;
    }

    public void setData(List<SMStaticsInfo> data) {
        this.data = data;
    }
}
