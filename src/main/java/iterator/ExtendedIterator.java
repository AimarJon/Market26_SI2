package iterator;

import java.util.Iterator;

public interface ExtendedIterator<T> extends Iterator<T> {
	//return the actual	element	and	go to the previous
	public T previous();
	//true if ther is a	previous element
	public boolean hasPrevious();
	// It is placed	in the first element
	public void goFirst();
	// It is placed	in the last	element
	public void goLast();
}
