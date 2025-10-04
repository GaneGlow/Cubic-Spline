package com.cgvsu.protocurvefxapp;

import CubicSplineProg.CubicSpline2D;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class ProtoCurveController {

    private List<Double> xPoints = new ArrayList<>();
    private List<Double> yPoints = new ArrayList<>();
    private XYChart.Series<Number, Number> pointSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> splineSeries = new XYChart.Series<>();

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    ArrayList<Point2D> points = new ArrayList<Point2D>();

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(canvas.getGraphicsContext2D(), event);
            }
        });
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

        final int POINT_RADIUS = 3;
        graphicsContext.fillOval(
                clickPoint.getX() - POINT_RADIUS, clickPoint.getY() - POINT_RADIUS,
                2 * POINT_RADIUS, 2 * POINT_RADIUS);

        if (points.size() > 0) {
            final Point2D lastPoint = points.get(points.size() - 1);
            graphicsContext.strokeLine(lastPoint.getX(), lastPoint.getY(), clickPoint.getX(), clickPoint.getY());
        }
        points.add(clickPoint);
    }

    private List<double[]> calculateInterpolation(double[] x, double[] y, int num) {
        CubicSpline2D cubicSpline2D = new CubicSpline2D(x, y);
        double[] params = cubicSpline2D.getParams();

        double start = params[0];
        double end = params[params.length - 1];

        List<double[]> result = new ArrayList<>();

        double step = (end - start) / (num - 1);
        for (int i = 0; i < num; i++) {
            double param = start + i * step;
            double[] point = cubicSpline2D.point(param);
            if (point != null) {
                result.add(point);
            }
        }

        return result;
    }

    private void displaySpline() {
        double[] xArray = xPoints.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yArray = yPoints.stream().mapToDouble(Double::doubleValue).toArray();

        List<double[]> points = calculateInterpolation(xArray, yArray, 500);

        splineSeries.getData().clear();
        for (double[] point : points) {
            splineSeries.getData().add(new XYChart.Data<>(point[0], point[1]));
        }
    }

    private void updatePoints() {
        pointSeries.getData().clear();
        for (int i = 0; i < xPoints.size(); i++) {
            pointSeries.getData().add(new XYChart.Data<>(xPoints.get(i), yPoints.get(i)));
        }
    }

    private double screenToData(double screenCoord, NumberAxis axis) {
        double lowerBound = axis.getLowerBound();
        double upperBound = axis.getUpperBound();
        double length = axis.getWidth();

        return lowerBound + (screenCoord / length) * (upperBound - lowerBound);
    }
}