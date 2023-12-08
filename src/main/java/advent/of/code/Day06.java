package advent.of.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day06 {
    void main() throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day06.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) throws IOException {
        long[] times = Arrays.stream(reader.readLine().split(" +")).skip(1).mapToLong(Long::parseLong).toArray();
        long[] records = Arrays.stream(reader.readLine().split(" +")).skip(1).mapToLong(Long::parseLong).toArray();
        
        long product = IntStream.range(0, times.length)
            .mapToLong(i -> getNumberOfWaysToWin2(times[i], records[i]))
            .reduce(1L, (i, j) -> i * j);
        
        System.out.println(product);
    }
    
    void part2(BufferedReader reader) throws IOException {
        long time = Long.parseLong(Arrays.stream(reader.readLine().split(" +")).skip(1).collect(Collectors.joining()));
        long record = Long.parseLong(Arrays.stream(reader.readLine().split(" +")).skip(1).collect(Collectors.joining()));
        
        long ways = getNumberOfWaysToWin2(time, record);
        
        System.out.println(ways);
    }
    
    // Either of these is fast enough for both parts
    
    long getNumberOfWaysToWin(long totalTime, long recordDistance) {
        int ways = 0;
        for (int i = 0; i < totalTime; i++) {
            if (diffDistance(i, totalTime, recordDistance) > 0) {
                ways++;
            }
        }
        return ways;
    }
    
    long getNumberOfWaysToWin2(long totalTime, long recordDistance) {
        // binary search for first buttonHeldTime that wins, then calculate wins from that
        long lo = 0;
        long hi = totalTime/2+1;
        long pos = -1;
        
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            long cmp = diffDistance(mid, totalTime, recordDistance);
            if (cmp > 0) {
                hi = pos = mid;
            } else if (cmp < 0) {
                lo = mid+1;
            } else {
                pos = mid;
                break;
            }
        }
        
        return pos < 0 ? 0 : (totalTime + 1) - (2 * pos);
    }
    
    long diffDistance(long buttonHeldTime, long totalTime, long recordDistance) {
        long dist = buttonHeldTime * (totalTime - buttonHeldTime);
        return dist - recordDistance;
    }
}
