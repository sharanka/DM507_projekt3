
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Encode {

    private int[] bytes = new int[256]; //Entries that contains frequencies for every single byte
    private String[] keywords = new String[256]; //Content of the entries.
    private String fileName;
    private FileInputStream fileInput;
    private FileOutputStream fileOutput;
    private BitOutputStream bitOutput;
    private BitInputStream bitInput;
    private PQHeap pqHeap;
    private String encodedFileName;

    public static void main(String[] args) {
        Encode e = new Encode();
        if (args.length > 1) {
            //Name of the files, that are used to encode and decode
            e.setFileName(args[0]);
            e.setEncodedFileName(args[1]);
        } else {
            System.out.println("You need at least 2 args: java Encode filename encodedfilename");
        }
        try {
            System.out.println("before scan input");
            e.scanInput(); //Scanning the input file 
        } catch (IOException ex) { 
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
        }
        e.generateKeyWords(e.huffman(e.getBytes())); //Bytes from the entries are converted into keywords
        try {
            e.toFile(e.keywords); //The encoded occurances and every byte's keyword are written into the file. 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /*
     * Scan input file using FileInputStream  
     */
    public void scanInput() throws FileNotFoundException, IOException {
        for (int i = 0; i < getBytes().length; i++) { //looping through entries 
            bytes[i] = 0; //Makes sure every indexes contains something in the entry array
        }
        int _byte;
        try {
            fileInput = new FileInputStream(new File(fileName)); 

            while ((_byte = fileInput.read()) != -1) { //Reads input of the entry array
                getBytes()[_byte]++; //Increments the index place in the entry array
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        } finally {
            try {
                if (fileInput != null) { //Check if input file is not null
                    fileInput.close(); //, and closes FileInputStream if that is the case
                }

            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
        System.out.println("end scan input");
    }
    /*
     * Convert content from bytes to keywords
     */
    public void generateKeyWords(Element e) {
        HuffManTree ht = (HuffManTree) e.data;
        for (int i = 0; i < keywords.length; i++) { //Loops through the content of every entries
            convert(ht.getRoot(), i, ""); //Convert from byte to keyword
        }
    }
    /*
     * Convert from Huffman's tree to table
     */
    public String convert(Node current, Integer _byte, String path) {
        if (current != null) { //If current Node is not null
            if (current.getLeftChild() != null) { //And if left child
                path = path.concat("0"); //add 0 to the path.
                path = convert(current.getLeftChild(), _byte, path); //convert left child 
            }
            if (current.getRightChild() != null) { //If right child
                path = path.concat("1"); //add 1 to the path.
                path = convert(current.getRightChild(), _byte, path); //convert right child
            }
            if (current.get_byte() == _byte) { //get the current byte
                keywords[_byte] = path; //add the byte to keywords to the path
                System.out.println("path is: " + path);
            }
        }
        if (path.length() > 0) { //checks if length of the path is greater than 0
            path = path.substring(0, path.length() - 1); //removes a character from the path
        }
        return path;
    }
    /*
     * Insert entires to HuffmanTree
    */
    private void buildPQHeap(PQHeap pqHeap, int[] bytes) { 
        int n = bytes.length;
        for (int i = 0; i < n; i++) {
            pqHeap.insert(new Element(bytes[i], new HuffManTree(new Node(i)))); //Using insert from PQHeap to add the entries to the Huffman tree as a Node
        }
    }

    /*
     * HuffMan algorithm
    */
    public Element huffman(int[] _bytes) {
        System.out.println("enter huffman");
        int n = _bytes.length;
        pqHeap = new PQHeap(n);
        buildPQHeap(pqHeap, _bytes);
        for (int i = 1; i < n; i++) {
            Node z = new Node(); 
            Element x = pqHeap.extractMin(); //Extracts first element
            Element y = pqHeap.extractMin(); //Extracts the new first element
            HuffManTree hx = (HuffManTree) x.data; //Add the element to the Huffman tree
            HuffManTree hy = (HuffManTree) y.data; //Add the new first element to the Huffmann tree

            z.setLeftChild(hx.getRoot()); //The first element of the Huffmann tree is a root of the tree
            z.setRightChild(hy.getRoot()); //The new first element of the Huffman tree is a root of the tree
            HuffManTree data = new HuffManTree(z); //Create new trees
            Element element = new Element(x.key + y.key, data); //Merge two trees
            pqHeap.insert(element); //Insert that element to the tree
        }
        System.out.println("exit huffman");
        return pqHeap.extractMin();
    }
    /*
     * Write all the occurances and every bytes keyword
    */

    public void toFile(String[] passwords) throws FileNotFoundException {
        fileOutput = new FileOutputStream(new File(encodedFileName));
        bitOutput = new BitOutputStream(fileOutput);
        fileInput = new FileInputStream(new File(fileName));

        for (int i = 0; i < getBytes().length; i++) { //Loop through the entry array
            try {
                bitOutput.writeInt(getBytes()[i]); //Output is written to the file
            } catch (IOException ex) {
                Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int _byte;
        try {

            while ((_byte = fileInput.read()) != -1) { //Read the compressed file 
                String string = keywords[_byte];
                if (string != "" && string != null) { //Check if the content of keywords in the entry array is null 
                    if (string.length() <= 9) { //Check if keyword's length is less than 9
                        bitOutput.writeInt(Integer.parseInt(keywords[_byte])); //, and write the int value to the output stream
                    } else { //if the length of keyword is greater than 9, the string is split into two 
                        bitOutput.writeInt(Integer.parseInt(keywords[_byte].substring(0,9))); 
                        bitOutput.writeInt(Integer.parseInt(string.substring(9, string.length())));
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        } finally {
            try {
                if (fileInput != null) { //Check if input file is null
                    fileInput.close(); // if not null, the input file is closed
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }

    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param encodedFileName the encodedFileName to set
     */
    public void setEncodedFileName(String encodedFileName) {
        this.encodedFileName = encodedFileName;
    }

    /**
     * @return the bytes
     */
    public int[] getBytes() {
        return bytes;
    }
}
