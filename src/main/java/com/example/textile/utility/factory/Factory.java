package com.example.textile.utility.factory;

import com.example.textile.entity.*;

import java.util.ArrayList;
import java.util.List;

public interface Factory {
    Country country = new Country();
    State state = new State();
    Address address = new Address();
    Company company = new Company();
    List<BankDetail> bankDetails = new ArrayList<>();
}
