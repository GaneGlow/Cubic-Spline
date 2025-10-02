import java.util.Arrays;

public class CubicSpline {
    private double[] a;
    private double[] b;
    private double[] c;
    private double[] d;
    private double[] x;
    private double[] y;
    private int nx;

    private double[][] calculateA(double[] h) {
        double[][] result = new double[nx][nx];

        for (int i = 0; i < nx; i++) {
            Arrays.fill(result[i], 0);
        }
        result[0][0] = 1;

        for (int i = 0; i < nx - 1; i++) {
            if (i != nx - 2){
                result[i + 1][i + 1] = 2 * (h[i] + h[i + 1]);
            }
            result[i + 1][i] = h[i];
            result[i][i + 1] = h[i];
        }
        result[0][1] = 0;
        result[nx - 1][nx - 2] = 0;
        result[nx - 1][nx - 1] = 1;
        
        return result;
    }

    private double[] calculateB(double[] h) {
        double[] result = new double[nx];

        for (int i = 0; i < nx; i++) {
            Arrays.fill(result, 0);
        }
        for (int i = 0; i < nx - 2; i++) {
            result[i + 1] = 3 * ((a[i + 2] - a[i + 1]) / h[i + 1] - (a[i + 1] - a[i]) / h[i]);
        }
        
        return result;
    }

    public CubicSpline(double[] x, double[] y) {
        this.x = x;
        this.y = y;
        this.nx = x.length;
        this.a = new double[nx];
        this.b = new double[nx - 1];
        this.c = new double[nx];
        this.d = new double[nx - 1];

        System.arraycopy(y, 0, a, 0, nx);

        double[] h = new double[nx - 1];
        for (int i = 0; i < nx - 1; i++) {
            h[i] = x[i + 1] - x[i];
        }
    }
}