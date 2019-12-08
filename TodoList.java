package com.example.todolist;

import java.io.*;
import java.util.*;
import android.content.*;

/**
 * A simple class designed to manage a set of TodoListItems.
 */
public class TodoList implements ConsoleView.ResponseListener
{
    public static final String OUTPUT_FILE_NAME = "save.txt";

    private ConsoleView console;
    private Collection<TodoListItem> items;

    public TodoList(ConsoleView console)
    {
        this.console = console;
        items = new TreeSet<>();

        // Log a startup message.
        console.println("Hello! Welcome to TODOLIST, Chat edition.");
    }

    // Start communicating with the user!
    public void startInteraction()
    {
        console.println(""); // Yes. I added this later. It was annoying me.
        console.println("What do you want to do (Want help? Say so!)");

        console.awaitResponse("...", this);
    }

    // Make a new todo item!
    private void addTodo(String content)
    {
        long msSince1970 = (new Date()).getTime();

        items.add(new TodoListItem(content, msSince1970));
    }

    // Load the list from a file!
    public void loadList()
    {
        try
        {
            FileInputStream fileInputStream =
                    console.getContext().openFileInput(OUTPUT_FILE_NAME);
            Scanner reader = new Scanner(fileInputStream);

            TodoListItem newItem = TodoListItem.load(reader);

            // Loop until we can't read anymore!
            while (newItem != null)
            {
                items.add(newItem);

                newItem = TodoListItem.load(reader);
            }

            // Close it.
            reader.close();
        } // If something goes wrong...
        catch (Exception exception)
        {
            console.println("===> Failed to load.");
        }
    }

    // Save the list to its output file!
    public void saveList()
    {
        try
        {
            FileOutputStream fileOutputStream =
                    console.getContext().openFileOutput(OUTPUT_FILE_NAME,
                            Context.MODE_PRIVATE);
            PrintStream outputStream = new PrintStream(fileOutputStream);

            for (TodoListItem item : items)
            {
                item.save(outputStream);
            }

            outputStream.close();
        }
        catch (Exception exception)
        {
            console.println("===> Failed to save.");
        }
    }

    // When the user enters a command.
    public void onResponse(String response)
    {
        String filteredVersion = response.trim();        // Remove leading and trailing spaces.
        filteredVersion = filteredVersion.toLowerCase(); // To lower case.

        if (filteredVersion.equals("help"))
        {
            console.println("Here's what I can do: ");
            console.println("help          - Give you help.");
            console.println("add           - Add a new item to the list");
            console.println("save          - Saves the list.");
            console.println("clear         - Clears the list.");
            console.println("anything else - Print the list.");

            startInteraction();
        } // Adds an item to the TODO
        else if (filteredVersion.startsWith("add"))
        {
            console.awaitResponse("What should I add? ", new ConsoleView.ResponseListener()
            {
                public void onResponse(String content)
                {
                    addTodo(content);
                    startInteraction();
                }
            });
        }
        else if (response.equals("save"))
        {
            saveList();

            startInteraction();
        }
        else if (response.equals("clear"))
        {
            items.clear();

            startInteraction();
        }
        else // Otherwise, show all TODOs.
        {
            console.println("TODO: ");

            // For each item        in  items
            for (TodoListItem item  :   items)
            {
                // Print it.
                console.println(item.getName());
            }

            startInteraction();
        }
    }
}
