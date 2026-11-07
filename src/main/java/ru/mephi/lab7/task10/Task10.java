package ru.mephi.lab7.task10;

import java.util.*;

public class Task10 {
    public record Neighbor(String city, long distance) {
        public Neighbor {
            Objects.requireNonNull(city);
            if (distance < 0) throw new IllegalArgumentException("Расстояние не может быть отрицательным");
        }
    }
    public record Result(Map<String, Long> distances, Map<String, String> previous) {
        public List<String> pathTo(String destination) {
            if (!distances.containsKey(destination)) return List.of();
            LinkedList<String> path = new LinkedList<>();
            for (String city = destination; city != null; city = previous.get(city)) path.addFirst(city);
            return path;
        }
    }
    public static Result shortestPaths(Map<String, Set<Neighbor>> graph, String start) {
        Objects.requireNonNull(start);
        Map<String, Long> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Neighbor> queue = new PriorityQueue<>(Comparator.comparingLong(Neighbor::distance));
        distances.put(start, 0L); queue.add(new Neighbor(start, 0));
        while (!queue.isEmpty()) {
            Neighbor current = queue.remove();
            if (current.distance() != distances.get(current.city())) continue;
            for (Neighbor edge : graph.getOrDefault(current.city(), Set.of())) {
                long distance = Math.addExact(current.distance(), edge.distance());
                if (!distances.containsKey(edge.city()) || distance < distances.get(edge.city())) {
                    distances.put(edge.city(), distance); previous.put(edge.city(), current.city());
                    queue.add(new Neighbor(edge.city(), distance));
                }
            }
        }
        return new Result(Map.copyOf(distances), Map.copyOf(previous));
    }
    public static void main(String[] args) {
        Map<String, Set<Neighbor>> graph = Map.of(
            "A", Set.of(new Neighbor("B", 5), new Neighbor("C", 1)),
            "C", Set.of(new Neighbor("B", 2)), "B", Set.of(new Neighbor("D", 3)));
        Result result = shortestPaths(graph, "A");
        System.out.println(result.distances()); System.out.println(result.pathTo("D"));
    }
}
