
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Encode {

    private int[] bytes = new int[256];
    private String[] keywords = new String[256];
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
            e.setFileName(args[0]);
            e.setEncodedFileName(args[1]);
        } else {
            System.out.println("You need at least 2 args: java Encode filename encodedfilename");
        }
        try {
            System.out.println("before scan input");
            e.scanInput();
        } catch (IOException ex) {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
        }
        e.generateKeyWords(e.huffman(e.getBytes()));
        try {
            e.toFile(e.keywords);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void scanInput() throws FileNotFoundException, IOException {
        for (int i = 0; i < getBytes().length; i++) {
            bytes[i] = 0;
        }
        int _byte;
        try {
            fileInput = new FileInputStream(new File(fileName));

            while ((_byte = fileInput.read()) != -1) {
                getBytes()[_byte]++;
                //System.out.println(getBytes()[_byte]);
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        } finally {
            try {
                if (fileInput != null) {
                    fileInput.close();
                }

            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
        System.out.println("end scan input");
    }

    public void generateKeyWords(Element e) {
        HuffManTree ht = (HuffManTree) e.data;
        for (int i = 0; i < keywords.length; i++) {
            convert(ht.getRoot(), i, "");
        }
    }

    public String convert(Node current, Integer _byte, String path) {
        if (current != null) {
            if (current.getLeftChild() != null) {
                path = path.concat("0");
                //System.out.println("added 0 total path is: " + path);
                path = convert(current.getLeftChild(), _byte, path);
            }
            if (current.getRightChild() != null) {
                path = path.concat("1");
                //System.out.println("added 1 total path is: " + path);
                path = convert(current.getRightChild(), _byte, path);
            }
            if (current.get_byte() == _byte) {
                keywords[_byte] = path;
                System.out.println("path is: " + path);
            }
        }
        if (path.length() > 0) {
            //System.out.println("removing last bit");
            path = path.substring(0, path.length() - 1);
            //System.out.println("path is: " + path);
        }
        return path;
    }

    private void buildPQHeap(PQHeap pqHeap, int[] bytes) {
        int n = bytes.length;
        for (int i = 0; i < n; i++) {
            //System.out.println(bytes[i]);
            pqHeap.insert(new Element(bytes[i], new HuffManTree(new Node(i))));
        }
    }

    /*public Element huffman(int[] _bytes) {
        int n = _bytes.length;
        pqHeap = new PQHeap(n);
        buildPQHeap(pqHeap, _bytes);
        for (int i = 1; i < n; i++) {
            Node z = new Node();
            HuffManTree ht = (HuffManTree) pqHeap.extractMin().data;
            z.setLeftChild(ht.getRoot());
            ht = (HuffManTree) pqHeap.extractMin().data;
            z.setRightChild(ht.getRoot());
            HuffManTree data = new HuffManTree(z);
            System.out.println("left child byte frequency" + _bytes[z.getLeftChild().get_byte()]);
            System.out.println("right child byte frequency" + _bytes[z.getRightChild().get_byte()]);
            Element element = new Element(_bytes[z.getLeftChild().get_byte()] + _bytes[z.getRightChild().get_byte()], data);
            pqHeap.insert(element);
        }
        return pqHeap.extractMin();
    }*/
    public Element huffman(int[] _bytes) {
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
        return pqHeap.extractMin();
    }

    public void toFile(String[] passwords) throws FileNotFoundException {

        //Write all the occurances
        //write every bytes keyword
        fileOutput = new FileOutputStream(new File(encodedFileName));
        bitOutput = new BitOutputStream(fileOutput);
        fileInput = new FileInputStream(new File(fileName));

        for (int i = 0; i < getBytes().length; i++) {
            try {
                bitOutput.writeInt(getBytes()[i]);
            } catch (IOException ex) {
                Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int _byte;
        try {

            while ((_byte = fileInput.read()) != -1) {
                //System.out.println("byte frequency is: " + _byte);
                //System.out.println("keywords at _byte is: " + keywords[_byte]);
                String string = keywords[_byte];
                if (string != "" && string != null) {
                    if (string.length() <= 9) {
                        bitOutput.writeInt(Integer.parseInt(keywords[_byte]));
                    } else {
                        bitOutput.writeInt(Integer.parseInt(keywords[_byte].substring(0,9)));
                        bitOutput.writeInt(Integer.parseInt(string.substring(9, string.length())));
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        } finally {
            try {
                if (fileInput != null) {
                    fileInput.close();
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
