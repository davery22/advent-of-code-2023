package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Day23 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day23.txt")))) {
            int[][] grid = reader.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
            boolean[][] seen = new boolean[grid.length][grid[0].length];
            seen[0][1] = true; // Starting pos
            int longest = dfs1(seen, grid, 0, 1, 0);
            System.out.println(longest);
        }
    }
    
    int dfs1(boolean[][] seen, int[][] grid, int y, int x, int steps) {
        if (y == grid.length-1 && x == grid[0].length-2) {
            return steps;
        }
        int longest = 0;
        switch (grid[y][x]) {
            case '.' -> {
                // Go in any direction that is in bounds and not forest (#)
                if (y > 0 && grid[y-1][x] != '#' && !seen[y-1][x]) {
                    seen[y-1][x] = true;
                    longest = Math.max(longest, dfs1(seen, grid, y-1, x, steps+1));
                    seen[y-1][x] = false;
                }
                if (y < grid.length-1 && grid[y+1][x] != '#' && !seen[y+1][x]) {
                    seen[y+1][x] = true;
                    longest = Math.max(longest, dfs1(seen, grid, y+1, x, steps+1));
                    seen[y+1][x] = false;
                }
                if (x > 0 && grid[y][x-1] != '#' && !seen[y][x-1]) {
                    seen[y][x-1] = true;
                    longest = Math.max(longest, dfs1(seen, grid, y, x-1, steps+1));
                    seen[y][x-1] = false;
                }
                if (x < grid[0].length-1 && grid[y][x+1] != '#' && !seen[y][x+1]) {
                    seen[y][x+1] = true;
                    longest = Math.max(longest, dfs1(seen, grid, y, x+1, steps+1));
                    seen[y][x+1] = false;
                }
            }
            case '^' -> {
                if (y > 0 && grid[y-1][x] != '#' && !seen[y-1][x]) {
                    seen[y-1][x] = true;
                    longest = Math.max(longest, dfs1(seen, grid, y-1, x, steps+1));
                    seen[y-1][x] = false;
                }
            }
            case 'v' -> {
                if (y < grid.length-1 && grid[y+1][x] != '#' && !seen[y+1][x]) {
                    seen[y+1][x] = true;
                    longest = Math.max(longest, dfs1(seen, grid, y+1, x, steps+1));
                    seen[y+1][x] = false;
                }
            }
            case '<' -> {
                if (x > 0 && grid[y][x-1] != '#' && !seen[y][x-1]) {
                    seen[y][x-1] = true;
                    longest = Math.max(longest, dfs1(seen, grid, y, x-1, steps+1));
                    seen[y][x-1] = false;
                }
            }
            case '>' -> {
                if (x < grid[0].length-1 && grid[y][x+1] != '#' && !seen[y][x+1]) {
                    seen[y][x+1] = true;
                    longest = Math.max(longest, dfs1(seen, grid, y, x+1, steps+1));
                    seen[y][x+1] = false;
                }
            }
        }
        return longest;
    }
    
    int dfs2(boolean[][] seen, int[][] grid, int y, int x, int steps) {
        if (y == grid.length-1 && x == grid[0].length-2) {
            return steps;
        }
        int longest = 0;
        switch (grid[y][x]) {
            case '.', '^', 'v', '<', '>' -> {
                // Go in any direction that is in bounds and not forest (#)
                if (y > 0 && grid[y-1][x] != '#' && !seen[y-1][x]) {
                    seen[y-1][x] = true;
                    longest = Math.max(longest, dfs2(seen, grid, y-1, x, steps+1));
                    seen[y-1][x] = false;
                }
                if (y < grid.length-1 && grid[y+1][x] != '#' && !seen[y+1][x]) {
                    seen[y+1][x] = true;
                    longest = Math.max(longest, dfs2(seen, grid, y+1, x, steps+1));
                    seen[y+1][x] = false;
                }
                if (x > 0 && grid[y][x-1] != '#' && !seen[y][x-1]) {
                    seen[y][x-1] = true;
                    longest = Math.max(longest, dfs2(seen, grid, y, x-1, steps+1));
                    seen[y][x-1] = false;
                }
                if (x < grid[0].length-1 && grid[y][x+1] != '#' && !seen[y][x+1]) {
                    seen[y][x+1] = true;
                    longest = Math.max(longest, dfs2(seen, grid, y, x+1, steps+1));
                    seen[y][x+1] = false;
                }
            }
        }
        return longest;
    }
}
