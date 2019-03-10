import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLEdge;
import net.sourceforge.gxl.GXLGraphElement;
import net.sourceforge.gxl.GXLNode;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DfsMain {

    public static void main(String[] args) throws IOException, SAXException {
        GXLDocument gxlDocument = new GXLDocument(new File("simpleExample.gxl"));

        List<String> idNode = new ArrayList<String>();

        Map<String,ArrayList<String>> _mapa = new HashMap<String, ArrayList<String>>();

        if (gxlDocument == null)
        {
            System.out.println("file not loaded");
            return;
        }

        //pobranie informacji o liczbie lementów w grafie nr 0
        int elements = gxlDocument.getDocumentElement().getGraphAt(0).getGraphElementCount();
        System.out.println("liczba elementow grafu " + elements);

        //zmienna do przechowywania ilości wierzchołków
        int liczbaNodow = 0;

        for (int i = 0; i < elements; i++) {
            //pobranie elementu z grafu
            GXLGraphElement graphElementAt = gxlDocument.getDocumentElement().getGraphAt(0).getGraphElementAt(i);

            //sprawdzamy czy element jest wierzchołkiem
            if (graphElementAt instanceof GXLNode) {

                //dodajemy jego id/nazwę do kolejkcji
                idNode.add(graphElementAt.getID());

                //jeżeli wierzchołek jeszcze nie jest w kolejkcji mapowania to dodajemy go
                if(!_mapa.containsKey(graphElementAt.getID()))
                {
                    _mapa.put(graphElementAt.getID(),new ArrayList<String>());
                }

                //podnosimy liczbę wierzchołków
                liczbaNodow++;
            }

            //sprawdzamy czy element jest krawędzią
            if ((graphElementAt instanceof GXLEdge)) {

                //dodajemy informację o tym że jeden wierzchołek wskazuje na drugi
                //najpierw zabezpieczenie żeby sprawdzić czy mamy wierzchołek źródłowy w mapie
                if(!_mapa.containsKey(((GXLEdge) graphElementAt).getSourceID()))
                {
                    _mapa.put(((GXLEdge) graphElementAt).getSourceID(),new ArrayList<String>());
                    _mapa.get(((GXLEdge) graphElementAt).getSourceID()).add(((GXLEdge) graphElementAt).getTargetID());
                }else
                {
                    _mapa.get(((GXLEdge) graphElementAt).getSourceID()).add(((GXLEdge) graphElementAt).getTargetID());
                }

                if(!((GXLEdge) graphElementAt).isDirected())
                {
                    //teraz dodajemy w drugą stronę z wierzchołka docelowego do źrodłowego
                    if(!_mapa.containsKey(((GXLEdge) graphElementAt).getTargetID()))
                    {
                        _mapa.put(((GXLEdge) graphElementAt).getTargetID(),new ArrayList<String>());
                        _mapa.get(((GXLEdge) graphElementAt).getTargetID()).add(((GXLEdge) graphElementAt).getSourceID());
                    }else
                    {
                        _mapa.get(((GXLEdge) graphElementAt).getTargetID()).add(((GXLEdge) graphElementAt).getSourceID());
                    }
                }


            }
        }

        //dodaje 2 spacje na początku żeby ładnie wyświetlić macierz
        System.out.printf("  ");

        //wypisuje sobie wszystkie nody dla kolumn
        for (String id : idNode) {
            System.out.printf(id + " ");
        }

        System.out.printf("\n");

        int adjacencyMatrix[][] = new int[liczbaNodow][liczbaNodow];

        for (int i = 0; i < liczbaNodow; i++)//wiersze
        {
            for (int j = 0; j < liczbaNodow; j++)//kolumny
            {
                //wypisanie nazwy noda dla wiersza
                if (j == 0)
                {
                    System.out.printf(idNode.get(i) + " ");
                }

                //jeżeli wieżchołek i wskazuje na j
                if (_mapa.get(idNode.get(i)).contains(idNode.get(j)))
                {
                    System.out.printf("1 ");
                    adjacencyMatrix[i][j] = 1;
                }else
                {
                    System.out.printf("0 ");
                    adjacencyMatrix[i][j] = 0;
                }
                //System.out.printf("%d",matrix[i][j]);
            }
            System.out.printf("\n");
        }

        //dfs
        DepthFirstSearch dfs = new DepthFirstSearch();

        ArrayList<DepthFirstSearch.Node> nodes = new ArrayList<>();

        //dodanie wierzchołków
        for (String id : idNode) {

            DepthFirstSearch.Node newNode = new DepthFirstSearch.Node(id);

            nodes.add(newNode);

            dfs.AddNode(newNode);
        }

        //wylicz dsf na podstawie macierzy sąsiedztwa oraz wierzchołka początkowego
        dfs.dfs(adjacencyMatrix, nodes.get(0));
        dfs.clearVisitedFlags();
        dfs.dfsUsingStack(adjacencyMatrix, nodes.get(0));

        //testowa prosta funkcja
        //System.out.println("");
        //depthFirstSearch(adjacencyMatrix,1);
    }

    /*static void depthFirstSearch(int[][] matrix, int source){
        boolean[] visited = new boolean[matrix.length];
        visited[source-1] = true;
        Stack<Integer> stack = new Stack<>();
        stack.push(source);
        int i,x;
        System.out.println("The depth first order is");
        System.out.println(source);
        while(!stack.isEmpty()){
            x = stack.pop();
            for(i=0; i<matrix.length; i++){
                if(matrix[x-1][i] == 1 && visited[i] == false){
                    stack.push(x);
                    visited[i] = true;
                    System.out.println(i+1);
                    x = i+1;
                    i = -1;
                }
            }
        }
    }*/
}
