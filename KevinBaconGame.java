import java.io.*;
import java.util.*;

// Shaket Chaudhary and Juan Castano
// Fall 2016

public class KevinBaconGame {
	static String centerOfTheUniverse = "Kevin Bacon";
	static Graph<String, Set<String>> centerOfTheUniverseGraph;

	public static Map<Integer, String> mapGenerator(String fileName) {
		// Create a map that will keep track of Actor/Movie ID and the names
		Map<Integer, String> output = new HashMap<Integer, String>();
		BufferedReader input = null; // we keep it null so we can run the try
										// catch

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
				// Split by spaces and enters
				String[] tempLine = line.split("\\|");
				// first string is the actor id
				int actorId = Integer.parseInt(tempLine[0]);
				// second string is the actor's name
				String actorName = tempLine[1];
				output.put(actorId, actorName);
				// Move on to next line
				line = input.readLine();

				// Test code

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

	public static Map<String, Set<String>> MovieActorMapGenerator(String fileName, Map<Integer, String> actorMap,
			Map<Integer, String> movieMap) {
		// Create a map that will keep track of Actor/Movie ID and the names
		HashMap<String, Set<String>> output = new HashMap<String, Set<String>>();
		BufferedReader input = null; // we keep it null so we can run the try
										// catch

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
				// split the via via spaces and returns
				String[] tempLine = line.split("\\|");
				// first line is the movieId
				int movieId = Integer.parseInt(tempLine[0]);
				// get movie name from movie map
				String movieName = movieMap.get(movieId);
				// second line is the actor id
				int actorId = Integer.parseInt(tempLine[1]);
				// get actor name from actor id
				String actorName = actorMap.get(actorId);
				if (output.containsKey(movieName)) {
					output.get(movieName).add(actorName);
				} else {
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

	public static Graph<String, Set<String>> graphGenerator(Map<String, Set<String>> movieActorMap,
			Map<Integer, String> movieMap, Map<Integer, String> actorMap) {
		Graph<String, Set<String>> graph = new AdjacencyMapGraph<String, Set<String>>();
		for (int actor : actorMap.keySet()) {
			String actorName = actorMap.get(actor);
			graph.insertVertex(actorName);
		}
		for (String movie : movieActorMap.keySet()) {
			// compare all the actors to one anothers
			for (String actor1 : movieActorMap.get(movie)) {
				for (String actor2 : movieActorMap.get(movie)) {
					// quick check that allows us to use the 4 loop
					if (actor1 != actor2) {
						// The actors might have an edge from a previous move so
						// we want to add to the set directly just in case
						if (graph.hasEdge(actor1, actor2)) {
							graph.getLabel(actor1, actor2).add(movie);
						} else {
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

	// Helper method to create a new center of the universe
	public static void newCenterOfUniverse(Graph<String, Set<String>> g, String actor) {
		centerOfTheUniverseGraph = GraphLib.bfs(g, actor);
	}

	// Helper method to get actor names from string (saves time later)
	public static String getActorName(String[] array) {
		String output = "";
		// strings after command will be the name
		for (int i = 1; i < array.length; i++) {
			output += array[i];
			if (i != array.length - 1)
				output += " ";
		}
		return output;
	}

	public static void kevinBaconGame(Graph<String, Set<String>> graph) {
		Scanner in = new Scanner(System.in);
		// initiate the game with kevin bacon as the center of the universe
		newCenterOfUniverse(graph, centerOfTheUniverse);
		try {
			while (true) {
				System.out.print(centerOfTheUniverse + " Game >");
				String temp = in.nextLine();
				String[] line = temp.split(" ");

				// Change the center of the universe
				if (line[0].equals("u")) {
					String actor = getActorName(line);
					if (graph.hasVertex(actor)) {
						// update person at the center of the universe
						centerOfTheUniverse = actor;
						// update the center of the universe
						newCenterOfUniverse(graph, actor);
						// Print confirmation to console
						System.out.println(actor + " is now the center of the acting universe, connected to "
								+ centerOfTheUniverseGraph.numEdges() + "/9235 actors with average separation "
								+ GraphLib.averageSeparation(centerOfTheUniverseGraph, actor));
					} else {
						System.out.println("Error: Actor listed is not in graph");
					}

				}

				// find path from <name> to current center of the universe
				else if (line[0].equals("p")) {
					String actor = getActorName(line);
					// make sure the actors are connected
					if (centerOfTheUniverseGraph.hasVertex(actor)) {
						List<String> path = GraphLib.getPath(centerOfTheUniverseGraph, actor);
						System.out.println(actor + "'s number is " + (path.size() - 1));
						String temp1 = actor;
						// Go through the path and list connections
						for (int j = 1; j < path.size(); j++) {
							String temp2 = path.get(j);
							Set<String> movieTheyActedIn = centerOfTheUniverseGraph.getLabel(temp2, temp1);
							System.out.println(temp1 + " appeared in " + movieTheyActedIn + " with " + temp2);
							temp1 = temp2;
						}
					} else {
						System.out.println(actor + " is not connected to " + centerOfTheUniverse);
					}
				}

				// Print out actors with infinite separation
				else if (line[0].equals("i")) {
					Set<String> missingVerticies = GraphLib.missingVertices(graph, centerOfTheUniverseGraph);
					System.out.println("Actors with infinite separation:" + missingVerticies);
				}


				// Quit the game
				else if (line[0].equals("q")) {
					break;
				}

				// Prints out the number of connections the current center of
				// the universe has
				else if (line[0].equals("a")) {
					int numberOfConections = centerOfTheUniverseGraph.numVertices() - 1;
					System.out.println(centerOfTheUniverse + " is connected to " + numberOfConections + " actors");

				}

				// Print out average seperation for the current center of the
				// universe
				else if (line[0].equals("s")) {
					System.out.println("The average seperation from " + centerOfTheUniverse + " is "
							+ GraphLib.averageSeparation(centerOfTheUniverseGraph, centerOfTheUniverse));
				}

				else if (line[0].equals("b")) {
					// Run a bfs on kevin bacon to exclude people who are not
					// connected to him
					Graph<String, Set<String>> KBGraph = GraphLib.bfs(graph, "Kevin Bacon");
					String bestOption = "";
					System.out.println("Processing the best Bacon: please wait");
					double lowestSeperationNumber = 100;
					// Loops through all actors connected to Kevin Bacon
					for (String vertex : KBGraph.vertices()) {
						// set each actor as the center of the universe
						Graph<String, Set<String>> actorAsCenter = GraphLib.bfs(graph, vertex);
						// if the average separation is less then the current
						// low, update the low
						if (GraphLib.averageSeparation(actorAsCenter, vertex) < lowestSeperationNumber) {
							lowestSeperationNumber = GraphLib.averageSeparation(actorAsCenter, vertex);
							bestOption = vertex;
						}

					}
					System.out.println("The best bacon option via lowest averageSeparation is :" + bestOption);
				}

				else if (line[0].equals("v")) {
					List<String> tempGraph = GraphLib.verticesByInDegree(graph);
					System.out.println("The best bacon option via most out Degrees is: " + tempGraph.get(0) + " with "
							+ graph.outDegree(tempGraph.get(0)) + " degrees");
				}

				else {
					System.out.println("Invalid input: try Again");
				}
			}
		} catch (Exception e) {
			System.out.println("Error!" + e.getMessage());
		}
		in.close();
	}

	public static void main(String[] args) throws Exception {
		// generate proper maps from text files
		Map<Integer, String> actorMap = BuildMaps.mapGenerator("inputs/actorsTest.txt");
		Map<Integer, String> movieMap = BuildMaps.mapGenerator("inputs/moviesTest.txt");
		Map<String, Set<String>> movieActorMap = BuildMaps.MovieActorMapGenerator("inputs/movie-actorsTest.txt", actorMap,
				movieMap);
		Graph<String, Set<String>> graph = BuildMaps.graphGenerator(movieActorMap, movieMap, actorMap);
		System.out.println(graph);

		// Call main function
		kevinBaconGame(graph);
	}
}
