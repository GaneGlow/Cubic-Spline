package CubicSplineProg;

import java.util.Arrays;

public class CubicSpline {
    private double[] a;
    private double[] b;
    private double[] c;
    private double[] d;
    private double[] x;
    private double[] y;
    private int n;

    public CubicSpline(double[] x, double[] y){
        this.x = Arrays.copyOf(x, x.length);
        this.y = Arrays.copyOf(y, y.length);
        this.n = x.length;

        this.a = new double[n];
        this.b = new double[n - 1];
        this.c = new double[n];
        this.d = new double[n - 1];

        calculateSplineCoefficients();
    }

    private void calculateSplineCoefficients(){
        double[] h = new double[n - 1];
        for (int i = 0; i < n - 1; i++) {
            h[i] = x[i + 1] - x[i];

        }

        System.arraycopy(y, 0, a, 0, n);

        if (n == 2) {
            c[0] = 0;
            c[1] = 0;
        } else {
            double[] C = new double[n - 2]; // C[i] как в методичке
            double[] A = new double[n - 2]; // A[i] как в методичке
            double[] B = new double[n - 2]; // B[i] как в методичке
            double[] D = new double[n - 2]; // D[i] как в методичке

            for (int i = 0; i < n - 2; i++) {
                C[i] = h[i];
                A[i] = 2 * (h[i] + h[i + 1]);
                B[i] = h[i + 1];

                D[i] = 3 * (
                        (a[i + 2] - a[i + 1]) / h[i + 1] -
                                (a[i + 1] - a[i]) / h[i]
                );
            }

            solveDiagonalSystem(C, A, B, D, 1, n - 2);
        }

        c[0] = 0;
        c[n - 1] = 0;

        for (int i = 0; i < n - 1; i++) {
            d[i] = (c[i + 1] - c[i]) / (3.0 * h[i]);
            b[i] = (a[i + 1] - a[i]) / h[i] - h[i] * (c[i + 1] + 2.0 * c[i]) / 3.0;
        }
    }

    private void solveDiagonalSystem(double[] C, double[] A, double[] B, double[] D,
                                     int startIdx, int endIdx) {
        int nSystem = endIdx - startIdx + 1;
        double[] v = new double[nSystem];
        double[] u = new double[nSystem];

        int systemIndex = 0;
        int actualIndex;

        v[systemIndex] = -B[systemIndex] / A[systemIndex];
        u[systemIndex] = D[systemIndex] / A[systemIndex];

        for (int i = 1; i < nSystem; i++) {
            systemIndex = i;

            double denominator = A[systemIndex] + C[systemIndex] * v[systemIndex - 1];
            v[systemIndex] = -B[systemIndex] / denominator;
            u[systemIndex] = (D[systemIndex] - C[systemIndex] * u[systemIndex - 1]) / denominator;
        }

        systemIndex = nSystem - 1;
        actualIndex = startIdx + systemIndex;
        c[actualIndex] = u[systemIndex];

        for (int i = nSystem - 2; i >= 0; i--) {
            systemIndex = i;
            actualIndex = startIdx + i;
            c[actualIndex] = v[systemIndex] * c[actualIndex + 1] + u[systemIndex];
        }
    }

    public double point(double xPoint) {
        if (xPoint <= x[0]) {
            return y[0];
        }
        if (xPoint >= x[n - 1]) {
            return y[n - 1];
        }

        int idx = findIndex(xPoint);

        double dx = xPoint - x[idx];
        return a[idx] + b[idx] * dx + c[idx] * dx * dx + d[idx] * dx * dx * dx;
    }

    private int findIndex(double xPoint) {
        int left = 0;
        int right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (x[mid] == xPoint) {
                return mid;
            } else if (x[mid] < xPoint) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return left - 1;
    }
}
