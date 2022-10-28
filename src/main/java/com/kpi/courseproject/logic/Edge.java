package com.kpi.courseproject.logic;

import java.io.Serializable;
import java.util.Objects;

public class Edge implements Serializable, Comparable<Edge> {
    private String name;
    private Vertical head;
    private Vertical tail;
    private int weight;
    private double trap;

    public Edge(Vertical head, Vertical tail) {
        this.head = head;
        this.tail = tail;
        name = getDefaultName();
        trap = 0;
    }

    public Edge(Vertical head, Vertical tail, int weight) {
        this.head = head;
        this.tail = tail;
        this.weight = weight;
        name = getDefaultName();
        trap = 0;
    }

    public Edge(String name, Vertical head, Vertical tail, int weight) {
        this.head = head;
        this.tail = tail;
        this.weight = weight;
        this.name = name;
        trap = 0;
    }

    public Edge(String name, Vertical head, Vertical tail, int weight, double trap) {
        this.head = head;
        this.tail = tail;
        this.weight = weight;
        this.trap = trap;
        this.name = name;
    }

    private String getDefaultName () {
         return head.getName() + " -> " + tail.getName();
    }

    public Vertical getHead() {
        return head;
    }

    public void setHead(Vertical head) {
        this.head = head;
    }

    public Vertical getTail() {
        return tail;
    }

    public void setTail(Vertical tail) {
        this.tail = tail;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTrap() {
        return trap;
    }

    public void setTrap(double trap) {
        this.trap = trap;
    }

    @Override
    public Object clone() {
        return new Edge(name, head, tail, weight, trap);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "name='" + name+
                ", price=" + weight +
                ", trap=" + trap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (hashCode() != o.hashCode()) return false;
        Edge edge = (Edge) o;
        if (Objects.equals(head, edge.head) && Objects.equals(tail, edge.tail)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail);
    }

    @Override
    public int compareTo(Edge o) {
        return getDefaultName().compareTo(o.getDefaultName());
    }
}
