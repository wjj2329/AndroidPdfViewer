package com.github.barteksc.pdfviewer.search;

import java.util.ArrayList;

//This uses the Trie data structure to store our words we collect from our pdf
public class Dictionary {

    private Node root = new Node();

    public void addWord(String word) {
        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            if (current.children[(int) word.charAt(i) - 'a'] == null) {
                current.children[(int) word.charAt(i) - 'a'] = new Node();
            }
            current = current.children[(int) word.charAt(i) - 'a'];
        }
        current.data.add(new PdfWordData());
    }

    public ArrayList<PdfWordData> getWordCount(String word) {
        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            if (current.children[(int) word.charAt(i) - 'a'] == null) {
                return null;
            }
            current = current.children[(int) word.charAt(i) - 'a'];
        }
        return current.data;
    }

    private class Node {
        Node[] children;
        ArrayList<PdfWordData> data = new ArrayList<>();

        Node() {
            children = new Node[26];
        }
    }

}
