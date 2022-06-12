package com.mazeco.utilities;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import com.mazeco.exception.UnsolvableMazeException;

/**
 * Static Utility Class for solving any given maze configuration 
 * utilising A Star Graph Search Algorithm to gurantee optimal solution. 
 */
public final class MazeSolver {
    /**
     * Implementation of the A Star Graph Search Algorithm to solve a given maze problem
     * @param mazeProblem {@code MazeProblem} Object presenting the problem configuration.
     * @return A {@code Node} Object if a solution has been found, {@code null} otherwise.
     * @throws UnsolvableMazeException if the given mazeProblem cannot be solved.
     */
    public static Node aStarGraphSearch(MazeProblem mazeProblem) throws UnsolvableMazeException{
        Node currentNode = new Node(mazeProblem.initial);

        if(mazeProblem.goalTest(currentNode.getState()))
            return currentNode;

        HashSet<Node> explored = new HashSet<Node>();
        PriorityQueue<Node> frontier = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node aNode, Node anotherNode) {
                return Integer.compare(aStarFValue(aNode, mazeProblem), aStarFValue(anotherNode, mazeProblem));
            }
            
        });

        frontier.add(currentNode);
        
        while(!frontier.isEmpty()){
            currentNode = frontier.poll();

            if(mazeProblem.goalTest(currentNode.getState()))
                return currentNode;

            explored.add(currentNode);

            for (Node aChildNode : currentNode.expand(mazeProblem)) {
                if(!explored.contains(aChildNode) && !frontier.contains(aChildNode)){
                    frontier.add(aChildNode);
                }
                else if(frontier.contains(aChildNode)){
                    // The incumbent node that shares the same state as the child node.  
                    Node existingChildNode = null;

                    // Find existing child node 
                    for (Node aFrontierNode: frontier){
                        if(aFrontierNode.equals(aChildNode)){
                            existingChildNode = aFrontierNode;
                        }
                    }
                    
                    if(aStarFValue(aChildNode, mazeProblem) < aStarFValue(existingChildNode, mazeProblem)){
                        // Delete the incumbent node
                        frontier.remove(existingChildNode);
                        // Add the new childNode with less g(n) cost
                        frontier.add(aChildNode);
                    }
                }
            }
        }
        // Solution cannot be found
        throw new UnsolvableMazeException("This maze configuration is unsolvable. Please modify and try again.");
    }

    /**
     * Weight function for the Priority Queue to transform Best First Graph Search 
     * to A* Graph Search Algorithm
     * @param aNode {@code Node} Object contatining the state to calculate weight value for.
     * @param problem {@code MazeProblem} Object presenting the problem configuration.
     * @return integer representing the a {@code Node} Object's weight/priority in the BFS Priority Queue.
     */
    private static int aStarFValue(Node aNode, MazeProblem problem){
        // A* search is best-first graph search with f(n) = g(n)+h(n)
        return aNode.getPathCost() + aStarHeuristicValue(aNode, problem);
    } 

    /**
     * Heuristic function for the A* Graph Search Algorithm, determined by the Manhanttan Distance 
     * from the current node state to the goal state.
     * 
     * @param aNode {@code Node} Object contatining the state to calculate heuristic value for.
     * @param problem {@code MazeProblem} Object presenting the problem configuration.
     * @return integer representing the Manhanttan Distance in number of blocks.
     */
    private static int aStarHeuristicValue(Node aNode, MazeProblem problem){
        return Distance.getManhanttanDistance(aNode.getState(), problem.mazeModel.getEndPosition());
    }
}