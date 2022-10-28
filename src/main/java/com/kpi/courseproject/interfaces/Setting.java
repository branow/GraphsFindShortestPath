package com.kpi.courseproject.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

public class Setting implements Serializable {

    public Setting() {
        setDefaultValue();
    }

    public Locale locale;

    public void setDefaultValue () {
        locale = new Locale("en");
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("com/kpi/courseproject/Locale", locale);
    }

}
