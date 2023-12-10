package advent.of.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day10.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) throws IOException {
        int[][] grid = reader.lines().map(s -> s.chars().toArray()).toArray(int[][]::new);
        int y = 0, x = 0;
        loop: for (; y < grid.length; y++) {
            for (x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 'S') {
                    break loop;
                }
            }
        }
        
        // up can be |F7
        // down can be |JL
        // right can be -7J
        // left can be -FL
        
        // find next dir, move according to dir
        Dir dir = null;
        if (x > 0 && (dir = lookLeft(grid[y][x-1])) != null) {
            x--;
        }
        if (dir == null && x < grid[y].length-1 && (dir = lookRight(grid[y][x+1])) != null) {
            x++;
        }
        if (dir == null && y > 0 && (dir = lookUp(grid[y-1][x])) != null) {
            y--;
        }
        if (dir == null && y < grid.length-1 && (dir = lookDown(grid[y+1][x])) != null) {
            y++;
        }
        
        int step = 1;
        while (dir != null) {
            step++;
            dir = switch (dir) {
                case UP -> lookUp(grid[--y][x]);
                case DOWN -> lookDown(grid[++y][x]);
                case LEFT -> lookLeft(grid[y][--x]);
                case RIGHT -> lookRight(grid[y][++x]);
            };
        }
        
        System.out.println(step/2);
    }
    
    enum Dir {
        LEFT, RIGHT, UP, DOWN
    }
    
    void part2(BufferedReader reader) throws IOException {
        int[][] grid = reader.lines().map(s -> s.chars().toArray()).toArray(int[][]::new);
        int y = 0, x = 0;
        loop: for (; y < grid.length; y++) {
            for (x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 'S') {
                    break loop;
                }
            }
        }
        
        // up can be |F7
        // down can be |JL
        // right can be -7J
        // left can be -FL
        
        // find next dir, move according to dir
        
        
        // TODO: Avoid Dir enum
//        int dir; // 1 = clockwise; -1 = counter-clockwise
//        if (x > 0 && (List.of('-', 'F', 'L').contains((char) grid[y][x-1]))) {
//            dir = -1;
//            x--;
//        } else if (x < grid[y].length-1 && List.of('-', '7', 'J').contains((char) grid[y][x+1])) {
//            dir = 1;
//            x++;
//        } else if (y > 0 && List.of('|', 'F', '7').contains((char) grid[y-1][x])) {
//            dir = 1;
//            y--;
//        } else {
//            dir = -1;
//            y++;
//        }
//
//        System.out.println(y);
//        System.out.println(x);
//        int step = 1;
//        while (grid[y][x] != 'S') {
//            step++;
//            switch (grid[y][x]) {
//                case '|' -> y += dir;
//                case '-' -> x += dir;
//                case 'F' -> { if (dir == 1) x++; else y++; }
//                case 'L' -> { if (dir == 1) }
//                case '7' -> y++;
//                case 'J' -> x--;
//            }
//        }
//        System.out.println(step);
        
        Dir dir = null;
        if (x > 0 && (dir = lookLeft(grid[y][x-1])) != null) {
            x--;
        }
        if (dir == null && x < grid[y].length-1 && (dir = lookRight(grid[y][x+1])) != null) {
            x++;
        }
        if (dir == null && y > 0 && (dir = lookUp(grid[y-1][x])) != null) {
            y--;
        }
        if (dir == null && y < grid.length-1 && (dir = lookDown(grid[y+1][x])) != null) {
            y++;
        }
        
        int y0 = y, x0 = x;
        Set<Pos> path = new HashSet<>();
        path.add(new Pos(y, x));

        while (dir != null) {
            dir = switch (dir) {
                case UP -> lookUp(grid[--y][x]);
                case DOWN -> lookDown(grid[++y][x]);
                case LEFT -> lookLeft(grid[y][--x]);
                case RIGHT -> lookRight(grid[y][++x]);
            };
            path.add(new Pos(y, x));
        }
        
        // out F-J in
        // out F-7 out
        // out L-J out
        // out L-7 in
        // out |   in
        // out S   ??
        
        int insideCount = 0;
        for (int i = 0; i < grid.length; i++) {
            boolean inside = false;
            int mode = 0; // 0 = none; 'L' = seek-L; 'F' = seek-F
            for (int j = 0; j < grid[i].length; j++) {
                int see = grid[i][j];
                if (path.contains(new Pos(i, j))) {
                    switch (see) {
                        case '|', 'S' -> inside ^= true; // flip
                        case 'F', 'L' -> mode = see;
                        case '7' -> { inside ^= mode == 'L'; mode = 0; }
                        case 'J' -> { inside ^= mode == 'F'; mode = 0; }
                        case '-' -> { }
//                        case 'S' -> mode = 'F';
                        default -> throw new IllegalArgumentException();
                    }
                } else if (inside) {
                    grid[i][j] = ' ';
                    insideCount++;
                }
            }
        }
        System.out.println(insideCount);

//        dir = dir0;
//        y = y0; x = x0;
//        int leftArea = 0;
//        int rightArea = 0;
//
//        while (dir != null) {
//            dir = switch (dir) {
//                case UP -> {
//                    rightArea += explore(grid, path, new Pos(y, x+1), 'r');
//                    leftArea  += explore(grid, path, new Pos(y, x-1), ' ');
//                    yield lookUp(grid[--y][x]);
//                }
//                case DOWN -> {
//                    rightArea += explore(grid, path, new Pos(y, x-1), 'r');
//                    leftArea  += explore(grid, path, new Pos(y, x+1), ' ');
//                    yield lookDown(grid[++y][x]);
//                }
//                case LEFT -> {
//                    rightArea += explore(grid, path, new Pos(y-1, x), 'r');
//                    leftArea  += explore(grid, path, new Pos(y+1, x), ' ');
//                    yield lookLeft(grid[y][--x]);
//                }
//                case RIGHT -> {
//                    rightArea += explore(grid, path, new Pos(y+1, x), 'r');
//                    leftArea  += explore(grid, path, new Pos(y-1, x), ' ');
//                    yield lookRight(grid[y][++x]);
//                }
//            };
//        }
//
//        System.out.println(leftArea);
//        System.out.println(rightArea);
        
        // no code time
        for (int[] row : grid) {
            System.out.println(Arrays.stream(row).mapToObj(Character::toString).collect(Collectors.joining()));
        }
        
        // idea: move orthogonal to the path in both directions ('left' and 'right') until more path is hit
        // (or out-of-bounds). Sum up the non-path space between. Need to know when we are encountering path vs random
        // pipe - traverse twice, first to store path positions, then to survey area.
        
        
    }
    
    int explore(int[][] grid, Set<Pos> path, Pos start, char mark) {
        Deque<Pos> queue = new ArrayDeque<>();
        queue.add(start);
        int count = 0;
        while (!queue.isEmpty()) {
            Pos curr = queue.pop();
            int y = curr.y;
            int x = curr.x;
            if (y >= 0 && y < grid.length && x >= 0 && x < grid[y].length && !path.contains(curr) && grid[y][x] != mark) {
                count++;
                grid[y][x] = mark;
                queue.add(new Pos(y-1, x-1));
                queue.add(new Pos(y-1, x));
                queue.add(new Pos(y-1, x+1));
                queue.add(new Pos(y, x-1));
                queue.add(new Pos(y, x+1));
                queue.add(new Pos(y+1, x-1));
                queue.add(new Pos(y+1, x));
                queue.add(new Pos(y+1, x+1));
            }
        }
        return count;
    }
    
    Dir lookUp(int see) {
        return switch (see) {
            case '|' -> Dir.UP;
            case 'F' -> Dir.RIGHT;
            case '7' -> Dir.LEFT;
            default  -> null;
        };
    }
    
    Dir lookDown(int see) {
        return switch (see) {
            case '|' -> Dir.DOWN;
            case 'J' -> Dir.LEFT;
            case 'L' -> Dir.RIGHT;
            default  -> null;
        };
    }
    
    Dir lookLeft(int see) {
        return switch (see) {
            case '-' -> Dir.LEFT;
            case 'F' -> Dir.DOWN;
            case 'L' -> Dir.UP;
            default  -> null;
        };
    }
    
    Dir lookRight(int see) {
        return switch (see) {
            case '-' -> Dir.RIGHT;
            case '7' -> Dir.DOWN;
            case 'J' -> Dir.UP;
            default -> null;
        };
    }
    
    record Pos(int y, int x) { }
    
    // tried: 414(?) - too low
    // tried: 423 - someone else's answer
}
