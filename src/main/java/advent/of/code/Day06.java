package advent.of.code;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Day06 {
//    long[] times = Arrays.stream(reader.readLine().split(" +")).skip(1).mapToLong(Long::parseLong).toArray();
//    long[] records = Arrays.stream(reader.readLine().split(" +")).skip(1).mapToLong(Long::parseLong).toArray();
//
//    long time = Long.parseLong(Arrays.stream(reader.readLine().split(" +")).skip(1).collect(Collectors.joining()));
//    long record = Long.parseLong(Arrays.stream(reader.readLine().split(" +")).skip(1).collect(Collectors.joining()));
    
    void main() {
        part2();
    }
    
    void part1() {
        Map<Integer, Integer> recordDistanceByTime = Map.of(
            44, 277,
            89, 1136,
            96, 1890,
            91, 1768
        );
        
        long product = recordDistanceByTime.entrySet().stream()
            .mapToLong(e -> getNumberOfWaysToWin2(e.getKey(), e.getValue()))
            .reduce(1L, (i, j) -> i * j);
        
        System.out.println(product);
    }
    
    void part2() {
        long time = 44_89_96_91L;
        long recordDistance = 277_1136_1890_1768L;
        
        long ways = getNumberOfWaysToWin2(time, recordDistance);
        
        System.out.println(ways);
    }
    
    int getNumberOfWaysToWin(long time, long recordDistance) {
        int ways = 0;
        for (int i = 0; i < time; i++) {
            if (diffDistance(i, time, recordDistance) > 0) {
                ways++;
            }
        }
        return ways;
    }
    
    long getNumberOfWaysToWin2(long time, long recordDistance) {
        long lo = 0;
        long hi = time/2+1;
        long pos = -1;
        
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            long cmp = diffDistance(mid, time, recordDistance);
            if (cmp > 0) {
                hi = mid;
                pos = mid;
            } else if (cmp < 0) {
                lo = mid+1;
            } else {
                pos = mid;
                break;
            }
        }
        
        // first buttonHeldTime that wins
        return (time + 1) - (2 * pos); // time=4; 0x 1x 2 3x 4x   time=5; 0x 1 2 3 4 5x
    }
    
    long diffDistance(long buttonHeldTime, long totalTime, long recordDistance) {
        long dist = buttonHeldTime * (totalTime - buttonHeldTime);
        return dist - recordDistance;
    }
}
