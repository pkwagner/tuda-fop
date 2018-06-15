package hospital.heap;

/**
 * Class for modeling a binary heap with an abstract internal data structure.
 *
 * @param <T> the type of the elements in this heap. The type must implement the
 *            Comparable interface.
 * @author Martin Hess
 * @version 1.0
 */
public abstract class AbstractBinaryHeap<T extends Comparable<T>> {

    /**
     * Returns the number of elements stored in this heap.
     *
     * @return the number of elements stored in this heap
     */
    public abstract int getSize();

    /**
     * Returns true if the heap is empty and false otherwise.
     *
     * @return true if the heap has no elements and false otherwise
     */
    public abstract boolean isEmpty();

    /**
     * Returns true if the specified parent and child fulfill the heap
     * condition.
     *
     * @param parentIndex the index of the parent element
     * @param childIndex  the index of the parent element
     * @return true if the specified parent and child fulfill the heap
     * condition, false otherwise
     */
    protected abstract boolean isHeap(int parentIndex, int childIndex);

    /**
     * Swaps the parent node with the child node.
     *
     * @param parentIndex the index of the parent element
     * @param childIndex  the index of the parent element
     */
    protected abstract void swapNodes(int parentIndex, int childIndex);

    /**
     * Inserts a new element into the heap.
     *
     * @param element the element to be inserted into the heap.
     */
    public abstract void push(T element);

    /**
     * Returns the element in the root node of the binary tree.
     *
     * @return the element in the root node
     */
    public abstract T top();

    /**
     * Retrieves and removes the element stored in the root node.
     *
     * @return the element in the root node
     */
    public abstract T pop();

    /**
     * Restores the heap condition from bottom to top, starting from the
     * specified index.
     *
     * @param index the index to start from
     */
    protected void heapifyUp(int index) {
        // Skip process if index is root
        if (index == 0)
            return;

        // Get the parent for the specified index
        int parent = getParentIndex(index);

        // Check heap condition
        if (!isHeap(parent, index)) {
            // Violated - swap node to restore heap condition and continue
            swapNodes(parent, index);
            heapifyUp(parent);
        }
    }

    /**
     * Restores the heap condition from top to bottom, starting from the
     * specified index.
     *
     * @param index the index to start from
     */
    protected void heapifyDown(int index) {
        // Get indices for the children
        int leftChildIndex = getLeftChildIndex(index);
        int rightChildIndex = getRightChildIndex(index);

        // Check heap condition for the left child, if it exists
        if (leftChildIndex < getSize() && !isHeap(index, leftChildIndex)) {
            // Swap node to restore heap condition and continue
            swapNodes(index, leftChildIndex);
            heapifyDown(leftChildIndex);
        }

        // Check heap condition for the right child, if it exists
        if (rightChildIndex < getSize() && !isHeap(index, rightChildIndex)) {
            // Swap node to restore heap condition and continue
            swapNodes(index, rightChildIndex);
            heapifyDown(rightChildIndex);
        }
    }

    /**
     * Calculates the linear index of the left child node of the specified
     * parent index.
     * Note: the method does not check if such an index actually exists, i.e.,
     * if the result is less than the heap size.
     *
     * @param parentIndex the parent index
     * @return the index of the left child node in a linear data structure.
     */
    protected int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    /**
     * Calculates the linear index of the right child node of the specified
     * parent index.
     * Note: the method does not check if such an index actually exists, i.e.,
     * if the result is less than the heap size.
     *
     * @param parentIndex the parent index
     * @return the index of the right child node in a linear data structure.
     */
    protected int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    /**
     * Calculates the linear index of the parent node of the specified child
     * index.
     * Note: the method does not check if such an index actually exists, i.e.,
     * it will return a negative value for childIndex == 0.
     *
     * @param childIndex the child index
     * @return the index of the parent node in a linear data structure.
     */
    protected int getParentIndex(int childIndex) {
        return (childIndex - 1) >> 1;
    }
}
