package com.mazeco.utilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 *  A node in a search tree/graph. Contains a pointer to the parent (the node
 *  that this is a successor of) and to the actual state for this node. Note
 *  that if a state is arrived at by two paths, then there are two nodes with
 *  the same state.  Also includes the action that to this state, and
 *  the total path_cost (also known as g) to reach the node.  Other functions
 *  may add an f and h value.
 * 
 * @see MazeSolver
 * @see MazeProblem
 */
public final class Node{
    private Point state;
    private Node parentNode;
    private String action;
    private int pathCost;

    /**
     * Constructs a {@code Node} Object given the state.
     * @param state {@code Point} Object representing the state.
     */
    public Node(Point state){
        this.state = state;
        this.parentNode = null;
        this.action = null;
        this.pathCost = 0;
    }

    /**
     * Constructs a {@code Node} Object given the state, parent node, action and path cost.
     * 
     * @param state {@code Point} Object representing the state.
     * @param parent {@code Node} Object pointing to the parent of the current constructing {@code Node}.
     * @param action the action required to get from the parent {@code Node} state to the constructing {@code Node} state.
     * @param pathCost the cost of required to get from the starting {@code Node} state to the constructing {@code Node} state.
     */
    public Node(Point state, Node parent, String action, int pathCost){
        this.state = state;
        this.parentNode = parent;
        this.action = action;
        this.pathCost = pathCost;
    }

    /**
     * Gets the state of the {@code Node} Object.
     * 
     * @return state {@code Point} Object representing the state.
     */
    public Point getState(){
        return state;
    }

    /**
     * Gets the the cost of the required to get from the starting {@code Node} state to the current {@code Node} state.
     * 
     * @return integer presenting the cost of the {@code Node} Object.
     */
    public int getPathCost(){
        return pathCost;
    }

    /**
     * Gets the list of nodes reachable in one step from the node.
     * @param problem {@code MazeProblem} Object presenting the problem configuration.
     * @return ArrayList of {@code Node} Object reachable in one step from the node.
     */
    public ArrayList<Node> expand(MazeProblem problem){
        ArrayList<Node> expandedNodes = new ArrayList<Node>();
       
        for (String action : problem.actions(this.state)) {
            expandedNodes.add(getChildNode(problem, action));
        }

        return expandedNodes;
    }
    
    /**
     * Construct and return a child node corresponding to an 'action'
     * @param problem {@code MazeProblem} Object presenting the problem configuration.
     * @param action the applied action. i.e. 'Up', 'Down', 'Left', or 'Right'
     * @return {@code Node Object} reprenting the child node.
     */
    private Node getChildNode(MazeProblem problem, String action){

        Point nextState = problem.result(state, action);

        return new Node(nextState, this, action, problem.pathCost(this.pathCost));

    }

    /**
     * Return a list of points forming the path from the initial node to the goal node.
     * 
     * @return ArrayList of {@code Point} Object required to travel from the initial node to the goal node.
     */
    public ArrayList<Point> getSolutionPoints(){
        ArrayList<Point> solutionPathPoints = getSolutionPath()
                                              .stream()
                                              .map(node -> node.state)
                                              .collect(Collectors.toCollection(ArrayList::new));

        solutionPathPoints.remove(0);

        return solutionPathPoints;
    }

    /**
     * Return a list of nodes forming the path from the initial node to the goal node.
     * 
     * @return ArrayList of {@code Node} Object required to travel from the initial node to goal node.
     */
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

    /**
     * Required the queue of nodes in A* graph search to have no duplicated states.
     * Therefore treat nodes with the same state as equal.
     * 
     * @see MazeSolver
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node node = (Node)obj;
            return this.state.equals(node.state);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }

    

}
