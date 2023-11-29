import java.util.Arrays;

/**
 * @author Duy Nguyen
 *
 */
public class MaxHeap<T extends Comparable<? super T>> implements HeapInterface<T> {
	private T[] heap; // Array of heap entries; ignore heap[0]
	private int lastIndex; // Index of last entry and number of entries
	private boolean integrityOK = false;
	private static final int DEFAULT_CAPACITY = 25;
	private static final int MAX_CAPACITY = 10000;

	public MaxHeap() {
		this(DEFAULT_CAPACITY); // Call next constructor
	} // end default constructor

	public MaxHeap(int initialCapacity) {
		// Is initialCapacity too small?
		if (initialCapacity < DEFAULT_CAPACITY)
			initialCapacity = DEFAULT_CAPACITY;
		else
			checkCapacity(initialCapacity); // Is initialCapacity too big?
		// The cast is safe because the new array contains null entries
		@SuppressWarnings("unchecked")
		T[] tempHeap = (T[]) new Comparable[initialCapacity + 1];
		heap = tempHeap;
		lastIndex = 0;
		integrityOK = true;
	} // end constructor

	public MaxHeap(T[] entries) {
		checkCapacity(entries.length);
		// The cast is safe because the new array contains null entries
		@SuppressWarnings("unchecked")
		T[] tempHeap = (T[]) new Comparable[entries.length + 1];
		heap = tempHeap;
		lastIndex = entries.length;
		// Copy given array to data field
		for (int index = 0; index < entries.length; index++)
			heap[index + 1] = entries[index];
		// Create heap
		for (int rootIndex = lastIndex / 2; rootIndex > 0; rootIndex--)
			reheap(rootIndex);
		integrityOK = true;
	}

	public void add(T newEntry) {
		checkIntegrity();
		int newIndex = lastIndex + 1;
		int parentIndex = newIndex / 2;
		while ((parentIndex > 0) && newEntry.compareTo(heap[parentIndex]) > 0) {
			heap[newIndex] = heap[parentIndex];
			newIndex = parentIndex;
			parentIndex = newIndex / 2;
		}
		heap[newIndex] = newEntry;
		lastIndex++;
		ensureCapacity();
	} // end add

	public T removeMax() {
		checkIntegrity();
		T root = null;
		if (!isEmpty()) {
			root = heap[1]; // Return value
			heap[1] = heap[lastIndex]; // Form a semiheap
			lastIndex--; // Decrease size
			reheap(1); // Transform to a heap
		}
		return root;
	}

	public T getMax() {
		checkIntegrity();
		T root = null;
		if (!isEmpty())
			root = heap[1];
		return root;
	} // end getMax

	public boolean isEmpty() {
		return lastIndex < 1;
	} // end isEmpty

	public int getSize() {
		return lastIndex;
	} // end getSize

	public void clear() {
		checkIntegrity();
		while (lastIndex > -1) {
			heap[lastIndex] = null;
			lastIndex--;
		}
		lastIndex = 0;
	}

	// Precondition: checkIntegrity has been called.
	private void reheap(int rootIndex) {
		boolean done = false;
		T orphan = heap[rootIndex];
		int leftChildIndex = 2 * rootIndex;
		while (!done && (leftChildIndex <= lastIndex)) {
			int largerChildIndex = leftChildIndex; // assume larger
			int rightChildIndex = leftChildIndex + 1;
			if ((rightChildIndex <= lastIndex) && heap[rightChildIndex].compareTo(heap[largerChildIndex]) > 0)
				largerChildIndex = rightChildIndex;
			if (orphan.compareTo(heap[largerChildIndex]) < 0) {
				heap[rootIndex] = heap[largerChildIndex];
				rootIndex = largerChildIndex;
				leftChildIndex = 2 * rootIndex;
			} else {
				done = true;
			}
		}
		heap[rootIndex] = orphan;
	}

	// Doubles the capacity of the array heap if it is full.
	// Precondition: checkIntegrity has been called.
	private void ensureCapacity() {
		if (lastIndex >= heap.length) {
			int newCapacity = 2 * heap.length;
			checkCapacity(newCapacity);
			heap = Arrays.copyOf(heap, newCapacity);
		}
	}

	// Throws an exception if this object is not integrityOK.
	private void checkIntegrity() {
		if (!integrityOK)
			throw new SecurityException("MaxHeap object is not integrityOK properly.");
	}

	// Ensures that the client requests a capacity
	// that is not too small or too large.
	private void checkCapacity(int capacity) {
		if (capacity < DEFAULT_CAPACITY)
			capacity = DEFAULT_CAPACITY;
		else if (capacity > MAX_CAPACITY) {
			throw new IllegalStateException(
					"Attempt to create a heap " + "whose capacity is larger than " + MAX_CAPACITY);
		}
	}
}
