package CubicSplineProg;

public class CubicSpline2D {
    private CubicSpline splineX;
    private CubicSpline splineY;
    private double[] params;

    public CubicSpline2D(double[] x, double[] y){

        this.params = new double[x.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = i;
        }

        this.splineX = new CubicSpline(params, x);
        this.splineY = new CubicSpline(params, y);
    }

    public double[] point(double param) {
        double x = splineX.point(param);
        double y = splineY.point(param);
        return new double[] {x, y};
    }

    public double[] getParams() {
        return params;
    }
}