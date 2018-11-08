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
    //public static final int INFINITY = Double.POSITIVE_INFINITY;
    public static final int MAX_PASSENGERS = 20;
    public static  int final_Destination;
    public static Scanner input = new Scanner(System.in);
  
    public static class Adj_City{
        
        int destination, edge_Cost;
        
        private Adj_City(int a, int b){
            a = destination;
            b = edge_Cost;
        }
   	}

	public static class City_State{
        
        int id, passengers, current_Cost;
        
        private City_State(int a, int b, int c){
            id = a;
            passengers = b;
            current_Cost = c;
        }
   	}

    public static void main(String[] args) {
          
        // reading in the number of ciries and roads.
        num_Of_Cities = input.nextInt();
        num_Of_Roads = input.nextInt();
        
        //Adjaency List of int arrayList ints of the id of the ciies connected
        ArrayList<ArrayList<Adj_City>> adjacencyList = new ArrayList<>(num_Of_Cities);
        
        //Array if the number of pirates and bribe cost attached to the index array.
        int[] num_Of_Pirates_Array = new int[num_Of_Cities];
        int[] bribe_Cost_Array = new int[num_Of_Cities];

        // Read the roads and store them in the adjacency list right away.
        nodes(num_Of_Pirates_Array, bribe_Cost_Array);
        edges(adjacencyList);
        dijkstra(adjacencyList, num_Of_Pirates_Array, bribe_Cost_Array);
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

        for(int i = 0; i < num_Of_Roads; i++){
            city1 = input.nextInt();
            city2 = input.nextInt();
            cost = input.nextInt();
            
            adjacencyList.get(city1).add(new Adj_City(city2, cost));
            adjacencyList.get(city2).add(new Adj_City(city1, cost));       
        }
    }

    public static void dijkstra( ArrayList<ArrayList<Adj_City>> adjacencyList, int[]  num_Of_Pirates_Array, int[] bribe_Cost_Array){
 
        int current_City_Id = 0;
        int num_Of_Passengers = 20;
        int overall_Cost = 0;
        int num_Of_AdjCities;
        int adj_City_Id;
        int adj_City_Cost;
        City_State considered_City;
        City_State pop_City;

        // Comparator<Integer> comparator = new IntComparator();
        
        // Visited array automaticaly initialiized to 0  for all possible states.
        int [][] visited = new int[num_Of_Cities][MAX_PASSENGERS];
       
        //Priority queue of states ordered by the cost indexes
        //Needs a comparator.
        PriorityQueue<City_State> dijkstraQueue =  new PriorityQueue<>();
        
        
        // Repeat until the destination city has been added to the visited list;
        while(current_City_Id!= final_Destination){
	        /*  Form your current node check all the other adjacent nodes,that have not been visited
	            with those, put those states into the PQ with updated costs.      
	        */

	        // The length of all adjacent cities (very inefficient Order (n).
	        num_Of_AdjCities = adjacencyList.get(current_City_Id).size();
	        
	        //	For all those adjacent cities go through the list and consider them.(Order (n))
	        for (int i = 0; i < num_Of_AdjCities; i++){
	            

	           adj_City_Id = adjacencyList.get(current_City_Id).get(i).destination;
                   adj_City_Cost = adjacencyList.get(current_City_Id).get(i).edge_Cost;

	            
	           // If we have not visited city state yet.
	           // Update the cost of the city and add it to the priority queue.
	           if(visited[adj_City_Id][num_Of_Passengers] != 0){

                        // NO ned to compare might end up with duplicate states in the PQ
                        considered_City = new City_State(adj_City_Id, num_Of_Passengers, overall_Cost + (num_Of_Passengers * adj_City_Cost) );
                        dijkstraQueue.add(considered_City);  
	           }	            
	        }
	        
	        //  Pop the top of the priority queue,and add it to the visited List.
	        pop_City = dijkstraQueue.poll();
	        visited[pop_City.id][pop_City.passengers] = 1;

	        //Update the current city id and the overall cost so far.
	        current_City_Id = pop_City.id;
	        overall_Cost = pop_City.current_Cost;
        }
    }
}