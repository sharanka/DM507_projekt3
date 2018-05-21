import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Decode {
    private int[] bytes = new int[256];
    private String fileName;
    private String decodedName;
    private FileInputStream fileInput;
    private BitInputStream bitInput;
    private FileOutputStream fileOutput;
    private HuffManTree ht;
    private Encode e;

    public void scanInput() throws FileNotFoundException, IOException {
        int counter = 0;
        for (int i = 0; i < getBytes().length; i++) {
            getBytes()[i] = 0;
        }
        int _byte;
        int total = 0;
        try {
            fileInput = new FileInputStream(new File(fileName));
            bitInput = new BitInputStream(fileInput);
            fileOutput = new FileOutputStream(new File(decodedName));

            while ((_byte = bitInput.readInt()) != -1) {
                if (counter < getBytes().length) {
                    getBytes()[_byte]++;
                    counter++;
                }
            }
            e = new Encode();
            ht = (HuffmanTree) e.huffman(bytes).data;
            for (int i : bytes) {
                total += bytes[i];
            }
            counter = 0;
            while (_byte = bitInput.readBit() != -1) {
                if (counter < getBytes().length) {
                    counter++;
                    continue;
                }
                if (ht.search(ht.getRoot(), _byte) != null) {
                    
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
        System.out.println("end scan input");
    }

    public int[] getBytes() {
        return bytes;
    }
}
