package com.kpi.courseproject.controllers.support;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class WrapTableTraps {

    private SimpleStringProperty name;
    private SimpleStringProperty trap;

    public WrapTableTraps(SimpleStringProperty name, SimpleStringProperty trap) {
        this.name = name;
        this.trap = trap;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getTrap() {
        return trap.get();
    }

    public SimpleStringProperty trapProperty() {
        return trap;
    }

    public void setTrap(String trap) {
        this.trap.set(trap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrapTableTraps traps = (WrapTableTraps) o;
        return Objects.equals(name, traps.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "WrapTableTraps{" +
                "name=" + name +
                ", trap=" + trap +
                '}';
    }
}
