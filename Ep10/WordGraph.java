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

/**
 * $ head -n1000 DATA/Pwords | java-algs4 WordGraph | head -n10
 * Time to build word graph: 0.182
 * a
 *    à
 * à
 *    a
 * Aarão
 * aba
 *    abas
 * abacate
 *    abacates
 * $ head -n1000 DATA/Pwords | java-algs4 WordGraph | tail -n10
 *    abeirados
 *    abeirada
 * abeirados
 *    abeirado
 *    abeiradas
 * abeirai
 *    abeirais
 *    abeira
 * abeirais
 *    abeirai
 * $ 
 */

import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Based on WordLadder.java
 */

public class WordGraph {
    private TST<Integer> st; // string -> index
    private String[] keys;           // index  -> string
    private Graph graph;             // the underlying graph

    private boolean sameLength = true;      // all words have the same length
    private int wordLength;          // is sameLength, all words have this length
    
    public WordGraph(In in) {
        st = new TST<Integer>();
        // First pass builds the index by reading strings to associate
        // distinct strings with an index
        while (!in.isEmpty()) {
            String[] a = in.readAllStrings();
	        wordLength = a[0].length();
            for (int i = 0; i < a.length; i++) {
                if (!st.contains(a[i])) {
                    st.put(a[i], st.size());
		            if (a[i].length() != wordLength) sameLength = false;
		        }
            }
        }
        // inverted index to get string keys in an array
        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }
        // second pass builds the graph
        graph = new Graph(st.size());
	    for (int i = 0; i < keys.length; i++) {
            boolean[] marked = new boolean[keys.length];
            String word1 = keys[i];
            int vert1 = st.get(word1);
            

            for(int j = 0; j < word1.length(); j++){
                String prefix = word1.substring(0, j);
                String suffix = word1.substring(j+1);

                Iterable<String> keys3 = st.keysThatMatch(prefix + '.' + suffix);
                for(String key3 : keys3){
                    int num3 = st.get(key3);
                    if(vert1 < num3){
                        if (!marked[num3] && word1.compareTo(key3) != 0){
                            graph.addEdge(st.get(word1), num3);
                            marked[num3] = true;
                        }
                    }
                }
                if(word1.length() > 1){
                    Iterable<String> keys4 = st.keysThatMatch(prefix + suffix);
                    for(String key4 : keys4){ 
                        int num4 = st.get(key4);
                            if (!marked[num4] && word1.compareTo(key4) != 0){
                                graph.addEdge(st.get(word1), num4);
                                marked[num4] = true;
                        }
                    }
                }
            }
	    }
    }

    // return true if two strings differ in exactly one letter
    // or the shorter string can be obtained from the longer string
    // deleting exactly one letter
    public static boolean isNeighbor(String a, String b) {
	if (a.length() == b.length()) { 
	    int differ = 0;
	    for (int i = 0; i < a.length(); i++) {
		if (a.charAt(i) != b.charAt(i)) differ++;
		if (differ > 1) return false;
	    }
	    return true;
	}
	if (a.length() + 1 == b.length() || a.length() == b.length() + 1) {
	    if (a.length() > b.length()) {
		    String t = a; a = b; b = t;
	    }
        
        int differ = 0;
	    for (int j = 0; j < a.length(); j++) {
            if(a.charAt(j-differ) != b.charAt(j)){
                differ++;
            }
	    }
        if (a.charAt(a.length()-1) != b.charAt(a.length())) differ++;
        if (differ > 1) return false;
	    return true;
	}
	return false;
    }

    public boolean contains(String s) {
        return st.contains(s);
    }

    public int indexOf(String s) {
        return st.get(s);
    }

    public String nameOf(int v) {
        validateVertex(v);
        return keys[v];
    }

    public Graph graph() {
        return graph;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = graph.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }


    public static void main(String[] args) {
        In in;
	if (args.length > 0) in = new In(args[0]);
	else in = new In();
	Stopwatch sw = new Stopwatch();
        WordGraph wg = new WordGraph(in);
	StdOut.println("Time to build word graph: " + sw.elapsedTime());
        Graph graph = wg.graph();
	for (int i = 0; i < graph.V(); i++) {
	    StdOut.println(wg.nameOf(i));
	    for (int j : graph.adj(i)) {
		StdOut.println("   " + wg.nameOf(j));
	    }
        }
    }
}
