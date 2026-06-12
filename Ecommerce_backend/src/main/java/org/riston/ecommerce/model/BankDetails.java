package org.riston.ecommerce.model;

import lombok.Data;

@Data
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    //used to identify banks and their branches
    private String ifscCode;
}
