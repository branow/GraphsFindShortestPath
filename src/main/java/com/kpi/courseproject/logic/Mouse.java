package com.kpi.courseproject.logic;

public class Mouse {

    private final static double DefaultSpeed = 10;

    private double speed = DefaultSpeed;
    private Route route;
    private Edge last;
    private boolean death;

    public Mouse() {}

    public Mouse(int speed) {
        this.speed = speed;
    }

    public boolean go (Route route) {
        this.route = route;
        for (Edge edge: route.getEdges()) {
            double trap = ((edge.getWeight()/speed) * edge.getTrap() + edge.getTrap())/2 ;
            double rand = Math.random();
            if (trap>=1 || rand < trap) {
                death = true;
                last = edge;
                return false;
            }
        }
        death = false;
        return true;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Edge getLast() {
        return last;
    }

    public boolean isDeath() {
        return death;
    }
}
