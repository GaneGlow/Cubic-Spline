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

public class ProtoCurveController {


    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    private ArrayList<Point2D> points = new ArrayList<>();

    @FXML
    private void initialize() {

        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> {

                    try {
                        handlePrimaryClick(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void handlePrimaryClick(MouseEvent event) throws Exception {

        Point2D newPoint = new Point2D(event.getX(), event.getY());
        points.add(newPoint);

        redraw();
    }

    private void redraw() throws Exception {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.RED);
        final double POINT_RADIUS = 5;
        for (Point2D p : points) {
            gc.fillOval(p.getX() - POINT_RADIUS / 2, p.getY() - POINT_RADIUS / 2, POINT_RADIUS, POINT_RADIUS);
        }

        if (points.size() > 1) {
            double[] xPoints = points.stream().mapToDouble(Point2D::getX).toArray();
            double[] yPoints = points.stream().mapToDouble(Point2D::getY).toArray();

            double[][] interpolatedPoints = interpolation(xPoints, yPoints, 1000);

            gc.setStroke(Color.BLUE);
            gc.setLineWidth(2);

            for (int i = 1; i < interpolatedPoints[0].length; i++) {
                double x1 = interpolatedPoints[0][i - 1];
                double y1 = interpolatedPoints[1][i - 1];
                double x2 = interpolatedPoints[0][i];
                double y2 = interpolatedPoints[1][i];

                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    private static double[][] interpolation(double[] x, double[] y, int num){
        CubicSpline2D spline2D = new CubicSpline2D(x, y);
        double[] params = spline2D.getParams();

        double start = params[0];
        double end = params[params.length - 1];
        double step = (end - start) / (num - 1);

        double[] resx = new double[num];
        double[] resy = new double[num];

        for (int i = 0; i < num; i++) {
            double param = start + i * step;
            double[] point = spline2D.point(param);
            resx[i] = point[0];
            resy[i] = point[1];
        }

        return new double[][] {resx, resy};
    }
}

