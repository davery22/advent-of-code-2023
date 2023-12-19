package advent.of.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day19 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day19.txt")))) {
            part2(reader);
        }
    }
    
    void part1(BufferedReader reader) throws IOException {
        Map<String, Workflow> workflowByKey = getWorkflowByKey(reader);
        
        var sum = reader.lines()
            .mapToInt(partStr -> {
                // Always in order: x,m,a,s
                int[] part = Arrays.stream(partStr.substring(1, partStr.length()-1).split(","))
                    .mapToInt(s -> Integer.parseInt(s.substring(2)))
                    .toArray();
                for (Workflow curr = workflowByKey.get("in");;) {
                    String sendTo = curr.sendTo;
                    for (var rule : curr.rules) {
                        int cmp = part[rule.arg] - rule.expected;
                        if (cmp != 0 && rule.gt == cmp > 0) {
                            sendTo = rule.sendTo;
                            break;
                        }
                    }
                    switch (sendTo) {
                        case "A" -> { return Arrays.stream(part).sum(); }
                        case "R" -> { return 0; }
                        default ->  curr = workflowByKey.get(sendTo);
                    }
                }
            })
            .sum();
        
        System.out.println(sum);
    }
    
    void part2(BufferedReader reader) throws IOException {
        Map<String, Workflow> workflowByKey = getWorkflowByKey(reader);
        int[][] partConstraints = new int[][] {
            { 0, 4001 },
            { 0, 4001 },
            { 0, 4001 },
            { 0, 4001 }
        };
        long combos = recurse(partConstraints, workflowByKey, workflowByKey.get("in"), 0);
        System.out.println(combos);
    }
    
    long recurse(int[][] partConstraints,
                 Map<String, Workflow> workflowByKey,
                 Workflow curr,
                 int ruleIdx) {
        if (ruleIdx == curr.rules.size()) {
            return nextWorkflow(partConstraints, workflowByKey, curr.sendTo);
        }
        
        Rule rule = curr.rules.get(ruleIdx);
        int[] constraint = partConstraints[rule.arg];
        
        if (rule.expected <= constraint[0]) {
            return !rule.gt ? 0 : nextWorkflow(partConstraints, workflowByKey, rule.sendTo);
        }
        
        if (rule.expected >= constraint[1]) {
            return rule.gt ? 0 : nextWorkflow(partConstraints, workflowByKey, rule.sendTo);
        }
        
        long sum = 0;
        int leftIdx = rule.gt ? 0 : 1, rightIdx = rule.gt ? 1 : 0;
        int left = constraint[leftIdx], right = constraint[rightIdx];
        
        // One side passes (goto next workflow)
        constraint[leftIdx] = rule.expected;
        sum += nextWorkflow(partConstraints, workflowByKey, rule.sendTo);
        constraint[leftIdx] = left;
        
        // Other side fails (goto next rule)
        constraint[rightIdx] = rule.expected + (rule.gt ? 1 : -1);
        sum += recurse(partConstraints, workflowByKey, curr, ruleIdx+1);
        constraint[rightIdx] = right;
        
        return sum;
    }
    
    long nextWorkflow(int[][] partConstraints, Map<String, Workflow> workflowByKey, String sendTo) {
        return switch (sendTo) {
            case "R" -> 0;
            case "A" -> countCombos(partConstraints);
            default -> recurse(partConstraints, workflowByKey, workflowByKey.get(sendTo), 0);
        };
    }
    
    long countCombos(int[][] partConstraints) {
        return Arrays.stream(partConstraints)
            .mapToLong(constraint -> constraint[1] - constraint[0] - 1)
            .reduce(1L, (a, b) -> a * b);
    }
    
    Map<String, Workflow> getWorkflowByKey(BufferedReader reader) throws IOException {
        Map<String, Workflow> workflowByKey = new HashMap<>();
        for (String line; !(line = reader.readLine()).isBlank(); ) {
            List<Rule> rules = new ArrayList<>();
            String[] split = line.split("[{}]");
            String[] rulesArr = split[1].split(",");
            for (int i = 0; i < rulesArr.length-1; i++) {
                String rule = rulesArr[i];
                int idx = rule.indexOf(':');
                rules.add(new Rule(
                    switch (rule.charAt(0)) {
                        case 'x' -> 0;
                        case 'm' -> 1;
                        case 'a' -> 2;
                        case 's' -> 3;
                        default -> throw new IllegalArgumentException();
                    },
                    rule.charAt(1) == '>',
                    Integer.parseInt(rule.substring(2, idx)),
                    rule.substring(idx+1)
                ));
            }
            workflowByKey.put(split[0], new Workflow(rules, rulesArr[rulesArr.length-1]));
        }
        return workflowByKey;
    }
    
    record Workflow(List<Rule> rules, String sendTo) {}
    record Rule(int arg, boolean gt, int expected, String sendTo) {}
}
