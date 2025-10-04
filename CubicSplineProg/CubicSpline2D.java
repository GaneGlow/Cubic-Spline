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
        int n = x.length;
        double[] dx = new double[n - 1];
        double[] dy = new double[n - 1];
        this.ds = new double[n - 1];

        for (int i = 0; i < n - 1; i++) {
            dx[i] = x[i + 1] - x[i];
            dy[i] = y[i + 1] - y[i];
            ds[i] = Math.sqrt(Math.pow(dx[i], 2) + Math.pow(dy[i], 2));
        }

        double[] s = new double[n];
        s[0] = 0.0;
        for (int i = 1; i < n; i++) {
            s[i] = s[i - 1] + ds[i - 1];
        }

        return s;
    }

    public double[] point(double param) {
        Double x = sx.point(param);
        Double y = sy.point(param);

        return new double[] {x, y};
    }
}