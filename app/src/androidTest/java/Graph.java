import com.example.graphproject.Advertisement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graph {

        private Map<String, List<Advertisement>> graph = new HashMap<>();

        public void addEdge(String source, Advertisement destination) {

            graph.get(source).add(destination);

        }



        public boolean hasEdge(String source, Advertisement destination) {
            if(graph.get(source).contains(destination)) {
                return true;
            }else {
                return false;
            }
        }


        private void addVertex(String vertex) {
            graph.put(vertex, new LinkedList<Advertisement>());
        }
    }
