import java.util.*;

// Shaket Chaudhary and Juan Castano
// Fall 2016

public class GraphLib {
	public static <V, E> Graph<V, E> bfs(Graph<V, E> g, V source) {
		Graph<V, E> bfs = new AdjacencyMapGraph<V, E>();
		Queue<V> q = new LinkedList<V>();

		q.add(source);
		bfs.insertVertex(source);
		while (!q.isEmpty()) {
			V temp = q.remove();
			// Add neighbors to queue
			for (V neighbor : g.outNeighbors(temp)) {
				// if we haven't visited the neighbor add it to queue and add to
				// bfs
				if (!bfs.hasVertex(neighbor)) {
					bfs.insertVertex(neighbor);
					bfs.insertDirected(temp, neighbor, g.getLabel(temp, neighbor));
					q.add(neighbor);
				}
			}
		}
		return bfs;
	}

	public static <V, E> List<V> getPath(Graph<V, E> tree, V v) {
		List<V> path = new ArrayList<V>();
		if (!tree.hasVertex(v)) {
			return path;

		} else {
			path.add(v);
			getPathHelper(tree, v, path);
			return path;
		}
	}

	public static <V, E> void getPathHelper(Graph<V, E> tree, V v, List<V> path) {
		// check to see if the tree has neighbors
		if (tree.inDegree(v) > 0) {
			// iterate through the neighbors
			for (V neighbor : tree.inNeighbors(v)) {
				// if the neighbor is not already in our list
				if (!path.contains(neighbor)) {
					path.add(neighbor);
					getPathHelper(tree, neighbor, path);
				}
			}
		}
	}

	public static <V, E> Set<V> missingVertices(Graph<V, E> graph, Graph<V, E> subgraph) {
		Set<V> missingVertices = new HashSet<V>();
		// Compare the vertices from the subgraph to the big graph, if they're
		// not there then add them to missingVerticies
		for (V vertex2 : graph.vertices()) {
			if (!subgraph.hasVertex(vertex2)) {
				missingVertices.add(vertex2);
			}
		}
		return missingVertices;

	}

	public static <V, E> double averageSeparation(Graph<V, E> tree, V root) {
		double num = 0;
		// keep track of visted to prevent looping
		Set<V> visited = new HashSet<V>();
		double numberOfEdges = averageSeparationHelper(tree, root, num, visited);
		double avgSeperation = numberOfEdges / ((double) tree.numVertices());
		return avgSeperation;
	}

	public static <V, E> double averageSeparationHelper(Graph<V, E> tree, V root, double num, Set<V> visited) {
		// increment count
		double numHelp = num + 1;
		// iterate though neighbors
		for (V neighbor : tree.outNeighbors(root)) {
			// if we havent visited the vertex yet, add to path and call the
			// function on it
			if (!visited.contains(neighbor)) {
				visited.add(neighbor);
				num += averageSeparationHelper(tree, neighbor, numHelp, visited);
			}
		}
		return num;

	}

	public static <V, E> List<V> verticesByInDegree(Graph<V, E> g) {
		List<V> vs = new ArrayList<V>();
		// add all the vertices to the list
		for (V v : g.vertices()) {
			vs.add(v);
		}
		// sort them via inDegrees
		vs.sort((v1, v2) -> g.inDegree(v2) - g.inDegree(v1));
		return vs;
	}
}