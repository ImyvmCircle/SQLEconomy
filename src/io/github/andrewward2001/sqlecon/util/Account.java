package io.github.andrewward2001.sqlecon.util;

import java.util.UUID;

public class Account {

    public String name;
    public UUID uid;
    public double bal;

    public Account(String name, UUID uid, double bal) {
        this.name = name;
        this.uid = uid;
        this.bal = bal;
    }
}
