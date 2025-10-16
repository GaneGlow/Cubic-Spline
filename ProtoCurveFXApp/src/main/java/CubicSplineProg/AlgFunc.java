package CubicSplineProg;

import java.util.Arrays;
import java.util.HashMap;

public class AlgFunc {
    private static final HashMap<String, Double> unique = new HashMap<>();

    public static double detCountStr(double[][] arr) throws Exception{

        if (!isSquare(arr)){
            throw new Exception("Matrix is not square! Try another matrix!");
        }

        String k = Arrays.deepToString(arr);
        if (unique.containsKey(k)) {
            return unique.get(k);
        }

        if (arr.length == 1) {
            return arr[0][0];
        }

        if (arr.length == 0) {
            throw new Exception("There is no matrix!");
        }

        if (arr.length == 2){
            double count = arr[0][0] * arr[1][1] - arr[1][0] * arr[0][1];
            unique.put(k, count);
            return count;
        }
        double summa = 0;
        for (int i = 0; i < arr.length; i++) {
            summa += arr[0][i] * Math.pow(-1, 2 + i) * detCountStr(newArr(arr, i));
        }
        unique.put(k, summa);
        return summa;
    }

    private static boolean isSquare(double[][] arr) {
        int len = arr.length;
        boolean tr = true;
        for (int i = 0; i < arr.length; i++) {
            if (len != arr[i].length) {
                tr = false;
            }
            if (!tr) {
                break;
            }
        }
        return tr;
    }

    private static double[][] newArr (double[][] arr, int c){
        double[][] nArr = new double[arr[0].length - 1][arr.length - 1];
        int help = 0;

        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (j != c) {
                    nArr[i - 1][help] = arr[i][j];
                    help ++;
                }
            }
            help = 0;
        }
        return nArr;
    }

    public static double[][] reverse (double[][] arr) throws Exception {
        if (!isSquare(arr)) {
            throw new Exception("Matrix must be square!");
        }

        double[][] reverseArr = new double[arr.length][arr.length];

        if (detCountStr(arr) == 0) {
            throw new Exception("Inverse matrix doesn't exist!");
        }
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr.length; j++){
                double[][] newArr = newArr2(arr, i, j);
                reverseArr[i][j] = Math.pow(-1, i + j) * detCountStr(newArr) / detCountStr(arr);
            }
        }

        return transpos(reverseArr);
    }

    public static double[][] transpos(double[][] arr){
        double[][] newArr= new double[arr[0].length][arr.length];

        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[0].length; j++){
                newArr[j][i] = arr[i][j];
            }
        }

        return newArr;
    }

    public static double[][] newArr2 (double[][] arr, int r, int c){
        double[][] newArr = new double[arr.length - 1][arr.length - 1];
        int help1 = 0;

        for (int i = 0; i < arr.length; i++) {
            if (i != r) {
                int help2 = 0;

                for (int j = 0; j < arr.length; j++) {
                    if (j != c) {
                        newArr[help1][help2] = arr[i][j];
                        help2++;
                    }
                }
                help1++;
            }
        }
        return newArr;
    }

    public static double[][] multiplication (double[][] arr1, double[][] arr2) throws Exception {
        if (arr1[0].length != arr2.length) {
            throw new Exception("The matrices cannot be multiplied 'cause the sizes aren't compatible!");
        }

        double[][] result = new double[arr1.length][arr2[0].length];

        for (int i = 0; i < arr1.length; i++){
            for (int j = 0; j < arr2[0].length; j++) {
                for (int k = 0; k < arr1[0].length; k++) {
                    result[i][j] += arr1[i][k] * arr2[k][j];
                }
            }
        }

        return  result;

    }
}
