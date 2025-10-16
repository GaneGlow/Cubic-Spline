package CubicSplineProg;

public class CubicSpline2D {
    private double[] params;
    private CubicSpline sx;
    private CubicSpline sy;
    private double[] ds;

    public CubicSpline2D(double[] x, double[] y) {
        this.params = calculateParams(x, y);
        this.sx = new CubicSpline(params, x);
        this.sy = new CubicSpline(params, y);
    }

    private double[] calculateParams(double[] x, double[] y) {
        double[] dx = new double[x.length - 1];
        double[] dy = new double[y.length - 1];
        this.ds = new double[dx.length];

        for (int i = 0; i < dx.length; i++) {
            dx[i] = x[i + 1] - x[i];
            dy[i] = y[i + 1] - y[i];
            ds[i] = Math.sqrt(dx[i] * dx[i] + dy[i] * dy[i]);
        }

        double[] s = new double[x.length];
        s[0] = 0.0;
        for (int i = 1; i < s.length; i++) {
            s[i] = s[i - 1] + ds[i - 1];
        }

        return s;
    }

    public double[] getParams() {
        return params;
    }

    public double[] point(double param) {
        Double x = sx.point(param);
        Double y = sy.point(param);

        return new double[] {x, y};
    }
}