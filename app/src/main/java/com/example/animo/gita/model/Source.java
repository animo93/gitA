package com.example.animo.gita.model;

/**
 * Created by animo on 27/5/17.
 */

public class Source {

    private String full_name;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    private Owner owner;
}
