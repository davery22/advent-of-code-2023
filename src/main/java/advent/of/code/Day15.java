package advent.of.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class Day15 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day15.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) throws IOException {
        var sum = Arrays.stream(reader.readLine().split(","))
            .mapToInt(this::hash)
            .sum();
        System.out.println(sum);
    }
    
    void part2(BufferedReader reader) throws IOException {
        String[] sequence = reader.readLine().split(",");
        var boxes = IntStream.range(0, 256).mapToObj(_ -> new LinkedHashMap<String, Integer>()).toList();
        
        // LinkedHashMap conveniently does the heavy lifting to keep lenses in order
        for (String step : sequence) {
            if (step.endsWith("-")) {
                String label = step.substring(0, step.length()-1);
                boxes.get(hash(label)).remove(label);
            } else {
                String label = step.substring(0, step.length()-2);
                int focalLen = Character.getNumericValue(step.charAt(step.length()-1));
                boxes.get(hash(label)).put(label, focalLen);
            }
        }
        
        var sum = IntStream.range(0, 256)
            .map(i -> {
                int boxSum = 0;
                int j = 0;
                for (var focalLen : boxes.get(i).values()) {
                    boxSum += (1+i) * (1+j) * focalLen;
                    j++;
                }
                return boxSum;
            })
            .sum();
        
        System.out.println(sum);
    }
    
    int hash(String str) {
        return str.chars().reduce(0, (sum, i) -> ((sum + i) * 17) % 256);
    }
}
