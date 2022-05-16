import java.util.Scanner;

public class InputManager {
    private String formula;
    
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
        System.out.print("Введите a: ");
        int a = scanner.nextInt();
        System.out.print("Введите b: ");
        int b = scanner.nextInt();
        System.out.print("Введите количество отрезков: ");
        int n = scanner.nextInt();
        return new int[]{a, b, n};
    }
}
