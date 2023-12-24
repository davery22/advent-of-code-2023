package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class Day21 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day21.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) {
        int[][] grid = reader.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int y0 = 0, x0 = 0;
        loop: for (; y0 < grid.length; y0++) {
            for (x0 = 0; x0 < grid[0].length; x0++) {
                if (grid[y0][x0] == 'S') {
                    grid[y0][x0] = '.';
                    break loop;
                }
            }
        }
        
        Set<Pos> curr = new HashSet<>();
        curr.add(new Pos(y0, x0));
        for (int i = 0; i < 64; i++) {
            curr = next(curr, grid);
        }
        
        System.out.println(curr.size());
    }
    
    void part2(BufferedReader reader) {
        int[][] grid = reader.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        grid[65][65] = '.';
        int y0 = 65, x0 = 65; // Hard-coded start
        int steps = 26501365;
        int tilesRight = (steps-65) / 131; // no remainder
        int diamondsAcross = 2*tilesRight+1;
        
        long innerDiamonds = diamondsAcross;
        long evenInnerDiamonds = diamondsAcross/2+1; // 'even' inner diamonds touch the tile edge
        for (long i = diamondsAcross-2; i > 0; i -= 2) {
            innerDiamonds += 2*i; // eg 15 + 2*13 + 2*11 + ... + 2*1; I'm sure there's a formula for this
            evenInnerDiamonds += i+1;
        }
        long oddInnerDiamonds = innerDiamonds-evenInnerDiamonds;
        long outerDiamonds = innerDiamonds-1;
        
        Set<Pos> curr = new HashSet<>();
        curr.add(new Pos(y0, x0));
        
        // Find counts of even and odd gardens in the tile's inner diamond
        for (int i = 0; i < 64; i++) {
            curr = next(curr, grid);
        }
        int innerOddCount = curr.size();
        curr = next(curr, grid);
        int innerEvenCount = curr.size();
        
        // Find counts of even and odd gardens throughout the tile
        for (int i = 0; i < 64; i++) {
            curr = next(curr, grid);
        }
        Set<Pos> tileEvens = Set.copyOf(curr);
        int tileEvenCount = curr.size();
        Set<Pos> tileOdds = Set.copyOf(curr = next(curr, grid));
        int tileOddCount = curr.size();
        
        // Put these together to find the counts of even and odd gardens outside the tile's inner diamond
        int combinedOuterEvenCount = tileEvenCount - innerEvenCount;
        int combinedOuterOddCount = tileOddCount - innerOddCount;
        
        // Find counts of even and odd gardens in the top-right + bottom-left
        // Use these to find counts of even and odd gardens in the top-left + bottom-right
        // Use that to find counts of even and odd gardens in the tile's outer diamond (formed by outside corners)
        int trblEvenCount = 0, trblOddCount = 0;
        for (int i = 0; i < 65; i++) {
            for (int j = 66+i; j < grid.length; j++) { // Assumed square grid centered at 65,65
                if (tileEvens.contains(new Pos(i, j))) {
                    trblEvenCount++;
                }
                if (tileOdds.contains(new Pos(i, j))) {
                    tileOddCount++;
                }
                if (tileEvens.contains(new Pos(j, i))) {
                    trblEvenCount++;
                }
                if (tileOdds.contains(new Pos(j, i))) {
                    tileOddCount++;
                }
            }
        }
        int tlbrEvenCount = combinedOuterEvenCount - trblEvenCount;
        int tlbrOddCount = combinedOuterOddCount - trblOddCount;
        int outerEvenCount = trblEvenCount + tlbrOddCount;
        int outerOddCount = trblOddCount + tlbrEvenCount;
        
        System.out.println("steps " + steps);
        System.out.println("tilesRight " + tilesRight);
        System.out.println("diamondsAcross " + diamondsAcross);
        System.out.println("innerDiamonds " + innerDiamonds);
        System.out.println("outerDiamonds " + outerDiamonds);
        System.out.println("evenInnerDiamonds " + evenInnerDiamonds);
        System.out.println("oddInnerDiamonds " + oddInnerDiamonds);
        System.out.println("tileEvenCount " + tileEvenCount);
        System.out.println("tileOddCount " + tileOddCount);
        System.out.println("innerEvenCount " + innerEvenCount);
        System.out.println("innerOddCount " + innerOddCount);
        System.out.println("combinedOuterEvenCount " + combinedOuterEvenCount);
        System.out.println("combinedOuterOddCount " + combinedOuterOddCount);
        System.out.println("trblEvenCount " + trblEvenCount);
        System.out.println("trblOddCount " + trblOddCount);
        System.out.println("tlbrEvenCount " + tlbrEvenCount);
        System.out.println("tlbrOddCount " + tlbrOddCount);
        System.out.println("outerEvenCount " + outerEvenCount);
        System.out.println("outerOddCount " + outerOddCount);
        
        long ans = (oddInnerDiamonds * innerOddCount) + (evenInnerDiamonds * innerEvenCount)
            + (outerDiamonds/2 * outerOddCount) + (outerDiamonds/2 * outerEvenCount);
        System.out.println(ans);
    }
    
    private static Set<Pos> next(Set<Pos> curr, int[][] grid) {
        Set<Pos> next = new HashSet<>();
        for (Pos pos : curr) {
            int y = pos.y, x = pos.x;
            if (y > 0 && grid[y-1][x] == '.') {
                next.add(new Pos(y-1, x));
            }
            if (y < grid.length-1 && grid[y+1][x] == '.') {
                next.add(new Pos(y+1, x));
            }
            if (x > 0 && grid[y][x-1] == '.') {
                next.add(new Pos(y, x-1));
            }
            if (x < grid[0].length-1 && grid[y][x+1] == '.') {
                next.add(new Pos(y, x+1));
            }
        }
        return next;
    }
    
    record Pos(int y, int x) {}
}
