package com.example.huangshan.admin.bean.table;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "换乘信息")
public class ChangeCarPrice {

    @SmartColumn(id = 0, name = "路线")
    private String route;
    @SmartColumn(id = 1, name = "价格")
    private String price;

    public ChangeCarPrice(String route, String price) {
        this.route = route;
        this.price = price;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
