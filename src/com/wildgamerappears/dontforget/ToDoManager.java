/*
 * This class is responsible for managing a list of ToDo items.
 *
 * The constructor is private and called using the get() method instead.
 * This ensures that we only have one instance of the ActiveToDoList
 * and one instance of the ArchiveToDoList.
 * 
 * This implementation prevents duplicate code by managing
 * both the archive list and active list. 
 * 
 */

package com.wildgamerappears.dontforget;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ToDoManager {

	// Separate files for archive and active list
	// allows us to save or load one without saving or loading the other
	private static final String ACTIVE_FILE = "active_todos.json";
	private static final String ARCHIVE_FILE = "archive_todos.json";
	
	private static ToDoManager sActiveToDoList;
	private static ToDoManager sArchiveToDoList;

	private String mFile;
	private Context mContext;
	private ArrayList<ToDo> mToDoItems;

	private ToDoManager(Context context, final String file) {
		mFile = file;
		mContext = context;
		
		// If we get an error while loading the file
		// fail gracefully and just instantiate a new ArrayList
		try {
			mToDoItems = loadFromFile();
		} catch (Exception e) {
			mToDoItems = new ArrayList<ToDo>();
		}
	}
	
	// Saves the array list of to do items to a file as JSON. 
	// Uses googles GSON library to make it less painful.
	private void sendToFile() throws IOException {
		//don't save selection state to a file
		//whether or not a user has selected an item
		//shouldn't persist past App shutdown
		setSelectedAll(false);
		Writer writer = null;
		try {
			Gson gson = new Gson();
			OutputStream out = mContext.openFileOutput(mFile, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			gson.toJson(mToDoItems, writer);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	// Grabs JSON from a file and returns an instantiated ArrayList
	// with the help of GSON magic.
	private ArrayList<ToDo> loadFromFile() throws IOException {
		ArrayList<ToDo> toDos = new ArrayList<ToDo>();
		Reader reader = null;
		
		try {
			Gson gson = new Gson();
			InputStream input = mContext.openFileInput(mFile);
			reader = new InputStreamReader(input);
			// sweet! gson dumps straight from the reader to our ArrayList
			toDos = gson.fromJson(reader, new TypeToken<ArrayList<ToDo>>() {}.getType());
			
		} finally {
			if (reader != null)
				reader.close();
		}
		
		
		// gson can return null if reading from a file that doesn't exist
		// In this case we can just return an empty ArrayList. 
		if (mToDoItems == null)
			mToDoItems = new ArrayList<ToDo>();
		
		return toDos;
	}
	
	
	// Used to construct a ToDoManager.
	// Singleton design pattern with two static ToDoManagers (does that make it a doubleton?)
	// The isActiveList flag tells method which manager to retrieve
	public static ToDoManager get(Context context, Boolean isActiveList) {
		if (isActiveList) {
			// only instantiate sActiveToDoList if it isn't already insantiated
			if (sActiveToDoList == null) {
				sActiveToDoList = new ToDoManager(context.getApplicationContext(), ACTIVE_FILE);
			}
			return sActiveToDoList;
		}
		
		// ditto for sArchiveToDoList
		if (sArchiveToDoList == null) {
			sArchiveToDoList = new ToDoManager(context.getApplicationContext(), ARCHIVE_FILE);
		}
		return sArchiveToDoList;
		
	}

	// used to determine if we're working with the archive list or active list
	public Boolean isArchiveList() {
		if (this == sArchiveToDoList)
			return true;
		return false;
	}
	
	// public method for saving the list
	public boolean saveList() {
		try {
			sendToFile();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	
	// ------------------------------------------
	// Getter/Setter and Utility methods
	// for mToDoItems
	// ------------------------------------------
	public int getCount() {
		return mToDoItems.size();
	}
	
	public int getCompletedCount() {
		int count = 0;
		for (int i=0; i< mToDoItems.size(); i++) {
			ToDo item = mToDoItems.get(i);
			if (item.getIsComplete())
				count++;
		}
		return count;
	}
	
	public ArrayList<ToDo> getToDoItems() {
		return mToDoItems;
	}

	public void addToDo(ToDo newToDo) {
		mToDoItems.add(0, newToDo);
	}

	public void addToDos(ArrayList<ToDo> toDos) {
		mToDoItems.addAll(0, toDos);
	}
	
	public void removeSelectedToDos() {
		Iterator<ToDo> iter = mToDoItems.iterator();

		while (iter.hasNext()) {
			ToDo item = iter.next();
			if (item.getIsSelected()) {
				iter.remove();
			}
		}
	}

	public ArrayList<ToDo> popSelected() {
		ArrayList<ToDo> popped = new ArrayList<ToDo>();
		Iterator<ToDo> iter = mToDoItems.iterator();

		while (iter.hasNext()) {
			ToDo item = iter.next();
			if (item.getIsSelected()) {
				iter.remove();
				popped.add(item);
			}
		}
		return popped;
	}
	
	public void setSelectedAll(Boolean state) {
		for (int i=0; i< mToDoItems.size(); i++) {
			ToDo item = mToDoItems.get(i);
			item.setIsSelected(state);
		}
	}
	
	public String emailBodySelected() {
		StringBuilder combinedSelected = new StringBuilder("");
		for (int i=0; i< mToDoItems.size(); i++) {
			ToDo item = mToDoItems.get(i);
			if (item.getIsSelected()) {
				item.setIsSelected(false);
				String status = (item.getIsComplete())? "complete" : "incomplete";
				String task = (this == sActiveToDoList) ? "Task: " : "Archived Task: ";
				combinedSelected.append( task + item.getText() + "\n" + "Status: " + status + "\n\n");
			}
		}
		return combinedSelected.toString();
	}


}
