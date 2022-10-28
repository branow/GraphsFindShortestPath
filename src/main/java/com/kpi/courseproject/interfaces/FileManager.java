package com.kpi.courseproject.interfaces;

import com.kpi.courseproject.Main;

import java.io.*;

public class FileManager {

    public static final String pathGraphs = Main.class.getResource("Graphs.bin").getPath();
    public static final String pathSetting = Main.class.getResource("Setting.bin").getPath();

    public static Object read(String path) {
        Object object = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            assert fis != null;
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static void write(Object object, String path) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert fos != null;
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
