package com.kpi.courseproject.logic;

import com.kpi.courseproject.collection.ArrayListPlus;
import com.kpi.courseproject.collection.ListPlus;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;


public class Vertical implements Serializable {

    private String name;
    private ListPlus<Edge> edges;
    private Vertical parent;

    public Vertical(String name) {
        this.name = name;
        edges = new ArrayListPlus<>();
    }

    public Vertical(String name, ListPlus<Edge> edges) {
        this.name = name;
        this.edges = edges;
    }

    public void addEdge (Vertical vertical) {
        edges.add(new Edge(this, vertical));
    }

    public void addEdge (Vertical vertical, int weight) {
        edges.add(new Edge(this, vertical, weight));
    }

    public void removeEdge (Edge edge) {
        edges.remove(edge);
    }

    public boolean haveEdgeWith (Vertical vertical) {
        Edge e = new Edge(this, vertical);
        Iterator<Edge> it = edges.iterator();
        while (it.hasNext()) {
            Edge edge = it.next();
            if (edge.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public Edge getEdgeWith (Vertical vertical) {
        Edge e = new Edge(this, vertical);
        Iterator<Edge> it = edges.iterator();
        while (it.hasNext()) {
            Edge edge = it.next();
            if (edge.equals(e)) {
                return edge;
            }
        }
        throw new NullPointerException();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ListPlus<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ListPlus<Edge> edges) {
        this.edges = edges;
    }

    public Vertical getParent() {
        return parent;
    }

    public void setParent(Vertical parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "(" + name + ")";
    }

    @Override
    protected Object clone() {
        return new Vertical(name, edges);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (hashCode() != o.hashCode()) return false;
        Vertical vertical = (Vertical) o;
        return Objects.equals(name, vertical.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
