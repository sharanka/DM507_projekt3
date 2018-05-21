//Mikkel Bytoft Rasmussen - mikra16@student.sdu.dk
//Sharanka Shanmugalingam - shsha15@student.sdu.dk
//Niklas Brasch Pedersen - niklp16@student.sdu.dk
//Software Engineering 4. semester
public class Node {
    private Node leftChild;
    private Node rightChild;
    private Integer _byte; // Freqcuency for subtree

    public Node (int _byte) {
        this._byte = _byte;
        this.leftChild = null;
        this.rightChild = null;
    }

    public Node(Object o, Object o1) {
        this._byte = null;
        this.leftChild = (Node) o;
        this.rightChild = (Node) o1;
    }

    public Node () {
        this.leftChild = null;
        this.rightChild = null;
        this._byte = null;
    }

    public Node getLeftChild() { return this.leftChild; } // returns the left child

    public void setLeftChild(Node child) { this.leftChild = child; } // changes the left child

    public Node getRightChild() { return this.rightChild; } // returns the right child

    public void setRightChild(Node child) { this.rightChild = child; } // change the right child

    public Integer get_byte() { return this._byte; } // returns the key

    public void set_byte(int _byte) { this._byte = _byte;}

}
