import java.util.Scanner;

public class InputManager {
    private String formula;
    private MathParser parser;
    Scanner scanner;

    /**
     * считвание формулы из консоли в виде строки
     */
    public void inputFormula() {
         this.scanner = new Scanner(System.in);
        this.parser = new MathParser();
        System.out.print("Введите подинтегральное выражение: ");
        formula = scanner.nextLine();
    }

    public String getFormula() {
        return formula;
    }

    /**
     * a нижний предел
     * b верхний предел
     * n количество отрезков
     * @return int[]
     */
    public double[] inputParameters() {
        try {
            System.out.print("Введите нижний предел a: ");
            double a = parser.Parse(scanner.nextLine());
            System.out.print("Введите верхний предел b: ");
            double b = parser.Parse(scanner.nextLine());
            System.out.print("Введите количество отрезков: ");
            int n = scanner.nextInt();
            return new double[]{a, b, n};
        } catch (Exception e) {
            System.out.println("Error, invalid input data");
            return null;
        }
    }
}
