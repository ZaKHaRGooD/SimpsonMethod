public class SimpsonMethod {
    private final String mathFormula;
    private final double a;
    private final double b;
    private final double n;

    public SimpsonMethod(String mathFormula, double a, double b, double n) {
        this.mathFormula = mathFormula;
        this.a = a; // нижний предел
        this.b = b; // верхний предел
        this.n = n; // количество отрезков
    }
    /**
     * высчитывает определённый интеграл
     * ---------------------------------
     * @return double
     */
    public double simpson() {
        double h = (b - a) / (double) n;
        double x = a;
        double s = function(x) - function(b);
        // проверка на NaN
        if (Double.isNaN(s)) s = 0;
        double result = 0;
        for (int k = 1; k <= n; k++) {
            x = x + h / 2;
            s = s + 4 * function(x);
            System.out.println(s + "  " + x);

            // проверка на NaN
            if (Double.isNaN(s)) s = 0;
            x = x + h / 2;
            s = s + 2 * function(x);
            System.out.println(s + "  " + x);
            // проверка на NaN
            if (Double.isNaN(s)) s = 0;
            result = (h / 6) * s;
        }
        return result;
    }

    /**
     * высчитывает значение функции в определнной точке,
     * значение передаются
     * @param x
     * @return double
     */
    private double function(double x) {
        double result = 0.0;
        MathParser parser = new MathParser();
        MathParser.replaceVariable("x", x);
        try {
            result = parser.Parse(mathFormula);
        }
        catch (Exception e) {
            System.out.println("Неправильно введена формула");
        }
        return result;
    }
}
