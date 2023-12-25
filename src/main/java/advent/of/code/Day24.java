package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;

public class Day24 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day24.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) throws Exception {
        long[][][] trajectories = reader.lines()
            .map(line -> Arrays.stream(line.split(" +@ +"))
                .map(s -> Arrays.stream(s.split(", +")).mapToLong(Long::parseLong).toArray())
                .toArray(long[][]::new)
            )
            .toArray(long[][][]::new);
        
        long min = 200000000000000L, max = 400000000000000L;
        int count = 0;
        for (int i = 0; i < trajectories.length; i++) {
            for (int j = i+1; j < trajectories.length; j++) {
                if (intersect(trajectories[i], trajectories[j]).orElse(null) instanceof Pos(double y, double x)
                    && min <= y && y <= max && min <= x && x <= max) {
                    count++;
                }
            }
        }
        
        System.out.println(count);
    }
    
    Optional<Pos> intersect(long[][] a, long[][] b) {
        long[] pa = a[0];
        long[] va = a[1];
        long[] pb = b[0];
        long[] vb = b[1];
        
        double mA = va[1] / (double) va[0];
        double mB = vb[1] / (double) vb[0];
        if (mA == mB || Math.abs(mA-mB) < 0.0000001) { // Parallel lines
            return Optional.empty();
        }
        
        double x, y;
        if (va[0] == 0) { // a is vertical
            x = pa[0];
            y = mB * (x - pb[0]) + pb[1];
        } else if (vb[0] == 0) { // b is vertical
            x = pb[0];
            y = mA * (x - pa[0]) + pa[1];
        } else {
            // y - y0 = m(x - x0)
            // y  = m*x - m*x0 + y0
            //
            // yA = mA*x - mA*x0A + y0A
            // yB = mB*x - mB*x0B + y0B
            // yA = yB
            // x  = (y0B - y0A + mA*x0A - mB*x0B)/(mA - mB)
            x = (pb[1] - pa[1] + mA * pa[0] - mB * pb[0]) / (mA - mB);
            y = mA * (x - pa[0]) + pa[1];
        }
        boolean aFuture = va[0] > 0 == x > pa[0]; // In future if positive velocity implies positive displacement
        boolean bFuture = vb[0] > 0 == x > pb[0];
        return aFuture && bFuture ? Optional.of(new Pos(y, x)) : Optional.empty();
    }
    
    void part2(BufferedReader reader) throws Exception {
        long[][][] trajectories = reader.lines()
            .map(line -> Arrays.stream(line.split(" +@ +"))
                .map(s -> Arrays.stream(s.split(", +")).mapToLong(Long::parseLong).toArray())
                .toArray(long[][]::new)
            )
            .toArray(long[][][]::new);
        
        for (long vx = -1000; vx < 1000; vx++) {
            for (long vy = -1000; vy < 1000; vy++) {
                long[][] first = trajectories[0];
                first[1][0] -= vx; first[1][1] -= vy;
                try {
                    long[][] next = trajectories[1];
                    next[1][0] -= vx; next[1][1] -= vy;
                    Optional<Pos> opos = intersect(first, next);
                    next[1][0] += vx; next[1][1] += vy;
                    if (!(opos.orElse(null) instanceof Pos(double y1, double x1))) {
                        continue;
                    }
                    for (int i = 2; i < trajectories.length; i++) {
                        next = trajectories[i];
                        next[1][0] -= vx; next[1][1] -= vy;
                        opos = intersect(first, next);
                        next[1][0] += vx; next[1][1] += vy;
                        if (!(opos.orElse(null) instanceof Pos(double y2, double x2)
                            && Math.abs(y1-y2) < 6
                            && Math.abs(x1-x2) < 6)) {
                            opos = null; // Arbitrary failure signal
                            break;
                        }
                    }
                    if (opos != null) {
                        next[1][0] -= vx; next[1][1] -= vy; // Undo cleanup! Need the moving reference frame
                        double t1 = first[1][0] != 0 ? (x1 - first[0][0])/first[1][0] : (y1 - first[0][1])/first[1][1];
                        double t2 = next[1][0] != 0  ? (x1 - next[0][0])/next[1][0]   : (y1 - next[0][1])/next[1][1];
                        double vz = (first[0][2] - next[0][2] + t1*first[1][2] - t2*next[1][2])/(t1 - t2);
                        double z1 = first[0][2] + t1*(first[1][2]-vz);
                        System.out.println(vx);
                        System.out.println(vy);
                        System.out.println(vz);
                        System.out.println();
                        System.out.println(x1);
                        System.out.println(y1);
                        System.out.println(z1);
                        System.out.println();
                        System.out.println(Math.round(x1) + Math.round(y1) + Math.round(z1));
                        return;
                    }
                } finally {
                    first[1][0] += vx; first[1][1] += vy;
                }
            }
        }
        
        System.out.println("done");
    }
    
    record Pos(double y, double x) {}
}
