package CubicSplineProg;

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
        result[0][0] = 1.0;

        for (int i = 0; i < nx - 1; i++) {
            if (i != nx - 2){
                result[i + 1][i + 1] = 2.0 * (h[i] + h[i + 1]);
            }
            result[i + 1][i] = h[i];
            result[i][i + 1] = h[i];
        }
        result[0][1] = 0.0;
        result[nx - 1][nx - 2] = 0.0;
        result[nx - 1][nx - 1] = 1.0;

        return result;
    }

    private double[] calculateB(double[] h) {
        double[] result = new double[nx];

        Arrays.fill(result, 0);

        for (int i = 0; i < nx - 2; i++) {
            result[i + 1] = 3.0 * ((a[i + 2] - a[i + 1]) / h[i + 1] - (a[i + 1] - a[i]) / h[i]);
        }

        return result;
    }

    public static double[] methodGauss(double[][] a, double[] b) {
        /*int n = b.length;
        double[][] matrix = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, matrix[i], 0, n);
            matrix[i][n] = b[i];
        }


        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(matrix[k][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = k;
                }
            }

            double[] temp = matrix[i];
            matrix[i] = matrix[maxRow];
            matrix[maxRow] = temp;

            for (int k = i + 1; k < n; k++) {
                double factor = matrix[k][i] / matrix[i][i];
                for (int j = i; j <= n; j++) {
                    matrix[k][j] -= factor * matrix[i][j];
                }
            }
        }

        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += matrix[i][j] * solution[j];
            }
            solution[i] = (matrix[i][n] - sum) / matrix[i][i];
        }

        return solution;*/
        int n = b.length;
        double[] x = new double[n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double factor = a[j][i] / a[i][i];
                for (int k = i; k < n; k++) {
                    a[j][k] -= factor * a[i][k];
                }
                b[j] -= factor * b[i];
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += a[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / a[i][i];
        }

        return x;
    }


    public CubicSpline(double[] x, double[] y) {
        this.x = Arrays.copyOf(x, x.length);
        this.y = Arrays.copyOf(y, y.length);
        this.nx = x.length;
        this.a = Arrays.copyOf(y, y.length);
        this.b = new double[nx - 1];
        this.c = new double[nx];
        this.d = new double[nx - 1];

        //System.arraycopy(y, 0, a, 0, nx);

        double[] h = new double[nx - 1];
        for (int i = 0; i < nx - 1; i++) {
            h[i] = x[i + 1] - x[i];
        }

        double[][] a = calculateA(h);
        double[] b = calculateB(h);
        this.c = methodGauss(a, b);

        for (int i = 0; i < nx - 1; i++) {
            d[i] = (c[i + 1] - c[i]) / (3.0  * h[i]);
            b[i] = (this.a[i + 1] - this.a[i]) / h[i] - h[i] * (this.c[i + 1] + 2.0 * this.c[i]) / 3.0;
        }
    }

    private int searchIndex(double x) {
        int lo = 0;
        int hi = this.x.length;

        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (x < this.x[mid]) {
                hi = mid;
            }
            else {
                lo = mid + 1;
            }
        }
        return lo - 1;
    }

    public Double point(double param) {
        if (param < this.x[0] || param > this.x[x.length - 1]) {
            return null;
        }

        int i = searchIndex(param);

        if (i >= this.b.length || i >= this.d.length) {
            i = this.b.length - 1;
        }

        double dx = param - this.x[i];
        return a[i] + b[i] * dx + c[i] * dx * dx + d[i] * dx * dx * dx;
    }

}
