import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main {
    private String defaultString; //параметр
    private String[] defaultStringArr; //массив из параметров
    private int defaultArrLength; // длина массива параметров
    private int[] foundArr; //индексы строк, подходящих под стравнение
    private ArrayList<String> inputArr = new ArrayList<>(); //вводимые для сравнения строки

    public static void main(String[] args) {
        Main main = new Main();
        main.readConsole();
        main.search();
        main.printLines();
    }

    private void readConsole(){
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
            System.out.println("Введите параметр программы:");
            defaultString = bufferedReader.readLine();

            System.out.println("Введите строки:");
            String tmpString;
            while (true) {
                tmpString = bufferedReader.readLine();
                if (tmpString.isEmpty()) break;
                inputArr.add(tmpString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void search(){
        defaultStringArr = defaultString.split("\\s+");
        defaultArrLength = defaultStringArr.length;
        foundArr = new int[inputArr.size()];

        if (defaultArrLength > 1) {
            findByEquals();
        } else if (defaultArrLength == 1 && defaultStringArr[0].length() != 0) {
            if (maybeReg(defaultStringArr[0]))
                findByReg();
            else
                findByEquals();
        } else System.out.println("Нет параметров для сравнения");
    }

    private void findByEquals() { // поиск совпадений в случае обычного параметра
        for (int i = 0; i < defaultArrLength; i++) {
            String currentStr = defaultStringArr[i];

            for (int j = 0; j < inputArr.size(); j++) {
                String[] currentInputArr = inputArr.get(j).split("\\s+|;");

                for (int k = 0; k < currentInputArr.length; k++) {
                    if (currentInputArr[k].equalsIgnoreCase(currentStr)) foundArr[j] = 1;
                }
            }
        }
    }

    private void findByReg() { // поиск совпадений в случае регулярного выражения
        try {
            Pattern pattern = Pattern.compile(defaultStringArr[0]);
            Matcher matcher;
            for (int i = 0; i < inputArr.size(); i++) {
                String[] currentInputArr = inputArr.get(i).split("\\s+|;");

                for (int j = 0; j < currentInputArr.length; j++) {
                    matcher = pattern.matcher(currentInputArr[j]);
                    if (matcher.find()) foundArr[i] = 1;
                }
            }
        } catch (PatternSyntaxException e) {// если регулярное выражение не проход
            findByEquals();
        }

    }

    private boolean maybeReg(String verifiable){ //попытка проверить параметр на регулярное выражение
        Pattern pattern = Pattern.compile("([A-Za-z0-9;]+)");//
        Matcher matcher = pattern.matcher(verifiable);
        return !matcher.matches();
    }

    private void printLines() {
        for (int i = 0; i < foundArr.length; i++) {
            if (foundArr[i] == 1) System.out.println(inputArr.get(i));
        }
    }
}