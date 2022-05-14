public class Main {
    public static void main(String[] args) {
        InputManager inputManager = new InputManager();
//        inputManager.inputFormula();
        int[] parameters = inputManager.inputParameters();
        SimpsonMethod simpsonMethod = new SimpsonMethod();
        System.out.print("Результат: ");
        System.out.println(simpsonMethod.simpson(parameters[0], parameters[1], parameters[2]));
    }
}
