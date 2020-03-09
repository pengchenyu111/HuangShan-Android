package com.example.huangshan.admin.bean.table;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "帐篷收费表")
public class CampPrice {

    @SmartColumn(id = 0, name = "帐篷类型", autoMerge = true)
    private String type;
    @SmartColumn(id = 1, name = "时间区间")
    private String time;
    @SmartColumn(id = 2, name = "收费标准")
    private String price;

    public CampPrice() {
    }

    public CampPrice(String type, String time, String price) {
        this.type = type;
        this.time = time;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
