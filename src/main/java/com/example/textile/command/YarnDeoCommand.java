package com.example.textile.command;

import com.example.textile.entity.Company;
import com.example.textile.entity.YarnDeo;

import java.util.List;

public class YarnDeoCommand {

    private Company company;
    private List<YarnDeo> yarnDeoList;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<YarnDeo> getYarnDeoList() {
        return yarnDeoList;
    }

    public void setYarnDeoList(List<YarnDeo> yarnDeoList) {
        this.yarnDeoList = yarnDeoList;
    }
}
