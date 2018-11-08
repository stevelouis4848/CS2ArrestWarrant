/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs2arrestwarrant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class CS2ArrestWarrant {
    
    public static int num_Of_Cities;
    public static int num_Of_Roads;
    public static final double INFINITY = Double.POSITIVE_INFINITY;
    public static final int MAX_PASSENGERS = 20;
    public static  int final_Destination;
  
    public static class Adj_City{
        
        int destination, edge_Cost;
        
        private Adj_City(int a, int b){
            a = destination;
            b = edge_Cost;
        }
   	}

	public static class City_State{
        
        int destination,passengers,current_Cost;
        
        private Adj_City(int a, int b, int c){
            destination = a;
            passengers = b;
            current_Cost = c;
        }
   	}

    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        int city1,city2, cost;
         
        // reading in the number of ciries and roads.
        num_Of_Cities = input.nextInt();
        num_Of_Roads = input.nextInt();
        
        //Adjaency List of double arrayList ints of the id of the ciies connected
        ArrayList<ArrayList<Adj_City>> adjacencyList = new ArrayList<>(num_Of_Cities);
        
        //Array if the number of pirates and bribe cost attached to the index array.
        int[] numOfPirates = new int[num_Of_Cities];
        int[] bribeCost = new int[num_Of_Cities];

        // Read the roads and store them in the adjacency list right away.

		graph();
		dijkstra();
    }
    
    public static void  nodes(){

         // Store the roads in an arrayList of roads
        for(int i = 0; i < num_Of_Cities; i++){
            numOfPirates[i] = input.nextInt(); 
            bribeCost[i] = input.nextInt();
        } 

    }
    public static void edges(){

         for(int i = 0; i < num_Of_Roads; i++){
            city1 = input.nextInt();
            city2 = input.nextInt();
            cost = input.nextInt();
            
           
        	adjacencyList.get(city1).add(new Adj_City(city2, cost));
         	adjacencyList.get(city2).add(new Adj_City(city1, cost));       
        }
    }

    public static void graph(){
        nodes();
        vertex();
    }
    
    public static void dijkstra( ArrayList<ArrayList<Adj_City>> adjacencyList){
 
        int current_City_Id = 0;
        int num_Of_Passengers = 20;
        int num_Of_AdjCities;
        int new_Adj_City_id;
        double overall_Cost;
        City_State current_City;
        City_State pop_City;

        // Comparator<Integer> comparator = new IntComparator();
        
        //The state contains the number of passengers and the cost;
        // The index is the state that it is coming from.
        int[][] state;
        
        // Visited array automaticaly initialiized to 0  for all possible states.
        int [][] visited = new int[num_Of_Cities][MAX_PASSENGERS];
       

        //Priority queue of states ordered by the cost indexes
        //Needs a comparator.
        PriorityQueue<City_State> dijkstraQueue =  new PriorityQueue<>();
        
        
        // Repeat until the destination city has been added to the visited list;
        while(pop_City.destination != final_Destination){
	        /*  Form your current node check all the other adjacent nodes,that have not been visited
	            with those, put those states into the PQ with updated costs.      
	        */

	        // The length of all adjacent cities.
	        num_Of_AdjCities = adjacencyList.get(current_City).size();
	        
	        //	For all those adjacent cities go through the list and consider them.
	        for (int i = 0; i < num_Of_AdjCities; i++){
	            

	           adj_City_Id = adjacencyList.get(current_City).get(i).destination;
	            
	           // If we have not visited city state yet.
	           // Update the cost of the city and add it to the priority queue.
	           if(visited[adj_City][num_Of_Passengers] != 0){

					// NO ned to compare might end up with duplicate states in the PQ
	           		current_City = City_State(adj_City, num_Of_Passengers, current_Cost +  adj_City.edge_Cost );
					dijkstraQueue.add(current_City);  
	           }
	            
	        }
	        
	        //  Pop the top of the priority queue,and add it to the visited List.
	        pop_City = dijkstraQueue.poll();
	        visited[pop_City.destination][pop_City.passengers] = 1;
        }
    }
}
