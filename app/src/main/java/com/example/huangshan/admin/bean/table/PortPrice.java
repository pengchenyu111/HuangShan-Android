package com.example.huangshan.admin.bean.table;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "停车收费")
public class PortPrice {

    @SmartColumn(id = 0, name = "时间", autoMerge = true)
    private String time;
    @SmartColumn(id = 1, name = "车辆类型")
    private String type;
    @SmartColumn(id = 2, name = "收费标准")
    private String price;

    public PortPrice(String time, String type, String price) {
        this.time = time;
        this.type = type;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
