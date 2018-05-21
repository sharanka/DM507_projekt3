//Mikkel Bytoft Rasmussen - mikra16@student.sdu.dk
//Sharanka Shanmugalingam - shsha15@student.sdu.dk
//Niklas Brasch Pedersen - niklp16@student.sdu.dk
//Software Engineering 4. semester
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Decode {

    public static void main(String[] args) {
        try {
            FileInputStream input = new FileInputStream(args[0]); //Create a inputstream to read from the file
            //Specified from the first commandline argument
            BitInputStream bitInput = new BitInputStream(input); //Create a bitinputstream from the inputstream
            FileOutputStream output = new FileOutputStream(args[1]); //Create an output stream used to write to
            // the file specified in the second commandline argument
            int[] bytes = new int[256]; // the array that holds frequency of the bytes
            int totalBytes = 0; // the number of total bytes in the file
            for (int i = 0; i < 256; i++) { // loops through the first 256 bytes from the file, which is
                // where we expect the frequency table to be stored
                bytes[i] = bitInput.readInt(); // sets the freqcuency in the array that stores the frequency to
                // whatever is read from the file
                totalBytes += bytes[i]; // increases the amount of totalbytes with the frequency of the byte
                // that has just been read
            }
            Encode e = new Encode();
            HuffManTree ht = (HuffManTree) e.huffman(bytes).data; // Create a new huffman tree from the
            // frequency table create from the input file above
            Node current = ht.getRoot(); // start from the root
            int bit; // bits are read one at a time
            while ((bit = bitInput.readBit()) != -1 && totalBytes > 0) { // loops through each bit in the input file
                // and recreate the keywords from the huffmantree
                if (bit == 0) { // if the bit read is 0 then we go left in the huffmantree
                    current = current.getLeftChild();
                } else { // otherwise we go right
                    current = current.getRightChild();
                }
                if (current.getLeftChild() == null && current.getRightChild() == null) { // when we reach a node
                    // where both childs are null, then we know that we are at a bottom leaf
                    // and we can read the byte stored in that node and recreate the file step by step
                    output.write(current.getByte()); // writes the byte from the leaf node to the outputfile
                    // from the second commandline argument
                    totalBytes--; // decreases the amount of totalbytes as we have written one to a file
                    current = ht.getRoot(); // starts over from the root with the next byte
                }
            }
            bitInput.close(); // close the input stream
            output.close(); // close the output stream
        } catch (FileNotFoundException fnfe) {
            Logger.getLogger(Decode.class.getName()).log(Level.SEVERE, null, fnfe);
        } catch (IOException ioe) {
            Logger.getLogger(Decode.class.getName()).log(Level.SEVERE, null, ioe);
        }

    }

}
