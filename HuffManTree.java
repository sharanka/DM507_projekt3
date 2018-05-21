//Mikkel Bytoft Rasmussen - mikra16@student.sdu.dk
//Sharanka Shanmugalingam - shsha15@student.sdu.dk
//Niklas Brasch Pedersen - niklp16@student.sdu.dk
//Software Engineering 4. semester
public class HuffManTree {

    private Node root;

    public HuffManTree(int key) {
        this.root = new Node(key);
    }

    public HuffManTree(Node root) {
        this.root = root;
    }

    public Node getRoot() {return this.root;}

}

