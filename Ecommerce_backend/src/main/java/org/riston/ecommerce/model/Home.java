package org.riston.ecommerce.model;

import lombok.Data;

import java.util.List;
@Data
@SuppressWarnings("unused")
public class Home {
    private List<HomeCategory> grid;

    private List<HomeCategory> shopByCategories;

    private List<HomeCategory> electricCategories;

    private  List<Deal> deals;
}
