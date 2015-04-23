package simpledb;

import java.util.Iterator;
import java.util.List;

public class HPTupleIterator<T> implements Iterator<Tuple> {
	
	private List<Tuple> usedTuples;
	private Iterator<Tuple> it;
	
	public HPTupleIterator(List<Tuple> usedTuples) {
		this.usedTuples = usedTuples;
		this.it = usedTuples.iterator();
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return it.hasNext();
	}

	@Override
	public Tuple next() {
		// TODO Auto-generated method stub
		return it.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Should not call remove on Heap Page Tuple Iterator.");
	}
	
}
