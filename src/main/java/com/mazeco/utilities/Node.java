package com.mazeco.utilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class Node{
    private Point state;
    private Node parentNode;
    private String action;
    private int pathCost;

    public Node(Point state){
        this.state = state;
        this.parentNode = null;
        this.action = null;
        this.pathCost = 0;
    }

    public Node(Point state, Node parent, String action, int pathCost){
        this.state = state;
        this.parentNode = parent;
        this.action = action;
        this.pathCost = pathCost;
    }

    public Point getState(){
        return state;
    }

    public int getPathCost(){
        return pathCost;
    }

    public ArrayList<Node> expand(MazeProblem problem){
        ArrayList<Node> expandedNodes = new ArrayList<Node>();
       
        for (String action : problem.actions(this.state)) {
            expandedNodes.add(getChildNode(problem, action));
        }

        return expandedNodes;
    }
    
    private Node getChildNode(MazeProblem problem, String action){

        Point nextState = problem.result(state, action);

        return new Node(nextState, this, action, problem.pathCost(this.pathCost));

    }

    public ArrayList<String> getSolutionActions(){
        ArrayList<String> solutionPathActions = getSolutionPath()
                                               .stream()
                                               .map(node -> node.action)
                                               .collect(Collectors.toCollection(ArrayList::new));

        solutionPathActions.remove(0);

        return solutionPathActions;
    }

    public ArrayList<Point> getSolutionPoints(){
        ArrayList<Point> solutionPathPoints = getSolutionPath()
                                              .stream()
                                              .map(node -> node.state)
                                              .collect(Collectors.toCollection(ArrayList::new));

        solutionPathPoints.remove(0);

        return solutionPathPoints;
    }

    public ArrayList<Node> getSolutionPath(){
        Node currentNode = this;

        ArrayList<Node> solutionPath = new ArrayList<Node>();

        while(currentNode != null){
            solutionPath.add(currentNode);
            currentNode = currentNode.parentNode;
        }

        Collections.reverse(solutionPath);
        return solutionPath;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Node) && (this.state.equals(((Node) obj).state));
    }

    

}
