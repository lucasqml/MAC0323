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

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class HashingProfile {
    private static int M;
    private static Node[] table;

    private static int[] primes = {
        1, // not quite a prime!
        2, 3, 7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
        32749, 65521, 131071,
        262139, 524287, 1048573, 2097143, 4194301, 8388593, 16777213, 33554393,
        67108859, 134217689, 268435399, 536870909, 1073741789, 2147483647
        };
    
    private static class Node {
        private String key;
        private Integer val;
        private Node next;

        public Node(String key, Integer val, Node next)  {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    private static int hashUsual(String key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    private static int hashUniversal(String key, int p) {
        int h = 0, a = 31415, b = 27183;
        String s = (String) key;
        for (int i = 0; i < s.length(); i++, a = a*b % (p-1))
            h = ((a*h + s.charAt(i)) & 0x7fffffff) % p;
        return h;
    }

    private static Node put(Node x, String key, Integer val){
        if (x == null) return new Node(key, val, null);
        if (key.compareTo(x.key) == 0){
            x.val = val;
            return x;
        }
        if (key.compareTo(x.key) < 0){
            return new Node(key, val, x);
        }
        x.next = put(x.next, key, val);
        return x;
    }

    private static int sizeChaining(Node x){
        if(x == null) return 0;
        int n = 1;
        while(x.next != null){
            x = x.next;
            n++;
        }
        return n;
    }

    public static void plotBars(double[] a) {
        int n = a.length;
        StdDraw.setXscale(-.5, n - .5);
        for (int i = 0; i < n; i++) {
            StdDraw.filledRectangle(i, a[i]/2, 0.35, a[i]/2);
        }
    }

    public static void main(String[] args){
        int cardinality = 0; 
        int countString = 0;
        int hash = 0;
        int hits = 0;
        boolean isUniversal = false;
        boolean printHistogram = false;
        boolean printKeys = false;

        cardinality = Integer.parseInt(args[0]);

        if(args[1].equals("-p")){
            M = primes[cardinality];
        } else {
            M = (int) Math.pow(2, cardinality);
        }

        table = new Node[M];

        if(args[2].equals("-u")){
            isUniversal = true;
        }
        if(args.length > 3){
            printHistogram = true;
        }
        if(args.length > 4){
            printKeys = true;
        }

        for (int i = 0; !StdIn.isEmpty(); i++){
            String key = StdIn.readString(); 
            if(isUniversal == true)
            {
                hash = hashUniversal(key, M);
            } else{
                hash = hashUsual(key);
            }
            
            table[hash] = put(table[hash], key, i);
        }

        for (int i = 0; i < M; i++){
            int p = sizeChaining(table[i]);
            if(p != 0){
                hits++;
                Node x = table[i];
                while(x != null){
                    countString++;
                    x = x.next;
                }
            }
        }

        StdOut.println("M = " + M);
        StdOut.println("M log M = " +  M * Math.log(M));
        StdOut.println("N = " + countString);
        double doubi = (double) countString/M;
        StdOut.printf("N/M = %f\n", (double) countString/M);
        
        if(printHistogram){
            int[] buckets = new int[M];
            for (int i = 0; i < M; i++) {
                int b = sizeChaining(table[i]); 
                buckets[i] = b;
            }
            
            int max = StdStats.max(buckets);
            double[] nbuckets = new double[M];
            for (int i = 0; i < M; i++) 
            nbuckets[i] = .5*buckets[i]/max;
            
            StdDraw.setCanvasSize(1200, 400);
            // StdStats.plotBars(nbuckets);
            plotBars(nbuckets);
        }
        
        if (printKeys){
            for (int i = 0; i < M; i++){
                int n = sizeChaining(table[i]);
                if(n != 0){
                    StdOut.printf("[ %d] %d:\n", n, i);
                    Node x = table[i];
                    while(x != null){
                        StdOut.println(x.key);
                        x = x.next;
                    }
                }
            }
        }
        StdOut.println("hits = " + hits);
        
    }

}