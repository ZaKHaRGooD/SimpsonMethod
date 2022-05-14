public class SimpsonMethod {
    /**
     * высчитывает определнный интеграл
     * --------------------------------
     * @param a нижний предел
     * @param b верхний предел
     * @param n количество отрезков
     * @return double
     */
    public double simpson(int a, int b, int n) {
        double h = (b - a) / (double) n;
        double x = a;
        double s = function(x) - function(b);
        double result = 0;
        for (int k = 1; k <= n; k++) {
            x = x + h / 2;
            s = s + 4 * function(x);
            x = x + h / 2;
            s = s + 2 * function(x);
            result = (h / 6) * s;
        }
        return result;
    }

    /**
     * высчитывает значение функции в определнной точке
     * значение передаются
     * @param x
     * @return double
     */
    private double function(double x) {
        return x / (4 + x * x);
    }
}
