package com.mazeco;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;

import com.mazeco.models.Block;
import com.mazeco.models.Matrix;

import org.junit.jupiter.api.*;


public class MatrixTest 
{
    @Nested
    class IntMatrixTest{
        Matrix<Integer> matrix;
        private static final int WIDTH = 7;
        private static final int HEIGHT = 6;


        @BeforeEach 
        public void constructMatrix(){
            matrix = new Matrix<Integer>(WIDTH, HEIGHT);
        }

        @Test 
        public void constructNullMatrix(){
            assertTrue(matrix.stream()
                             .filter(cell -> cell == null)
                             .collect(Collectors.toList())
                             .size() == matrix.size());
        }

        @Test 
        public void constructMatrixWithDefaultValue(){
            matrix = new Matrix<Integer>(WIDTH, HEIGHT, 7);
            
            assertTrue(matrix.stream()
                             .filter(cell -> cell.equals(7))
                             .collect(Collectors.toList())
                             .size() == matrix.size());
        }

        @Test 
        public void testMatrixSize(){
            assertEquals(HEIGHT * WIDTH, matrix.size(), "Matrix Size implemented incorrectly");
        }

        @Test 
        public void testMatrixInsert(){
            matrix.insert(4, 0, 4);
            matrix.insert(3, 0, 3);
            matrix.insert(2, 0, 2);
            matrix.insert(1, 0, 1);
            matrix.insert(0, 0, 0);

            assertAll(() -> {
                assertEquals(0, matrix.get(0, 0));
                assertEquals(1, matrix.get(0, 1));
                assertEquals(2, matrix.get(0, 2));
                assertEquals(3, matrix.get(0, 3));
                assertEquals(4, matrix.get(0, 4));
            }
            );

        }

        @Test
        public void testMatrixInsertShouldThrowIndexOutOfBoundsException(){
            assertAll(() -> {
                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.insert(99, -1, -1);
                });

                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.insert(99, WIDTH + 1, HEIGHT + 1);
                });
            });
        }

        @Test 
        public void testMatrixGetReturnValue(){
            matrix.insert(0, 0, 0);
            matrix.insert(1, 0, 1);
            matrix.insert(2, 0,2);
            matrix.insert(3, 0, 3);

            assertAll(() -> {
                assertEquals(0, matrix.get(0, 0));
                assertEquals(1, matrix.get(0, 1));
                assertEquals(2, matrix.get(0, 2));
                assertEquals(3, matrix.get(0, 3));
            }
            );

        }

        @Test 
        public void testMatrixGetReturnNull(){
            assertAll(() -> {
                assertEquals(null, matrix.get(0, 0));
                assertEquals(null, matrix.get(WIDTH/2, HEIGHT/2));
                assertEquals(null, matrix.get(WIDTH - 1, HEIGHT - 1));
                }
            );

        }

        @Test
        public void testMatrixGetShouldThrowIndexOutOfBoundsException(){
            assertAll(() -> {
                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.get(-1, -1);
                });

                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.get(WIDTH + 1, HEIGHT + 1);
                });
            });
        }

        @Test
        public void testMatrixIterator(){
            int value = 0;
            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    matrix.insert(value++, col, row);
                }
            }

            String expected = "";
            for (int i = 0; i < matrix.getWidth() * matrix.getHeight(); i++) {
                expected += Integer.toString(i);
            }

            String actual = "";
            for (int number : matrix) {
                actual += Integer.toString(number);
            }

            assertEquals(expected, actual, "Iterator implemented incorrectly");
        }

        @Test
        public void testToString(){
            int value = 0;
            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    matrix.insert(value++, col, row);
                }
            }

            String expected = "";
            int count = 0;
            for (int i = 0; i < matrix.getHeight(); i++) {
                for (int j = 0; j < matrix.getWidth(); j++) {
                    expected += Integer.toString(count) + " ";
                    count ++;
                }
                expected += "\n";
            }

            String actual = matrix.toString();

            assertEquals(expected, actual, "toString implemented incorrectly");
        }

    }

    @Nested
    class StringMatrixTest{
        Matrix<String> matrix;
        private static final int WIDTH = 7;
        private static final int HEIGHT = 6;


        @BeforeEach 
        public void constructMatrix(){
            matrix = new Matrix<String>(WIDTH, HEIGHT);
        }

        @Test 
        public void constructNullMatrix(){
            assertTrue(matrix.stream()
                             .filter(cell -> cell == null)
                             .collect(Collectors.toList())
                             .size() == matrix.size());
        }

        @Test 
        public void constructMatrixWithDefaultValue(){
            matrix = new Matrix<String>(WIDTH, HEIGHT, "7");
            
            assertTrue(matrix.stream()
                             .filter(cell -> cell.equals("7"))
                             .collect(Collectors.toList())
                             .size() == matrix.size());
        }

        @Test 
        public void testMatrixSize(){
            assertEquals(HEIGHT * WIDTH, matrix.size(), "Matrix Size implemented incorrectly");
        }

        @Test 
        public void testMatrixInsert(){
            matrix.insert("4", 0, 4);
            matrix.insert("3", 0, 3);
            matrix.insert("2", 0, 2);
            matrix.insert("1", 0, 1);
            matrix.insert("0", 0, 0);

            assertAll(() -> {
                assertEquals("0", matrix.get(0, 0));
                assertEquals("1", matrix.get(0, 1));
                assertEquals("2", matrix.get(0, 2));
                assertEquals("3", matrix.get(0, 3));
                assertEquals("4", matrix.get(0, 4));
            }
            );

        }

        @Test
        public void testMatrixInsertShouldThrowIndexOutOfBoundsException(){
            assertAll(() -> {
                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.insert("99", -1, -1);
                });

                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.insert("99", WIDTH + 1, HEIGHT + 1);
                });
            });
        }

        @Test 
        public void testMatrixGetReturnValue(){
            matrix.insert("0", 0, 0);
            matrix.insert("1", 0, 1);
            matrix.insert("2", 0,2);
            matrix.insert("3", 0, 3);

            assertAll(() -> {
                assertEquals("0", matrix.get(0, 0));
                assertEquals("1", matrix.get(0, 1));
                assertEquals("2", matrix.get(0, 2));
                assertEquals("3", matrix.get(0, 3));
            }
            );

        }

        @Test 
        public void testMatrixGetReturnNull(){
            assertAll(() -> {
                assertEquals(null, matrix.get(0, 0));
                assertEquals(null, matrix.get(WIDTH/2, HEIGHT/2));
                assertEquals(null, matrix.get(WIDTH - 1, HEIGHT - 1));
                }
            );
        }

        @Test
        public void testMatrixGetShouldThrowIndexOutOfBoundsException(){
            assertAll(() -> {
                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.get(-1, -1);
                });

                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.get(WIDTH + 1, HEIGHT + 1);
                });
            });
        }

        @Test
        public void testMatrixIterator(){
            int value = 0;
            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    matrix.insert(Integer.toString(value++), col, row);
                }
            }

            String expected = "";
            for (int i = 0; i < matrix.getWidth() * matrix.getHeight(); i++) {
                expected += Integer.toString(i);
            }

            String actual = "";
            for (String string : matrix) {
                actual += string;
            }

            assertEquals(expected, actual, "Iterator implemented incorrectly");
        }

        @Test
        public void testToString(){
            int value = 0;
            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    matrix.insert(Integer.toString(value++), col, row);
                }
            }

            String expected = "";
            int count = 0;
            for (int i = 0; i < matrix.getHeight(); i++) {
                for (int j = 0; j < matrix.getWidth(); j++) {
                    expected += Integer.toString(count) + " ";
                    count ++;
                }
                expected += "\n";
            }

            String actual = matrix.toString();

            assertEquals(expected, actual, "toString implemented incorrectly");
        }

    }
    @Nested
    class BlockMatrixTest{
        Matrix<Block> matrix;
        private static final int WIDTH = 7;
        private static final int HEIGHT = 6;


        @BeforeEach 
        public void constructMatrix(){
            matrix = new Matrix<Block>(WIDTH, HEIGHT);
        }

        @Test 
        public void constructNullMatrix(){
            assertTrue(matrix.stream()
                             .filter(cell -> cell == null)
                             .collect(Collectors.toList())
                             .size() == matrix.size());
        }

        @Test 
        public void constructMatrixWithDefaultValue(){
            matrix = new Matrix<Block>(WIDTH, HEIGHT, Block.BLANK);
            
            assertTrue(matrix.stream()
                             .filter(cell -> cell.equals(Block.BLANK))
                             .collect(Collectors.toList())
                             .size() == matrix.size());
        }

        @Test 
        public void testMatrixSize(){
            assertEquals(HEIGHT * WIDTH, matrix.size(), "Matrix Size implemented incorrectly");
        }

        @Test 
        public void testMatrixInsert(){
            matrix.insert(Block.BLANK, 0, 0);
            matrix.insert(Block.END, 0, 1);
            matrix.insert(Block.LOGO, 0, 2);
            matrix.insert(Block.PATH, 0, 3);
            matrix.insert(Block.START, 0, 4);

            assertAll(() -> {
                assertEquals(Block.BLANK, matrix.get(0, 0));
                assertEquals(Block.END, matrix.get(0, 1));
                assertEquals(Block.LOGO, matrix.get(0, 2));
                assertEquals(Block.PATH, matrix.get(0, 3));
                assertEquals(Block.START, matrix.get(0, 4));
            }
            );

        }

        @Test
        public void testMatrixInsertShouldThrowIndexOutOfBoundsException(){
            assertAll(() -> {
                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.insert(Block.BLANK, -1, -1);
                });

                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.insert(Block.BLANK, WIDTH + 1, HEIGHT + 1);
                });
            });
        }

        @Test 
        public void testMatrixGetReturnValue(){
            matrix.insert(Block.BLANK, 0, 0);
            matrix.insert(Block.BLANK, 0, 1);
            matrix.insert(Block.BLANK, 0,2);
            matrix.insert(Block.BLANK, 0, 3);

            assertAll(() -> {
                assertEquals(Block.BLANK, matrix.get(0, 0));
                assertEquals(Block.BLANK, matrix.get(0, 1));
                assertEquals(Block.BLANK, matrix.get(0, 2));
                assertEquals(Block.BLANK, matrix.get(0, 3));
            }
            );

        }

        @Test 
        public void testMatrixGetReturnNull(){
            assertAll(() -> {
                assertEquals(null, matrix.get(0, 0));
                assertEquals(null, matrix.get(WIDTH/2, HEIGHT/2));
                assertEquals(null, matrix.get(WIDTH - 1, HEIGHT - 1));
                }
            );
        }

        @Test
        public void testMatrixGetShouldThrowIndexOutOfBoundsException(){
            assertAll(() -> {
                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.get(-1, -1);
                });

                assertThrows(IndexOutOfBoundsException.class, () ->{
                    matrix.get(WIDTH + 1, HEIGHT + 1);
                });
            });
        }

        @Test
        public void testMatrixIterator(){
            int value = 0;
            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    matrix.insert(Block.BLANK, col, row);
                }
            }

            String expected = "";
            for (int i = 0; i < matrix.getWidth() * matrix.getHeight(); i++) {
                expected += Block.BLANK.toString();
            }

            String actual = "";
            for (Block block : matrix) {
                actual += block.toString();
            }

            assertEquals(expected, actual, "Iterator implemented incorrectly");
        }

        @Test
        public void testToString(){
            int value = 0;
            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    matrix.insert(Block.WALL, col, row);
                }
            }

            String expected = "";
            int count = 0;
            for (int i = 0; i < matrix.getHeight(); i++) {
                for (int j = 0; j < matrix.getWidth(); j++) {
                    expected += Block.WALL.toString() + " ";
                    count ++;
                }
                expected += "\n";
            }

            String actual = matrix.toString();

            assertEquals(expected, actual, "toString implemented incorrectly");
        }

    }
}
