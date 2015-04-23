package simpledb;

import java.util.*;

/**
 * Filter is an operator that implements a relational select.
 */
public class Filter extends Operator {
	
	// predicate to filter tuple with
	private Predicate p;
	// child operator
	private DbIterator child;

    private static final long serialVersionUID = 1L;

    /**
     * Constructor accepts a predicate to apply and a child operator to read
     * tuples to filter from.
     * 
     * @param p
     *            The predicate to filter tuples with
     * @param child
     *            The child operator
     */
    public Filter(Predicate p, DbIterator child) {
        // some code goes here
    	this.p = p;
    	this.child = child;
    }

    public Predicate getPredicate() {
        // some code goes here
        return this.p;
    }

    public TupleDesc getTupleDesc() {
        // some code goes here
        return this.child.getTupleDesc();
    }

    public void open() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        // some code goes here
    	super.open();
    	this.child.open();
    }

    public void close() {
        // some code goes here
    	super.close();
    	this.child.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // some code goes here
    	this.child.rewind();
    }

    /**
     * AbstractDbIterator.readNext implementation. Iterates over tuples from the
     * child operator, applying the predicate to them and returning those that
     * pass the predicate (i.e. for which the Predicate.filter() returns true.)
     * 
     * @return The next tuple that passes the filter, or null if there are no
     *         more tuples
     * @see Predicate#filter
     */
    protected Tuple fetchNext() throws NoSuchElementException,
            TransactionAbortedException, DbException {
        // some code goes here
        while (this.child.hasNext()) {
        	Tuple thist = this.child.next();
        	if (this.p.filter(thist)) {
        		return thist;
        	}
        }
        return null;
    }

    @Override
    public DbIterator[] getChildren() {
        // some code goes here
    	DbIterator[] arr = {this.child};
    	return arr;
    }

    @Override
    public void setChildren(DbIterator[] children) {
        // some code goes here
    	this.child = children[0];
    }

}
