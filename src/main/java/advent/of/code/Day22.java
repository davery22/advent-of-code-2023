package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Day22 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day22.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) {
        Set<Integer> notSafeToDestroy = new HashSet<>();
        List<Set<Integer>> allSupportedBy = new ArrayList<>();
        common(reader, notSafeToDestroy, allSupportedBy);
        
        System.out.println(allSupportedBy.size() - notSafeToDestroy.size());
    }
    
    void part2(BufferedReader reader) {
        Set<Integer> notSafeToDestroy = new HashSet<>();
        List<Set<Integer>> allSupportedBy = new ArrayList<>();
        common(reader, notSafeToDestroy, allSupportedBy);
        
        List<Set<Integer>> allSupports = new ArrayList<>(allSupportedBy.size());
        for (int id = 0; id < allSupportedBy.size(); id++) {
            allSupports.add(new HashSet<>());
        }
        for (int id = 0; id < allSupportedBy.size(); id++) {
            for (int i : allSupportedBy.get(id)) {
                allSupports.get(i).add(id);
            }
        }
        int sum = 0;
        for (int id : notSafeToDestroy) {
            Set<Integer> union = new HashSet<>(Set.of(id));
            transitiveSupports(id, union, allSupports, allSupportedBy);
            sum += union.size()-1;
        }
        System.out.println(sum);
    }
    
    void common(BufferedReader reader, Set<Integer> notSafeToDestroy, List<Set<Integer>> allSupportedBy) {
        // Parse blocks and sort by z, so we can fill in x,y areas in z-order.
        Block[] blocks = reader.lines()
            .map(line -> {
                var a = Arrays.stream(line.split("~"))
                    .map(s -> Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray())
                    .toArray(int[][]::new);
                int xa = a[0][0], xb = a[1][0], ya = a[0][1], yb = a[1][1], za = a[0][2], zb = a[1][2];
                return new Block(Math.min(xa, xb), Math.max(xa, xb), Math.min(ya, yb), Math.max(ya, yb), Math.min(za, zb), Math.max(za, zb));
            })
            .sorted(Comparator.comparingInt(Block::z0))
            .toArray(Block[]::new);
        
        int[][] surface = new int[10][20];
        
        // Fill in which blocks support each block, and which blocks are not safe to destroy (solely support another block).
        for (int id = 0; id < blocks.length; id++) {
            Block block = blocks[id];
            Set<Integer> supportedBy = new HashSet<>();
            allSupportedBy.add(supportedBy);
            int supportZ = 0;
            for (int i = block.x0; i <= block.x1; i++) {
                for (int j = block.y0; j <= block.y1; j++) {
                    int below = surface[i][j*2];
                    int belowZ = surface[i][j*2+1];
                    if (belowZ == 0) {
                        // Do nothing
                    } else if (belowZ == supportZ) {
                        supportedBy.add(below-1);
                    } else if (belowZ > supportZ) {
                        supportZ = belowZ;
                        supportedBy.clear();
                        supportedBy.add(below-1);
                    }
                }
            }
            for (int i = block.x0; i <= block.x1; i++) {
                for (int j = block.y0; j <= block.y1; j++) {
                    surface[i][j*2] = id+1;
                    surface[i][j*2+1] = supportZ + block.z1-block.z0+1;
                }
            }
            if (supportedBy.size() == 1) {
                notSafeToDestroy.add(supportedBy.iterator().next());
            }
        }
    }
    
    void transitiveSupports(int id, Set<Integer> union, List<Set<Integer>> allSupports, List<Set<Integer>> allSupportedBy) {
        for (int i : allSupports.get(id)) {
            if (union.containsAll(allSupportedBy.get(i)) && union.add(i)) {
                transitiveSupports(i, union, allSupports, allSupportedBy);
            }
        }
    }
    
    record Block(int x0, int x1, int y0, int y1, int z0, int z1) {}
}
