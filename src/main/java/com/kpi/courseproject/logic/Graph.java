package com.kpi.courseproject.logic;

import com.kpi.courseproject.collection.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.*;

public class Graph implements Serializable {

    public static final int DFSearch = 0, BFSearch = 1, DijkstraSearch = 2;
    public static final int ShowAll = 0, ShowShortest=1, ShowSafest=2, ShowOptimal=3;
    public static final int waitTimeMilliseconds = 2000;

    private String name;
    private ObservableList<Route> routes;
    private ListPlus<Vertical> verticals;
    private ListPlus<Edge> edges;

    public Graph() {
        verticals = new ArrayListPlus<>();
        edges = new ArrayListPlus<>();
        routes = FXCollections.observableArrayList();
        name = getDefaultName();
    }

    public Graph(int [][] adjacencyMatrix) {
        verticals = getVerticals(adjacencyMatrix);
        edges = calculateEdges();
        routes = FXCollections.observableArrayList();
        name = getDefaultName();
    }

    public void addVertical(Vertical vertical) {
        verticals.add(vertical);
    }

    public int[][] getAdjacencyMatrix() {
        int[][] matrix = new int[verticals.size()][verticals.size()];
        for (int i=0; i<matrix.length; i++) {
            for (int j=0; j<matrix[i].length; j++) {
                if (verticals.get(i).haveEdgeWith(verticals.get(j))) {
                    matrix[i][j] = verticals.get(i).getEdgeWith(verticals.get(j)).getWeight();
                } else {
                    matrix[i][j] = -1;
                }
            }
        }
        return matrix;
    }

    public void setAdjacencyMatrix(int [][] adjacencyMatrix) {
        verticals = getVerticals(adjacencyMatrix);
        edges = calculateEdges();
        routes.clear();
    }

    private ListPlus<Edge> calculateEdges() {
        ListPlus<Edge> edges = new ArrayListPlus<>();
        for (Vertical vertical: verticals) {
            for (Edge edge: vertical.getEdges()) {
                if (!edges.contains(edge)) {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }

    private ListPlus<Vertical> getVerticals(int [][] adjacencyMatrix) {
        ListPlus<Vertical> verticalsList = new ArrayListPlus<>();

        for (int i=0; i<adjacencyMatrix.length; i++) {
            verticalsList.add(new Vertical("" + (i+1) ));
        }

        for (int i=0; i<adjacencyMatrix.length; i++) {
            for (int j=0; j<adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] >= 0) {
                    verticalsList.get(i).addEdge(verticalsList.get(j), adjacencyMatrix[i][j]);
                }
            }
        }

        return verticalsList;
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

    public ListPlus<Vertical> getRandomExit() {
        ListPlus<Vertical> outputs = new ArrayListPlus<>();
        BinaryTreeMapPlus<Integer, Vertical> tree = new BinaryTreeMapPlus<>();
        int i = 0;
        for (Vertical v: verticals) {
            tree.put(i, v);
            i++;
        }
        int size = verticals.size()/5;
        if (size<1) size=1;
        for (int j=0; j<size; ) {
            boolean similar = false;
            Vertical temp = tree.get((int)(Math.random()*tree.size()));
            for (Vertical v: outputs) {
                if (v.equals(temp)) {
                    similar = true;
                    break;
                }
            }
            if (!similar) {
                outputs.add(temp);
                j++;
            }
        }
        return outputs;
    }

    public void search (Vertical start, ListPlus<Vertical> finish, int typeSearch, int typeShow, ListPlus<String> errors) {
        ListPlus<Route> temp = new ArrayListPlus<>();
        for (int i=0; i<finish.size(); i++) {
            Vertical vertical = finish.get(i);
            Route route = null;
            try {
                if (typeSearch==DFSearch) {
                    route = DFS(start,vertical);
                } else if (typeSearch==BFSearch) {
                    route = BFS(start,vertical);
                } else if (typeSearch==DijkstraSearch) {
                    route = dijkstraAlgorithm(start,vertical);
                }
            } catch (Exception e) {
                errors.add(start.getName() + " -> "  +vertical.getName());
                Thread.currentThread().interrupt();
            }
            if (route != null) {
                temp.add(route);
            } else {
                errors.add(start.getName() + " -> "  +vertical.getName());
            }
        }
        chooseShowedRoutes(temp,typeShow);
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    public void addRoutes(ListPlus<Route> routes) {
        this.routes.addAll(routes.getList());
    }

    public void removeRoute(Route route) {
        routes.remove(route);
    }

    public void removeRoutes(List<Route> r) {
        routes.removeAll(r);
    }

    public void removeAllRoutes() {
        routes.clear();
    }

    private Route DFS(Vertical start, Vertical finish) {
        long searchTime = System.nanoTime();
        ListPlus<Vertical> history = new ArrayListPlus<>();
        LinkedListPlus<Vertical> path = new LinkedListPlus<>();
        Vertical temp = start;
        history.add(temp);
        boolean isNext;
        while (!temp.equals(finish)) {
            isNext = false;
            for (Edge edge: temp.getEdges()) {
                if (!history.contains(edge.getTail())) {
                    isNext = true;
                    history.add(edge.getTail());
                    path.add(temp);
                    temp = edge.getTail();
                    break;
                }
            }
            if (!isNext) {
                temp = path.removeLast();
            }
            if ((System.nanoTime()-searchTime)/1000 > waitTimeMilliseconds) {
                return null;
            }
        }
        path.add(temp);
        return new Route(path, "DFS");
    }

    private Route BFS(Vertical start, Vertical finish) {
        long searchTime = System.nanoTime();
        HashSet<Vertical> history = new HashSet<>();
        history.add(start);
        Vertical result = searchBFS(start, finish, history);
        LinkedListPlus<Vertical> path = new LinkedListPlus<>();
        while (!result.equals(start)) {
            path.addFirst(result);
            result = result.getParent();
            if ( (System.nanoTime()-searchTime)/1000 > waitTimeMilliseconds) {
                return null;
            }
        }
        path.addFirst(start);
        return new Route(path, "BFS");
    }

    private Vertical searchBFS ( Vertical start, Vertical finish, HashSet<Vertical> history) {
        Vertical temp = null;
        boolean find = false;
        LinkedListPlus<Vertical> verticals = new LinkedListPlus<>();
        verticals.add(start);
        while (!verticals.isEmpty()) {
            temp = verticals.getFirst();
            verticals.removeFirst();
            if (temp.equals(finish)) {
                find = true;
                break;
            }
            for (Edge edge: temp.getEdges()) {
                if (history.add(edge.getTail())) {
                    verticals.add(edge.getTail());
                    edge.getTail().setParent(temp);
                }
            }
        }
        if (find) {
            return temp;
        }
        return null;
    }

    private Route dijkstraAlgorithm(Vertical start, Vertical finish) {
        long searchTime = System.nanoTime();
        Set<Vertical> history = new HashSet<>();
        Map<Vertical, Integer> map = new HashMap<>();
        for (Vertical v: verticals) {
            map.put(v, null);
        }
        map.put(start, 0);
        Vertical temp;
        while (true) {
            temp = null;
            for (Map.Entry<Vertical, Integer> e:map.entrySet()) {
                if ( !history.contains(e.getKey())) {
                    if (temp == null || (e.getValue()!=null && ( map.get(temp) == null || map.get(temp) > e.getValue()) )) {
                        temp = e.getKey();
                    }
                }
            }
            if (temp.equals(finish)) {
                break;
            }
            for (Edge edge: temp.getEdges()) {
                if (!history.contains(edge.getTail())) {
                    if ( map.get(edge.getTail()) == null  ||  map.get(edge.getTail()) > (map.get(temp) + edge.getWeight()) ) {
                        map.put(edge.getTail(), map.get(temp) + edge.getWeight());
                        edge.getTail().setParent(temp);
                    }
                }
            }
            history.add(temp);
            if ((System.nanoTime()-searchTime)/1000 > waitTimeMilliseconds) {
                return null;
            }
        }
        LinkedListPlus<Vertical> path = new LinkedListPlus<>();
        while (!temp.equals(start)) {
            path.addFirst(temp);
            temp = temp.getParent();
            if ( (System.nanoTime()-searchTime)/1000 > waitTimeMilliseconds) {
                return null;
            }
        }
        path.addFirst(start);
        return new Route(path, "Find Shortest Path");
    }

    private void chooseShowedRoutes (ListPlus<Route> temp, int typeShow) {
        if (temp.isEmpty()) {
            return;
        }
        if (typeShow == ShowAll) {
            addRoutes(temp);
        } else if (typeShow == ShowShortest) {
            addRoute(getShortestRoute(temp));
        } else if (typeShow == ShowSafest) {
            addRoute(getSafestRoute(temp));
        } else if (typeShow == ShowOptimal) {
            addRoute(getOptimalRoute(temp));
        }
    }

    private Route getShortestRoute(ListPlus<Route> routeList) {
        Route temp = routeList.get(0);
        for (Route route: routeList) {
            if (temp.getWeight()>route.getWeight()) {
                temp = route;
            }
        }
        return temp;
    }

    private Route getSafestRoute(ListPlus<Route> routeList) {
        Route temp = routeList.get(0);
        for (Route route: routeList) {
            if (temp.getTrap()>route.getTrap()) {
                temp = route;
            }
        }
        return temp;
    }

    private Route getOptimalRoute(ListPlus<Route> routeList) {
        Route temp = routeList.get(0);
        for (Route route: routeList) {
            double deltaWeight = (temp.getWeight()-route.getWeight())/temp.getWeight();
            double deltaTrap = (temp.getTrap()-route.getTrap())/temp.getTrap();
            if (deltaWeight+deltaTrap>0) {
                temp = route;
            }
        }
        return temp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<Route> getRoutes() {
        return routes;
    }

    private String getDefaultName() {
        return "graph";
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Graph graph = new Graph(getAdjacencyMatrix());
        for (Edge edge: graph.getEdges()) {
            for (Edge edge1: getEdges()) {
                if (edge.equals(edge1)) {
                    edge.setTrap(edge1.getTrap());
                }
            }
        }
        ListPlus<Route> routeList = new ArrayListPlus<>();
        for (Route route: routes) {
            routeList.add( (Route) route.clone());
        }
        graph.addRoutes(routeList);
        return graph;
    }
}
