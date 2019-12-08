package com.example.todolist;

import android.app.Activity;

import android.os.Bundle;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure the ConsoleView.
        final ConsoleView console     = findViewById(R.id.console_view);
        final TodoList    listOfTodos = new TodoList(console);

        // Try to load the list.
        listOfTodos.loadList();

        // Communicate!
        listOfTodos.startInteraction();
    }
}
