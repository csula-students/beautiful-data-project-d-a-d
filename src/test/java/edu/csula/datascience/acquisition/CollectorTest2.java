package edu.csula.datascience.acquisition;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * A test case to show how to use Collector and Source
 */
public class CollectorTest2 {
    private MockCollector2 collector;
    private MockSource2 source;

    @Before
    public void setup() {
        collector = new MockCollector2();
        source = new MockSource2();
    }

    @Test
    public void mungee() throws Exception {
    	System.out.println(source.next().size());
    	
        List<MockData2> list = (List<MockData2>) collector.mungee(source.next());
        List<MockData2> expectedList = Lists.newArrayList(
      
        		new MockData2("4", "4444", "davida", "this is a tweet4", 0, "Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
				new MockData2("7", "7777", "adams", "this is a tweet7", 0,  "Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
				new MockData2("8", "4444", "davida", "this is a tweet8", 0,"Fri Apr 22 17:08:45 PDT 2016", 0, false, false),
				new MockData2("9", "9999", "adams", "this is a tweet9", 0, "Fri Apr 22 17:08:45 PDT 2016", 0, false, false)
        );

        Assert.assertEquals( expectedList.size() , list.size());

        for (int i = 0; i < list.size(); i ++) {
        	System.out.println("i:"+i);
            Assert.assertEquals(expectedList.get(i).getId(), expectedList.get(i).getId());
            Assert.assertEquals(list.get(i).getTweetId(), expectedList.get(i).getTweetId());
            Assert.assertEquals(list.get(i).getText(), expectedList.get(i).getText());
            Assert.assertEquals(list.get(i).getFav(), expectedList.get(i).getFav());
            Assert.assertEquals(list.get(i).getUsername(), expectedList.get(i).getUsername());
            Assert.assertEquals(list.get(i).getDate(), expectedList.get(i).getDate());
            Assert.assertEquals(list.get(i).getRetweet(), expectedList.get(i).getRetweet());
            Assert.assertEquals(list.get(i).isRetweeted(), expectedList.get(i).isRetweeted());
            Assert.assertEquals(list.get(i).isSensitive(), expectedList.get(i).isSensitive());
            
        }
    }
    
  
    
    
    
}