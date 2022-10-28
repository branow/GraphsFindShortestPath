package com.kpi.courseproject.interfaces;

import com.kpi.courseproject.collection.ListPlus;
import com.kpi.courseproject.collection.ArrayListPlus;
import com.kpi.courseproject.collection.MapPlus;
import com.kpi.courseproject.collection.BinaryTreeMapPlus;
import com.kpi.courseproject.logic.Edge;
import com.kpi.courseproject.logic.Graph;
import com.kpi.courseproject.logic.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Imprint {

    public static ListPlus<ImprintLabyrinth> doImprint(ListPlus<Graph> list) {
        ListPlus<ImprintLabyrinth> imprints = new ArrayListPlus<>(list.size());
        for (Graph l: list) {
            imprints.add(getImprintGraph(l));
        }
        return imprints;
    }

    public static ObservableList<Graph> readImprint(Object object) {
        if (object==null) {
            return FXCollections.observableArrayList();
        }
        ListPlus<ImprintLabyrinth> imprints = (ListPlus<ImprintLabyrinth>) object;
        List<Graph> list = new ArrayList<>(imprints.size());
        for (ImprintLabyrinth imprint: imprints) {
            list.add(getGraph(imprint));
        }
        return FXCollections.observableArrayList(list);
    }

    private static Graph getGraph(ImprintLabyrinth imprint) {
        Graph graph = new Graph();
        graph.setName(imprint.name);
        graph.setAdjacencyMatrix(imprint.matrix);
        graph.addRoutes((getRoutes(imprint.routes)));
        setTraps(graph.getEdges(), imprint.traps);
        return graph;
    }

    private static void setTraps(ListPlus<Edge> edges, MapPlus<Edge, Double> traps) {
        for (Edge e: edges) {
            if (traps.containsKey(e)) {
                e.setTrap(traps.get(e));
            }
        }
    }

    private static ListPlus<Route> getRoutes(ListPlus<Route> routes) {
        return routes;
    }

    private static ImprintLabyrinth getImprintGraph(Graph graph) {
        return new ImprintLabyrinth (graph.getName(), graph.getAdjacencyMatrix(),
                getImprintRoutes(graph.getRoutes()), getImprintTraps(graph.getEdges()));
    }

    private static MapPlus<Edge, Double> getImprintTraps(ListPlus<Edge> edges) {
        MapPlus<Edge, Double> traps = new BinaryTreeMapPlus<>();
        for (Edge e: edges) {
            if (e.getTrap()>0) {
                traps.put(e, e.getTrap());
            }
        }
        return traps;
    }

    private static ListPlus<Route> getImprintRoutes(ObservableList<Route> routes) {
        ListPlus<Route> routesImprint = new ArrayListPlus<>(routes.size());
        for (Route r: routes) {
            routesImprint.add(r);
        }
        return routesImprint;
    }

    static class ImprintLabyrinth implements Serializable {
        String name;
        int[][] matrix;
        ListPlus<Route> routes;
        MapPlus<Edge, Double> traps;

        public ImprintLabyrinth(String name, int[][] matrix, ListPlus<Route> routes, MapPlus<Edge, Double> traps) {
            this.name = name;
            this.matrix = matrix;
            this.routes = routes;
            this.traps = traps;
        }

        @Override
        public String toString() {
            return "ImprintLabyrinth{" +
                    "name='" + name + '\'' +
                    ", matrix=" + Arrays.toString(matrix) +
                    ", routes=" + routes +
                    ", traps=" + traps +
                    '}';
        }
    }

}

