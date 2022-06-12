package com.mazeco.models;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A generic 2D-matrix.
 * @param <E> the cell type.
 */
public class Matrix<E> extends AbstractCollection<E> implements Serializable{
    private static final long serialVersionUID = 2417231980546564746L;
    
    private ArrayList<ArrayList<E>> data;

    /**
     * Constructs a {@code Matrix} Object of {@code null} elements given the width and height.
     *
     * @param width  the number of rows.
     * @param height the number of columns.
     */
    public Matrix(int width, int height) {
        this(width, height, null);
    }

    /**
     * Constructs a {@code Matrix} Object given the width, height and cell type.
     * 
     * @param width the number of rows.
     * @param height the number of columns.
     * @param e the cell type.
     */
    public Matrix(int width, int height, E e) {
        this.data = new ArrayList<ArrayList<E>>();

        for (int i = 0; i < height; i++) {
            data.add(new ArrayList<E>());
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                data.get(row).add(e);
            }
        }
    }

    /**
     * Reset the {@code Matrix} Object with {@code null} elements.
     */
    public void reset(){
        reset(null);
    }

    /**
     * Reset the {@code Matrix} Object with the given cell type Objects.
     * 
     * @param e the cell type.
     */
    public void reset(E e){
        int height = data.size();
        
        int width = data.get(0).size();
        
        this.data.clear();

        for (int i = 0; i < height; i++) {
            data.add(new ArrayList<E>());
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                data.get(row).add(e);
            }
        }
    }

    /**
     * Assigns a value to a given cell, specified by its row, column coordinates.
     *
     * @param row   the row index with 0-based indexing.
     * @param col   the column index with 0-based indexing.
     * @param value the value to be assigned to the given cell.
     * @throws IndexOutOfBoundsException
     */
    public void insert(E value, int col, int row) {
        data.get(row).set(col, value);
    }


    /**
     * Gets the value at a given cell, specified by its row, column coordinates.
     *
     * @param row the row index with 0-based indexing.
     * @param col the column index with 0-based indexing.
     * @return the value located at the given cell.
     *
     * @throws IndexOutOfBoundsException
     */
    public E get(int col, int row) {
        return data.get(row).get(col);
    }

    /**
     * Gets the total number of cells in the matrix.
     *
     * @return an int equal to the total number of cells in the matrix.
     */
    public int size() {
        return data.size() * data.get(0).size();
    }

    /**
     * Converts the matrix to String format.
     *
     * @return a String representation of the matrix.
     */
   @Override
    public String toString() {
        if (data == null)
            return "null";

        String matrixString = "";
        
        int rows = data.size();
        int columns = data.get(0).size();

        for (int row = 0; row < rows; row ++) {
            for (int col = 0; col < columns; col++) {
                matrixString += this.get(col, row).toString() + " ";
            }
            matrixString += "\n";
        }
        return matrixString;
    }

    /**
     * Gets an iterator for the matrix. The iterator follows row-major order.
     *
     * @return an iterator for the matrix.
     */
    public Iterator<E> iterator() {

        return new Iterator<E>() {

            final int rows = data.size();
            final int cols = data.get(0).size();

            int row = 0;
            int col = 0;

            public boolean hasNext() {
                return row < rows;
            }

            public E next() {
                E result = data.get(row).get(col++);
                if (col == cols) {
                    col = 0;
                    row++;
                }
                return result;
            }

        };
    }

    /**
     * Gets the width of the matrix.
     * 
     * @return an integer representing the width of the {@code Matrix} Object. 
     */
    public int getWidth() {
        return data.get(0).size();
    }

    /**
     * Gets the height of the matrix.
     * 
     * @return an integer representing the height of the {@code Matrix} Object.
     */
    public int getHeight() {
        return data.size();
    }

}