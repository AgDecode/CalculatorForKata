import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

class Calculator {
    public static String calc(String input) {
        String[] parts = input.split(" ");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid input format");
        }

        String first_num = parts[0];
        String operator = parts[1];
        String second_num = parts[2];

        boolean isRomanA = isRoman(first_num);
        boolean isRomanB = isRoman(second_num);

        if (isRomanA != isRomanB) {
            throw new IllegalArgumentException("Cannot mix Roman and Arabic numerals");
        }

        int a = parseNumber(first_num, isRomanA);
        int b = parseNumber(second_num, isRomanB);

        int result = switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) {
                    throw new IllegalArgumentException("Cannot divide by zero");
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Invalid operator");
        };

        return isRomanA ? toRoman(result) : String.valueOf(result);
    }

    private static boolean isRoman(String input) {
        return input.matches("^(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");
    }

    private static int parseNumber(String input, boolean isRoman) {
        if (isRoman) {
            return fromRoman(input);
        } else {
            int num = Integer.parseInt(input);
            if (num < 1 || num > 10) {
                throw new IllegalArgumentException("Numbers must be between 1 and 10 inclusive");
            }
            return num;
        }
    }

    private static String toRoman(int num) {
        if (num < 1 || num > 3999) {
            throw new IllegalArgumentException("Number out of Roman numeral range");
        }

        String[] romanNumerals = {
                "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
        };

        int[] values = {
                1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1
        };

        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                roman.append(romanNumerals[i]);
                num -= values[i];
            }
        }
        return roman.toString();
    }

    private static int fromRoman(String roman) {
        Map<Character, Integer> romanValues = new HashMap<>();
        romanValues.put('I', 1);
        romanValues.put('V', 5);
        romanValues.put('X', 10);
        romanValues.put('L', 50);
        romanValues.put('C', 100);
        romanValues.put('D', 500);
        romanValues.put('M', 1000);

        int result = 0;
        int prevValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int value = romanValues.get(roman.charAt(i));

            if (value < prevValue) {
                result -= value;
            } else {
                result += value;
            }

            prevValue = value;
        }

        return result;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Input: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit()")) {
                break;
            }

            try {
                String output = Calculator.calc(input);
                System.out.println("Output: " + output);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
