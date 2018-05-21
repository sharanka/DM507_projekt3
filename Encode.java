//Mikkel Bytoft Rasmussen - mikra16@student.sdu.dk
//Sharanka Shanmugalingam - shsha15@student.sdu.dk
//Niklas Brasch Pedersen - niklp16@student.sdu.dk
//Software Engineering 4. semester
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Encode {

    private FileInputStream input; // inputstream to read from a file
    private int[] bytes = new int[256]; // int array that holds the frequency of the bytes from the file
    private PQHeap pqHeap; // priority queue to managed the merging of huffmantrees in the huffman algorithm

    public static void main(String[] args) {
        Encode e = new Encode(); // new encode object to call the algorithm on
        HuffManTree ht; // initial empty huffmantree
        int[] bytes = e.generateFrequencies(args[0]); // generates the frequency table based on the input file
        String[] keywords = new String[256]; // array of strings to hold the keywords
        ht = (HuffManTree) e.huffman(bytes).data; // sets the huffmantree to the tree that remains after n-1 merges
        ht.toFile(bytes, ht.convert(keywords, "", ht.getRoot()), args[0], args[1]); // writes the content
        // including the frequency table to a file
    }

    public int[] generateFrequencies(String path) { // generates the frequency table for the file at the path
        try {
            input = new FileInputStream(path); // new inputstream from the file
            int _byte; // the byte currently being read
            while ((_byte = input.read()) != -1) { // loops through all bytes in the file
                // to create the freqcuency table
                this.bytes[_byte]++; // increments the value in the frequency table equal to where the current
                // byte belongs
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return this.bytes;
    }

    public PQHeap buildPQHeap(PQHeap pqHeap, int[] _bytes) { // builds the priority queue
        int n = _bytes.length; // the amount of times we have to potentially insert an element into the queue
        for (int i = 0; i < n; i++) {
            if (_bytes[i] > 0) { // if the frequency at the i'th location in the array is bigger than zero
                // we insert it into the queue
                pqHeap.insert(new Element(_bytes[i], new HuffManTree(new Node(i))));
                // insert the element with a new huffmantree
            }
        }
        return pqHeap; // returns the queue
    }

    public Element huffman(int[] _bytes) { // the huffman algorithm that builds the tree by
        // doing n-1 merges of subtrees
        pqHeap = new PQHeap(_bytes.length); // new queue that can holds elements
        // equal to the length of the frequency table
        pqHeap = buildPQHeap(pqHeap, _bytes); // builds the priority queue
        int n = pqHeap.getSize(); // the actual size of the queue, since we do not insert frequencies that are zero
        for (int i = 1; i < n; i++) { // if we started at zero instead of one
            // we would get to a point where we only have one element in the queue, which is the final huffmantree
            Node z = new Node(); // new empty node
            Element x = pqHeap.extractMin(); // the smallest element in the queue
            Element y = pqHeap.extractMin(); // the new smallest element in the queue
            // so in each iteration we take the two smallest elements from the queue
            HuffManTree hx = (HuffManTree) x.data; // new huffmantree from the first element retreived
            HuffManTree hy = (HuffManTree) y.data; // new huffmantree from the second element retreived
            z.setLeftChild(hx.getRoot()); // sets the leftchild of z
            z.setRightChild(hy.getRoot()); // sets the rightchild z
            HuffManTree ht = new HuffManTree(z); // new huffmantree to store in z
            Element element = new Element(x.key + y.key, ht); // the summed frequency of the two
            // huffman trees stored in z
            pqHeap.insert(element); // insert the new huffmantree back into the queue,
            // so it now holds one less element
        }
        return pqHeap.extractMin(); // returns the remaining element after n-1 merges
        // that holds the full huffmantree
    }
}
