import java.util.Scanner;

public class InputManager {
    private String formula;

    /**
     * считвание формулы из консоли в виде строки
     */
    public void inputFormula() {
        Scanner scanner = new Scanner(System.in);
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
    public int[] inputParameters() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите нижний предел a: ");
        int a = scanner.nextInt();
        System.out.print("Введите верхний предел b: ");
        int b = scanner.nextInt();
        System.out.print("Введите количество отрезков: ");
        int n = scanner.nextInt();
        return new int[]{a, b, n};
    }
}
