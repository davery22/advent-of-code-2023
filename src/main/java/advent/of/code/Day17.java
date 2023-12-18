package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Day17 {
    void main() throws Exception {
        int part1dMin = 1, part1dMax = 3,
            part2dMin = 4, part2dMax = 10;
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day17.txt")))) {
            answer(reader, part2dMin, part2dMax);
        }
    }
    
    void answer(BufferedReader reader, int dMin, int dMax) {
        int[][] grid = reader.lines()
            .map(s -> s.chars().map(Character::getNumericValue).toArray())
            .toArray(int[][]::new);
        
        int[][] yWeights = new int[grid.length][grid[0].length];
        int[][] xWeights = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            Arrays.fill(yWeights[i], Integer.MAX_VALUE);
            Arrays.fill(xWeights[i], Integer.MAX_VALUE);
        }
        yWeights[0][0] = 0;
        xWeights[0][0] = 0;
        
        PriorityQueue<Step> queue = new PriorityQueue<>(Comparator.comparingInt(Step::weight));
        queue.add(new Step(0, 0, Axis.X, 0, null));
        queue.add(new Step(0, 0, Axis.Y, 0, null));
        
        int best = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            Step curr = queue.poll();
            int y = curr.y, x = curr.x;
            int[][] weights = curr.axis == Axis.Y ? yWeights : xWeights;
            int[][] prevWeights = curr.axis == Axis.Y ? xWeights : yWeights;
            if (curr.weight > Math.min(prevWeights[y][x], best)) {
                continue; // Skip - we already found (and processed) a cheaper way to get here
            }
            if (y == grid.length-1 && x == grid[0].length-1) {
                best = curr.weight;
                continue;
            }
            if (curr.axis == Axis.Y) {
                for (int weight = curr.weight, dy = -1; dy >= -dMax; dy--) {
                    int y2 = y + dy;
                    if (y2 < 0) {
                        break;
                    }
                    weight += grid[y2][x];
                    if (-dy >= dMin && weight < weights[y2][x]) {
                        queue.offer(new Step(y2, x, Axis.X, weights[y2][x] = weight, curr));
                    }
                }
                for (int weight = curr.weight, dy = 1; dy <= dMax; dy++) {
                    int y2 = y + dy;
                    if (y2 >= grid.length) {
                        break;
                    }
                    weight += grid[y2][x];
                    if (dy >= dMin && weight < weights[y2][x]) {
                        queue.offer(new Step(y2, x, Axis.X, weights[y2][x] = weight, curr));
                    }
                }
            } else { // curr.axis == Axis.X
                for (int weight = curr.weight, dx = -1; dx >= -dMax; dx--) {
                    int x2 = x + dx;
                    if (x2 < 0) {
                        break;
                    }
                    weight += grid[y][x2];
                    if (-dx >= dMin && weight < weights[y][x2]) {
                        queue.offer(new Step(y, x2, Axis.Y, weights[y][x2] = weight, curr));
                    }
                }
                for (int weight = curr.weight, dx = 1; dx <= dMax; dx++) {
                    int x2 = x + dx;
                    if (x2 >= grid[0].length) {
                        break;
                    }
                    weight += grid[y][x2];
                    if (dx >= dMin && weight < weights[y][x2]) {
                        queue.offer(new Step(y, x2, Axis.Y, weights[y][x2] = weight, curr));
                    }
                }
            }
        }
        
        System.out.println(best);
    }
    
    enum Axis { Y, X }
    record Step(int y, int x, Axis axis, int weight, Step prev) {}
}
