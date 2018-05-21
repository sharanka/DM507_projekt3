//Mikkel Bytoft Rasmussen - mikra16@student.sdu.dk
//Sharanka Shanmugalingam - shsha15@student.sdu.dk
//Niklas Brasch Pedersen - niklp16@student.sdu.dk
//Software Engineering 4. semester
public class PQHeap implements PQ {

    private Element[] heap;
    private int size;

    public PQHeap(int maxElms) {
        this.size = 0;
        this.heap = new Element[maxElms];
    }


    // Extracts the first element from the array of elements,
    // then decreases the size of it by one
    public Element extractMin() {
        Element min = heap[0]; // The first element in the array
        heap[0] = heap[size - 1]; // Sets the first element in the array equal to the last
        size--; // reduce the size by one
        minHeapify(heap, 0);
        return min;
    }

    // Insert an element into the heap
    public void insert(Element element) {
        int i = size;
        heap[i] = element; // Insert the element at the last position in the array
        size++; // Increase the size by one
        // while the parent key is greater than the newly inserted element key and the size is above zero,
        // swap the newly inserted element with the parent element
        while (i > 0 && heap[parent(i)].key > heap[i].key) {
            Element temp = heap[parent(i)];
            heap[parent(i)] = heap[i];
            heap[i] = temp;
            i = parent(i);
        }
    }
    // Checks a given index against the child nodes and if the minHeap structure is violated,
    // it swaps the elements in the array at the index and the node that creates the violation
    private void minHeapify(Element[] elements, int index) {
        int lowest;
        int l = left(index);
        int r = right(index);
        // Set lowest to the left child index if it is above the size and its key is lower than the index parsed
        // otherwise set lowest to the index parsed
        if (l <= size && elements[l].key < elements[index].key) {
            lowest = l;
        } else {
            lowest = index;
        }
        // Set lowest to the right child index if it is above the size and its key is lower than the index parsed
        if (r <= size && elements[r].key < elements[lowest].key) {
            lowest = r;
        }
        // if the lowest value is not equal to the index parsed, it violates the minHeap structure,
        // and therefore the elements at these indexes are swapped
        if (lowest != index) {
            Element temp = heap[lowest];
            heap[lowest] = heap[index];
            heap[index] = temp;
            minHeapify(elements, lowest);
        }
    }

    private int left(int i) { return (i) * 2 + 1; } // Returns left child node
    private int right(int i) { return (i) * 2 + 2; } // Returns right child node
    private int parent(int i) { return (i - 1) / 2; } // Returns parent node
}