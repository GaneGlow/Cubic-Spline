package com.cgvsu.protocurvefxapp;

import CubicSplineProg.CubicSpline2D;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

import javafx.scene.paint.Color;

import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ProtoCurveController {

    //private List<Point2D> splinePoints = new ArrayList<>();

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
    private CubicSpline2D spline2D;

    private ArrayList<Point2D> points = new ArrayList<>();

    @FXML
    private void initialize() {

        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(event);
            }
        });
        //redraw();
    }

    private void handlePrimaryClick(MouseEvent event) {
        /*points.add(new Point2D(screenX, screenY));

        *//*if (points.size() > 1) {
            displaySpline();
        }*/
        Point2D newPoint = new Point2D(event.getX(), event.getY());
        points.add(newPoint);

        redraw();
    }

    /*private void calculateInterpolation(double[] x, double[] y, int num) {
        CubicSpline2D cubicSpline2D = new CubicSpline2D(x, y);
        double[] params = cubicSpline2D.getParams();

        double start = params[0];
        double end = params[params.length - 1];

        splinePoints.clear();
        double step = (end - start) / (num - 1);
        for (int i = 0; i < num; i++) {
            double param = start + i * step;
            double[] point = cubicSpline2D.point(param);
            if (point != null) {
                splinePoints.add(new Point2D(point[0], point[1]));
            }
        }
    }

    *//*private void displaySpline() {
        //if (points.size() < 2) return;

        double[] xArray = new double[points.size()];
        double[] yArray = new double[points.size()];

        for (int i = 0; i < points.size(); i++) {
            xArray[i] = points.get(i).getX();
            yArray[i] = points.get(i).getY();
        }
        //splinePoints.clear();
        calculateInterpolation(xArray, yArray, 500);
    }*//*

    private void drawSpline(GraphicsContext gc) {
        double[] xArray = new double[points.size()];
        double[] yArray = new double[points.size()];

        for (int i = 0; i < points.size(); i++) {
            xArray[i] = points.get(i).getX();
            yArray[i] = points.get(i).getY();
        }
        calculateInterpolation(xArray, yArray, 500);
        //if (splinePoints.size() < 2) return;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        for (int i = 0; i < splinePoints.size() - 1; i++) {
            Point2D curr = splinePoints.get(i);
            Point2D next = splinePoints.get(i + 1);
            gc.strokeLine(curr.getX(), curr.getY(), next.getX(), next.getY());
        }
    }

    private void drawPoint(GraphicsContext gc) {
        final int POINT_RADIUS = 3;

        for (Point2D point : points) {
            gc.fillOval(
                    point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }
    }*/

    private void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.RED);
        final double POINT_RADIUS = 4;
        for (Point2D p : points) {
            gc.fillOval(p.getX() - POINT_RADIUS / 2, p.getY() - POINT_RADIUS / 2, POINT_RADIUS, POINT_RADIUS);
        }

        if (points.size() > 1) {
            double[] xPoints = points.stream().mapToDouble(Point2D::getX).toArray();
            double[] yPoints = points.stream().mapToDouble(Point2D::getY).toArray();

            spline2D = new CubicSpline2D(xPoints, yPoints);

            int numSamples = 500;
            double startParam = spline2D.getParams()[0];
            double endParam = spline2D.getParams()[spline2D.getParams().length - 1];
            double step = (endParam - startParam) / numSamples;

            gc.setStroke(Color.GREEN);
            gc.setLineWidth(2);

            Point2D prevPoint = null;
            for (int i = 0; i <= numSamples; i++) {
                double param = startParam + i * step;
                double[] pt = spline2D.point(param);
                Point2D currPoint = new Point2D(pt[0], pt[1]);

                if (prevPoint != null) {
                    gc.strokeLine(prevPoint.getX(), prevPoint.getY(), currPoint.getX(), currPoint.getY());
                }
                prevPoint = currPoint;
            }
        }
    }

}