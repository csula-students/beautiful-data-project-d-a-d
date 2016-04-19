package edu.csula.datascience.examples;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Simple test to test our implementation on Stats class
 */
public class SimpleStatsTest {
    private SimpleStats stats;

    @Before
    public void setUp() throws Exception {
        List<Double> data = Lists.newArrayList(
            13.0, 18.0, 13.0, 14.0, 13.0, 16.0, 14.0, 21.0, 13.0
        );
        stats = new SimpleStats(data);
    }

    @Test
    public void sum() throws Exception {
        Assert.assertEquals(135, stats.sum(), 0);
    }

    @Test
    public void mean() throws Exception {
        Assert.assertEquals(15, stats.mean(), 0);
    }

    @Test
    public void median() throws Exception {
        Assert.assertEquals(14, stats.median(), 0);
    }

    @Test
    public void range() throws Exception {
        Assert.assertEquals(8, stats.range(), 0);
    }

    @Test
    public void mode() throws Exception {
        Assert.assertEquals(13, stats.mode(), 0);
    }

    @Test
    public void max() throws Exception {
        Assert.assertEquals(21, stats.max(), 0);
    }

    @Test
    public void min() throws Exception {
        Assert.assertEquals(13, stats.min(), 0);
    }
}