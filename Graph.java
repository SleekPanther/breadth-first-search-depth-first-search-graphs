import java.util.*;

public class Graph {
	//structure to hold adjacency list. Indexes from 1 (ignoring 0)
	private ArrayList<LinkedList<Integer>> graphAdjacencyList = new ArrayList<LinkedList<Integer>>();
	private Integer[] validNodes = {1, 2, 3, 4, 5, 6, 7, 8};		//list of ALL nodes in the graph. Must be MANUALLY kept in sync with the graph created in the constructor. //Must be Integer arry since contains() doesn't work on primiatives


	public static void main(String[] args) {
		Graph testGraph = new Graph();
		System.out.println("Original Graph as adjacency list. \n1st number in brackets [] is the array index, 2nd set of numbers on the line is all nodes adjacent to that index node");
		System.out.println(testGraph);

		testGraph.depthFirstSearch(2);
		testGraph.breadthFirstSearch(2);
		testGraph.breadthFirstSearchLayers(2);

		boolean searchAgain=true;
		while(searchAgain){
			Scanner keyboard = new Scanner(System.in);
			int startingNode = 1;		//set a default value
			boolean badInput=true;
			while(badInput){
				System.out.print("Enter a node to start from: ");
				try{
					startingNode = keyboard.nextInt();		//throws exception if non-numeric data is present
					if(! testGraph.isValidNode(startingNode)){
						System.out.println("Error! Node must be in the graph.");
						continue;		//make them enter the loop again
					}
					badInput=false;
				}
				catch(InputMismatchException e){
					System.out.println("Error! Must be a number.");
					keyboard.nextLine();
				}
			}

			System.out.print("Enter \"D\" for DFS, \"B\" for BFS & \"L\" or \"layers\" for BFS with layers: ");
			String input = keyboard.next();
			if(getString1stLetter(input) == 'd' || input.toLowerCase().contains("dfs") ){
				System.out.println("\tdfs");
				testGraph.depthFirstSearch(startingNode);
			}else if(getString1stLetter(input) == 'b' || input.toLowerCase().contains("bfs") ){
				System.out.println("\tBBB");
				testGraph.breadthFirstSearch(startingNode);
			}else if(getString1stLetter(input) == 'l' || input.toLowerCase().contains("layer") ){
				System.out.println("\tLAYERS");
				testGraph.breadthFirstSearchLayers(startingNode);
			}else{
				System.out.println("Couldn't recognize input. Defaulted to BFS Layers");
				testGraph.breadthFirstSearchLayers(startingNode);
			}

			System.out.print("Run again from another node? (y/n)");
			String userResponse=keyboard.next();
			if(getString1stLetter(userResponse) != 'y'){
				searchAgain=false;
			}
		}

	}
	
	//constructor initializes adjacency list with nodes from the problem description
	public Graph(){
		graphAdjacencyList.add( new LinkedList<Integer>() );		//ignore 0th index to make future access more understandable
		graphAdjacencyList.add( new LinkedList<Integer>( Arrays.asList(2, 3) ) );
		graphAdjacencyList.add( new LinkedList<Integer>( Arrays.asList(1, 3, 4, 5) ) );
		graphAdjacencyList.add( new LinkedList<Integer>( Arrays.asList(1, 2, 5, 7, 8) ) );
		graphAdjacencyList.add( new LinkedList<Integer>( Arrays.asList(2, 5) ) );
		graphAdjacencyList.add( new LinkedList<Integer>( Arrays.asList(2, 3, 4, 6) ) );
		graphAdjacencyList.add( new LinkedList<Integer>( Arrays.asList(5) ) );
		graphAdjacencyList.add( new LinkedList<Integer>( Arrays.asList(3, 8) ) );
		graphAdjacencyList.add( new LinkedList<Integer>( Arrays.asList(3, 7) ) );
		
	}
	
	public ArrayList<LinkedList<Integer>> getAdjacencyList(){
		return graphAdjacencyList;
	}
	
	//Change the structure of the graph by passing in an existing adjacencyList
	public void setAdjacencyList(ArrayList<LinkedList<Integer>> newGraphStructure){
		graphAdjacencyList = newGraphStructure;
	}
	
	//return how many nodes (how many indexes in the adjacency list)
	public int getNodeCount(){
		return getAdjacencyList().size();
	}

	public boolean isValidNode(int node){
		return Arrays.asList(validNodes).contains(node);
	}

	public static char getString1stLetter(String string){
		return Character.toLowerCase( string.trim().charAt(0) );
	}
	
	public void breadthFirstSearch(int startingNode){
		Queue<Integer> nodeQueue = new LinkedList<Integer>();		//queue where new un-visited nodes are stored
		nodeQueue.add(startingNode);	//initializes the queue with the starting node
		//Array saying if a node has been visited. (INDEXED FROM 1, IGNORING 0). All future accesses must use an offset of +1 for "normal" arrays
		boolean[] visitedNodes = new boolean[ getNodeCount() +1];		
		ArrayList<Integer> nodesInVisitedOrder = new ArrayList<Integer>();	//list of nodes in the order that they are visited. Printed at the end of the algorithm
		
		while( !nodeQueue.isEmpty()){		//repeat the algorithm until queue is empty, like the pseudocaode says
			int nodeRemovedFromQueue = nodeQueue.remove();		//pick a node from the head of the queue
			
			if( !visitedNodes[nodeRemovedFromQueue+1]){		//If NOT visited. (offset by +1 to make human readable array indexes)
				visitedNodes[nodeRemovedFromQueue+1]=true;	//set to visited
				nodesInVisitedOrder.add(nodeRemovedFromQueue);	//append to list of nodes in order they are visited
				
				LinkedList<Integer> rowOfNodes = getAdjacencyList().get(nodeRemovedFromQueue);	//get a list of all nodes adjacent to the current Node
				ListIterator<Integer> iterator = rowOfNodes.listIterator();		//get an iterator for the row (using iterator since LinkedList)
				while(iterator.hasNext()){			//Iterate over all nodes adjacent to the current node. The pseudocode has this as a FOR LOOP, but I use an Iterator & while loop since each row is a LinkedList
					int nodeInColumn = iterator.next();
					if( !visitedNodes[nodeInColumn+1]){		//if it's NOT visited, add to queue
						nodeQueue.add(nodeInColumn);
					}
				}
			}
		}
		System.out.println("\nBreadth first search visitation order\n"+nodesInVisitedOrder);
	}
	
	public void breadthFirstSearchLayers(int startingNode){
		//Array saying if a node has been visited. (INDEXED FROM 1, IGNORING 0). All future accesses must use an offset of +1 for "normal" arrays
		boolean[] visitedNodes = new boolean[ getNodeCount() +1];		

		int layerCounter = 0;		//how many layers we find. Used to name the layers. Starts @ 0, but there is actually 1 more layer than this number since L0 counts as the 1st.
		ArrayList<ArrayList<Integer>> layers = new ArrayList<ArrayList<Integer>>();
		layers.add(new ArrayList<Integer>());		//add the 1st layer
		layers.get(0).add(startingNode);			//add Starting node to the 1st layer
		visitedNodes[startingNode]=true;			//visit that node so it won't be explored again
		
		while( !layers.get(layerCounter).isEmpty()){	//repeat until no more layers exist
			layers.add(new ArrayList<Integer>());		//create empty layer (makes an extra layer when algorithm terminate, but ignored when printing)

			for(int u : layers.get(layerCounter)){		//for each node in the current layer
				for(int v : getAdjacencyList().get(u)){		//for each node with an edge to the node u
					if(!visitedNodes[v]){
						visitedNodes[v]=true;		//If not visited, visit
						layers.get(layerCounter+1).add(v);	// add to the NEXT layer
					}
				}
			}
			layerCounter++;
		}
		System.out.println("\nBreadth first search Layers");
		printBfsLayers(layers);		//displays results
	}

	private void printBfsLayers(ArrayList<ArrayList<Integer>> layers){
		for(int i=0; i<(layers.size()-1); i++){		//size()-1 Skip the last empty layer
			System.out.print("Layer "+i+":  ");
			for(int vertex : layers.get(i)){
				System.out.print(vertex+ "  ");		//print actual vertex in layer separated by spaces
			}
			System.out.println();
		}
	}

	public void depthFirstSearch(int startingNode){
		Stack<Integer> nodeQueue = new Stack<Integer>();		//queue where new un-visited nodes are stored
		nodeQueue.add(startingNode);	//Initialize the queue with the starting node
		//Array saying if a node has been visited. (INDEXED FROM 1, IGNORING 0). All future accesses must use an offset of +1 for "normal" arrays
		boolean[] visitedNodes = new boolean[ getNodeCount() +1];		
		ArrayList<Integer> nodesInVisitedOrder = new ArrayList<Integer>();	//list of nodes in the order that they are visited. Printed at the end of the algorithm
		
		while( !nodeQueue.isEmpty()){		//repeat the algorithm until queue is empty, like the pseudocaode says
			int nodeRemovedFromQueue = nodeQueue.pop();		//pick a node from the head of the queue
			
			if( !visitedNodes[nodeRemovedFromQueue+1]){		//If NOT visited. (offset by +1 to make human readable array indexes)
				visitedNodes[nodeRemovedFromQueue+1]=true;	//set to visited
				nodesInVisitedOrder.add(nodeRemovedFromQueue);	//append to list of nodes in order they are visited
				
				LinkedList<Integer> rowOfNodes = getAdjacencyList().get(nodeRemovedFromQueue);	//get a list of all nodes adjacent to the current Node
				ListIterator<Integer> iterator = rowOfNodes.listIterator();		//get an iterator for the row (using iterator since LinkedList)
				while(iterator.hasNext()){			//Iterate over all nodes adjacent to the current node. The pseudocode has this as a FOR LOOP, but I use an Iterator & while loop since each row is a LinkedList
					int nodeInColumn = iterator.next();
					if( !visitedNodes[nodeInColumn+1]){		//if it's NOT visited, add to queue
						nodeQueue.add(nodeInColumn);
					}
				}
			}
		}
		System.out.println("\nDepth first search visitation order\n"+nodesInVisitedOrder);
	}
	
	@Override
	public String toString() {
		String adjacecyListAsLines="";			//string representation of graph added to line by line
		for(int i=1; i< getNodeCount(); i++){	//iterate over the graph START @ 1, END @ <= SIZE since graph representation is indexed from 1
			adjacecyListAsLines += "{"+i+"}  ";		//add the index in brackets as the 1st thing on a line
			adjacecyListAsLines += getAdjacencyList().get(i)+"\n";		//add each linked list on a new line (using LinkedList built-in toString)
		}
		return adjacecyListAsLines;
	}
	
}