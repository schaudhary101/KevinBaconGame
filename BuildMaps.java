import java.io.*;
import java.util.*;

// Shaket Chaudhary and Juan Castano
// Fall 2016

public class BuildMaps {
	public static Map<Integer, String> mapGenerator(String fileName) {
		// Create a map that will keep track of Actor/Movie ID and the names
		Map<Integer, String> output = new HashMap<Integer, String>();
		BufferedReader input = null; // we keep it null so we can run the try catch

		// Open the file, if possible
		try {
			input = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open file.\n" + e.getMessage());
		}

		// Read the file
		try {
			// go through every character in the file
			String line = input.readLine();
			while (line != null) {
				String[] tempLine = line.split("\\|");
				int actorId = Integer.parseInt(tempLine[0]);
				String actorName = tempLine[1];
				output.put(actorId, actorName);
				// Move on to next line
				line = input.readLine(); 
				
				//Test code
			
			}
		} catch (IOException e) {
			System.err.println("IO error while reading.\n" + e.getMessage());
		}

		// Close the file, if possible
		try {
			input.close();
		} catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		}
		return output;
	}
	public static Map<String, Set<String>> MovieActorMapGenerator(String fileName, Map<Integer, String> actorMap, Map<Integer, String> movieMap) {
		// Create a map that will keep track of Actor/Movie ID and the names
		HashMap<String, Set<String>> output = new HashMap<String, Set<String>>();
		BufferedReader input = null; // we keep it null so we can run the try catch

		// Open the file, if possible
		try {
			input = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open file.\n" + e.getMessage());
		}

		// Read the file
		try {
			// go through every character in the file
			String line = input.readLine();
			while (line != null) {
				String[] tempLine = line.split("\\|");
				int movieId = Integer.parseInt(tempLine[0]);
				String movieName = movieMap.get(movieId);
				int actorId = Integer.parseInt(tempLine[1]);
				String actorName = actorMap.get(actorId);
				if (output.containsKey(movieName)){
					output.get(movieName).add(actorName);
				}
				else{
					Set<String> emptySet = new HashSet<String>();
					output.put(movieName, emptySet);
					output.get(movieName).add(actorName);
				}
				// Move on to next line
				line = input.readLine(); 
			
			}
			
		} catch (IOException e) {
			System.err.println("IO error while reading.\n" + e.getMessage());
		}

		// Close the file, if possible
		try {
			input.close();
		} catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		}
		return output;
	}
	
	public static Graph<String, Set<String>> graphGenerator (Map<String, Set<String>> movieActorMap,Map<Integer, String> movieMap, Map<Integer, String> actorMap){
		Graph<String, Set<String>> graph = new AdjacencyMapGraph<String, Set<String>>();
		for (int actor: actorMap.keySet()){
			String actorName = actorMap.get(actor);
			graph.insertVertex(actorName);
		}
		for (String movie : movieActorMap.keySet()){
			// compare all the actors to one anothers
			for(String actor1 : movieActorMap.get(movie)){
				for(String actor2 : movieActorMap.get(movie)){
					//quick check that allows us to use the 4 loop
					if (actor1 != actor2){
						// The actors might have an edge from a previous move so we want to add to the set directly just incase
						if (graph.hasEdge(actor1, actor2)){
							graph.getLabel(actor1, actor2).add(movie);
						}
						else{
							Set<String> emptySet = new HashSet<String>();
							emptySet.add(movie);
							graph.insertUndirected(actor1, actor2, emptySet);
						}
						
					}
				}
			}
		}
		return graph;
		
	}

	//public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
	//}
	
	public static void main(String[] args) throws Exception {
		Map<Integer, String> actorMap = mapGenerator("inputs/actorsTest.txt");
		Map<Integer, String> movieMap = mapGenerator("inputs/moviestest.txt");
		Map<String, Set<String>> movieActorMap = MovieActorMapGenerator("inputs/movie-actorsTest.txt", actorMap, movieMap);
		Graph<String, Set<String>> graph = graphGenerator(movieActorMap, movieMap, actorMap);
		Graph<String, Set<String>> bfsgraph = GraphLib.bfs(graph, "Kevin Bacon");
		List<String> temp = GraphLib.getPath(bfsgraph, "Dartmouth (Earl thereof)");
		System.out.println(temp);

	}
}

