import java.util.HashMap;

public class MathParser {

    private static HashMap<String, Double> var;

    public MathParser() {
        var = new HashMap<>();
        // Объявляем константы
        setVariable("pi",Math.PI);
        setVariable("e",Math.E);
        // Объявляем переменную x для работы с ней, при необходимости
        setVariable("x", 0.0);
    }

    public static void setVariable(String varName, Double varValue) {
        var.put(varName, varValue);
    }
    
    public static void replaceVariable(String key, Double value) {
        var.replace(key, value);
    }

    public Double getVariable(String varName) throws Exception {
        if(!var.containsKey(varName)) {
            throw new Exception("Error:Try get unexists " + "variable '"+varName+"'" );
        }
        return var.get(varName);
    }

    public double Parse(String s) throws Exception {
        if(s.isEmpty()) throw new Exception("Empty expression");
        Result result = plusMinus(s);
        if (!result.rest.isEmpty()) throw new Exception("Error: can't full parse \n "+ "rest: " + result.rest);
        return result.acc;
    }

    private Result plusMinus(String s) throws Exception {

        Result cur = mulDiv(s);
        double acc = cur.acc;

        cur.rest = skipSpaces(cur.rest);

        while(cur.rest.length() > 0){
            if(!(cur.rest.charAt(0) == '+' || cur.rest.charAt(0) == '-')) break;

            char sign = cur.rest.charAt(0);
            String next = cur.rest.substring(1);

            cur = mulDiv(next);
            if(sign == '+')
                acc+=cur.acc;
            else
                acc-=cur.acc;
        }
        return new Result(acc,cur.rest);
    }

    private Result mulDiv(String s) throws Exception{
        Result cur = exponentiation(s);
        double acc = cur.acc;

        cur.rest = skipSpaces(cur.rest);


        while(true){
            if(cur.rest.length() == 0)
                return cur;

            char sign = cur.rest.charAt(0);
            if(sign != '*' && sign != '/' && sign != '%' && sign != '\\')
                return cur;

            String next = cur.rest.substring(1);
            Result right = exponentiation(next);
            switch(sign){
                case '*':
                    acc*=right.acc;
                    break;
                case '/':
                    acc/=right.acc;
                    break;
            }
            cur = new Result(acc,right.rest);
        }
    }


    private Result exponentiation(String s) throws Exception{
        Result cur = bracket(s);
        double acc = cur.acc;

        cur.rest = skipSpaces(cur.rest);

        while(true){

            if(cur.rest.length() == 0) return cur;
            if(cur.rest.charAt(0) !='^') break;

            String next = cur.rest.substring(1);
            cur = bracket(next);
            cur.acc = Math.pow(acc,cur.acc);
        }
        return cur;
    }


    private Result bracket(String s) throws Exception{

        s = skipSpaces(s);
        char zeroChar = s.charAt(0);
        if (zeroChar == '(') {
            Result r = plusMinus(s.substring(1));
            if (!r.rest.isEmpty()) {
                r.rest = r.rest.substring(1);
            } else {
                throw new Exception("Expected closing bracket");
            }
            return r;
        }
        return functionVariable(s);
    }

    private Result functionVariable(String s) throws Exception{
        String f = "";
        int i = 0;
        // ищем название функции или переменной
        // имя обязательно должна начинаться с буквы
        while (i < s.length() && (Character.isLetter(s.charAt(i)) ||
                ( Character.isDigit(s.charAt(i)) && i > 0 ) )) {
            f += s.charAt(i);
            i++;
        }
        if (!f.isEmpty()) { // если что-нибудь нашли
            if ( s.length() > i && s.charAt( i ) == '(') {
                // и следующий символ скобка значит - это функция
                Result r = plusMinus(s.substring(f.length()+1));

                if(!r.rest.isEmpty() && r.rest.charAt(0) == ','){
                    // если функция с двумя параметрами
                    double acc = r.acc;
                    Result r2 = plusMinus(r.rest.substring(1));

                    r2 = closeBracket(r2);
                    return processFunction(f, acc,r2);

                } else {
                    r = closeBracket(r);
                    return processFunction(f, r);
                }
            } else { // иначе - это переменная
                return new Result(getVariable(f), s.substring(f.length()));
            }
        }
        return num(s);
    }
    private Result closeBracket(Result r) throws Exception{
        if(!r.rest.isEmpty() && r.rest.charAt(0) ==')'){
            r.rest = r.rest.substring(1);
        } else
            throw new Exception("Expected closing bracket");
        return r;
    }

    private Result num(String s) throws Exception{
        int i = 0;
        int dot_cnt = 0;
        boolean negative = false;
        // число также может начинаться с минуса
        if( s.charAt(0) == '-' ){
            negative = true;
            s = s.substring( 1 );
        }
        // разрешаем только цифры и точку
        while (i < s.length() &&
                (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
            // но также проверяем, что в числе может быть только одна точка!
            if (s.charAt(i) == '.' && ++dot_cnt > 1) {
                throw new Exception("not valid number '"
                        + s.substring(0, i + 1) + "'");
            }
            i++;
        }
        if( i == 0 ){ // что-либо похожее на число мы не нашли
            throw new Exception("can't get valid number in '" + s + "'" );
        }

        double dPart = Double.parseDouble(s.substring(0, i));
        if(negative) dPart = -dPart;
        String restPart = s.substring(i);

        return new Result(dPart, restPart);
    }

    private Result processFunction(String func, Result r) throws Exception{
        switch (func) {
            case "sin":
                return new Result(Math.sin(r.acc), r.rest);
            case "sinh": // гиперболический синус
                return new Result(Math.sinh(r.acc), r.rest);
            case "cos":
                return new Result(Math.cos(r.acc), r.rest);
            case "cosh": // гиперболический косинус
                return new Result(Math.cosh(r.acc), r.rest);
            case "tan":
                return new Result(Math.tan(r.acc), r.rest);
            case "tanh": // гиперболический тангенс
                return new Result(Math.tanh(r.acc), r.rest);
            case "ctg":
                return new Result(1/Math.tan(r.acc), r.rest);
            case "sec": // секанс
                return new Result(1/Math.cos(r.acc), r.rest);
            case "cosec": // косеканс
                return new Result(1/Math.sin(r.acc), r.rest);
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
    private Result processFunction(String func,
                                   double acc,
                                   Result r) throws Exception{
        switch(func){
            case "log": // логарифм x по основанию y
                return new Result(Math.log(acc)/Math.log(r.acc),
                        r.rest);
            case "xor": // исключающее или
                return new Result((int)acc ^ (int)r.acc,r.rest);
            default:
                throw new Exception("function '" + func +
                        "' is not defined");
        }
    }

    private String skipSpaces(String s){
        return s.trim();
    }


    private class Result {
        public double acc; // Аккумулятор
        public String rest; // остаток строки, которую мы еще не обработали
        public Result(double v, String r) {
            this.acc = v;
            this.rest = r;
        }
    }
}
