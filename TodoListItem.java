package com.example.todolist;

import java.io.*;
import java.util.*;

/*
  A single item in our list of todos!
                                ^
                                | In Spanish, this means everything.
 */

public class TodoListItem implements Comparable<TodoListItem>
{
    private String name;
    private long timeCreated;

    // Pre: itemName != null, creationTime >= 0 in milliseconds
    //since 1970 (why 1970? Who knows?).
    // Post: A TodoListItem has been created.
    public TodoListItem(String itemName, long creationTime)
    {
        name = itemName;
        timeCreated = creationTime;
    }

    // Get the item's name! This is its description.
    public String getName()
    {
        return name;
    }

    // Write the item to a stream.
    public void save(PrintStream output)
    {
        output.println(name);
        output.println(timeCreated);
    }

    // Read the item from a scanner.
    //Returns a todo item or null.
    public static TodoListItem load(Scanner input)
    {
        if (!input.hasNextLine())
        {
            return null;
        }

        String newName = input.nextLine();

        if (!input.hasNextLine())
        {
            return null;
        }

        long timeCreated = Long.parseLong(input.nextLine()); // We're assuming correct formatting
                                                             //for simplicity.

        return new TodoListItem(newName, timeCreated);
    }

    // Compare this item to another. If this item
    //was created before the other, a positive integer
    //is returned, else, a negative (if the same, zero
    //is returned).
    public int compareTo(TodoListItem other)
    {
        if (other.timeCreated > timeCreated)
        {
            return 1;
        }
        else if (other.timeCreated < timeCreated)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}
