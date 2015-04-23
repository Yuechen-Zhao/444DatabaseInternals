package simpledb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Knows how to compute some aggregate over a set of StringFields.
 */
public class StringAggregator implements Aggregator {
	
	private int gbfield;
	private Type gbfieldtype;
	private int afield;
	private Op what;
	
	private Map<Field, Integer> count;

    private static final long serialVersionUID = 1L;

    /**
     * Aggregate constructor
     * @param gbfield the 0-based index of the group-by field in the tuple, or NO_GROUPING if there is no grouping
     * @param gbfieldtype the type of the group by field (e.g., Type.INT_TYPE), or null if there is no grouping
     * @param afield the 0-based index of the aggregate field in the tuple
     * @param what aggregation operator to use -- only supports COUNT
     * @throws IllegalArgumentException if what != COUNT
     */

    public StringAggregator(int gbfield, Type gbfieldtype, int afield, Op what) {
        // some code goes here
    	this.gbfield = gbfield;
    	this.gbfieldtype = gbfieldtype;
    	this.afield = afield;
    	this.what = what;
    	this.count = new HashMap<Field, Integer>();
    }

    /**
     * Merge a new tuple into the aggregate, grouping as indicated in the constructor
     * @param tup the Tuple containing an aggregate field and a group-by field
     */
    public void mergeTupleIntoGroup(Tuple tup) {
        // some code goes here
    	Field tupgbfield;
    	if (this.gbfield == Aggregator.NO_GROUPING) {
    		tupgbfield = null;
    	} else {
    		tupgbfield = tup.getField(gbfield);
    	}
    	
    	if (!count.containsKey(tupgbfield)) {
    		count.put(tupgbfield, 0);
    	}
    	
    	count.put(tupgbfield, count.get(tupgbfield) + 1);
    }

    /**
     * Create a DbIterator over group aggregate results.
     *
     * @return a DbIterator whose tuples are the pair (groupVal,
     *   aggregateVal) if using group, or a single (aggregateVal) if no
     *   grouping. The aggregateVal is determined by the type of
     *   aggregate specified in the constructor.
     */
    public DbIterator iterator() {
        // some code goes here
    	List<Tuple> listTuples = new ArrayList<Tuple>();
        
        String[] names;
    	Type[] types;
    	if (this.gbfield == Aggregator.NO_GROUPING) {
    		names = new String[] {"aggregateValue"};
    		types = new Type[] {Type.INT_TYPE};
    	} else {
    		names = new String[] {"groupValue", "aggregateValue"};
    		types = new Type[] {this.gbfieldtype, Type.INT_TYPE};
    	}
    	TupleDesc td = new TupleDesc(types, names);
    	
    	Tuple newtup;
    	for (Field thisgroup : count.keySet()) {
    		int aVal = count.get(thisgroup);
    		newtup = new Tuple(td);
    		
    		if (this.gbfield == Aggregator.NO_GROUPING) {
    			newtup.setField(0, new IntField(aVal));
    		} else {
        		newtup.setField(0, thisgroup);
        		newtup.setField(1, new IntField(aVal));    			
    		}
    		listTuples.add(newtup);
    	}
    	
    	return new TupleIterator(td, listTuples);
    }

}
