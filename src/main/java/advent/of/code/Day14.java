package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Day14 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day14.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) {
        int[][] grid = reader.lines().map(s -> s.chars().toArray()).toArray(int[][]::new);
        tiltNorth(grid);
        System.out.println(findLoad(grid));
    }
    
    void part2(BufferedReader reader) {
        int[][] grid = reader.lines().map(s -> s.chars().toArray()).toArray(int[][]::new);
        List<int[][]> lookup = new ArrayList<>();
        HashMap<BitSet, Integer> seen = new HashMap<>();
        int cycles = 1_000_000_000;
        for (int i = 0; i < cycles; i++) {
            tiltNorth(grid);
            tiltWest(grid);
            tiltSouth(grid);
            tiltEast(grid);
            var j = seen.putIfAbsent(toBits(grid), i);
            if (j == null) {
                int[][] copy = Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
                lookup.add(copy);
            } else {
                // Found a repeat - use to calculate the first time the grid is in the state it would be in at the end.
                int first = j + (cycles-1-i) % (i-j);
                System.out.println(findLoad(lookup.get(first)));
                return;
            }
        }
        throw new IllegalStateException();
    }
    
    int findLoad(int[][] grid) {
        int load = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == 'O') {
                    load += grid.length-y;
                }
            }
        }
        return load;
    }
    
    void tiltNorth(int[][] grid) {
        for (int x = 0; x < grid[0].length; x++) {
            int last = -1;
            for (int y = 0; y < grid.length; y++) {
                switch (grid[y][x]) {
                    case '#' -> last = y;
                    case 'O' -> swap(grid, ++last, x, y, x);
                }
            }
        }
    }
    
    void tiltWest(int[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            int last = -1;
            for (int x = 0; x < grid[0].length; x++) {
                switch (grid[y][x]) {
                    case '#' -> last = x;
                    case 'O' -> swap(grid, y, ++last, y, x);
                }
            }
        }
    }
    
    void tiltSouth(int[][] grid) {
        for (int x = grid[0].length-1; x >= 0; x--) {
            int last = grid.length;
            for (int y = grid.length-1; y >= 0; y--) {
                switch (grid[y][x]) {
                    case '#' -> last = y;
                    case 'O' -> swap(grid, --last, x, y, x);
                }
            }
        }
    }
    
    void tiltEast(int[][] grid) {
        for (int y = grid.length-1; y >= 0; y--) {
            int last = grid[0].length;
            for (int x = grid.length-1; x >= 0; x--) {
                switch (grid[y][x]) {
                    case '#' -> last = x;
                    case 'O' -> swap(grid, y, --last, y, x);
                }
            }
        }
    }
    
    static void swap(int[][] grid, int y1, int x1, int y2, int x2) {
        int tmp = grid[y1][x1];
        grid[y1][x1] = grid[y2][x2];
        grid[y2][x2] = tmp;
    }
    
    // A little compression never hurt
    static BitSet toBits(int[][] grid) {
        var set = new BitSet(grid.length * grid[0].length);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                set.set(j + grid.length*i, grid[i][j] != '.');
            }
        }
        return set;
    }
}
