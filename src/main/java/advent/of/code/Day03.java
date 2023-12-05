package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day03 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day03.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) {
        String[] grid = reader.lines().toArray(String[]::new);
        int width = grid[0].length();
        int partNumberSum = 0;
        
        for (int i = 0; i < grid.length; i++) {
            String line = grid[i];
            
            for (int j = 0; j < width; j++) {
                if (Character.isDigit(line.charAt(j))) {
                    int start = j;
                    int partNumber = 0;
                    for (; j < width && Character.isDigit(line.charAt(j)); j++) {
                        partNumber = partNumber * 10 + Character.getNumericValue(line.charAt(j));
                    }
                    if (anySymbolsOnBorder(grid, i, start-1, j)) {
                        partNumberSum += partNumber;
                    }
                }
            }
        }
        
        System.out.println(partNumberSum);
    }
    
    void part2(BufferedReader reader) {
        Map<Star, List<Integer>> numbersByStar = new HashMap<>();
        
        String[] grid = reader.lines().toArray(String[]::new);
        int width = grid[0].length();
        
        for (int i = 0; i < grid.length; i++) {
            String line = grid[i];
            
            for (int j = 0; j < width; j++) {
                if (Character.isDigit(line.charAt(j))) {
                    int start = j;
                    int partNumber = 0;
                    for (; j < width && Character.isDigit(line.charAt(j)); j++) {
                        partNumber = partNumber * 10 + Character.getNumericValue(line.charAt(j));
                    }
                    associateWithStars(grid, numbersByStar, partNumber, i, start-1, j);
                }
            }
        }
        
        var sum = numbersByStar.values().stream()
            .mapToInt(numbers -> numbers.size() == 2 ? numbers.get(0) * numbers.get(1) : 0)
            .sum();
        
        System.out.println(sum);
    }
    
    record Star(int x, int y) {}
    
    boolean anySymbolsOnBorder(String[] grid, int row, int start, int endIncl) {
        int width = grid[0].length();
        
        if (row > 0) {
            for (int j = start; j <= endIncl; j++) {
                if (j > 0 && j < width && isSymbol(grid[row-1].charAt(j))) {
                    return true;
                }
            }
        }
        
        if (row < grid.length-1) {
            for (int j = start; j <= endIncl; j++) {
                if (j > 0 && j < width && isSymbol(grid[row+1].charAt(j))) {
                    return true;
                }
            }
        }
        
        if (start > 0 && isSymbol(grid[row].charAt(start))) {
            return true;
        }
        
        if (endIncl < width && isSymbol(grid[row].charAt(endIncl))) {
            return true;
        }
        
        return false;
    }
    
    boolean isSymbol(char ch) {
        return ch != '.' && !Character.isDigit(ch);
    }
    
    void associateWithStars(String[] grid, Map<Star, List<Integer>> numbersByStar, int partNumber, int row, int start, int endIncl) {
        int width = grid[0].length();
        
        if (row > 0) {
            for (int j = start; j <= endIncl; j++) {
                if (j > 0 && j < width && grid[row-1].charAt(j) == '*') {
                    numbersByStar.computeIfAbsent(new Star(row-1, j), _ -> new ArrayList<>()).add(partNumber);
                }
            }
        }
        
        if (row < grid.length-1) {
            for (int j = start; j <= endIncl; j++) {
                if (j > 0 && j < width && grid[row+1].charAt(j) == '*') {
                    numbersByStar.computeIfAbsent(new Star(row+1, j), _ -> new ArrayList<>()).add(partNumber);
                }
            }
        }
        
        if (start > 0 && grid[row].charAt(start) == '*') {
            numbersByStar.computeIfAbsent(new Star(row, start), _ -> new ArrayList<>()).add(partNumber);
        }
        
        if (endIncl < width && grid[row].charAt(endIncl) == '*') {
            numbersByStar.computeIfAbsent(new Star(row, endIncl), _ -> new ArrayList<>()).add(partNumber);
        }
    }
}
