package advent.of.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day05 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day05.txt")))) {
            part2(reader);
        }
    }
    
    record Range(long destStart, long srcStart, long length) {}
    
    void part1(BufferedReader reader) throws IOException {
        long[] seeds = Arrays.stream(reader.readLine().substring("seeds: ".length()).split(" "))
            .mapToLong(Long::parseLong)
            .toArray();
        List<List<Range>> maps = new ArrayList<>();
        reader.readLine(); // Skip blank line
        reader.readLine(); // Skip header line
        
        do {
            List<Range> map = new ArrayList<>();
            maps.add(map);
            for (String s; (s = reader.readLine()) != null && !s.isBlank(); ) {
                long[] rangeNums = Arrays.stream(s.split(" ")).mapToLong(Long::parseLong).toArray();
                map.add(new Range(rangeNums[0], rangeNums[1], rangeNums[2]));
            }
        } while (reader.readLine() != null);
        
        long minLocation = Integer.MAX_VALUE;
        for (var seed : seeds) {
            for (var map : maps) {
                for (var range : map) {
                    if (seed >= range.srcStart && seed < range.srcStart + range.length) {
                        seed = range.destStart + (seed - range.srcStart);
                        break;
                    }
                }
            }
            minLocation = Math.min(seed, minLocation);
        }
        
        System.out.println(minLocation);
    }
    
    void part2(BufferedReader reader) throws IOException {
        long[] seeds = Arrays.stream(reader.readLine().substring("seeds: ".length()).split(" "))
            .mapToLong(Long::parseLong)
            .toArray();
        List<List<Range>> maps = new ArrayList<>();
        reader.readLine();
        reader.readLine();
        
        do {
            List<Range> map = new ArrayList<>();
            maps.add(map);
            for (String s; (s = reader.readLine()) != null && !s.isBlank(); ) {
                long[] rangeNums = Arrays.stream(s.split(" ")).mapToLong(Long::parseLong).toArray();
                map.add(new Range(rangeNums[0], rangeNums[1], rangeNums[2]));
            }
        } while (reader.readLine() != null);
        
        Instant start = Instant.now();
        long minLocation = getMinLocation(seeds, maps);
        Instant end = Instant.now();
        System.out.println(minLocation);
        System.out.println(Duration.between(start, end));
    }
    
    private static long getMinLocation(long[] seeds, List<List<Range>> maps) {
        return IntStream.range(0, seeds.length/2)
            .mapToLong(i -> {
                long start = seeds[i*2];
                long len = seeds[i*2+1];
                return LongStream.range(0, len)
                    .map(j -> {
                        long seed = start + j;
                        for (var map : maps) {
                            for (var range : map) {
                                if (seed >= range.srcStart && seed < range.srcStart + range.length) {
                                    seed = range.destStart + (seed - range.srcStart);
                                    break;
                                }
                            }
                        }
                        return seed;
                    })
                    .parallel()
                    .min().orElseThrow(); // Min of one range
            })
            .parallel()
            .min().orElseThrow(); // Min of all ranges
    }
}
