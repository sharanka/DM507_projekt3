//Mikkel Bytoft Rasmussen - mikra16@student.sdu.dk
//Sharanka Shanmugalingam - shsha15@student.sdu.dk
//Niklas Brasch Pedersen - niklp16@student.sdu.dk
//Software Engineering 4. semester
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class HuffManTree {

    private Node root; // the root of the tree
    private FileOutputStream output; // the outputstream used to write to create a bitoutputstream
    private FileInputStream input; // the input stream used to read from a file
    private BitOutputStream bitOutput; // the bitoutputstream used to write bits and ints to a file

    public HuffManTree(Node root) { //constructor to create a new huffmantree with a node as root
        this.root = root;
    }

    public String[] convert(String[] keywords, String path, Node current) { // used to generate the list of keywords
        if (current.getLeftChild() != null) { // if the left child is not null, we go left in the tree
            // and add "0" to the path and check again with a recursive call
            convert(keywords, path.concat("0"), current.getLeftChild());
        }
        if (current.getRightChild() != null) { // if the right child is not null, we go right in the tree
            // and add "1" to the path and check again with a recursive call
            convert(keywords, path.concat("1"), current.getRightChild());
        }
        if (current.getLeftChild() == null && current.getRightChild() == null) { // when both childs are null
            // we are at the bottom of the tree and add the path to the keywords
            keywords[current.getByte()] = path;
        }
        return keywords; // returns the list of keywords
    }

    public void toFile(int[] frequencies, String[] keywords, String inputPath, String outputPath ) {
        // writes the content to a file through the bitoutputstream
        try {
            output = new FileOutputStream(outputPath); // outputstream to the file at the outputPath
            input = new FileInputStream(inputPath); // inputstream from the file at the inputPath
            bitOutput = new BitOutputStream(output); // bitoutputstream to write to the file at outputPath
            for (int i = 0; i < frequencies.length; i++) { // loops through each frequency stored in the table
                // of frequencies as writes those to the file at outputPath
                bitOutput.writeInt(frequencies[i]);
            }
            int _byte; // the byte currently being read
            while ((_byte = input.read()) != -1) { // reads through the file at inputPath
                String keyword = keywords[_byte]; // string representing the keyword
                // of the byte currently being read
                char[] keywordToCharArray = keyword.toCharArray(); // converts the keyword to a char array
                // so it can be written to the file at outputPath one bit at a time
                for (char c : keywordToCharArray) {
                    bitOutput.writeBit(Integer.parseInt(String.valueOf(c)));
                    // writes one bit to the file at outputPath
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Node getRoot() {
        return root; // returns the root of the tree
    }

}
