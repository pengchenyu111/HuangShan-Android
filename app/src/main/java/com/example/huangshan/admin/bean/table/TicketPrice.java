package com.example.huangshan.admin.bean.table;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "票价")
public class TicketPrice {
    @SmartColumn(id = 0, name = "类型",autoMerge = true)
    private String peopleType;
    @SmartColumn(id = 1, name = "条件")
    private String explanation;
    @SmartColumn(id = 2, name = "价格")
    private String price;

    public TicketPrice(String peopleType, String explanation, String price) {
        this.peopleType = peopleType;
        this.explanation = explanation;
        this.price = price;
    }

    public String getPeopleType() {
        return peopleType;
    }

    public void setPeopleType(String peopleType) {
        this.peopleType = peopleType;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
