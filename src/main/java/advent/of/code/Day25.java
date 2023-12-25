package advent.of.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Day25 {
    void main() throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/day25.txt")))) {
            part1(reader);
        }
    }
    
    void part1(BufferedReader reader) throws Exception {
        Map<String, Set<String>> graph = new HashMap<>();
        for (String line; (line = reader.readLine()) != null; ) {
            String[] parts = line.split(": ");
            String from = parts[0];
            List<String> to = Arrays.asList(parts[1].split(" "));
            graph.computeIfAbsent(from, _ -> new TreeSet<>(Comparator.naturalOrder())).addAll(to);
            to.forEach(t -> graph.computeIfAbsent(t, _ -> new TreeSet<>(Comparator.naturalOrder())).add(from));
        }
        
        // idea: Count edge occurrences on the shortest path from each node to every other node;
        // The edges we want to remove should occur more frequently than other edges,
        // as they are the only way to cross between sub-graphs.
        String[] components = graph.keySet().toArray(String[]::new);
        Map<Edge, Long> edgeCounts = new HashMap<>();
        for (int i = 0; i < components.length; i++) {
            for (int j = i+1; j < components.length; j++) {
                String a = components[i];
                String b = components[j];
                path(a, b, graph, edgeCounts);
            }
//            System.out.println(i);
//            if (i % 50 == 0) {
//                edgeCounts.entrySet().stream()
//                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                    .limit(50)
//                    .forEach(System.out::println);
//            }
        }
        
        List<Edge> bigEdges = edgeCounts.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(3)
            .map(Map.Entry::getKey)
            .toList();
        System.out.println(bigEdges);
        
        // Remove the big edges
        for (Edge edge : bigEdges) {
            graph.get(edge.a).remove(edge.b);
            graph.get(edge.b).remove(edge.a);
        }
        
        // BFS from anywhere to see how big one split of the graph is (can infer other split)
        Set<String> seen = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        String curr = components[0];
        queue.offer(curr);
        seen.add(curr);
        while ((curr = queue.poll()) != null) {
            for (var n : graph.get(curr)) {
                if (seen.add(n)) {
                    queue.offer(n);
                }
            }
        }
        
        System.out.println(seen.size()*(components.length-seen.size()));
    }
    
    void path(String start, String end, Map<String, Set<String>> graph, Map<Edge, Long> counts) {
        Set<String> seen = new HashSet<>();
        class Link { Link prev = null; String key; }
        Link curr = new Link(); curr.key = start;
        Deque<Link> queue = new ArrayDeque<>();
        queue.offer(curr);
        seen.add(start);
        while ((curr = queue.poll()) != null) {
            if (curr.key.equals(end)) {
                while (curr.prev != null) {
                    counts.merge(new Edge(curr.prev.key, curr.key), 1L, Long::sum);
                    curr = curr.prev;
                }
                return;
            }
            for (var n : graph.get(curr.key)) {
                if (seen.add(n)) {
                    Link link = new Link(); link.key = n; link.prev = curr;
                    queue.offer(link);
                }
            }
        }
    }
    
    static class Edge {
        String a, b;
        Edge(String a, String b) {
            if (a.compareTo(b) < 0) { this.a = a; this.b = b;
            } else {                  this.a = b; this.b = a; }
        }
        public boolean equals(Object obj) { return obj instanceof Edge e && e.a.equals(a) && e.b.equals(b); }
        public int hashCode() { return a.hashCode() * 31 + b.hashCode(); }
        public String toString() { return STR."Edge(\{a}, \{b})"; }
    }
}
