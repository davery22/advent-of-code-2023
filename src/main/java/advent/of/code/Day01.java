package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.BiFunction;

public class Day01 {
    void main() throws Exception {
        BiFunction<String, Integer, Integer> part1NumberFunc = (s, i) -> digitToNumber(s.charAt(i));
        BiFunction<String, Integer, Integer> part2NumberFunc = (s, i) -> {
            int num = digitToNumber(s.charAt(i));
            return num == -1 ? digitNameToNumber(s, i) : num;
        };
        
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day01.txt")))) {
            var sum = reader.lines()
                .mapToInt(line -> getCalibrationValue(line, part2NumberFunc))
                .sum();
            System.out.println(sum);
        }
    }
    
    int getCalibrationValue(String line, BiFunction<String, Integer, Integer> toNumberFunc) {
        int first = -1;
        int last = -1;
        for (int i = 0; i < line.length() && (first == -1 || last == -1); i++) {
            if (first == -1) {
                first = toNumberFunc.apply(line, i);
            }
            if (last == -1) {
                last = toNumberFunc.apply(line, line.length() - i - 1);
            }
        }
        return first * 10 + last;
    }
    
    int digitToNumber(char ch) {
        return Character.isDigit(ch) ? Character.getNumericValue(ch) : -1;
    }
    
    int digitNameToNumber(String line, int index) {
        if (line.startsWith("zero", index)) return 0;
        if (line.startsWith("one", index)) return 1;
        if (line.startsWith("two", index)) return 2;
        if (line.startsWith("three", index)) return 3;
        if (line.startsWith("four", index)) return 4;
        if (line.startsWith("five", index)) return 5;
        if (line.startsWith("six", index)) return 6;
        if (line.startsWith("seven", index)) return 7;
        if (line.startsWith("eight", index)) return 8;
        if (line.startsWith("nine", index)) return 9;
        return -1;
    }
}