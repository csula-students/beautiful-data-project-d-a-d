package edu.csula.datascience.acquisition;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import twitter4j.Status;

/**
 * A mock implementation of collector for testing
 */
public class MockCollector2 implements Collector<MockData2, MockData2> {
	
    @Override
    public Collection<MockData2> mungee(Collection<MockData2> src) {
        // in your example, you might need to check src.hasNext() first
//        return src
//            .stream()
//            .filter(data -> data.getContent() != null)
//            .map(SimpleModel::build)
//            .collect(Collectors.toList());
//        
//        
    	
    	List<MockData2> list = Lists.newArrayList();
    	List<MockData2> cleaned = Lists.newArrayList();
    	if(src !=null && !src.isEmpty())
    	{
    		list.addAll(src);
    		
    		
	    		
	    		for (int i = 0; i < list.size(); i++)
		    	{
		        	if (   list.get(i).getId() != null && list.get(i).getTweetId() != null 
		        			&& list.get(i).getUsername() != null && list.get(i).getFav() >= 0 && list.get(i).getText() != null
		        			&& list.get(i).getRetweet() >= 0 && list.get(i).getDate() != null )
		    		{
		    			cleaned.add(list.get(i)); // add that tweet from the document
		    		}
		        	System.out.println("INDEX: "+i);
		        	
		    	}
	    	
    	}
        return cleaned;
        
    }

	@Override
	public void save(Collection<MockData2> data) {
		// TODO Auto-generated method stub
		
	}


}
