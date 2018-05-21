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

    public Integer search(Node root, Integer _byte) {
        if (root == null || root.get_byte() == _byte) {
            return root;
        }
        if (root.get_byte() == _byte) {
            return root.get_byte();
        }
        if (root.get_byte() > _byte) {
            return search(root.getRightChild(), _byte);
        }
        return search(root.getLeftChild(), _byte);
    }

    public Node getRoot() {return this.root;}

}

