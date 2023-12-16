package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class Day16 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day16.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) {
        int[][] grid = reader.lines().map(String::chars).map(IntStream::toArray).toArray(int[][]::new);
        System.out.println(findEnergized(grid, new Step(0, 0, Dir.RIGHT)));
    }
    
    void part2(BufferedReader reader) {
        int[][] grid = reader.lines().map(String::chars).map(IntStream::toArray).toArray(int[][]::new);
        int max = 0;
        for (int y = 0; y < grid.length; y++) {
            max = Math.max(max, findEnergized(grid, new Step(y, 0, Dir.RIGHT)));
            max = Math.max(max, findEnergized(grid, new Step(y, grid[0].length-1, Dir.LEFT)));
        }
        for (int x = 0; x < grid[0].length; x++) {
            max = Math.max(max, findEnergized(grid, new Step(0, x, Dir.DOWN)));
            max = Math.max(max, findEnergized(grid, new Step(grid.length-1, x, Dir.UP)));
        }
        System.out.println(max);
    }
    
    int findEnergized(int[][] grid, Step start) {
        BitSet energized = new BitSet(grid.length * grid[0].length);
        Set<Step> seen = new HashSet<>();
        Deque<Step> queue = new ArrayDeque<>();
        queue.add(start);
        
        while (!queue.isEmpty()) {
            Step step = queue.pop();
            if (step.y < 0 || step.y == grid.length || step.x < 0 || step.x == grid[0].length || !seen.add(step)) {
                continue;
            }
            energized.set(step.y * grid.length + step.x);
            switch (step.dir) {
                case UP -> {
                    switch (grid[step.y][step.x]) {
                        case '-'      -> { stepLeft(queue, step); stepRight(queue, step); }
                        case '\\'     -> stepLeft(queue, step);
                        case '/'      -> stepRight(queue, step);
                        case '|', '.' -> stepUp(queue, step);
                    }
                }
                case DOWN -> {
                    switch (grid[step.y][step.x]) {
                        case '-'      -> { stepLeft(queue, step); stepRight(queue, step); }
                        case '\\'     -> stepRight(queue, step);
                        case '/'      -> stepLeft(queue, step);
                        case '|', '.' -> stepDown(queue, step);
                    }
                }
                case LEFT -> {
                    switch (grid[step.y][step.x]) {
                        case '-', '.' -> stepLeft(queue, step);
                        case '\\'     -> stepUp(queue, step);
                        case '/'      -> stepDown(queue, step);
                        case '|'      -> { stepUp(queue, step); stepDown(queue, step); }
                    }
                }
                case RIGHT -> {
                    switch (grid[step.y][step.x]) {
                        case '-', '.' -> stepRight(queue, step);
                        case '\\'     -> stepDown(queue, step);
                        case '/'      -> stepUp(queue, step);
                        case '|'      -> { stepUp(queue, step); stepDown(queue, step); }
                    }
                }
            }
        }
        
        return energized.cardinality();
    }
    
    void stepUp(Deque<Step> queue, Step curr) { queue.add(new Step(curr.y-1, curr.x, Dir.UP)); }
    void stepDown(Deque<Step> queue, Step curr) { queue.add(new Step(curr.y+1, curr.x, Dir.DOWN)); }
    void stepLeft(Deque<Step> queue, Step curr) { queue.add(new Step(curr.y, curr.x-1, Dir.LEFT)); }
    void stepRight(Deque<Step> queue, Step curr) { queue.add(new Step(curr.y, curr.x+1, Dir.RIGHT)); }
    
    enum Dir { UP, DOWN, LEFT, RIGHT }
    record Step(int y, int x, Dir dir) {}
}
