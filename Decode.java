import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Decode {
    private int[] bytes = new int[256];
    private String fileName;
    private FileInputStream fileInput;
    private BitInputStream bitInput;

    public void scanInput() throws FileNotFoundException, IOException {
        int counter = 0;
        for (int i = 0; i < getBytes().length; i++) {
            getBytes()[i] = 0;
        }
        int _byte;
        try {
            fileInput = new FileInputStream(new File(fileName));
            bitInput = new BitInputStream(fileInput);

            while ((_byte = bitInput.readBit()) != -1 && counter < bytes.length) {
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

    public int[] getBytes() {
        return bytes;
    }
}
