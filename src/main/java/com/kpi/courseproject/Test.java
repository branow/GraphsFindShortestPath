package com.kpi.courseproject;

import com.kpi.courseproject.collection.BinaryTreeMapPlus;
import com.kpi.courseproject.collection.HashMapPlus;
import com.kpi.courseproject.collection.LinkedListPlus;
import com.kpi.courseproject.collection.MapPlus;
import com.kpi.courseproject.interfaces.FileManager;
import com.kpi.courseproject.interfaces.Imprint;
import com.kpi.courseproject.logic.Edge;
import com.kpi.courseproject.logic.Graph;

import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        LinkedListPlus<Integer> lis = new LinkedListPlus<>();
        lis.add(2);
        lis.add(1);
        lis.add(6);
        lis.add(4);
        lis.add(0);
        System.out.println(lis);
        System.out.println(lis.get(0));
        System.out.println(lis.get(1));
        System.out.println(lis.get(2));
        System.out.println(lis.get(3));


    }

    private static void rand(int [][] ar) {
        for (int i=0; i< ar.length; i++) {
            for (int j=i+1; j<ar[i].length; j++) {
                int value = (int)(Math.random() * 10);
                double v = Math.pow(ar.length, 2) * 79/54600 + ar.length * (-4283.)/54600 + (419./364);
                if ( Math.random() > v ) {
                    ar[i][j]=-1;
                    ar[j][i]=-1;
                } else {
                    ar[i][j] = value;
                    ar[j][i] = value;
                }
            }
        }
    }

}
