package simpledb;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class HFTupleIterator implements DbFileIterator {
	
	private List<Tuple> listtuples;
	private Iterator<Tuple> it;
	
	public HFTupleIterator(List<Tuple> listtuples) {
		this.listtuples = listtuples;
		this.it = null;
	}

	@Override
	public void open() throws DbException, TransactionAbortedException {
		this.it = this.listtuples.iterator();
	}

	@Override
	public boolean hasNext() throws DbException, TransactionAbortedException {
		if (this.it == null) {
			return false;
		}
		return it.hasNext();
	}

	@Override
	public Tuple next() throws DbException, TransactionAbortedException,
			NoSuchElementException {
		if (this.it == null || !it.hasNext()) {
			throw new NoSuchElementException();
		}
		return it.next();
	}

	@Override
	public void rewind() throws DbException, TransactionAbortedException {
		this.open();
	}

	@Override
	public void close() {
		this.it = null;
	}

}
