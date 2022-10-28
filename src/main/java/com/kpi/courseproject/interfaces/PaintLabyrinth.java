package com.kpi.courseproject.interfaces;

import com.kpi.courseproject.collection.HashMapPlus;
import com.kpi.courseproject.collection.MapPlus;
import com.kpi.courseproject.controllers.MainController;
import com.kpi.courseproject.logic.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class PaintLabyrinth {

    private int typePlacing;
    private GraphicsContext gcGraph;
    private GraphicsContext gcRoutes;
    private GraphicsContext gcMouse;
    private Canvas cMouse;
    private Canvas cGraph;
    private Canvas cRoutes;
    private Pane root;
    private List<Point2D> points;
    private MapPlus<Route, Color> routesColors;
    private Color gColor = Color.rgb(64,0,0);
    private static List<Color> colors;

    private final double lengthArrow = 15;
    private final double lineWidth = 2;
    private final double vRadius = 12;

    static  {
        colors = new ArrayList<>(20);
//        colors.add(new Color(0.3,0,0, 0.3));
//        colors.add(new Color(0,0.3,0,0.3));
//        colors.add(new Color(0,0,0.3,0.3));
//        colors.add(new Color(0.3,0.3,0,0.3));
//        colors.add(new Color(0.3,0,0.3,0.3));
//        colors.add(new Color(0,0.3,0.3,0.3));
//        colors.add(new Color(0.3,0.3,0.3,0.3));
//        colors.add(new Color(0.5,0,0, 0.3));
//        colors.add(new Color(0,0.5,0,0.3));
//        colors.add(new Color(0,0,0.5,0.3));
//        colors.add(new Color(0.5,0.5,0,0.3));
//        colors.add(new Color(0.5,0,0.5,0.3));
//        colors.add(new Color(0,0.5,0.5,0.3));
        colors.add(new Color(0.75,0,0, 0.3));
        colors.add(new Color(0,0.75,0,0.3));
        colors.add(new Color(0,0,0.75,0.3));
//        colors.add(new Color(0.75,0.75,0,0.3));
        colors.add(new Color(0.75,0,0.75,0.3));
//        colors.add(new Color(0,0.75,0.75,0.3));
        colors.add(new Color(1,0,0, 0.3));
        colors.add(new Color(0,1,0,0.3));
        colors.add(new Color(0,0,1,0.3));
        colors.add(new Color(1,1,0,0.3));
        colors.add(new Color(1,0,1,0.3));
        colors.add(new Color(0,1,1,0.3));
    }

    public PaintLabyrinth (Pane root, int typePlacing) {
        this.root = root;
        points = new ArrayList<>();
        this.typePlacing = typePlacing;
        routesColors = new HashMapPlus<>();
        setCanvases();
    }

    public void paintGraph(boolean calculatePoint) {
        if (MainController.graph==null) {
            return;
        }
        setCanvases();
        gcGraph.setLineWidth(lineWidth);
        gcGraph.setStroke(gColor);
        gcGraph.setFill(gColor);
        gcGraph.setLineCap(StrokeLineCap.ROUND);
        gcGraph.setImageSmoothing(true);

        if (calculatePoint) {
            points.clear();
            if (typePlacing == 0) {
                calculatePointRandom();
            } else if (typePlacing == 1) {
                calculatePointCircle();
            } else if (typePlacing == 2) {
                calculatePointTable();
            }
        }
        paintEdges();
        for (int i=0; i< points.size(); i++) {
            gcGraph.fillOval(points.get(i).getX()-vRadius, points.get(i).getY()-vRadius, 2*vRadius, 2*vRadius);
            gcGraph.setFill(Color.WHITE);
            gcGraph.setFont(new Font(20));
            gcGraph.fillText(MainController.graph.getVerticals().get(i).getName(), points.get(i).getX()-(vRadius/2), points.get(i).getY()+(vRadius/2));
            gcGraph.setFill(gColor);
        }
        paintWeight();
        paintTrap();
        paintRoutes();
    }

    public void newPaintMouse(Mouse mouse, MainController con) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                paintMouse(mouse);
                con.onController();
            }
        });
        thread.start();
    }

    private void paintMouse(Mouse mouse) {
        gcMouse.clearRect(0, 0, root.getPrefWidth(), root.getPrefHeight());
        ImageView image = new ImageView("D:/Java-Poject/CourseProject/src/main/resources/com/kpi/courseproject/mouse.png");
        image.setFitWidth(30);
        image.setFitHeight(30);
        Graph graph = MainController.graph;
        for (Edge edge: mouse.getRoute().getEdges()) {
            double x1 = points.get(graph.getVerticals().indexOf(edge.getHead())).getX();
            double y1 = points.get(graph.getVerticals().indexOf(edge.getHead())).getY();
            double x2 = points.get(graph.getVerticals().indexOf(edge.getTail())).getX();
            double y2 = points.get(graph.getVerticals().indexOf(edge.getTail())).getY();
            int mul = 50;
            int loop = mul;
            if (mouse.isDeath() && mouse.getLast().equals(edge)) {
                loop = 25;
            }
            double dx = (x2-x1)/(edge.getWeight()*mul);
            double dy = (y2-y1)/(edge.getWeight()*mul);
            double x = x1;
            double y = y1;
            for (int i=0; i< edge.getWeight()*loop; i++) {
                gcMouse.clearRect(0, 0, root.getPrefWidth(), root.getPrefHeight());
                gcMouse.drawImage(image.getImage(), x,y ,30 ,30);
                x+=dx;
                y+=dy;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (mouse.isDeath() && mouse.getLast().equals(edge)) {
                ImageView trap = new ImageView("D:/Java-Poject/CourseProject/src/main/resources/com/kpi/courseproject/trap.png");
                trap.setFitWidth(60);
                trap.setFitHeight(60);
                gcMouse.drawImage(trap.getImage(), x-20, y-20, 60 , 60);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gcMouse.clearRect(0, 0, root.getPrefWidth(), root.getPrefHeight());
                break;
            }
        }
    }

    public void repaint() {
        recalculatePoint();
        paintGraph(false);
    }

    private void paintRoutes() {
        double widthPoint = 15;
        if (MainController.graph==null) {
            return;
        }
        Graph graph = MainController.graph;
        gcRoutes.setLineWidth(6*lineWidth);
        gcRoutes.setLineCap(StrokeLineCap.ROUND);
        gcRoutes.setImageSmoothing(true);
        for (Route route: graph.getRoutes()) {
            if (!routesColors.containsKey(route)) {
                routesColors.put(route, colors.get( (int)(Math.random()*(colors.size()-1)) ));
            }
        }
        for (MapPlus.Entry<Route, Color> entry: routesColors.entrySet()) {
            for (int i=1; i<entry.getKey().getVerticals().size(); i++) {
                gcRoutes.setStroke(entry.getValue());
                double x1 = points.get( graph.getVerticals().indexOf(entry.getKey().getVerticals().get(i-1))).getX();
                double y1 = points.get( graph.getVerticals().indexOf(entry.getKey().getVerticals().get(i-1))).getY();
                double x2 = points.get( graph.getVerticals().indexOf(entry.getKey().getVerticals().get(i))).getX();
                double y2 = points.get( graph.getVerticals().indexOf(entry.getKey().getVerticals().get(i))).getY();
                gcRoutes.strokeLine(x1,y1,x2,y2);
                if (i==1) {
                    gcRoutes.setFill(Color.rgb(0,255,0,0.8));
                    gcRoutes.fillOval(x1-widthPoint/2,y1-30, widthPoint, widthPoint);
                }
                if (i == entry.getKey().getVerticals().size()-1) {
                    gcRoutes.setFill(Color.rgb(255,0,0,0.8));
                    gcRoutes.fillOval(x2-widthPoint/2,y2-30, widthPoint, widthPoint);
                }
            }
        }
    }

    private void paintEdges() {
        if (MainController.graph.getEdges()==null || MainController.graph.getEdges().isEmpty()) {
            return;
        }
        Graph graph = MainController.graph;
        for (int i=0; i<graph.getVerticals().size(); i++) {
            for (Edge edge: graph.getVerticals().get(i).getEdges()) {
                if (edge.getTail().equals(edge.getHead())) {
                    continue;
                }
                double x1 = points.get(i).getX();
                double y1 = points.get(i).getY();
                double x2 = points.get(graph.getVerticals().indexOf(edge.getTail())).getX();
                double y2 = points.get(graph.getVerticals().indexOf(edge.getTail())).getY();
                gcGraph.strokeLine(x1,y1,x2,y2);
                double angle = Math.atan((y1-y2)/(x2-x1));
                double ax2, ay2, ax3, ay3, ax4, ay4;
                if (x1<=x2) {
                    ax2 = x2 - lengthArrow * Math.cos(angle);
                    ay2 = y2 + lengthArrow * Math.sin(angle);
                    ax3 = ax2 - lengthArrow * Math.cos(angle-Math.PI/8);
                    ay3 = ay2 + lengthArrow * Math.sin(angle-Math.PI/8);
                    ax4 = ax2 - lengthArrow * Math.cos(angle+Math.PI/8);
                    ay4 = ay2 + lengthArrow * Math.sin(angle+Math.PI/8);
                } else {
                    ax2 = x2 + lengthArrow * Math.cos(angle);
                    ay2 = y2 - lengthArrow * Math.sin(angle);
                    ax3 = ax2 + lengthArrow * Math.cos(angle-Math.PI/8);
                    ay3 = ay2 - lengthArrow * Math.sin(angle-Math.PI/8);
                    ax4 = ax2 + lengthArrow * Math.cos(angle+Math.PI/8);
                    ay4 = ay2 - lengthArrow * Math.sin(angle+Math.PI/8);
                }
                gcGraph.strokeLine(ax2, ay2, ax3, ay3);
                gcGraph.strokeLine(ax2, ay2, ax4, ay4);
            }
        }
    }

    private void paintWeight() {
        Graph graph = MainController.graph;
        double width = 20;
        for (Edge edge: graph.getEdges()) {
            if (edge.getTail().equals(edge.getHead()) || edge.getWeight()==0) {
                continue;
            }
            double x1 = points.get(graph.getVerticals().indexOf(edge.getHead())).getX();
            double y1 = points.get(graph.getVerticals().indexOf(edge.getHead())).getY();
            double x2 = points.get(graph.getVerticals().indexOf(edge.getTail())).getX();
            double y2 = points.get(graph.getVerticals().indexOf(edge.getTail())).getY();
            double centerX = (x2+x1)/2;
            double centerY = (y2+y1)/2;
            double x = centerX-10;
            double y = centerY-10;
            gcGraph.setFont(new Font(16));
            gcGraph.setFill(Color.rgb(255,250,0,0.5));
            gcGraph.fillRect(x-width/2, y-width/2, width, width);
            gcGraph.setFill(Color.BLACK);
            gcGraph.fillText(edge.getWeight()+"", x-width/2.5, y+width/2.5);
        }
    }

    private void paintTrap() {
        Graph graph = MainController.graph;
        double width = 20;
        for (Edge edge: graph.getEdges()) {
            if (edge.getTail().equals(edge.getHead()) || edge.getTrap()==0) {
                continue;
            }
            double x1 = points.get(graph.getVerticals().indexOf(edge.getHead())).getX();
            double y1 = points.get(graph.getVerticals().indexOf(edge.getHead())).getY();
            double x2 = points.get(graph.getVerticals().indexOf(edge.getTail())).getX();
            double y2 = points.get(graph.getVerticals().indexOf(edge.getTail())).getY();
            double centerX = (x2+x1)/2;
            double centerY = (y2+y1)/2;
            double x = centerX+10;
            double y = centerY-10;
            gcGraph.setFill(Color.rgb(255,0,0,0.5));
            gcGraph.fillRect(x-width/2, y-width/2, width*1.5, width);
            gcGraph.setFont(new Font(16));
            gcGraph.setFill(Color.BLACK);
            gcGraph.fillText(edge.getTrap()+"", x-width/2.5, y+width/2.5);
        }
    }

    private void recalculatePoint() {
        double oldWidth = cGraph.getWidth();
        double oldHeight = cGraph.getHeight();
        setCanvases();
        double vWidth = cGraph.getWidth()/oldWidth;
        double vHeight = cGraph.getHeight()/oldHeight;
        List<Point2D> point2D = new ArrayList<>(points.size());
        for (int i=0; i<points.size(); i++) {
            double x = points.get(i).getX();
            double y = points.get(i).getY();
            point2D.add(new Point2D(x*vWidth, y*vHeight));
        }
        points.clear();
        points = point2D;
    }

    private void calculatePointRandom() {
        Graph graph = MainController.graph;
        double diversion = 30;
        double length = 0;
        if (graph.getVerticals().size()<5) {
            length = 30;
        } else if (graph.getVerticals().size()<10) {
            length = 20;
        } else if (graph.getVerticals().size()<15) {
            length = 10;
        } else {
            length = 5;
        }
        for (int i=0; i<graph.getVerticals().size();) {
            boolean similar = false;
            double x = Math.random()*(cGraph.getWidth()-2*diversion) + diversion;
            double y = Math.random()*(cGraph.getHeight()-2*diversion) + diversion;
            Point2D point = new Point2D(x, y);
            for (Point2D p: points) {
                if ( (point.getX()>(p.getX()-length) && point.getX()<(p.getX()+length))) {
                    similar = true;
                }
                if (point.getY()>(p.getY()-length) && point.getY()<(p.getY()+length)) {
                    similar = true;
                }
            }
            if (!similar) {
                i++;
                points.add(point);
            }
        }
    }

    private void calculatePointCircle() {
        Graph graph = MainController.graph;
        Point2D center = new Point2D(gcGraph.getCanvas().getWidth()/2,gcGraph.getCanvas().getHeight()/2);
        int radius;
        if (gcGraph.getCanvas().getHeight()>gcGraph.getCanvas().getWidth()) {
            radius = (int)(gcGraph.getCanvas().getWidth()-60)/2;
        } else {
            radius = (int)(gcGraph.getCanvas().getHeight()-60)/2;
        }
        double angle = 2*Math.PI/graph.getVerticals().size();
        double tAngle = 0;
        for (Vertical vertical: graph.getVerticals()) {
            double x = center.getX() + radius * Math.cos(tAngle);
            double y = center.getY() + radius * Math.sin(tAngle);
            points.add(new Point2D(x, y));
            tAngle+=angle;
        }
    }

    private void calculatePointTable() {
        Graph graph = MainController.graph;
        int numCol = (int) (gcGraph.getCanvas().getWidth()/(gcGraph.getCanvas().getHeight()+gcGraph.getCanvas().getWidth()) * (Math.sqrt(graph.getVerticals().size())*2));
        int numRow = (int) Math.ceil(graph.getVerticals().size() / numCol);
        double width = gcGraph.getCanvas().getWidth()/(numCol+1);
        double height = gcGraph.getCanvas().getHeight()/(numRow+2);
        double w = width;
        double h = height;
        for (Vertical v: graph.getVerticals()) {
            points.add(new Point2D(w,h));
            w+=width;
            if ((w+12)>gcGraph.getCanvas().getWidth()) {
                w = width;
                h+=height;
            }
        }
    }

    private void setCanvases() {
        root.getChildren().clear();
        cMouse = new Canvas(root.getPrefWidth(), root.getPrefHeight());
        cGraph = new Canvas(root.getPrefWidth(), root.getPrefHeight());
        cRoutes = new Canvas(root.getPrefWidth(), root.getPrefHeight());
        gcMouse = cMouse.getGraphicsContext2D();
        gcGraph = cGraph.getGraphicsContext2D();
        gcRoutes = cRoutes.getGraphicsContext2D();
        root.getChildren().addAll(cRoutes, cGraph, cMouse);
    }

    public void removeRoutes(List<Route> routes) {
        for (Route route: routes) {
            routesColors.remove(route);
        }
    }

    public void removeAllRoutes() {
        routesColors.clear();
    }

    public void setTypePlacing(int typePlacing) {
        this.typePlacing = typePlacing;
    }
}
