/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs2arrestwarrant;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class CS2ArrestWarrant {
    
    public static int num_Of_Cities;
    public static int num_Of_Roads;
    //public static final int INFINITY = Double.POSITIVE_INFINITY;
    public static final int MAX_PASSENGERS = 20;
    public static Scanner input = new Scanner(System.in);
  
    public static class Adj_City{
        
        int destination, edge_Cost;
        
        private Adj_City(int a, int b){
            destination = a;
            edge_Cost = b;
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
        
        /*
        for(int i = 0; i < num_Of_Cities; i++){
            System.out.print(" Pirates[" + i + "]: " + num_Of_Pirates_Array[i]);
            System.out.print(" bribe Cost[" + i +"]: " + bribe_Cost_Array[i] + "\n");
        }
        */
        
        edges(adjacencyList);
        //System.out.print(" adjList Size: " + adjacencyList.size() + "\n");
        
        /*
        for(int i = 0; i < num_Of_Cities; i++){

            for(int j = 0; j < adjacencyList.get(i).size(); j++){

                System.out.print("Destination[" + i + 1 + "]: " + adjacencyList.get(i).get(j).destination + " Cost[" + i + 1 + "]: " + adjacencyList.get(i).get(j).edge_Cost + "\n");
            }         
        }
        */
        
        //System.out.print("min Cost: " + dijkstra(adjacencyList, num_Of_Pirates_Array, bribe_Cost_Array) + "\n");
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
        
    }

    public static int dijkstra( ArrayList<ArrayList<Adj_City>> adjacencyList, int[]  num_Of_Pirates_Array, int[] bribe_Cost_Array){
        
        int final_Destination = num_Of_Cities;
        int current_City_Id = 1;
        int num_Of_Passengers = 20;
        int min_Passengers ;
        int  number_Of_Bribes, number_Of_Arrest;      
        int overall_Cost = 0;
        int num_Of_AdjCities;
        int adj_City_Id, adj_City_Passengers, adj_City_Road_Cost, adj_City_Bribe_Cost, adj_City_Overall_Cost;
        int numerator;
        
        City_State considered_City;
        City_State pop_City;
        
        // Visited array automaticaly initialiized to 0  for all possible states.
        int [][] visited = new int[num_Of_Cities - 1][MAX_PASSENGERS];
        int [] destination_Visited = new int[MAX_PASSENGERS + 1];
       
        for(int i = 0; i < MAX_PASSENGERS; i++){
            
            visited[0][i] = 1;                 
        }
        
        //Priority queue of states ordered by the cost indexes
        PriorityQueue<City_State> dijkstraQueue =  new PriorityQueue<>();
            
        while(current_City_Id != final_Destination){
            /*  Form your current node check all the other adjacent nodes,that have not been visited
                with those, put those states into the PQ with updated costs.      
            */

            // The length of all adjacent cities (very inefficient Order (n).
            num_Of_AdjCities = adjacencyList.get(current_City_Id - 1).size();

            //  For all those adjacent cities go through the list and consider them.(Order (n))
            for (int i = 0; i < num_Of_AdjCities; i++){

                adj_City_Id = adjacencyList.get(current_City_Id - 1).get(i).destination;

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

                while((adj_City_Passengers = num_Of_Passengers + number_Of_Bribes - number_Of_Arrest) >= min_Passengers &&  number_Of_Bribes >= 0){

                    /*
                    // Debugging
                    System.out.print(" number of passengers " + num_Of_Passengers + "\n" );
                    System.out.print(" number of passengers in preview pre-Calc state:" + num_Of_Pirates_Array[adj_City_Id - 1]  + "\n" );
                    System.out.print(" number of passengers in preview post_Calc state:" + adj_City_Passengers  + "\n" );
                    System.out.print(" number of bribes " + number_Of_Bribes + "\n" );
                    System.out.print(" number_Of_Arrest " + number_Of_Arrest + "\n" );
                    */

                    // If this state has not been visited yet,calculate cost to travel and bribe and add it to the Queue.
                    if( (adj_City_Id != final_Destination && visited[adj_City_Id - 1][adj_City_Passengers  - 1] != 1) || 
                            (adj_City_Id == final_Destination && destination_Visited[adj_City_Passengers] != 1) ){

                        adj_City_Road_Cost = num_Of_Passengers * adjacencyList.get(current_City_Id - 1).get(i).edge_Cost;
                        adj_City_Bribe_Cost =  number_Of_Bribes * bribe_Cost_Array[adj_City_Id - 1];


                        adj_City_Overall_Cost = adj_City_Bribe_Cost + adj_City_Road_Cost;

                        considered_City = new City_State(adj_City_Id, adj_City_Passengers, overall_Cost +  adj_City_Overall_Cost);

                        dijkstraQueue.add(considered_City);

                         /*System.out.print("Added to queue id: " + considered_City.id + 
                            " passengers: " + considered_City.passengers + " Cost: " + considered_City.current_Cost + "\n\n");*/
                    }

                    // Decrement bribes and increment arrest for next state of the city.
                    number_Of_Bribes--;
                    number_Of_Arrest++;
                }
            }                

            // Pop the top of the priority queue,and add it to the visited List if it is not the current city you are at.
            pop_City = dijkstraQueue.poll();

            current_City_Id = pop_City.id;
            overall_Cost = pop_City.current_Cost;  
            num_Of_Passengers = pop_City.passengers;

            if(current_City_Id  != final_Destination){

                // Mark our current state as visited;
                visited[current_City_Id - 1][num_Of_Passengers - 1] = 1;
            }
            else{
               destination_Visited[current_City_Id - 1] = 1;
            }         

            // Update the current city id and the overall cost so far.

            //System.out.print("\ncurrent City: " + current_City_Id + " overall cost: "  + overall_Cost + " number of passengers " + num_Of_Passengers + "\n\n" );
        }
        
        return overall_Cost;
    }
}