package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day18.txt")))) {
            Span[] plan = reader.lines()
                .map(s -> s.split(" "))
//                .map(s -> new Span(s[0].charAt(0), Integer.parseInt(s[1])))
                .map(s -> new Span(
                    switch (s[2].charAt(7)) {
                        case '0' -> 'R';
                        case '1' -> 'D';
                        case '2' -> 'L';
                        case '3' -> 'U';
                        default -> throw new IllegalArgumentException();
                    },
                    Integer.parseInt(s[2].substring(2, 7), 16)
                ))
                .toArray(Span[]::new);
            // Find extrema, so we can size a grid to fit
            int y = 0, x = 0, yMax = 0, yMin = 0, xMax = 0, xMin = 0;
            for (Span span : plan) {
                switch (span.dir) {
                    case 'U' -> { y -= span.len; yMin = Math.min(y, yMin); }
                    case 'D' -> { y += span.len; yMax = Math.max(y, yMax); }
                    case 'L' -> { x -= span.len; xMin = Math.min(x, xMin); }
                    case 'R' -> { x += span.len; xMax = Math.max(x, xMax); }
                }
            }
            
            int height = yMax-yMin+1;
            int width = xMax-xMin+1;
            
            List<List<Span>> rangeList = new ArrayList<>(height);
            for (int i = 0; i < height; i++) {
                rangeList.add(new ArrayList<>());
            }
            y = -yMin;
            x = -xMin;
            Span next = plan[0];
            
            for (Span span : Arrays.asList(plan).reversed()) {
                switch (span.dir) {
                    case 'U' -> {
                        rangeList.get(y).add(new Span(next.dir == 'L' ? '7' : 'F', x));
                        for (int i = 1; i < span.len; i++) {
                            rangeList.get(++y).add(new Span('|', x));
                        }
                        ++y;
                    }
                    case 'D' -> {
                        rangeList.get(y).add(new Span(next.dir == 'L' ? 'J' : 'L', x));
                        for (int i = 1; i < span.len; i++) {
                            rangeList.get(--y).add(new Span('|', x));
                        }
                        --y;
                    }
                    case 'L' -> {
                        rangeList.get(y).add(new Span(next.dir == 'U' ? 'L' : 'F', x));
                        x += span.len;
                    }
                    case 'R' -> {
                        rangeList.get(y).add(new Span(next.dir == 'U' ? 'J' : '7', x));
                        x -= span.len;
                    }
                }
                next = span;
            }
            
//            System.out.println(STR."F \{(int) 'F'} L \{(int) 'L'} J \{(int) 'J'} 7 \{(int) '7'}");
            long coverage = 0;
            for (var ranges : rangeList) {
                ranges.sort(Comparator.comparing(Span::len));
//                System.out.println(ranges);
                boolean inside = false;
                int prevX = 0;
                int mode = 0;
                for (Span curr : ranges) {
//                    if (inside) {
//                        coverage += curr.len - prevX + 1;
//                    }
                    switch (curr.dir) {
                        case 'F', 'L' -> {
                            mode = curr.dir;
                            if (!inside) {
                                prevX = curr.len;
                            }
                        }
                        case '7' -> {
                            inside ^= mode == 'L';
                            if (!inside) {
                                coverage += curr.len - prevX + 1;
                            }
                        }
                        case 'J' -> {
                            inside ^= mode == 'F';
                            if (!inside) {
                                coverage += curr.len - prevX + 1;
                            }
                        }
                        case '|' -> {
                            inside ^= true; // flip
                            if (!inside) {
                                coverage += curr.len - prevX + 1;
                            } else {
                                prevX = curr.len;
                            }
                        }
                        default -> throw new IllegalArgumentException();
                    }
                }
            }
            
            System.out.println(coverage);
            
//            int[][] grid = new int[height][width];
//            for (var row : grid) {
//                Arrays.fill(row, '.'); // For printing
//            }
//            y = -yMin;
//            x = -xMin;
//            int count = 0;
//            for (Span span : plan) {
//                int dy = 0, dx = 0;
//                switch (span.dir) {
//                    case 'U' -> dy = -1;
//                    case 'D' -> dy =  1;
//                    case 'L' -> dx = -1;
//                    case 'R' -> dx =  1;
//                    default -> throw new IllegalArgumentException();
//                }
//                for (int i = 0; i < span.len; i++) {
//                    grid[y += dy][x += dx] = '#';
//                    count++;
//                }
//            }
//
//            Deque<Pos> queue = new ArrayDeque<>();
//            queue.offer(new Pos(-yMin-1, -xMin-1)); // TODO: Generalize
//
//            while (!queue.isEmpty()) {
//                Pos pos = queue.poll();
//                if (grid[pos.y][pos.x] == '#') {
//                    continue;
//                }
//                count++;
//                grid[pos.y][pos.x] = '#';
//                queue.offer(new Pos(pos.y-1, pos.x-1));
//                queue.offer(new Pos(pos.y-1, pos.x));
//                queue.offer(new Pos(pos.y-1, pos.x+1));
//                queue.offer(new Pos(pos.y, pos.x-1));
//                queue.offer(new Pos(pos.y, pos.x+1));
//                queue.offer(new Pos(pos.y+1, pos.x-1));
//                queue.offer(new Pos(pos.y+1, pos.x));
//                queue.offer(new Pos(pos.y+1, pos.x+1));
//            }
//
//            System.out.println(count);
//            Arrays.stream(grid)
//                .map(row -> Arrays.stream(row).mapToObj(Character::toString).collect(Collectors.joining()))
//                .forEach(System.out::println);
        }
    }
    
    record Span(int dir, int len) {}
    record Pos(int y, int x) {}
}
