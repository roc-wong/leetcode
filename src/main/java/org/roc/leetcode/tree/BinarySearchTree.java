package org.roc.leetcode.tree;

/**
 * 已知一个搜索二叉树后续遍历的数组postArr，请根据posArr，重建出整棵树， 返回新建树的头结点
 *
 * @author roc
 * @since 2020/5/8 15:48
 */
public class BinarySearchTree {

    public static void main(String[] args) {

        int[] postArr = new int[]{2, 4, 3, 6, 8, 7, 5};
        BinarySearchTree binarySearchTree = new BinarySearchTree();

        Node node = binarySearchTree.posArrayToBST1(postArr);
        posOrder(node);
    }

    public static void posOrder(Node node) {
        if (node != null) {
            if (node.getLeft() != null) {
                posOrder(node.getLeft());
            }
            if (node.getRight() != null) {
                posOrder(node.getRight());
            }
            System.out.print(node.getValue() + " ");
        }
    }

    private Node posArrayToBST1(int[] postArr) {

        return process1(postArr, 0, postArr.length - 1);

    }

    private Node process1(int[] postArr, int L, int R) {

        if (L > R) {
            return null;
        }

        Node head = new Node(postArr[R]);
        if (L == R) {
            return head;
        }
        int M = L - 1;
        for (int i = L; i < R; i++) {
            if (postArr[i] < postArr[R]) {
                M = i;
            }
        }
        head.setLeft(process1(postArr, L, M));
        head.setRight(process1(postArr, M + 1, R - 1));
        return head;
    }
}


class Node {

    private int value;

    private Node left;

    private Node right;

    public Node(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Node{");
        sb.append("value=").append(value);
        sb.append(", left=").append(left);
        sb.append(", right=").append(right);
        sb.append('}');
        return sb.toString();
    }
}