package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day07 {
    void main() throws Exception {
        Comparator<HandBid> part1Comp = Comparator.comparing((HandBid hand) -> getType(hand.hand))
            .thenComparing(HandBid::hand, handComparator(this::toStrength))
            .reversed();
        Comparator<HandBid> part2Comp = Comparator.comparing((HandBid hand) -> getType2(hand.hand))
            .thenComparing(HandBid::hand, handComparator(this::toStrength2))
            .reversed();
        
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day07.txt")))) {
            HandBid[] handBids = reader.lines()
                .map(line -> {
                    String[] parts = line.split(" ");
                    return new HandBid(parts[0], Integer.parseInt(parts[1]));
                })
                .sorted(part1Comp)
                .toArray(HandBid[]::new);
            
            var sum = IntStream.range(0, handBids.length)
                .map(i -> handBids[i].bid * (i+1))
                .sum();
            
            System.out.println(sum);
        }
    }
    
    record HandBid(String hand, int bid) {}
    
    enum Type {
        FIVE_OF_A_KIND,  // 1 group of 5
        FOUR_OF_A_KIND,  // 2 groups, 1 of 4 and 1 of 1
        FULL_HOUSE,      // 2 groups, 1 of 3 and 1 of 2
        THREE_OF_A_KIND, // 3 groups, 1 of 3 and 2 of 1
        TWO_PAIR,        // 3 groups, 2 of 2 and 1 of 1
        ONE_PAIR,        // 4 groups, 1 of 2 and 3 of 1
        HIGH_CARD,       // 5 groups
    }
    
    Comparator<String> handComparator(ToIntFunction<Character> strengthFunc) {
        return (a, b) -> {
            for (int i = 0; i < 5; i++) {
                int cmp = strengthFunc.applyAsInt(a.charAt(i)) - strengthFunc.applyAsInt(b.charAt(i));
                if (cmp != 0) return Integer.signum(cmp);
            }
            return 0;
        };
    }
    
    Type getType(String hand) {
        Map<Integer, Long> counts = hand.chars().boxed().collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        long highCount = counts.values().stream().max(Comparator.naturalOrder()).orElseThrow();
        
        return switch (counts.size()) {
            case 1 -> Type.FIVE_OF_A_KIND;
            case 2 -> highCount == 4 ? Type.FOUR_OF_A_KIND : Type.FULL_HOUSE;
            case 3 -> highCount == 3 ? Type.THREE_OF_A_KIND : Type.TWO_PAIR;
            case 4 -> Type.ONE_PAIR;
            case 5 -> Type.HIGH_CARD;
            default -> throw new IllegalArgumentException();
        };
    }
    
    int toStrength(char label) {
        return switch (label) {
            case 'A' -> 0;
            case 'K' -> 1;
            case 'Q' -> 2;
            case 'J' -> 3;
            case 'T' -> 4;
            default -> 14 - Character.getNumericValue(label);
        };
    }
    
    Type getType2(String hand) {
        Map<Integer, Long> counts = hand.chars().boxed().collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        var sortedEntries = counts.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .toArray(Map.Entry[]::new);
        
        if (sortedEntries.length == 1) {
            return Type.FIVE_OF_A_KIND;
        }
        
        // Replace Jokers
        int pos = sortedEntries[0].getKey().equals((int) 'J') ? 1 : 0;
        counts.merge(
            (Integer) sortedEntries[pos].getKey(),
            Objects.requireNonNullElse(counts.remove((int) 'J'), 0L),
            Long::sum
        );
        
        long highCount = (long) sortedEntries[pos].getValue(); //counts.values().stream().max(Comparator.naturalOrder()).orElseThrow();
        
        return switch (counts.size()) {
            case 1 -> Type.FIVE_OF_A_KIND;
            case 2 -> highCount == 4 ? Type.FOUR_OF_A_KIND : Type.FULL_HOUSE;
            case 3 -> highCount == 3 ? Type.THREE_OF_A_KIND : Type.TWO_PAIR;
            case 4 -> Type.ONE_PAIR;
            case 5 -> Type.HIGH_CARD;
            default -> throw new IllegalArgumentException();
        };
    }
    
    int toStrength2(char label) {
        return switch (label) {
            case 'A' -> 0;
            case 'K' -> 1;
            case 'Q' -> 2;
            case 'T' -> 3;
            case 'J' -> 12;
            default -> 13 - Character.getNumericValue(label);
        };
    }
}
