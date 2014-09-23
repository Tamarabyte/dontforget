/*
 * Base class for our archive/active array adapter.
 * Contains the their shared code.
 */


package com.wildgamerappears.dontforget;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

public abstract class BaseToDoAdapter extends ArrayAdapter<ToDo>{

	public BaseToDoAdapter(Context context, ArrayList<ToDo> toDos) {
		// Pass 0 in for the default layout, as we will be using our own
		super(context, 0, toDos);
	}
	
	// Used to assign a pretty purple background to selected items
	protected void setItemBackground(View view, Boolean isSelected) {
		int color = isSelected ? R.color.purple : R.color.white;
		view.setBackgroundResource(color);
	}
}
