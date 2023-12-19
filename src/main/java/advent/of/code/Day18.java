package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;

public class Day18 {
    void main() throws Exception {
        Function<String[], Span> part1SpanMapper = s -> new Span(s[0].charAt(0), Integer.parseInt(s[1]));
        Function<String[], Span> part2SpanMapper = s -> new Span(
            switch (s[2].charAt(7)) {
                case '0' -> 'R';
                case '1' -> 'D';
                case '2' -> 'L';
                case '3' -> 'U';
                default -> throw new IllegalArgumentException();
            },
            Integer.parseInt(s[2].substring(2, 7), 16)
        );
        
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day18.txt")))) {
            Span[] plan = reader.lines()
                .map(s -> s.split(" "))
                .map(part2SpanMapper)
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
            
            int length = yMax-yMin+1;
            List<List<Span>> rangeList = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                rangeList.add(new ArrayList<>());
            }
            
            // Idea: Partition each row of the 'grid' into horizontal spans, and sum the area of the 'inside' spans.
            // We need to demarcate corners and walls to know when we are 'inside', similar to Day 10.
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
            
            // Calculate actual area by sorting span-bounds and summing 'inside' area.
            long coverage = 0;
            for (var ranges : rangeList) {
                ranges.sort(Comparator.comparing(Span::len));
                boolean inside = false;
                int prevX = 0;
                int mode = 0;
                for (Span curr : ranges) {
                    switch (curr.dir) {
                        case 'F', 'L' -> {
                            mode = curr.dir;
                            if (!inside) {
                                prevX = curr.len;
                            }
                        }
                        case '7', 'J' -> {
                            inside ^= mode == (curr.dir == '7' ? 'L' : 'F');
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
        }
    }
    
    record Span(int dir, int len) {}
}
