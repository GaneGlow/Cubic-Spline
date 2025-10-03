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

    public static double[][] countTriangleMatrix(double[][] arr, int k) {
        int c = 0;
        for (int n = 0; n < arr[0].length - k; n++) {
            for (int i = c + 1; i < arr.length; i++) {
                double coef = arr[i][c] / arr[c][c];
                for (int j = c; j < arr[0].length; j++) {
                    arr[i][j] = arr[i][j] - coef * arr[c][j];
                }
            }
            c++;
        }
        return arr;
    }

    public static double[] methodGauss(double[][] a, double[] b) {
        int n = b.length;
        double[][] matrix = new double[n][n + 1];

        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, matrix[i], 0, n);
            matrix[i][n] = b[i];
        }
        double[][] arr = countTriangleMatrix(matrix, 2);
        double[] answers = new double[arr.length];
        int c = arr[0].length - 3;
        if (arr[arr.length - 1][arr[0].length - 2] != 0) {
            answers[arr.length - 1] = arr[arr.length - 1][arr[0].length - 1] / arr[arr.length - 1][arr[0].length - 2];
            for (int i = arr.length - 2; i >= 0; i--) {
                if (c >= 0) {
                    for (int j = arr[0].length - 2; j > c; j--) {
                        arr[i][arr[0].length - 1] -= arr[i][j] * answers[j];
                    }
                }
                answers[i] = arr[i][arr[0].length - 1] / arr[i][c];
                c--;
            }
        }
        return answers;
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

        double[][] a = calculateA(h);
        double[] b = calculateB(h);
        this.c = methodGauss(a, b);

        for (int i = 0; i < nx - 1; i++) {
            d[i] = (c[i + 1] - c[i]) / (3 * h[i]);
            double tb = (this.a[i + 1] - this.a[i]) / h[i] - h[i] * (c[i + 1] + 2.0 * c[i]) / 3.0;
            b[i] = tb;
        }
    }

    private int searchIndex(double x) {
        int lo = 0;
        int hi = this.x.length;

        while (lo < hi) {
            int mid = (lo + hi) /2;
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
        if (param < this.x[0] || param > this.x[-1]) {
            return null;
        }

        int i = searchIndex(param);
        double dx = param - this.x[i];
        return this.a[i] + this.b[i] * dx + this.c[i] * Math.pow(dx, 2) + this.d[i] * Math.pow(dx, 3);
    }
}