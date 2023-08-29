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

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;

public class TwoSAT
{   
    private static final int INFINITY = Integer.MAX_VALUE; 
    private boolean hasSolution;
    private boolean[] marked;
    private boolean[] marked_bfs;
    private boolean[] assignment;
    private int[] components;
    private int[] edgeTo;      
    private int[] distTo;     
    private int xUnSAT;
    private String unSATProof;
    

    public TwoSAT(ImplicationGraph impgr){
        Digraph G = impgr.g();
        Stack<Integer> stack = new Stack<Integer>();
        marked = new boolean[G.V()];
        
        components = new int[G.V()];
        
        int variables = G.V()/2;
        assignment = new boolean[variables+1];

        hasSolution = computeTwoSAT(G);
        if(!hasSolution){
            unSATProof = "";
            makeProof(G, xUnSAT, xUnSAT + 1);
            unSATProof += '\n';
            makeProof(G, xUnSAT + 1, xUnSAT);
            unSATProof += '\n';
        }
    }

    private boolean computeTwoSAT(Digraph G){
        
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < G.V(); i++) {
            if (!marked[i])
                dfs1(G, i, stack);
        }

        Digraph reverseG = G.reverse();
        int cnt = 0;
        while(!stack.isEmpty()){
            int v = stack.pop();
            if(components[v] == 0){
                cnt++;
                dfs2(reverseG, v, cnt);
            }
        }

        for(int i = 0; i < G.V(); i += 2){
            if(components[i] == components[i+1]){
                xUnSAT = i;
                return false;
            }
            assignment[(i/2)+1] = components[i] > components[i+1];
        }
        return true;
    }

    public boolean hasSolution(){
        return hasSolution;
    }

    public boolean[] assignment(){
        return assignment;
    }

    public String unSATProof(){
        return unSATProof;
    }

    private void dfs1(Digraph G, int v, Stack<Integer> stack) {
        marked[v] = true;
        for (int u : G.adj(v)) {
            if (!marked[u])
                dfs1(G, u, stack);
        }
        stack.push(v);
    }

    private void dfs2(Digraph reverseG, int v, int count) {
        components[v] = count;
        for (int u : reverseG.adj(v)) {
            if (components[u] == 0)
                dfs2(reverseG, u, count);
        }
    }

    private void bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        marked_bfs[s] = true;
        distTo[s] = 0;
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked_bfs[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked_bfs[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    private Iterable<Integer> pathTo(int v) {

        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }

    public boolean hasPathTo(int v) {
        return marked_bfs[v];
    }

    private void makeProof(Digraph G, int from, int to){
        marked_bfs = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            distTo[i] = INFINITY; 
        }
        bfs(G, from);
        if (hasPathTo(to)){
            for (int x : pathTo(to)) {
                if (x == from){
                    if(x % 2 > 0){
                        unSATProof += -((x/2 + 1));
                    }
                    else{
                        unSATProof += ((x/2) + 1);
                    }
                }  
                else{
                    if(x % 2 > 0){
                        unSATProof = unSATProof + " => " + (-((x/2 + 1)));
                    }
                    else{
                        unSATProof = unSATProof + " => " + ((x/2) + 1);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        In in;
        if (args.length > 0) in = new In(args[0]);
        else in = new In();
        ImplicationGraph impgr = new ImplicationGraph(in);
        TwoSAT twoSAT = new TwoSAT(impgr);
    
        if (!twoSAT.hasSolution()) { 
            StdOut.println("Not satisfiable:\n");
            StdOut.println(twoSAT.unSATProof());
            StdOut.println("Check the graph:\n");
            StdOut.print(impgr);
        } else {
            StdOut.println("Satisfying assignment:");
            boolean[] s = twoSAT.assignment();
            for (int i = 1; i < s.length; i++) 
            StdOut.println(i + ": " + s[i]);
            impgr.evalFull(s);
            StdOut.println("Truth value of 2SAT: " + impgr.eval(s));
        }
        }
}

