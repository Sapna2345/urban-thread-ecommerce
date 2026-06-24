package com.Sapna.Ecomm.model;

import java.util.Map;

public class OrderRequest {
    //key-product id
    //value -quanity
    private Map<Long,Integer> productQuantities;;

    public Map<Long, Integer> getProductQuantities() {

        return productQuantities;
    }

    public void setProductQuantities(Map<Long, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }

}
