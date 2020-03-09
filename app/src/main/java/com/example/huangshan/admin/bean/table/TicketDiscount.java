package com.example.huangshan.admin.bean.table;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "优待政策")
public class TicketDiscount {

    @SmartColumn(id = 0, name = "人群",autoMerge = true)
    private String peopleType;
    @SmartColumn(id = 1, name = "适用条件")
    private String explanation;
    @SmartColumn(id = 2, name = "折扣")
    private String discount;

    public TicketDiscount() {
    }

    public TicketDiscount(String peopleType, String explanation, String discount) {
        this.peopleType = peopleType;
        this.explanation = explanation;
        this.discount = discount;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
