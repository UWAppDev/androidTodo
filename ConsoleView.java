package com.example.todolist;

import android.graphics.*;
import android.widget.*; // I changed this to import android.widget.* to
                         //match CS 142/143 styling guidelines.
                         //Note: By the same rules, these are BAD comments.

import android.content.*;
import android.util.*;
import android.view.*;

import java.util.Stack;

/*
  A class that acts like a console.

  @author me
 */

//              Yes, our editor underlines this,
//              but it's because our IDE wants us
//              to use libraries Android made to
//              make code compatible with old and
//              new Android versions. We don't care.
//              We want SIMPLE code. (This comment,
//              though, was not simple).
//                                v
public class ConsoleView extends EditText implements View.OnKeyListener
{
    // An interface that lets clients listen for user input!
    public interface ResponseListener
    {
        void onResponse(String response);
    }

    private Stack<ResponseListener> listeners = new Stack<>();
    private Context myContext;

    /*
      Make a ConsoleView from a Context.
      A context is somewhat of a global UI
      element that everyone passes around.
      More generally, EditText needs it to
      function.
     */
   public ConsoleView(Context context)
   {
        super(context);

        init(context);
   }

   /*
     We'll look into how/why these other constructors
     work in a bit. Basically, Android wants to make
     copies of this object for us and the editText
     wants settings.
    */
   public ConsoleView(Context context,
                      AttributeSet attrs)
   {
       super(context, attrs);

       init(context);
   }

   public ConsoleView(Context context,
                      AttributeSet attrs,
                      int defStyleAttr)
   {
       super(context, attrs);

       init(context);
   }

   // We might not even need this one,
   // but we shall see!
   public ConsoleView(Context context,
                      AttributeSet attrs,
                      int defStyleAttr,
                      int defStyleRes)
   {
       super(context, attrs, defStyleAttr,
                defStyleRes);

       init(context);
   }

   // Save the context so we can use it if we please.
   private void init(Context context)
   {
        myContext = context;

        // We want to listen for events!
        setOnKeyListener(this);

        // Styling!
        setGravity(Gravity.LEFT | Gravity.TOP);
        setTextColor(Color.WHITE);
        setBackgroundColor(Color.BLACK);
        setTypeface(Typeface.MONOSPACE);
   }

   // Get the last line of text in the console.
    public String getLine()
    {
        String myText = getText().toString();

        // Get a list of all lines.
        String[] lines = myText.split("\n");

        if (lines.length > 0)
        {
            return lines[lines.length - 1];
        }

        return "";
    }

    // Push a single line of text to the
    //console.
    public void println(String lineText)
    {
        String myText = getText().toString();

        myText += '\n';
        myText += lineText;

        // Now, set the text.
        setText(myText);
    }

    // Wait for the user to respond to a prompt, prompt. The response
    //is expected on the next line.
    public void awaitResponse(String prompt, ResponseListener listener)
    {
        println(prompt);
        println(""); // Push the user's input to the next line.

        String myText = getText().toString();
        setSelection(myText.length()); // Select the line.

        listeners.push(listener);
    }

    public boolean onKey(View whatViewWas, int keyNumber, KeyEvent eventDescription)
    {
        int actionNumber = eventDescription.getAction(); // Get a number Android
                                                         //gives the action.

        if (actionNumber == KeyEvent.ACTION_DOWN
                && keyNumber == KeyEvent.KEYCODE_ENTER)
        {
            if (listeners.isEmpty())
            {
                return true; // No one is listening? DO NOT ADD A NEW LINE.
            }

            ResponseListener lastListener = listeners.pop();
            String lastLine = getLine();

            lastListener.onResponse(lastLine);
        }

        // Let Android process the key.
        return false;
    }
}
