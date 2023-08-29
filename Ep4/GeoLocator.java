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
import edu.princeton.cs.algs4.ST;

public class GeoLocator{
    private static ST<Long, Location> dict = new ST<Long, Location>(); 
    
    public GeoLocator(String filename){
        In file = new In(filename);
        
        while (!file.isEmpty()){
            String line = file.readLine();
            TokenFinder token = new TokenFinder(line);
            
            long ip1 = IPConv.noip(token.nextToken());
            long ip2 = IPConv.noip(token.nextToken());
            String region = token.nextToken();
            String country = token.nextToken();
            String state = token.nextToken();
            String city = token.nextToken();
            Double latitude = Double.parseDouble(token.nextToken());
            Double longitude = Double.parseDouble(token.nextToken());
            
            Location loc = new Location(region, country, state, city, latitude, longitude);
            
            dict.put(ip1, loc);
        }

    }
    public Location location(String q){
        long ip = IPConv.noip(q);
        if(ip < dict.min() || ip > dict.max()){
            return null;
        }
        return dict.get(dict.floor(IPConv.noip(q)));
    }
}


