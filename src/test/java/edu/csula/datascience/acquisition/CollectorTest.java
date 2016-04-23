package edu.csula.datascience.acquisition;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * A test case to show how to use Collector and Source
 */
public class CollectorTest {
    private Collector<SimpleModel, MockData> collector;
    private Source<MockData> source;

    @Before
    public void setup() {
        collector = new MockCollector();
        source = new MockSource();
    }

    @Test
    public void mungee() throws Exception {
        List<SimpleModel> list = (List<SimpleModel>) collector.mungee(source.next());
        List<SimpleModel> expectedList = Lists.newArrayList(
            new SimpleModel("2", "content2"),
            new SimpleModel("3", "content3")
        );

        Assert.assertEquals(list.size(), 2);

        for (int i = 0; i < 2; i ++) {
            Assert.assertEquals(list.get(i).getId(), expectedList.get(i).getId());
            Assert.assertEquals(list.get(i).getContent(), expectedList.get(i).getContent());
        }
    }
    
    @Test
    public void mungeeSingleton() throws Exception {

        List<MockData2> list = Lists.newArrayList(
        
        		new MockData2("0", null, "adams", "this is a tweet", 0, "Los Angeles, ca", "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, ""),
        		new MockData2("1", "1111", null, "this is a tweet", 0, "Los Angeles,ca ", "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, ""),
        		new MockData2("2", "2222", "davidp", null, 0, "Los Angeles, ca", "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, ""),
        		new MockData2("3", "3333", "adams", "this is a tweet again", -1, "Los Angeles, ca", "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, ""),
        		new MockData2("4", "4444", "davida", "this is a tweet", 0, null, "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, ""),
        		new MockData2("5", "5555", "davida", "this is a tweet", 0, "Los Angeles, ca", null, 0, false, false, ""),
       			new MockData2("6", "6666", "davidp", "this is a tweet", 0, "Los Angeles, ca", "Fri Apr 22 17:08:45 PDT 2016", -1, false, false, ""),
				new MockData2("7", "7777", "adams", "this is a tweet", 0, "Los Angeles, ca", "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, ""),
				new MockData2("8", "4444", "davida", "this is a tweet", 0, "Los Angeles, ca", "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, ""),
				new MockData2("9", "9999", "adams", "this is a tweet", 0, "Los Angeles, ca", "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, "")

        );
        
        List<MockData2> cleaned = new ArrayList<MockData2>();
        
        for(int i = 0; i < list.size(); i++)
        {
        	//if(mungee.)
        }
        
        
        List<MockData2> expectedList = Lists.newArrayList(
        	new MockData2("8", "4444", "davida", "this is a tweet", 0, "Los Angeles, ca", "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, ""),
        	new MockData2("9", "9999", "adams", "this is a tweet", 0, "Los Angeles, ca", "Fri Apr 22 17:08:45 PDT 2016", 0, false, false, "")
           
        );

        Assert.assertEquals(list.size(), 2);

        for (int i = 0; i < 2; i ++) {
            Assert.assertEquals(list.get(i).getId(), expectedList.get(i).getId());
          //  Assert.assertEquals(list.get(i).getContent(), expectedList.get(i).getContent());
        }
    }
    
    
    
}