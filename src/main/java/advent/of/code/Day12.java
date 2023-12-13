package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 {
    void main() throws Exception {
        int part1Times = 1;
        int part2Times = 5;
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day12.txt")))) {
            var sum = reader.lines()
                .mapToLong(line -> getNumberOfArrangements(line, part2Times))
                .parallel()
                .sum();
            
            System.out.println(sum);
        }
    }
    
    long getNumberOfArrangements(String line, int times) {
        String[] parts = line.split(" ");
        String sprs = IntStream.range(0, times).mapToObj(_ -> parts[0]).collect(Collectors.joining("?"));
        String nums = IntStream.range(0, times).mapToObj(_ -> parts[1]).collect(Collectors.joining(","));
        int[] springs = sprs.chars().toArray();
        int[] damagedGroups = Arrays.stream(nums.split(",")).mapToInt(Integer::parseInt).toArray();
        return rec(springs, 0, 0, 0, damagedGroups, new HashMap<>());
    }
    
    record Key(int index, int run, int group) { }
    
    long rec(int[] springs, int index, int run, int group, int[] damagedGroups, Map<Key, Long> memo) {
        Key key = new Key(index, run, group);
        Long val = memo.get(key);
        
        if (val != null) {
            return val;
        }
        
        if (index == springs.length) {
            if (group != damagedGroups.length || (run > 0 && damagedGroups[group-1] != run)) {
                memo.put(key, val = 0L);
                return val;
            }
            memo.put(key, val = 1L);
            return val;
        }
        
        if (springs[index] == '#') {
            memo.put(key, val = recHash(springs, index, run, group, damagedGroups, memo));
            return val;
        } else if (springs[index] == '.') {
            memo.put(key, val = recDot(springs, index, run, group, damagedGroups, memo));
            return val;
        } else { // '?'
            memo.put(key, val =
                  recHash(springs, index, run, group, damagedGroups, memo)
                + recDot(springs, index, run, group, damagedGroups, memo));
            return val;
        }
    }
    
    long recHash(int[] springs, int index, int run, int group, int[] damagedGroups, Map<Key, Long> memo) {
        // About to start a new group when there are no more groups?
        if (run == 0 && group >= damagedGroups.length) {
            return 0;
        }
        return rec(springs, index+1, run+1, run == 0 ? group+1 : group, damagedGroups, memo);
    }
    
    long recDot(int[] springs, int index, int run, int group, int[] damagedGroups, Map<Key, Long> memo) {
        // About to end current group
        if (run > 0 && damagedGroups[group-1] != run) {
            return 0;
        }
        return rec(springs, index+1, 0, group, damagedGroups, memo);
    }
}
