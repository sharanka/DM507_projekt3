
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Decode {

    private int[] bytes = new int[256];
    private String fileName;
    private String decodedName;
    private PQHeap pqHeap;
    private FileInputStream fileInput;
    private BitInputStream bitInput;
    private FileOutputStream fileOutput;
    private HuffManTree ht;

    public static void main(String[] args) {
        Decode e = new Decode();
        if (args.length > 1) {
            e.setFileName(args[0]);
            e.setDecodedFileName(args[1]);
        } else {
            System.out.println("You need at least 2 args: java Encode filename encodedfilename");
        }
        try {
            System.out.println("before scan input");
            e.scanInput();
        } catch (IOException ex) {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public void setDecodedFileName(String name) {
        this.decodedName = name;
    }

    public void scanInput() throws FileNotFoundException, IOException {
        fileInput = new FileInputStream(new File(fileName));
        bitInput = new BitInputStream(fileInput);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }
        int _byte;
        try {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = bitInput.readInt();
            }

            fileOutput = new FileOutputStream(new File(decodedName));

            ht = (HuffManTree) huffman(bytes).data;
            Node current = ht.getRoot();

            while ((_byte = bitInput.readBit()) != -1) {
                if (current.get_byte() != null) {
                    fileOutput.write(current.get_byte());
                    current = ht.getRoot();
                } else {
                    if (_byte == 0) {
                        if (current.getLeftChild() != null) {
                            current = current.getLeftChild();
                        }
                    } else {
                        if (current.getRightChild() != null) {
                            current = current.getRightChild();
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        } finally {
            try {
                if (bitInput != null) {
                    bitInput.close();
                }

            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }

        /*
        Their code below
         */
//        int counter = 0;
//
//        int total = 0;
//        try {
//
//            e = new Encode();
//            ht = (HuffManTree) e.huffman(bytes).data;
//            for (int i : bytes) {
//                total += bytes[i];
//            }
//            counter = 0;
//            while ((_byte = bitInput.readBit()) != -1) {
//                if (counter < getBytes().length) {
//                    counter++;
//                    continue;
//                }
//                if (ht.search(ht.getRoot(), _byte) != null) {
//
//                }
//            }
//
//        } catch (IOException ioe) {
//            System.out.println(ioe);
//        } finally {
//            try {
//                if (fileInput != null) {
//                    fileInput.close();
//                }
//
//            } catch (IOException ioe) {
//                System.out.println(ioe);
//            }
//        }
        System.out.println("end scan input");
    }

    private void buildPQHeap(PQHeap pqHeap, int[] bytes) {
        int n = bytes.length;
        for (int i = 0; i < n; i++) {
            //System.out.println(bytes[i]);
            pqHeap.insert(new Element(bytes[i], new HuffManTree(new Node(i))));
        }
    }

    public Element huffman(int[] _bytes) {
        System.out.println("enter huffman");
        int n = _bytes.length;
        pqHeap = new PQHeap(n);
        buildPQHeap(pqHeap, _bytes);
        for (int i = 1; i < n; i++) {
            Node z = new Node();
            Element x = pqHeap.extractMin();
            Element y = pqHeap.extractMin();
            HuffManTree hx = (HuffManTree) x.data;
            HuffManTree hy = (HuffManTree) y.data;

            z.setLeftChild(hx.getRoot());
            z.setRightChild(hy.getRoot());
            HuffManTree data = new HuffManTree(z);
            Element element = new Element(x.key + y.key, data);
            pqHeap.insert(element);
        }
        System.out.println("exit huffman");
        return pqHeap.extractMin();
    }

    public int[] getBytes() {
        return bytes;
    }
}
