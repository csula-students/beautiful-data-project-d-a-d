package edu.csula.datascience.acquisition;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * A mock source to provide data
 */
public class MockSource2 implements Source<MockData2> {
    int index = 0;

    @Override
    public boolean hasNext() {
        return index < 1;
    }

    @Override
    public Collection<MockData2> next() {
        return Lists.newArrayList(
        		new MockData2("0", null, "adams", "this is a tweet", 0,  "Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
        		new MockData2("1", "1111", null, "this is a tweet", 0,  "Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
        		new MockData2("2", "2222", "davidp", null, 0,  "Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
        		new MockData2("3", "3333", "adams", "this is a tweet again", -1,  "Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
        		new MockData2("4", "4444", "davida", "this is a tweet", 0, "Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
        		new MockData2("5", "5555", "davida", "this is a tweet", 0, null, 0, false, false),
       			new MockData2("6", "6666", "davidp", "this is a tweet", 0,  "Fri Apr 22 17:08:45 PDT 2016", -1, false, false),
				new MockData2("7", "7777", "adams", "this is a tweet", 0,  "Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
				new MockData2("8", "4444", "davida", "this is a tweet", 0,"Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
				new MockData2("9", "9999", "adams", "this is a tweet", 0, "Fri Apr 22 17:08:45 PDT 2016", 0, false, false)
        		 
        );
    }
}
