package advent.of.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day13 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day13.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) throws IOException {
        var sum = parseGrids(reader).stream()
            .mapToInt(grid -> {
                int count = findHorizontalReflection(grid);
                return count < 0 ? findHorizontalReflection(transpose(grid)) : 100 * count;
            })
            .parallel()
            .sum();
            
        System.out.println(sum);
    }
    
    void part2(BufferedReader reader) throws IOException {
        var sum = parseGrids(reader).stream()
            .mapToInt(grid -> {
                int count = findHorizontalSmudgeReflection(grid);
                return count < 0 ? findHorizontalSmudgeReflection(transpose(grid)) : 100 * count;
            })
            .parallel()
            .sum();
        
        System.out.println(sum);
    }
    
    List<int[][]> parseGrids(BufferedReader reader) throws IOException {
        List<int[][]> grids = new ArrayList<>();
        for (String line = ""; line != null; ) {
            List<int[]> rows = new ArrayList<>();
            while ((line = reader.readLine()) != null && !line.isBlank()) {
                rows.add(line.chars().toArray());
            }
            grids.add(rows.toArray(int[][]::new));
        }
        return grids;
    }
    
    int[][] transpose(int[][] grid) {
        return IntStream.range(0, grid[0].length)
            .mapToObj(col -> Arrays.stream(grid)
                .mapToInt(row -> row[col])
                .toArray())
            .toArray(int[][]::new);
    }
    
    int findHorizontalReflection(int[][] grid) {
        loop: for (int i = 1; i < grid.length; i++) {
            for (int j = 0; i+j < grid.length && i-1-j >= 0; j++) {
                if (!Arrays.equals(grid[i+j], grid[i-1-j])) {
                    continue loop;
                }
            }
            return i;
        }
        return -1;
    }
    
    int findHorizontalSmudgeReflection(int[][] grid) {
        loop: for (int i = 1; i < grid.length; i++) {
            int tolerance = 1;
            for (int j = 0; i+j < grid.length && i-1-j >= 0; j++) {
                if ((tolerance -= diff(grid[i+j], grid[i-1-j], tolerance)) < 0) {
                    continue loop;
                }
            }
            if (tolerance == 0) { // Must be exactly one smudge, not zero
                return i;
            }
        }
        return -1;
    }
    
    int diff(int[] a, int[] b, int tolerance) {
        int diffs = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i] && ++diffs > tolerance) {
                break;
            }
        }
        return diffs;
    }
}
