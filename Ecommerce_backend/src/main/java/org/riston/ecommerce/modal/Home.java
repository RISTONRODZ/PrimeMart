package org.riston.ecommerce.modal;

import lombok.Data;

import java.util.List;
@Data
public class Home {
    private List<HomeCategory> grid;

    private List<HomeCategory> shopByCategories;

    private List<HomeCategory> electricCategories;

    private  List<Deal> deals;
}
