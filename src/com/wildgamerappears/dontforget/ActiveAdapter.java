/*
 * Gets the view for each row for active to Do items.
 * The ActiveAdapter is much more complicated than the Archived
 * Adapter because has to allow for editing text of each ToDo and
 * marking them completed. 
 */


package com.wildgamerappears.dontforget;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class ActiveAdapter extends BaseToDoAdapter {

	@Override
	protected int getCheckBoxFull()
	{
		return R.drawable.checkbox_full;
	}
	@Override
	protected int getCheckBoxEmpty()
	{
		return R.drawable.checkbox_empty;
	}
	
	public ActiveAdapter(Context context, ArrayList<ToDo> toDos) {
		super(context, toDos);
	}
	
	// class for holding our row items together
	// allows us to save them together and retrieve them with getTag
	// this stops us from having to use findByView every time
	static class ViewHolder {
		public ToDo item;
		public EditText toDoText;
		public ImageButton checkBoxImage;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// inflate a view if we don't have one already
		// this includes creating a new view holder item
		// saving it with setTag, and setting all listeners
		if (view == null) {
			LayoutInflater inflator = LayoutInflater.from(this.getContext());
			view = inflator.inflate(R.layout.active_item, parent, false);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.item = getItem(position);
			viewHolder.toDoText =  (EditText) view.findViewById(R.id.to_do_item);
			
			// long clicking the text on a To Do should set it to selected and change it's background color
			viewHolder.toDoText.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					ToDo item = (ToDo)viewHolder.item;
					item.toggleIsSelected();
					setItemBackground((View)v.getParent(), item.getIsSelected());
					return true;
				}
			});
			
			// clicking on text should edit the associated ToDos text
			viewHolder.toDoText.addTextChangedListener(new TextWatcher() {
				 
				public void onTextChanged(CharSequence c, int start, int before, int count) {
					ToDo item = (ToDo)viewHolder.item;
					item.setText(c.toString());
				}
				 
				public void beforeTextChanged(CharSequence c, int start, int count, int after) {}
				public void afterTextChanged(Editable c) {} 
			 });
			
			
			viewHolder.checkBoxImage = (ImageButton) view.findViewById(R.id.complete_checkbox);
			// checkbox image should toggle between the checked/nonchecked resource icons
			viewHolder.checkBoxImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ToDo item = (ToDo)viewHolder.item;
					item.toggleIsComplete();
					setCheckBoxImage((ImageButton)v, item.getIsComplete());
				}
			}); 
			
			view.setTag(viewHolder);
		}
		else {
			((ViewHolder) view.getTag()).item = getItem(position);
		}
		
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.toDoText.setText(viewHolder.item.getText());
        setCheckBoxImage(viewHolder.checkBoxImage, viewHolder.item.getIsComplete());
        setItemBackground(view, viewHolder.item.getIsSelected());

		return view;
	}
}
