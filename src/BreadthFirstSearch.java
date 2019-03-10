import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearch {

    private Queue<ListNode> queue;
    static ArrayList<ListNode> nodes=new ArrayList<ListNode>();

    public BreadthFirstSearch()
    {
        queue = new LinkedList<ListNode>();
    }

    public void bfs(ListNode node)
    {
        queue.add(node);
        node.visited=true;
        while (!queue.isEmpty())
        {
            ListNode element=queue.remove();
            System.out.print(element.data + " ");
            List<ListNode> neighbours=element.getNeighbours();
            for (int i = 0; i < neighbours.size(); i++) {
                ListNode n=neighbours.get(i);
                if(n!=null && !n.visited)
                {
                    queue.add(n);
                    n.visited=true;

                }
            }

        }
    }
}
