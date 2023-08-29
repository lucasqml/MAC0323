/*********************************************************************

  AO PREENCHER ESSE CABEÇALHO COM O MEU NOME E O MEU NÚMERO USP,
  DECLARO QUE SOU O ÚNICO AUTOR E RESPONSÁVEL POR ESSE PROGRAMA.
  TODAS AS PARTES ORIGINAIS DESSE EXERCÍCIO-PROGRAMA (EP) FORAM
  DESENVOLVIDAS E IMPLEMENTADAS POR MIM SEGUINDO AS INSTRUÇÕES DESSE
  EP E QUE PORTANTO NÃO CONSTITUEM PLÁGIO. DECLARO TAMBÉM QUE SOU
  RESPONSÁVEL POR TODAS AS CÓPIAS DESSE PROGRAMA E QUE EU NÃO
  DISTRIBUI OU FACILITEI A SUA DISTRIBUIÇÃO. ESTOU CIENTE DE QUE OS
  CASOS DE PLÁGIO SÃO PUNIDOS COM REPROVAÇÃO DIRETA NA DISCIPLINA.

  NOME: LUCAS QUARESMA MEDINA LAM
  NUSP: 11796399

  Referências: com a exceção de códigos fornecidos no enunciado e em
  aula, caso você tenha utilizado alguma referência, liste-as
  explicitamente para que seu programa não seja considerada plágio.

  Exemplo:

  - Meu programa é baseado no programa DoublyLinkedList.java em
    https://algs4.cs.princeton.edu/13stacks/DoublyLinkedList.java.html

*********************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.UF;
import edu.princeton.cs.algs4.Interval1D;


public class Threshold {

    public static boolean pointsConnected(double d, int N, double[] list)
    {
        int rows = (int) (1.0 / d);    // # rows in grid
        int cols = (int) (1.0 / d);    // # columns in grid
        
        // initialize data structure
        UF bag = new UF(N);
        Point2D[] points = new Point2D[N];
        Queue<Integer>[][] grid = (Queue<Integer>[][]) new Queue[rows+2][cols+2];
        for (int i = 0; i <= rows+1; i++){
            for (int j = 0; j <= cols+1; j++){
                grid[i][j] = new Queue<Integer>();
            }
        }

	    for (int k = 0; k < N; k++) { 
	        double x = list[2*k];
	        double y = list[2*k+1];
	        Point2D p  = new Point2D(x, y);
            int row = 1 + (int) (x * rows);
            int col = 1 + (int) (y * cols);
            grid[row][col].enqueue(k);
            points[k] = p;
            for (int i = row-1; i <= row+1; i++) {
                for (int j = col-1; j <= col+1; j++) {
                    for (int q : grid[i][j]){
                        if (p.distanceTo(points[q]) <= d) {
                            bag.union(k, q);
                        } 
                    }
                }
            }
        }
        
        if(bag.count() == 1)
            return true;
        else
            return false;
    }

    public static Interval1D getInterval(int N, double[] list)
    {
        Interval1D interval = new Interval1D(0, Math.sqrt(2));
        
        double mid;
        while (interval.length() > Math.pow(10, -9))
        {
            mid = ((interval.max() + interval.min()) / 2);
            if (pointsConnected(mid, N, list))
                interval = new Interval1D(interval.min(), mid);
            else 
                interval = new Interval1D(mid, interval.max());
        }
        return interval;
    }

    public static void draw(Interval1D interval, int N, double[] list)
    {
        double d = interval.max();

        int rows = (int) (1.0 / d);    // # rows in grid
        int cols = (int) (1.0 / d);    // # columns in grid
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.enableDoubleBuffering();	

        // initialize data structure
        Queue<Point2D>[][] grid = (Queue<Point2D>[][]) new Queue[rows+2][cols+2];
        for (int i = 0; i <= rows+1; i++)
            for (int j = 0; j <= cols+1; j++)
                grid[i][j] = new Queue<Point2D>();

            for (int k = 0; k < N; k++) { 
                double x = list[2*k];
                double y = list[2*k+1];
                Point2D p  = new Point2D(x, y);
                int row = 1 + (int) (x * rows);
                int col = 1 + (int) (y * cols);
                for (int i = row-1; i <= row+1; i++) {
                    for (int j = col-1; j <= col+1; j++) {
                        for (Point2D q : grid[i][j]){
                            double distancePQ = p.distanceTo(q);
                            if (distancePQ <= d){
                                if (distancePQ > interval.min()){
                                    StdDraw.setPenColor(StdDraw.RED);
                                }
                            StdDraw.setPenRadius(0.002);
                            p.drawTo(q);
                            StdDraw.setPenRadius(0.005);
                            }
                        StdDraw.setPenColor(StdDraw.BLACK);
                        }
                    }
                }
            grid[row][col].enqueue(p);
	        p.draw();
            }
	    StdDraw.show();	  
    }

    public static void main(String[] args)
    {
        double lista[] = StdIn.readAllDoubles();
        int N = lista.length/2;

        Interval1D solution = getInterval(N, lista);

        StdOut.println("Connectivity threshold in " + solution.toString());

        if (args.length > 0)
            draw(solution, N, lista);

    }
}
