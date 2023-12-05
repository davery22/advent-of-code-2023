package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.ToIntFunction;

public class Day02 {
    void main() throws Exception {
        ToIntFunction<String> part1 = this::getGameIdIfPossible;
        ToIntFunction<String> part2 = this::getProductOfMinimumPossibleCounts;
        
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day02.txt")))) {
            var sum = reader.lines()
                .mapToInt(part2)
                .sum();
            System.out.println(sum);
        }
    }
    
    int getGameIdIfPossible(String game) {
        // Number of each color that we have
        int red = 12;
        int green = 13;
        int blue = 14;
        
        String[] gameStrings = game.split(": ");
        int gameId = Integer.parseInt(gameStrings[0].split(" ")[1]);
        String[] subsets = gameStrings[1].split("; ");
        
        for (String subset : subsets) {
            String[] colorCounts = subset.split(", ");
            
            for (String colorCount : colorCounts) {
                String[] colorAndCount = colorCount.split(" ");
                int count = Integer.parseInt(colorAndCount[0]);
                String color = colorAndCount[1];
                
                switch (color) {
                    case "red"   -> { if (count > red)   return 0; }
                    case "green" -> { if (count > green) return 0; }
                    case "blue"  -> { if (count > blue)  return 0; }
                }
            }
        }
        
        return gameId; // Possible
    }
    
    int getProductOfMinimumPossibleCounts(String game) {
        // Maximum of each color seen so far
        int red = 0;
        int green = 0;
        int blue = 0;
        
        String[] gameStrings = game.split(": ");
        String[] subsets = gameStrings[1].split("; ");
        
        for (String subset : subsets) {
            String[] colorCounts = subset.split(", ");
            
            for (String colorCount : colorCounts) {
                String[] colorAndCount = colorCount.split(" ");
                int count = Integer.parseInt(colorAndCount[0]);
                String color = colorAndCount[1];
                
                switch (color) {
                    case "red"   -> red = Math.max(red, count);
                    case "green" -> green = Math.max(green, count);
                    case "blue"  -> blue = Math.max(blue, count);
                }
            }
        }
        
        return red * green * blue;
    }
}
