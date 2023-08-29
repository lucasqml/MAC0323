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

import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Ortho {

    public static boolean binarySearch(String vetor[], String key) 
    {

        int lo = 0;
        int hi = vetor.length - 1;
        while (lo <= hi) 
        {
            // Key is in a[lo..hi] or not present.
            int mid = lo + (hi - lo) / 2;
            if      (key.compareTo(vetor[mid]) < 0) hi = mid - 1;
            else if (key.compareTo(vetor[mid]) > 0) lo = mid + 1;
            else return true;
        }
        return false;

    }
    public static void main(String[] args) 
    {

        // 
        In dic = new In(args[0]);
        String[] allowlist = dic.readAllStrings();

        // read integer key from standard input; print if not in allowlist
        String s = StdIn.readAll();
	    s = s.replaceAll("[^A-Za-z \n]", " ");
	    String[] words = s.split("\\s+");

        for (int i=0; i < words.length; i++)
        {

            if (binarySearch(allowlist, words[i]) == false)
            {
                if(binarySearch(allowlist, words[i].toLowerCase()) == false)
                {
                    StdOut.println(words[i]);
                }
            }
        }
    }
}
