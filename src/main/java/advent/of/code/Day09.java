package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day09 {
    void main() throws Exception {
        boolean isPart1 = true;
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day09.txt")))) {
            answer(reader, isPart1);
        }
    }
    
    void answer(BufferedReader reader, boolean part1) {
        var sum = reader.lines()
            .mapToLong(line -> {
                long[] curr = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).toArray();
                List<long[]> seq = new ArrayList<>();
                seq.add(curr);
                
                for (boolean done = false; !done && (done = true); ) {
                    long[] next = new long[curr.length-1];
                    for (int i = 1; i < curr.length; i++) {
                        done &= (next[i-1] = curr[i] - curr[i-1]) == 0;
                    }
                    seq.add(curr = next);
                }
                
                long last = 0;
                for (int i = seq.size()-2; i >= 0; i--) {
                    long[] arr = seq.get(i);
                    last = part1 ? (arr[arr.length-1] + last) : (arr[0] - last);
                }
                return last;
            })
            .sum();
        System.out.println(sum);
    }
}
