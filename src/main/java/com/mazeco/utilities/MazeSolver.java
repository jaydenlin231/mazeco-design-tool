package com.mazeco.utilities;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class MazeSolver {
    public static Node aStarGraphSearch(MazeProblem mazeProblem){
        Node currentNode = new Node(mazeProblem.initial);
        System.out.println(currentNode);
        System.out.println(mazeProblem);
        System.out.println(mazeProblem.initial);

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
        return null;
    }


    private static int aStarFValue(Node aNode, MazeProblem problem){
        // f(n) = g(n) + h(n)
        return aNode.getPathCost() + aStarHeuristicValue(aNode, problem);
    } 

    private static int aStarHeuristicValue(Node aNode, MazeProblem problem){
        return Distance.getManhanttanDistance(aNode.getState(), problem.mazeModel.getEndPosition());
    }
}