package edu.csula.datascience.examples;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * A simple math statistics programming exercise
 */
public class SimpleStats {
    private final List<Double> data;
    public SimpleStats(List<Double> data) {
        this.data = data;
    }

    /**
     * Sum of entire list of numbers
     */
    public double sum() {
        return data.stream()
            .mapToDouble(Double::doubleValue)
            .sum();
    }

    /**
     * Average of the numbers
     */
    public double mean() {
        OptionalDouble avg = data.stream()
            .mapToDouble(Double::doubleValue)
            .average();

        return avg.isPresent() ? avg.getAsDouble() : 0;
    }

    /**
     * Sort the numbers, median is the middle value of the sorted list
     */
    public double median() {
        Collections.sort(data);

        if (data.size() == 0) {
            return 0;
        } else if (data.size() % 2 != 0) {
            return data.get(data.size() / 2);
        } else {
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
        }
    }

    /**
     * The value of max minus min
     */
    public double range() {
        return max() - min();
    }

    /**
     * The number that repeats the most in the numbers
     */
    public double mode() {
        Map<Double, Integer> count = Maps.newTreeMap();
        double result = -1;

        for (Double item: data) {
            count.put(
                item,
                count.getOrDefault(item, 0) + 1
            );
        }

        Iterator<Map.Entry<Double, Integer>> iterator = count.entrySet().iterator();

        Integer maxCount = 0;
        while (iterator.hasNext()) {
            Map.Entry<Double, Integer> entry = iterator.next();

            if (maxCount < entry.getValue()) {
                maxCount = entry.getValue();
                result = entry.getKey();
            }
        }

        return result;
    }

    /**
     * Maximum number of the numbers
     */
    public double max() {
        return Collections.max(data);
    }

    /**
     * Minimum number of the numbers
     */
    public double min() {
        return Collections.min(data);
    }
}
