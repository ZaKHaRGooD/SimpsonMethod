public class Main {
    public static void main(String[] args) {
        MathParser mathParser = new MathParser();
        mathParser.printOperations();
        InputManager inputManager = new InputManager();
        inputManager.inputFormula();
        String mathForm = inputManager.getFormula();
        double[] parameters = inputManager.inputParameters();
        SimpsonMethod simpsonMethod = new SimpsonMethod(mathForm, parameters[0], parameters[1], parameters[2]);
        System.out.print("Результат: ");
        System.out.println(simpsonMethod.simpson());
    }
}
