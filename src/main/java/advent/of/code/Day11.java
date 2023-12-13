package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Day11 {
    void main() throws Exception {
        int part1Expansion = 2;
        int part2Expansion = 1_000_000;
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day11.txt")))) {
            solve(reader, part1Expansion);
        }
    }
    
    record Pos(int y, int x) {}
    
    void solve(BufferedReader reader, int expansionFactor) {
        int[][] grid = reader.lines().map(String::chars).map(IntStream::toArray).toArray(int[][]::new);
        Pos[] galaxies = IntStream.range(0, grid.length)
            .mapToObj(y -> IntStream.range(0, grid[y].length)
                .filter(x -> grid[y][x] == '#')
                .mapToObj(x -> new Pos(y, x))
            )
            .flatMap(s -> s)
            .toArray(Pos[]::new);
        int[] emptyRows = IntStream.range(0, grid.length)
            .filter(i -> Arrays.stream(grid[i]).allMatch(c -> c == '.'))
            .toArray();
        int[] emptyCols = IntStream.range(0, grid[0].length)
            .filter(i -> Arrays.stream(grid).allMatch(r -> r[i] == '.'))
            .toArray();
        
        long sum = 0;
        for (int i = 0; i < galaxies.length; i++) {
            Pos pos1 = galaxies[i];
            for (int j = i+1; j < galaxies.length; j++) {
                Pos pos2 = galaxies[j];
                int dx = Math.abs(pos2.x - pos1.x);
                int dy = Math.abs(pos2.y - pos1.y);
                for (var row : emptyRows) {
                    if ((pos1.y < row && row < pos2.y) || (pos2.y < row && row < pos1.y)) {
                        dy += expansionFactor-1;
                    }
                }
                for (var col : emptyCols) {
                    if ((pos1.x < col && col < pos2.x) || (pos2.x < col && col < pos1.x)) {
                        dx += expansionFactor-1;
                    }
                }
                sum += dx + dy;
            }
        }
        
        System.out.println(sum);
    }
}
