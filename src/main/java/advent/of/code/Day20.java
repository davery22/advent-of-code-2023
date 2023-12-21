package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day20 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day20.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) {
        Map<String, Module> modulesByName = new HashMap<>();
        reader.lines().forEach(line -> {
            String[] parts = line.split(" -> ");
            List<String> outputs = List.of(parts[1].split(", "));
            switch (parts[0].charAt(0)) {
                case '%' -> modulesByName.put(parts[0].substring(1), new FlipFlop(outputs));
                case '&' -> modulesByName.put(parts[0].substring(1), new Conjunction(outputs));
                default -> modulesByName.put(parts[0], new Broadcast(outputs));
            }
        });
        // Add Conjunction inputs
        modulesByName.forEach((input, module) -> {
            for (var output : module.outputs()) {
                if (modulesByName.get(output) instanceof Conjunction c) {
                    c.prevInputs.put(input, false);
                }
            }
        });
        
        int hiCount = 0, loCount = 0;
        Deque<Pulse> pulses = new ArrayDeque<>();
        
        // Simulate 1000 button pushes
        for (int i = 0; i < 1000; i++) {
            pulses.offer(new Pulse("button", "broadcaster", false));
            while (!pulses.isEmpty()) {
                Pulse pulse = pulses.poll();
                if (pulse.isHi) {
                    hiCount++;
                } else {
                    loCount++;
                }
                switch (modulesByName.get(pulse.to)) {
                    case null -> { /* Do nothing */ }
                    case Broadcast b -> b.outputs().forEach(to -> pulses.offer(new Pulse(pulse.to, to, pulse.isHi)));
                    case FlipFlop f -> {
                        if (!pulse.isHi) {
                            f.isOn ^= true; // Flip
                            f.outputs().forEach(to -> pulses.offer(new Pulse(pulse.to, to, f.isOn)));
                        }
                    }
                    case Conjunction c -> {
                        c.prevInputs.put(pulse.from, pulse.isHi);
                        boolean allHi = c.prevInputs.values().stream().allMatch(b -> b);
                        c.outputs().forEach(to -> pulses.offer(new Pulse(pulse.to, to, !allHi)));
                    }
                }
            }
        }
        
        int product = hiCount * loCount;
        System.out.println(product);
    }
    
    // Part 2 is computer-assisted investigation, so not fully automated.
    
    void part2(BufferedReader reader) {
        Map<String, Module> modulesByName = new HashMap<>();
        reader.lines().forEach(line -> {
            String[] parts = line.split(" -> ");
            List<String> outputs = Arrays.stream(parts[1].split(", ")).sorted().toList();
            switch (parts[0].charAt(0)) {
                case '%' -> modulesByName.put(parts[0].substring(1), new FlipFlop(outputs));
                case '&' -> modulesByName.put(parts[0].substring(1), new Conjunction(outputs));
                default -> modulesByName.put(parts[0], new Broadcast(outputs));
            }
        });
        // Add Conjunction inputs
        modulesByName.forEach((input, module) -> {
            for (var output : module.outputs()) {
                if (modulesByName.get(output) instanceof Conjunction c) {
                    c.prevInputs.put(input, false);
                }
            }
        });
        
        Deque<Pulse> pulses = new ArrayDeque<>();
        
        for (int i = 0; i < 10000; i++) {
            pulses.offer(new Pulse("button", "broadcaster", false));
            while (!pulses.isEmpty()) {
                Pulse pulse = pulses.poll();
                switch (modulesByName.get(pulse.to)) {
                    case null -> { /* Do nothing */ }
                    case Broadcast b -> b.outputs().forEach(to -> pulses.offer(new Pulse(pulse.to, to, pulse.isHi)));
                    case FlipFlop f -> {
                        if (!pulse.isHi) {
                            f.isOn ^= true; // Flip
                            f.outputs().forEach(to -> pulses.offer(new Pulse(pulse.to, to, f.isOn)));
                        }
                    }
                    case Conjunction c -> {
                        c.prevInputs.put(pulse.from, pulse.isHi);
                        boolean allHi = c.prevInputs.values().stream().allMatch(b -> b);
                        c.outputs().forEach(to -> pulses.offer(new Pulse(pulse.to, to, !allHi)));
                    }
                }
                // TODO: Automate deriving intervals
                // TODO: Automate deriving these module names?
                // TODO: Automate deriving the relationships that tell us to look for these module names..?
                if (Set.of("ks", "pm", "dl", "vk").contains(pulse.from) && pulse.isHi) {
                    System.out.println(STR."\{i} \{pulse}");
                }
            }
        }
        
        // TODO: Automate deriving intervals - see above
        var nums = List.of(3769, 3833, 3877, 3917);
        var lcm = nums.stream()
            .mapToLong(i -> i)
            .reduce(1L, (a, b) -> (a * b) / gcd(a, b)); // LCM(a,b) = a*b/GCD(a,b)
        System.out.println(lcm);
    }
    
    long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
    
    record Pulse(String from, String to, boolean isHi) {}
    
    sealed interface Module {
        List<String> outputs();
    }
    
    record Broadcast(List<String> outputs) implements Module {}
    
    static final class FlipFlop implements Module {
        final List<String> outputs;
        boolean isOn = false;
        
        FlipFlop(List<String> outputs) { this.outputs = outputs; }
        
        public List<String> outputs() { return outputs; }
    }
    
    static final class Conjunction implements Module {
        final List<String> outputs;
        final Map<String, Boolean> prevInputs = new HashMap<>();
        
        Conjunction(List<String> outputs) { this.outputs = outputs; }
        
        public List<String> outputs() { return outputs; }
    }
}
