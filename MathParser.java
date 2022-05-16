import java.util.HashMap;

public class MathParser {

    private static HashMap<String, Double> var;
    private static HashMap<String, String> operations;

    public MathParser() {
        var = new HashMap<>();
        // Объявляем константы
        setVariable("pi", Math.PI);
        setVariable("e", Math.E);
        // Объявляем переменную x для работы с ней, при необходимости
        setVariable("x", 0.0);
        operations = new HashMap<>();
        operations.put("+", "сложение");
        operations.put("-", "вычитание");
        operations.put("*", "умножение");
        operations.put("/", "деление");
        operations.put("^", "возведение в степень");
        operations.put("(", "открывающася скобка");
        operations.put(")", "закрывающаяся скобка");
        operations.put("sin", "синус");
        operations.put("sinh", "гиперболический синус");
        operations.put("cos", "косинус");
        operations.put("cosh", "гиперболический косинус");
        operations.put("tan", "тангенс");
        operations.put("tanh", "гиперболический тангенс");
        operations.put("ctg", "котангенс");
        operations.put("sec", "секанс");
        operations.put("cosec", "косеканс");
        operations.put("abs", "модуль");
        operations.put("ln", "натуральный логарифм");
        operations.put("lg", "десятичный логарифм");
        operations.put("log", "логарифм х по основанию y");
        operations.put("sqrt", "квадратный корень");
    }

    /**
     * Установка собственных переменных, переменная должна начинаться только с буквы
     * @param varName название переменной
     * @param varValue значние переменной
     */
    public static void setVariable(String varName, Double varValue) {
        var.put(varName, varValue);
    }

    /**
     * получить значение переменной
     * @param varName имя переменной
     * @return double
     * @throws Exception нет переменной в списке переменных
     */
    public Double getVariable(String varName) throws Exception {
        if (!var.containsKey(varName)) {
            throw new Exception("Error:Try get unexists variable '" + varName + "'");
        }
        return var.get(varName);
    }

    /**
     * замена переменной на её значение
     * @param key название переменной
     * @param value значение перменной
     */
    public static void replaceVariable(String key, Double value) {
        var.replace(key, value);
    }

    /**
     * парсинг формулы из консоли методом рекурсивного спуска
     * @param s строка с форумулой, которую пользователь вводит с консоли
     * @return double
     * @throws Exception
     */
    public double Parse(String s) throws Exception {
        if (s.isEmpty()) throw new Exception("Empty expression");
        Result result = plusMinus(s);
        if (!result.rest.isEmpty()) throw new Exception("Error: can't full parse \n " + "rest: " + result.rest);
        return result.acc;
    }

    /**
     * выполнение операций сложения и вычитания в формуле
     * @param s строка с оставшейся формулой
     * @return Result
     * @throws Exception
     */
    private Result plusMinus(String s) throws Exception {

        Result cur = mulDiv(s);
        double acc = cur.acc;

        cur.rest = skipSpaces(cur.rest);

        while (cur.rest.length() > 0) {
            if (!(cur.rest.charAt(0) == '+' || cur.rest.charAt(0) == '-')) break;

            char sign = cur.rest.charAt(0);
            String next = cur.rest.substring(1);

            cur = mulDiv(next);
            if (sign == '+')
                acc += cur.acc;
            else
                acc -= cur.acc;
        }
        return new Result(acc, cur.rest);
    }

    /**
     * выполнение операций умножения и деления в формуле
     * @param s строка с оставшейся формулой
     * @return Result
     * @throws Exception
     */
    private Result mulDiv(String s) throws Exception {
        // cur - объект класса Result с текущим значением переменных acc и rest
        Result cur = exponentiation(s);
        double acc = cur.acc;

        cur.rest = skipSpaces(cur.rest);


        while (true) {
            if (cur.rest.length() == 0)
                return cur;

            char sign = cur.rest.charAt(0);
            if (sign != '*' && sign != '/' && sign != '%' && sign != '\\')
                return cur;

            String next = cur.rest.substring(1);
            Result right = exponentiation(next);
            switch (sign) {
                case '*':
                    acc *= right.acc;
                    break;
                case '/':
                    acc /= right.acc;
                    break;
            }
            cur = new Result(acc, right.rest);
        }
    }

    /**
     * выполение операции возведения в степень в формуле
     * @param s строка с оставшейся формулой
     * @return Result
     * @throws Exception
     */
    private Result exponentiation(String s) throws Exception {
        // cur - объект класса Result с текущим значением переменных acc и rest
        Result cur = bracket(s);
        double acc = cur.acc;

        cur.rest = skipSpaces(cur.rest);

        while (true) {

            if (cur.rest.length() == 0) return cur;
            if (cur.rest.charAt(0) != '^') break;

            String next = cur.rest.substring(1);
            cur = bracket(next);
            cur.acc = Math.pow(acc, cur.acc);
        }
        return cur;
    }

    /**
     * выполнения мат.операций с приоритетностью скобок
     * @param s строка с оставшейся формулой
     * @return Result
     * @throws Exception
     */
    private Result bracket(String s) throws Exception {

        s = skipSpaces(s);
        char zeroChar = s.charAt(0);
        if (zeroChar == '(') {
            Result result = plusMinus(s.substring(1));
            if (!result.rest.isEmpty()) {
                result.rest = result.rest.substring(1);
            } else {
                throw new Exception("Expected closing bracket");
            }
            return result;
        }
        return functionVariable(s);
    }

    /**
     * поиск констант и переменных в формуле, переменная должна начинаться только с буквы
     * @param s строка с оставшейся формулой
     * @return Result
     * @throws Exception
     */
    private Result functionVariable(String s) throws Exception {
        StringBuilder f = new StringBuilder();
        int i = 0;
        while (i < s.length() && (Character.isLetter(s.charAt(i)) ||
                (Character.isDigit(s.charAt(i)) && i > 0))) {
            f.append(s.charAt(i));
            i++;
        }
        if (f.length() > 0) {
            // если скобка следующий символ после набора букв, то это функция
            if (s.length() > i && s.charAt(i) == '(') {
                Result result = plusMinus(s.substring(f.length() + 1));
                // функция с двумя параметрами
                if (!result.rest.isEmpty() && result.rest.charAt(0) == ',') {
                    double acc = result.acc;
                    Result result2 = plusMinus(result.rest.substring(1));

                    result2 = closeBracket(result2);
                    return processFunction(f.toString(), acc, result2);

                } else {
                    result = closeBracket(result);
                    return processFunction(f.toString(), result);
                }
                // если ничего из вышеперечисленного не выполнилось, то это переменная
            } else {
                return new Result(getVariable(f.toString()), s.substring(f.length()));
            }
        }
        return num(s);
    }

    /**
     * проверка на закрывающиюся сскобку
     * @param result
     * @return Result
     * @throws Exception пропуск закрывающейся строки
     */
    private Result closeBracket(Result result) throws Exception {
        if (!result.rest.isEmpty() && result.rest.charAt(0) == ')') {
            result.rest = result.rest.substring(1);
        } else
            throw new Exception("Expected closing bracket");
        return result;
    }

    /**
     * Инверсия числа, если перед ним минус. Валидация чисел с дробной частью. 
     * @param s строка с оставшейся формулой
     * @return Result
     * @throws Exception
     */
    private Result num(String s) throws Exception {
        int i = 0;
        int dot_cnt = 0;
        boolean negative = false;
        // обработка случая, если число начинается на минус
        if (s.charAt(0) == '-') {
            negative = true;
            s = s.substring(1);
        }
        // валидация формулы, разрешены только точка, как разделитель целой и дробной части, и цифры
        while (i < s.length() &&
                (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
            // валидация точки, количество идущих подряд точек ограничено одной
            if (s.charAt(i) == '.' && ++dot_cnt > 1) {
                throw new Exception("not valid number '" + s.substring(0, i + 1) + "'");
            }
            i++;
        }
        // выкидывает исключение, если не прошли валидацию, то есть не нашли ничего похожего на число
        if (i == 0) {
            throw new Exception("can't get valid number in '" + s + "'");
        }

        // инверсия числа, если флаг negative будет true
        double dPart = Double.parseDouble(s.substring(0, i));
        if (negative) dPart = -dPart;
        String restPart = s.substring(i);

        return new Result(dPart, restPart);
    }

    /**
     * обработка некоторго набора функции, например тригонометрических и других
     * @param func навзвание функции
     * @param r класс результата, в котором храниться текущие значеник формулы и остаток формулы
     * @return result
     * @throws Exception возникает в случае ненахождения функции
     */
    private Result processFunction(String func, Result r) throws Exception {
        switch (func) {
            case "sin": // синус
                return new Result(Math.sin(r.acc), r.rest);
            case "sinh": // гиперболический синус
                return new Result(Math.sinh(r.acc), r.rest);
            case "cos": // косинус
                return new Result(Math.cos(r.acc), r.rest);
            case "cosh": // гиперболический косинус
                return new Result(Math.cosh(r.acc), r.rest);
            case "tan": // тангенс
                return new Result(Math.tan(r.acc), r.rest);
            case "tanh": // гиперболический тангенс
                return new Result(Math.tanh(r.acc), r.rest);
            case "ctg": // котангенс
                return new Result(1 / Math.tan(r.acc), r.rest);
            case "sec": // секанс
                return new Result(1 / Math.cos(r.acc), r.rest);
            case "cosec": // косеканс
                return new Result(1 / Math.sin(r.acc), r.rest);
            case "abs": // модуль
                return new Result(Math.abs(r.acc), r.rest);
            case "ln": // натуральный логарифм
                return new Result(Math.log(r.acc), r.rest);
            case "lg": // десятичный логарифм
                return new Result(Math.log10(r.acc), r.rest);
            case "sqrt": // квадратный корень
                return new Result(Math.sqrt(r.acc), r.rest);
            default:
                throw new Exception("function '" + func + "' is not defined");
        }
    }

    /**
     * вычисление логарифма
     * @param func функция логарифма
     * @param acc текущие числовое значение части формулы
     * @param result остаток формулы строкой
     * @return Result
     * @throws Exception
     */
    private Result processFunction(String func, double acc, Result result) throws Exception {
        switch (func) {
            case "log": // логарифм x по основанию y
                return new Result(Math.log(acc) / Math.log(result.acc), result.rest);
            default:
                throw new Exception("function '" + func + "' is not defined");
        }
    }

    private String skipSpaces(String s) {
        return s.trim();
    }

    /**
     * вывод в консоль списка операций с их описанием
     */
    public void printOperations() {
        for (HashMap.Entry<String, String> operation : operations.entrySet()) {
            System.out.println("'" + operation.getKey() + "' - " + operation.getValue());
        }
    }


    /**
     * класс, в котором хранится результат вычислений введенной функции
     */
    private class Result {
        public double acc; // Аккумулятор
        public String rest; // остаток строки, которую мы еще не обработали

        public Result(double v, String r) {
            this.acc = v;
            this.rest = r;
        }
    }
}
