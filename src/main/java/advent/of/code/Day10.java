package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Day10 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day10.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) {
        int[][] grid = reader.lines().map(s -> s.chars().toArray()).toArray(int[][]::new);
        Pos pos = startPos(grid);
        Dir dir = startDir(grid, pos);
        int y = pos.y, x = pos.x;
        
        int step = 0;
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
    
    void part2(BufferedReader reader) {
        int[][] grid = reader.lines().map(s -> s.chars().toArray()).toArray(int[][]::new);
        Pos pos = startPos(grid);
        Dir dir = startDir(grid, pos);
        int y = pos.y, x = pos.x;
        
        Dir outgoing = dir, incoming = dir;
        Set<Pos> path = new HashSet<>();

        while (dir != null) {
            path.add(new Pos(y, x));
            dir = switch (incoming = dir) {
                case UP -> lookUp(grid[--y][x]);
                case DOWN -> lookDown(grid[++y][x]);
                case LEFT -> lookLeft(grid[y][--x]);
                case RIGHT -> lookRight(grid[y][++x]);
            };
        }
        
        // Replace S with underlying pipe
        grid[y][x] = reverseLookup(incoming, outgoing);
        int insideCount = getInsideCount(grid, path);
        
        System.out.println(insideCount);
        
//        for (int[] row : grid) {
//            System.out.println(Arrays.stream(row).mapToObj(Character::toString).collect(Collectors.joining()));
//        }
    }
    
    private static int getInsideCount(int[][] grid, Set<Pos> path) {
        // out F-J in
        // out F-7 out
        // out L-J out
        // out L-7 in
        // out |   in
        
        int insideCount = 0;
        
        for (int i = 0; i < grid.length; i++) {
            boolean inside = false;
            int mode = 0;
            for (int j = 0; j < grid[i].length; j++) {
                if (path.contains(new Pos(i, j))) {
                    int see = grid[i][j];
                    switch (see) {
                        case 'F', 'L' -> mode = see;
                        case '7' -> inside ^= mode == 'L';
                        case 'J' -> inside ^= mode == 'F';
                        case '|' -> inside ^= true; // flip
                        case '-' -> { }
                        default -> throw new IllegalArgumentException();
                    }
                } else if (inside) {
                    //grid[i][j] = ' ';
                    insideCount++;
                }
            }
        }
        
        return insideCount;
    }
    
    Pos startPos(int[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == 'S') {
                    return new Pos(y, x);
                }
            }
        }
        throw new IllegalArgumentException();
    }
    
    Dir startDir(int[][] grid, Pos startPos) {
        int y = startPos.y, x = startPos.x;
        if (y > 0 && lookUp(grid[y-1][x]) != null) {
            return Dir.UP;
        } else if (y < grid.length-1 && lookDown(grid[y+1][x]) != null) {
            return Dir.DOWN;
        } else if (x > 0 && lookLeft(grid[y][x-1]) != null) {
            return Dir.LEFT;
        } else {
            return Dir.RIGHT;
        }
    }
    
    int reverseLookup(Dir incoming, Dir outgoing) {
        return switch (incoming) {
            case UP -> switch (outgoing) {
                case UP -> '|';
                case LEFT -> '7';
                case RIGHT -> 'F';
                case DOWN -> throw new AssertionError();
            };
            case DOWN -> switch (outgoing) {
                case LEFT -> 'J';
                case RIGHT -> 'L';
                case DOWN -> '|';
                case UP -> throw new AssertionError();
            };
            case LEFT -> switch (outgoing) {
                case UP -> 'L';
                case DOWN -> 'F';
                case LEFT -> '-';
                case RIGHT -> throw new AssertionError();
            };
            case RIGHT -> switch (outgoing) {
                case UP -> 'J';
                case DOWN -> '7';
                case RIGHT -> '-';
                case LEFT -> throw new AssertionError();
            };
        };
    }
    
    // up can be |F7
    // down can be |JL
    // right can be -7J
    // left can be -FL
    
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
            case 'L' -> Dir.UP;
            case 'F' -> Dir.DOWN;
            case '-' -> Dir.LEFT;
            default  -> null;
        };
    }
    
    Dir lookRight(int see) {
        return switch (see) {
            case 'J' -> Dir.UP;
            case '7' -> Dir.DOWN;
            case '-' -> Dir.RIGHT;
            default -> null;
        };
    }
    
    enum Dir { UP, DOWN, LEFT, RIGHT }
    record Pos(int y, int x) { }
}
