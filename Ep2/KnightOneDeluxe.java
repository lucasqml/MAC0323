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

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.IndexMinPQ;

public class KnightOneDeluxe {

    static int[][] board;
    static int N;
    static boolean imprimirFrase = false;

    static int[] iMove = {2, 1, -1, -2, -2, -1, 1, 2};
    static int[] jMove = {1, 2, 2, 1, -1, -2, -2, -1};

    private static void initializeBoard() {
	board = new int[N][N];
    }
	
	private static int jumps(int i, int j)
	{
		int cont = 0;
		for (int t = 0; t < 8; t++)
		{
			int ii = i + iMove[t];
			int jj = j + jMove[t];
			if(valid(ii, jj) && board[ii][jj] == 0)
			{
				cont++;
			}
		}
		return cont;
	}

    private static void findTours(int i, int j, int k) {
	
        
        if (!valid(i, j) || board[i][j] != 0) return;
        board[i][j] = k;
        if (k == N * N) {
            if(imprimirFrase == true)
            {
                StdOut.printf("There is a Knight's tour on a %dx%d board", N, N);
            }
            else
            {
                printBoard();
            }
            System.exit(0);
        } 
    
        IndexMinPQ<Integer> PQ = new IndexMinPQ<Integer>(8);

	    for (int t = 0; t < 8; t++) {

	        int ii = i + iMove[t];
	        int jj = j + jMove[t];
		    if(valid(ii, jj) && board[ii][jj] == 0)
		    {
		    	PQ.insert(t, jumps(ii, jj));
		    }
	}

	for (int u = 0; u < PQ.size(); u++)
	{
		int min = PQ.delMin();

		int ni = i + iMove[min];
		int nj = j + jMove[min];

		findTours(ni, nj, k + 1);
	}
	board[i][j] = 0;
    }


    private static void findTours() {
	initializeBoard();
	for (int i = 0; i < N; i++)
	    for (int j = 0; j < N; j++)
		findTours(i, j, 1);
    }

    private static void printBoard() {
	for (int i = 0; i < N; i++) {
	    for (int j = 0; j < N; j++) 
		StdOut.printf("%2d ", board[i][j]);
	    StdOut.println();
	}
    }

    private static boolean valid(int i, int j) {
	return 0 <= i && i < N && 0 <= j && j < N;
    }

    public static void main(String[] args) {
	N = Integer.parseInt(args[0]);
    imprimirFrase = args.length > 1;
	findTours();
	StdOut.println("Found no tours");
    }
}
