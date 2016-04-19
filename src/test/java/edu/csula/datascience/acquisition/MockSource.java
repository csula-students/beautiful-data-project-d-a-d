package edu.csula.datascience.acquisition;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * A mock source to provide data
 */
public class MockSource implements Source<MockData> {
    int index = 0;

    @Override
    public boolean hasNext() {
        return index < 1;
    }

    @Override
    public Collection<MockData> next() {
        return Lists.newArrayList(
            new MockData("1", null),
            new MockData("2", "content2"),
            new MockData("3", "content3")
        );
    }
}
