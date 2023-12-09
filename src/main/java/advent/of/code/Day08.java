package advent.of.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Day08 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day08.txt")))) {
            part1(reader);
        }
    }
    
    void part1(BufferedReader reader) throws IOException {
        var directions = reader.readLine();
        reader.readLine(); // Skip blank line
        var map = parseMap(reader);
        
        int step = 0;
        String curr = "AAA";
        while (!curr.equals("ZZZ")) {
            int i = step % directions.length();
            int dir = directions.charAt(i) == 'L' ? 0 : 1;
            curr = map.get(curr)[dir];
            step++;
        }
        
        System.out.println(step);
    }
    
    void part2(BufferedReader reader) throws IOException {
        var directions = reader.readLine();
        reader.readLine(); // Skip blank line
        var map = parseMap(reader);
        
        // Find number of steps for each starting node, individually.
        // The trips are periodic - they return to the ending node after the same number of steps.
        // The number of steps to reach the ending node of all trips simultaneously is the Least Common Multiple of
        // the number of steps to reach the ending node of each trip individually.
        long lcm = map.keySet().stream()
            .filter(node -> node.endsWith("A"))
            .mapToLong(node -> {
                int step = 0;
                while (!node.endsWith("Z")) {
                    int i = step % directions.length();
                    int dir = directions.charAt(i) == 'L' ? 0 : 1;
                    node = map.get(node)[dir];
                    step++;
                }
                return step;
            })
            .reduce(1L, (a, b) -> (a * b) / gcd(a, b)); // LCM(a,b) = a*b/GCD(a,b)
        
        System.out.println(lcm);
    }
    
    Map<String, String[]> parseMap(BufferedReader reader) throws IOException {
        Map<String, String[]> map = new HashMap<>();
        for (String s; (s = reader.readLine()) != null; ) {
            String[] parts = s.split(" = ");
            String[] dstParts = parts[1].substring(1, parts[1].length()-1).split(", ");
            map.put(parts[0], dstParts);
        }
        return map;
    }
    
    long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
}
