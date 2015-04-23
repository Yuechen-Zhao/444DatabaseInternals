package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {
	private File f;
	private TupleDesc td;

    /**
     * Constructs a heap file backed by the specified file.
     * 
     * @param f
     *            the file that stores the on-disk backing store for this heap
     *            file.
     */
    public HeapFile(File f, TupleDesc td) {
        // some code goes here
    	this.f = f;
    	this.td = td;
    }

    /**
     * Returns the File backing this HeapFile on disk.
     * 
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        return this.f;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     * 
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        // some code goes here
        return this.f.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * 
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return this.td;
    }

    // see DbFile.java for javadocs
    public Page readPage(PageId pid) {
        // some code goes here
    	if (getId() != pid.getTableId()) {
    		throw new IllegalArgumentException("The page does not exist in this file");
    	}
    	
        try {
        	RandomAccessFile raf = new RandomAccessFile(this.f, "r");
        	int offset = pid.pageNumber() * BufferPool.PAGE_SIZE;
        	byte[] pagedata = new byte[BufferPool.PAGE_SIZE];
        	
        	raf.seek(offset);
        	raf.read(pagedata, 0, BufferPool.PAGE_SIZE);
        	raf.close();
        	
        	return new HeapPage((HeapPageId)pid, pagedata);
        } catch (FileNotFoundException fnfe) {
        	fnfe.printStackTrace();
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        }
        throw new IllegalArgumentException("Exception from HeapFile.readPage().");
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // some code goes here
        // not necessary for lab1
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        // some code goes here
        return (int) Math.ceil(this.f.length() / BufferPool.PAGE_SIZE);
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
        // some code goes here
    	
    	// get the list of pages
    	List<HeapPage> listpages = new ArrayList<HeapPage>();
    	for (int i = 0; i < numPages(); i++) {
    		HeapPageId pageId = new HeapPageId(getId(), i);
    		try {
				HeapPage page = (HeapPage) Database.getBufferPool().getPage(tid, pageId, Permissions.READ_ONLY);
				listpages.add(page);
			} catch (TransactionAbortedException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			}
    	}
    	
    	// get the list of tuples from the pages
    	List<Tuple> listtuples = new ArrayList<Tuple>();
    	for (int i = 0; i < listpages.size(); i++) {
    		Iterator<Tuple> it = listpages.get(i).iterator();
    		while (it.hasNext()) {
    			listtuples.add(it.next());
    		}
    	}
    	
    	return new HFTupleIterator(listtuples);
    }

}

