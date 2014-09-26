/*
 * Base class for our archive/active array adapter.
 * Contains the their shared code.
 */


package com.wildgamerappears.dontforget;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

public abstract class BaseToDoAdapter extends ArrayAdapter<ToDo>{
	protected abstract int getCheckBoxFull();
	protected abstract int getCheckBoxEmpty();

	public BaseToDoAdapter(Context context, ArrayList<ToDo> toDos) {
		// Pass 0 in for the default layout, as we will be using our own
		super(context, 0, toDos);
	}
	
	// Used to assign a pretty purple background to selected items
	protected void setItemBackground(View view, Boolean isSelected) {
		int color = isSelected ? R.color.purple : R.color.white;
		view.setBackgroundResource(color);
	}
	protected void setCheckBoxImage(ImageButton image, Boolean isComplete) {
		int image_id = isComplete ? getCheckBoxFull()
				: getCheckBoxEmpty();
		image.setImageResource(image_id);
	}
}
