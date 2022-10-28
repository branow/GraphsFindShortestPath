package com.kpi.courseproject;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class SimpleStringPropertyPlus extends SimpleStringProperty implements Serializable {

    public SimpleStringPropertyPlus() {
        super();
    }

    public SimpleStringPropertyPlus(String var1) {
        super(var1);
    }

    public SimpleStringPropertyPlus(Object var1, String var2) {
        super(var1, var2);
    }

    public SimpleStringPropertyPlus(Object var1, String var2, String var3) {
        super(var1,var2,var3);
    }



}
