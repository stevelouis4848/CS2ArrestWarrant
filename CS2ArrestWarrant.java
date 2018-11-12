
/*
    Steve Louis
    CS2 Travis Meade 
    Fall 2018
    Assignment 4 Arrest Warrant
*/
    
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

public class CS2ArrestWarrant {
    
    public static int num_Of_Cities;
    public static int num_Of_Roads;
    public static final int MAX_PASSENGERS = 20;
    public static int overall_Min_Cost = 2000000000;
    public static int final_Destination;
    public static Scanner input = new Scanner(System.in);
  
    public static class Adj_City  implements Comparable<Adj_City>{
        
        int destination, edge_Cost;
        
        private Adj_City(int a, int b){
            destination = a;
            edge_Cost = b;
        }
        
         @Override
        public int compareTo(Adj_City c){
        if(this.edge_Cost < c.edge_Cost)
            return -1;
        
        else if(this.edge_Cost > c.edge_Cost)
              return 1;
        
            return 0;
        }
    }

    public static class City_State implements Comparable<City_State>{
        
        int id, passengers, current_Cost;
        
        private City_State(int a, int b, int c){
            id = a;
            passengers = b;
            current_Cost = c;
        }
        
          @Override
        public int compareTo(City_State c){
        if(this.current_Cost < c.current_Cost)
            return -1;
        
        else if(this.current_Cost > c.current_Cost)
              return 1;
        
            return 0;
        }
    }

    public static void main(String[] args) {
          
        // Reading in the number of ciries and roads.
        num_Of_Cities = input.nextInt();
        num_Of_Roads = input.nextInt();
        
        final_Destination = num_Of_Cities;
               
        // Adjaency List of int arrayList ints of the id of the ciies connected
        ArrayList<ArrayList<Adj_City>> adjacencyList = new ArrayList<>(num_Of_Cities);
        
        // Arrays for the number of pirates and bribe cost attached to the index of city_id -1.
        int[] num_Of_Pirates_Array = new int[num_Of_Cities];
        int[] bribe_Cost_Array = new int[num_Of_Cities];

        // Read the roads and store them in the adjacency list right away.
        nodes(num_Of_Pirates_Array, bribe_Cost_Array);
        
        edges(adjacencyList);
        
        System.out.print(dijkstra(adjacencyList, num_Of_Pirates_Array, bribe_Cost_Array));      
    }
    
    public static void  nodes(int[]  num_Of_Pirates_Array, int[] bribe_Cost_Array){

        // Store the roads in an arrayList of roads
        for(int i = 0; i < num_Of_Cities; i++){
            num_Of_Pirates_Array[i] = input.nextInt(); 
            bribe_Cost_Array[i] = input.nextInt();
        } 
    }
    public static void edges(ArrayList<ArrayList<Adj_City>> adjacencyList){

        int city1, city2, cost;

        // Intitlializes each index od the arrayList tot a new ArrayList
        for(int i = 0; i < num_Of_Cities; i++){
            adjacencyList.add(new ArrayList<>());
        }
        
        // Sets each of the slots with the proper adjacent city
        for(int i = 0; i < num_Of_Roads; i++){
            
            city1 = input.nextInt();
            city2 = input.nextInt();
            cost = input.nextInt();
            
            adjacencyList.get(city1 - 1).add(new Adj_City(city2, cost));
            
            adjacencyList.get(city2 - 1).add(new Adj_City(city1, cost));       
        }
        
         for(int i = 0; i < num_Of_Cities; i++){
            Collections.sort(adjacencyList.get(i));
        }
        
    }

    public static int dijkstra( ArrayList<ArrayList<Adj_City>> adjacencyList, int[]  num_Of_Pirates_Array, int[] bribe_Cost_Array){
          
        int current_City_Id = 1;
        int num_Of_Passengers = 20;           
        int overall_Cost = 0;
        int num_Of_AdjCities;

        City_State pop_City;
        
        // Visited array automaticaly initialiized to 0  for all possible states.
        int [][] visited = new int[num_Of_Cities - 1][MAX_PASSENGERS];
        int [] destination_Visited = new int[MAX_PASSENGERS + 1];
        int [][] considered_Min_Cost = new int[num_Of_Cities - 1][MAX_PASSENGERS];
        
        // Priority queue of states ordered by the cost indexes
        PriorityQueue<City_State> dijkstraQueue =  new PriorityQueue<>();
            
        while(current_City_Id != final_Destination){
            /*  Form your current node check all the other adjacent nodes,that have not been visited
                with those, put those states into the PQ with updated costs.      
            */

            // The length of all adjacent cities (very inefficient Order (n).
            num_Of_AdjCities = adjacencyList.get(current_City_Id - 1).size();

            for (int i = 0; i < num_Of_AdjCities; i++){
                dijkstraHelper(adjacencyList, dijkstraQueue, num_Of_Pirates_Array, bribe_Cost_Array, current_City_Id,
                   num_Of_Passengers, overall_Cost, i, visited, destination_Visited,  considered_Min_Cost);
            }
            // Pop the top of the priority queue,and add it to the visited List if it is not the current city you are at.
            pop_City = dijkstraQueue.poll();
            
            // Update the current city id and the overall cost so far.
            if(pop_City != null){
                current_City_Id = pop_City.id;
                overall_Cost = pop_City.current_Cost;  
                num_Of_Passengers = pop_City.passengers;
            }
            if(current_City_Id  != final_Destination){

                // Mark our current state as visited;
                visited[current_City_Id - 1][num_Of_Passengers - 1] = 1;
            }
            else{
               destination_Visited[num_Of_Passengers] = 1;
            }                                          
        }
        
        return overall_Cost;
    }
    
    public static void dijkstraHelper( ArrayList<ArrayList<Adj_City>> adjacencyList, 
            PriorityQueue<City_State> dijkstraQueue, int[]  num_Of_Pirates_Array, int[] bribe_Cost_Array, 
            int current_City_Id, int num_Of_Passengers, int overall_Cost, int current_Adj_City, 
            int[][] visited, int[] destination_Visited, int [][] considered_Min_Cost){
        
        int min_Passengers ;
        int number_Of_Bribes, number_Of_Arrest;  
        int adj_City_Id, adj_City_Passengers, adj_City_Road_Cost, adj_City_Bribe_Cost, adj_City_Overall_Cost;
        int numerator;
        
        City_State considered_City;

        // For all those adjacent cities go through the list and consider them.(Order (n))

        adj_City_Id = adjacencyList.get(current_City_Id - 1).get(current_Adj_City).destination;

        min_Passengers = 1;

        if(adj_City_Id == final_Destination){
            min_Passengers = 0;
        }

        // Bribe strategy             
        if(num_Of_Passengers + num_Of_Pirates_Array[adj_City_Id - 1] >  MAX_PASSENGERS){
            
            numerator = (num_Of_Passengers + num_Of_Pirates_Array[adj_City_Id - 1] -  MAX_PASSENGERS);
            number_Of_Bribes = numerator / 2;

            if(numerator % 2 != 0){
               number_Of_Bribes++; 
            }

            number_Of_Arrest = number_Of_Bribes; 
            number_Of_Bribes =  num_Of_Pirates_Array[adj_City_Id - 1]  - number_Of_Bribes;                     
        }
        else{
           number_Of_Arrest = 0;
           number_Of_Bribes = num_Of_Pirates_Array[adj_City_Id - 1];
        }

        // All possible states are check until we are below the min number of passengers
        while((adj_City_Passengers = num_Of_Passengers + number_Of_Bribes - number_Of_Arrest) >= min_Passengers &&  number_Of_Bribes >= 0){
          
            // If this state has not been visited yet,calculate cost to travel and bribe and add it to the Queue.
            if( (adj_City_Id != final_Destination && visited[adj_City_Id - 1][adj_City_Passengers  - 1] != 1) || 
                    (adj_City_Id == final_Destination && destination_Visited[adj_City_Passengers] != 1) ){

                adj_City_Road_Cost = num_Of_Passengers * adjacencyList.get(current_City_Id - 1).get(current_Adj_City).edge_Cost;
                adj_City_Bribe_Cost =  number_Of_Bribes * bribe_Cost_Array[adj_City_Id - 1];

                adj_City_Overall_Cost = adj_City_Bribe_Cost + adj_City_Road_Cost;
                                           
                if(adj_City_Overall_Cost < overall_Min_Cost){
                    
                    if(adj_City_Id == final_Destination ||  considered_Min_Cost[adj_City_Id - 1][adj_City_Passengers  - 1] == 0
                           || considered_Min_Cost[adj_City_Id - 1][adj_City_Passengers  - 1] > adj_City_Overall_Cost){
                        
                        considered_City = new City_State(adj_City_Id, adj_City_Passengers, overall_Cost +  adj_City_Overall_Cost);

                        dijkstraQueue.add(considered_City);
                        
                        if(adj_City_Id == final_Destination){

                            overall_Min_Cost =  adj_City_Overall_Cost;
                        }
                        else{
                            considered_Min_Cost[adj_City_Id - 1][adj_City_Passengers  - 1] = adj_City_Overall_Cost;                          
                        }
                    }
                }                
            }    

            // Decrement bribes and increment arrest for next state of the city.
            number_Of_Bribes--;
            number_Of_Arrest++;
        }
    }
}