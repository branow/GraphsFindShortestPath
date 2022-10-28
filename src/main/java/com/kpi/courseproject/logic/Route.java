package com.kpi.courseproject.logic;

import com.kpi.courseproject.collection.ArrayListPlus;
import com.kpi.courseproject.collection.ListPlus;
import com.kpi.courseproject.controllers.MainController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Route implements Serializable {

    private Vertical start, finish;
    private ListPlus<Vertical> verticals;
    private ListPlus<Edge> edges;
    private double weight;
    private double trap;
    private String type;

    public Route(ListPlus<Vertical> verticals, String type) {
        this.type = type;
        start = verticals.get(0);
        finish = verticals.get(verticals.size()-1);
        this.verticals = verticals;
        edges = calculateEdges();

        calculateWeight();
        calculateTrap();
    };

    private void calculateWeight () {
        int sum =0;
        for (Edge edge: edges) {
            sum+=edge.getWeight();
        }
        weight = sum;
    }

    private void calculateTrap () {
        if (edges.isEmpty()) {
            return;
        }
        double trap = edges.get(0).getTrap();
        for (Edge edge: edges) {
            if (trap<edge.getTrap()) {
                trap = edge.getTrap();
            }
        }
        this.trap = trap;
    }

    private ListPlus<Edge> calculateEdges () {
        ListPlus<Edge> edgeList = new ArrayListPlus<>();
        for (int i=1; i<verticals.size(); i++) {
            for (Edge e: verticals.get(i-1).getEdges()) {
                if (e.getTail().equals(verticals.get(i))) {
                    edgeList.add(e);
                }
            }
        }
        return edgeList;
    }

    public double getWeight() {
        return weight;
    }

    public Vertical getStart() {
        return start;
    }

    public void setStart(Vertical start) {
        this.start = start;
    }

    public Vertical getFinish() {
        return finish;
    }

    public void setFinish(Vertical finish) {
        this.finish = finish;
    }

    public ListPlus<Vertical> getVerticals() {
        return verticals;
    }

    public void setVerticals(ListPlus<Vertical> verticals) {
        this.verticals = verticals;
    }

    public ListPlus<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ListPlus<Edge> edges) {
        this.edges = edges;
    }

    public double getTrap() {
        return trap;
    }

    @Override
    public String toString() {
        return "("+ start.getName() + "->" + finish.getName() + ")" + " W: " + weight + " T: " + trap + " " + type;
    }

    @Override
    public Object clone()  {
        ListPlus<Vertical> verticals1 = new ArrayListPlus<>();
        for (Vertical v: verticals) {
            verticals1.add( (Vertical) v.clone());
        }
        return new Route(verticals1, type);
    }
}
