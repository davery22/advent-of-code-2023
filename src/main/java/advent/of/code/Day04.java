package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Day04 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day04.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) {
        var sum = reader.lines()
            .mapToInt(card -> {
                String[] numbers = card.split(": +")[1].split(" +\\| +");
                Set<Integer> winningNumbers = Arrays.stream(numbers[0].split(" +"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
                
                int points = 1;
                for (String number : numbers[1].split(" +")) {
                    int num = Integer.parseInt(number);
                    if (winningNumbers.contains(num)) {
                        points <<= 1;
                    }
                }
                
                return points >> 1;
            })
            .sum();
        
        System.out.println(sum);
    }
    
    void part2(BufferedReader reader) {
        String[] cards = reader.lines().toArray(String[]::new);
        int[] counts = new int[cards.length];
        Arrays.fill(counts, 1);
        
        for (int i = 0; i < cards.length; i++) {
            String card = cards[i];
            int count = counts[i];
            
            String[] numbers = card.split(": +")[1].split(" +\\| +");
            Set<Integer> winningNumbers = Arrays.stream(numbers[0].split(" +"))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
            
            long wins = Arrays.stream(numbers[1].split(" +"))
                .mapToInt(Integer::parseInt)
                .filter(winningNumbers::contains)
                .count();
            
            for (int j = i+1; j < i+1+wins && j < cards.length; j++) {
                counts[j] += count;
            }
        }
        
        var sum = Arrays.stream(counts).sum();
        System.out.println(sum);
    }
}
