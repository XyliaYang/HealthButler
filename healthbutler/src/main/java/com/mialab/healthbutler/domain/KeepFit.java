package com.mialab.healthbutler.domain;

/**
 * Created by hp on 2016/7/22.
 */
public class KeepFit {

    private int ic_item;
    private String item;
    private String  item_detail;

    public KeepFit(int ic_item, String item, String item_detail) {
        this.ic_item = ic_item;
        this.item = item;
        this.item_detail = item_detail;
    }

    public int getIc_item() {
        return ic_item;
    }

    public void setIc_item(int ic_item) {
        this.ic_item = ic_item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem_detail() {
        return item_detail;
    }

    public void setItem_detail(String item_detail) {
        this.item_detail = item_detail;
    }
}
