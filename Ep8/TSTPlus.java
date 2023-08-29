/*********************************************************************

  AO PREENCHER ESSE CABEÇALHO COM O MEU NOME E O MEU NÚMERO USP,
  DECLARO QUE SOU O ÚNICO AUTOR E RESPONSÁVEL POR ESSE PROGRAMA.
  TODAS AS PARTES ORIGINAIS DESSE EXERCÍCIO-PROGRAMA (EP) FORAM
  DESENVOLVIDAS E IMPLEMENTADAS POR MIM SEGUINDO AS INSTRUÇÕES DESSE
  EP E QUE PORTANTO NÃO CONSTITUEM PLÁGIO. DECLARO TAMBÉM QUE SOU
  RESPONSÁVEL POR TODAS AS CÓPIAS DESSE PROGRAMA E QUE EU NÃO
  DISTRIBUI OU FACILITEI A SUA DISTRIBUIÇÃO. ESTOU CIENTE DE QUE OS
  CASOS DE PLÁGIO SÃO PUNIDOS COM REPROVAÇÃO DIRETA NA DISCIPLINA.

  NOME: Lucas Quaresma Medina Lam
  NUSP: 11796399

  Referências: com a exceção de códigos fornecidos no enunciado e em
  aula, caso você tenha utilizado alguma referência, liste-as
  explicitamente para que seu programa não seja considerada plágio.

  Exemplo:

  - Meu programa é baseado no programa DoublyLinkedList.java em
    https://algs4.cs.princeton.edu/13stacks/DoublyLinkedList.java.html

*********************************************************************/

/******************************************************************************
 *  Compilation:  javac TSTPlus.java
 *  Execution:    java TSTPlus < words.txt
 *  Dependencies: StdIn.java
 *  Data files:   https://algs4.cs.princeton.edu/52trie/shellsST.txt
 *
 *  Symbol table with string keys, implemented using a ternary search
 *  trie (TSTPlus).
 *
 *
 *  % java TSTPlus < shellsST.txt
 *  keys(""):
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 *  longestPrefixOf("shellsort"):
 *  shells
 *
 *  keysWithPrefix("shor"):
 *  shore
 *
 *  keysThatMatch(".he.l."):
 *  shells
 *
 *  % java TSTPlus
 *  theory the now is the time for all good men
 *
 *  Remarks
 *  --------
 *    - can't use a key that is the empty string ""
 *
 *  Fiddled version of TST.java by yk
 * 
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

/**
 *  The {@code TSTPlus} class represents an symbol table of key-value
 *  pairs, with string keys and generic values.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides character-based methods for finding the string
 *  in the symbol table that is the <em>longest prefix</em> of a given prefix,
 *  finding all strings in the symbol table that <em>start with</em> a given prefix,
 *  and finding all strings in the symbol table that <em>match</em> a given pattern.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a ternary search trie.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class TSTPlus<Value> {
    private int n;              // size
    private Node<Value> root;   // root of TSTPlus
    private int maxLength;      // max length of keys
	
    private static class Node<Value> {
        private char c;                        // character
        private Node<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TSTPlus() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) n++;
        else if (val == null) n--;       // delete existing key
        root = put(root, key, val, 0);
	if (key.length() > maxLength) maxLength = key.length();
	if (val == null && key.length() == maxLength) {
	    int t = 0;
	    for (String s : keys())
		if (s.length() > t) t = s.length();
	    maxLength = t;
	}
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     *     or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (query.length() == 0) return null;
        int length = 0;
        Node<Value> x = root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
	if (prefix.length() == 0) return keys();
        Queue<String> queue = new Queue<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != null) queue.enqueue(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }


    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }
 
    private void collect(Node<Value> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pattern, queue);
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.val != null) queue.enqueue(prefix.toString() + x.c);
            if (i < pattern.length() - 1) {
                collect(x.mid, prefix.append(x.c), i+1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pattern, queue);
    }


    /* Added methods */

    /**
     * Returns the maximum string in the symbol table with a given prefix
     * or null, if no such string.
     */
    public String maxWithPrefix(String prefix) {
	if (prefix == null)
            throw new IllegalArgumentException("calls maxWithPrefix() with null argument");
    if (prefix.compareTo("") == 0){
        return getRestMax(root);
    }

    Node<Value> x = get(root, prefix, 0);

    if (x == null || x.mid == null) return null;
    String rest = getRestMax(x.mid);
    if (rest == null) return null;
	return prefix + rest;
    }

    private String getRestMax(Node<Value> x){

        if(x.right != null){
            return getRestMax(x.right);
        }
        if(x.mid != null){
            return x.c + getRestMax(x.mid);
        }
        return "" + x.c;
    }

    /**
     * Returns the minimum string in the symbol table with a given prefix
     * or null, if no such string.
     */
    public String minWithPrefix(String prefix) {
	if (prefix == null)
            throw new IllegalArgumentException("calls minWithPrefix() with null argument");
    if (prefix.compareTo("") == 0){
        return getRestMin(root);
    }

    Node<Value> x = get(root, prefix, 0);

    if (x == null || x.mid == null) return null;
    String rest = getRestMin(x.mid);
    if (rest == null) return null;
	return prefix + rest;
    }

    private String getRestMin(Node<Value> x){
        
        if(x.left != null){
            return getRestMin(x.left);
        }
        if(x.val != null){
            return "" + x.c;
        }
        if(x.mid != null){
            return x.c + getRestMin(x.mid);
        }
        return "" + x.c;
    }

    /**
     * Returns all of the keys in the symbol table that start with 
     * a match of {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatStartWith(String pattern) {
	    Queue<String> queue = new Queue<String>();
        makePrefix(root, "", 0, pattern, queue);
        return queue;
    }

    private void prefixMatch(Node<Value> x, String prefix, Queue<String> queue) {
        if (x == null) return;
        prefixMatch(x.left, prefix, queue);
        if (x.val != null) queue.enqueue(prefix + String.valueOf(x.c));
        prefixMatch(x.mid, prefix + String.valueOf(x.c), queue);
        prefixMatch(x.right, prefix, queue);
    }
    
    private void makePrefix(Node<Value> x, String prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        if (i == pattern.length()){
            prefixMatch(x, prefix, queue);
            return;
        }
        char c = pattern.charAt(i);
        if (c == '.'){
            makePrefix(x.left, prefix, i, pattern, queue);
            if (i == pattern.length() - 1 && x.val != null) queue.enqueue(prefix + String.valueOf(x.c));
            makePrefix(x.mid, prefix + String.valueOf(x.c), i+1, pattern, queue);
            makePrefix(x.right, prefix, i, pattern, queue);
        }
        else{
            if (c < x.c){
                makePrefix(x.left, prefix, i, pattern, queue);
            }
            if (c == x.c){
                if (i == pattern.length() - 1 && x.val != null) queue.enqueue(prefix + String.valueOf(x.c));
                makePrefix(x.mid, prefix + String.valueOf(x.c), i+1, pattern, queue);
            }
            if (c > x.c){
                makePrefix(x.right, prefix, i, pattern, queue);
            }
        }
    }

    public String floor(String query){
        if (query == null) throw new IllegalArgumentException("argument to ceilingfloor() is null");
        String x = floor(root, query, "", 0);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else           return x;  
    }
    
    private String floor(Node<Value> x, String query, String prefix, int d) {
        char c = query.charAt(d);
        Node<Value> node = x;
        //procura o prefixo da palavra na arvore
        while (node != null && node.c != c) {
            if (node.c > c) node = node.left; 
            else node = node.right;
        }
        //node == c
        if (node != null) {
            //chama recursivamente até acabar a string de entrada
            if (d + 1 < query.length()) {
                String word = floor(node.mid, query, prefix + node.c, d + 1);
                if (word != null) return word;
            } 
            else {
                if (node.val != null) return query;
            }
        }
        //node == null ou ainda não achou a maior palavra anterior
        node = x;
        Node<Value> floorNode = null;
        //procura o maior nó menor que 'c'
        while (node != null) {
            while (node != null && node.c >= c) {
                node = node.left;
            }
            if (node != null) {
                if (floorNode == null || node.c > floorNode.c) {
                floorNode = node;
                }
            }
            while (node != null && node.c < c) {
                if (floorNode == null || node.c < floorNode.c) {
                    floorNode = node;
                }
                node = node.right;
            }
            if (node != null) node = node.left;
        }
        //se achou, retorna a maior palavra com aquele prefixo
        if (floorNode != null) return maxWithPrefix(prefix + floorNode.c);
        else return null;
    }
    
    public String ceiling(String query) {
        if (query == null) throw new IllegalArgumentException("argument to ceiling() is null");
        String x = ceiling(root, query, "", 0);
        if (x == null) throw new NoSuchElementException("argument to ceiling() is too small");
        else           return x;  
    }

    private String ceiling(Node<Value> x, String query, String prefix, int d) {
        char c = query.charAt(d);
        Node<Value> node = x;
        //procura o prefixo da palavra na arvore
        while (node != null && node.c != c) {
            if (node.c > c) node = node.left;
            else node = node.right;  
        }
        //node == c
        if (node != null) {
            //chama recursivamente até acabar a string de entrada
            if (d + 1 < query.length()) {
                String word = ceiling(node.mid, query, prefix + node.c, d + 1);
                if (word != null) return word; 
            } 
            else {
                if (node.val != null) return query;
                //a string tem alguma chave como prefixo e queremos a menor destas
                else return minWithPrefix(query);
            }
        }
        //node == null ou ainda não achou a menor palavra posterior
        node = x;
        Node<Value> ceilNode = null;
        //procura o menor nó maior que 'c'
        while (node != null) {
            while (node != null && node.c <= c) {
                node = node.right;
            }
            if (node != null) {
                if (ceilNode == null || node.c < ceilNode.c) {
                    ceilNode = node;
                }
            }
            while (node != null && node.c > c) {
                if (ceilNode == null || node.c < ceilNode.c) {
                    ceilNode = node;
                }
                node = node.left;
            }
            if (node != null) node = node.right;
        }
        //se achou, retorna a menor palavra com aquele prefixo
        if (ceilNode != null) return minWithPrefix(prefix + ceilNode.c);
        else return null;
    }
 
    /**
     * Unit tests the {@code TSTPlus} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // build symbol table from standard input
        TSTPlus<Integer> st = new TSTPlus<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.keys()) {
                StdOut.println(key + " " + st.get(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"shell\"):");
        StdOut.println(st.longestPrefixOf("shell"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}

/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
