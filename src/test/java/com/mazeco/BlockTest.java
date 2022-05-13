package com.mazeco;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mazeco.models.Block;

import org.junit.jupiter.api.*;


public class BlockTest 
{
    @Test
    public void testToString()
    {
        assertAll(()->{
            assertEquals("W", Block.WALL.toString());
            assertEquals("B", Block.BLANK.toString());
            assertEquals("S", Block.START.toString());
            assertEquals("E", Block.END.toString());
            assertEquals("L", Block.LOGO.toString());
        });
    }
}
