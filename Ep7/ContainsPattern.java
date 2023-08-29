/*********************************************************************

  AO PREENCHER ESSE CABEÇALHO COM O MEU NOME E O MEU NÚMERO USP,
  DECLARO QUE SOU O ÚNICO AUTOR E RESPONSÁVEL POR ESSE PROGRAMA.
  TODAS AS PARTES ORIGINAIS DESSE EXERCÍCIO-PROGRAMA (EP) FORAM
  DESENVOLVIDAS E IMPLEMENTADAS POR MIM SEGUINDO AS INSTRUÇÕES DESSE
  EP E QUE PORTANTO NÃO CONSTITUEM PLÁGIO. DECLARO TAMBÉM QUE SOU
  RESPONSÁVEL POR TODAS AS CÓPIAS DESSE PROGRAMA E QUE EU NÃO
  DISTRIBUI OU FACILITEI A SUA DISTRIBUIÇÃO. ESTOU CIENTE DE QUE OS
  CASOS DE PLÁGIO SÃO PUNIDOS COM REPROVAÇÃO DIRETA NA DISCIPLINA.

  NOME:	LUCAS QUARESMA MEDINA LAM
  NUSP: 117963399

  Referências: com a exceção de códigos fornecidos no enunciado e em
  aula, caso você tenha utilizado alguma referência, liste-as
  explicitamente para que seu programa não seja considerada plágio.

  Exemplo:

  - Meu programa é baseado no programa DoublyLinkedList.java em
    https://algs4.cs.princeton.edu/13stacks/DoublyLinkedList.java.html

*********************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class ContainsPattern {

  	public static void main(String[] args)  {
    	In in = new In(args[0]);
    	// use TST to hold a set of substrings and yours respective string;
    	TSTPlus<Queue> st = new TSTPlus<Queue>(); 

    	while (!in.isEmpty()){
    		String key = in.readString();
    	  	StringBuilder subKey = new StringBuilder(key);

    	  	while (subKey.length() != 0) {
    	  	  	Queue<String> matchedKeys = new Queue<String>();
    	  	  	if (st.get(subKey.toString()) != null) matchedKeys = st.get(subKey.toString());
    	  	  	else matchedKeys = new Queue<String>(); 

    	  	  	st.put(subKey.toString(), matchedKeys);
    	  	  	subKey.deleteCharAt(0);
    	  	  	matchedKeys.enqueue(key.toString());
    	  	}
    	}

    	while (!StdIn.isEmpty()) {
			String p = StdIn.readString();
    	  	TST<Integer> words = new TST<Integer>();
			for (String s : st.keysThatStartWith(p)) {
    	    	Queue<String> matchedKeys = st.get(s);
    	    	while (!matchedKeys.isEmpty()) {
    	    		words.put(matchedKeys.dequeue(), 0);
    	    	}
			}
    	  	StdOut.println("Words that contain " + p + " (" + words.size() + ")");
			for (String w : words.keys()) StdOut.println(w);
			StdOut.println("- * - * -");	    
			}
  		}
}
