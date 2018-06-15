package hospital.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents binary heap based on a array list.
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class ArrayListHeap<T extends Comparable<T>> extends AbstractBinaryHeap<T> {

    private final List<T> content = new ArrayList<>();

    @Override
    public int getSize() {
        return content.size();
    }

    @Override
    public boolean isEmpty() {
        return content.isEmpty();
    }

    @Override
    protected boolean isHeap(int parentIndex, int childIndex) {
        if (childIndex < parentIndex) {
            throw new IllegalArgumentException("Child index is lower than parent index");
        }

        T parent = content.get(parentIndex);
        T child = content.get(childIndex);
        return parent.compareTo(child) <= 0;
    }

    @Override
    protected void swapNodes(int parentIndex, int childIndex) {
        if (childIndex < parentIndex) {
            throw new IllegalArgumentException("Child index is lower than parent index");
        }

        //save the elements before we override it
        T oldParent = content.get(parentIndex);
        T oldChild = content.get(childIndex);

        content.set(parentIndex, oldChild);
        content.set(childIndex, oldParent);
    }

    @Override
    public void push(T element) {
        content.add(element);

        //restore the heap condition
        int lastIndex = content.size() - 1;
        heapifyUp(lastIndex);
    }

    @Override
    public T top() {
        if (isEmpty()) {
            //there are no elements at all
            return null;
        }

        return content.get(0);
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            return null;
        }

        int lastIndex = content.size() - 1;
        swapNodes(0, lastIndex);

        T root = content.remove(lastIndex);

        heapifyDown(0);
        return root;
    }
}
