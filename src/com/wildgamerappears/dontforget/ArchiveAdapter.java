/*
 * Much simpler array adapter. In Archive mode we let
 * users edit ToDos. This is an intentional design decision to 
 * differentiate the Archive list from the Active list.
 * An archive should be a saved state not a fluid one.
 * Users can unarchive ToDos to modify them again.
 */

package com.wildgamerappears.dontforget;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class ArchiveAdapter extends BaseToDoAdapter {

	@Override
	protected int getCheckBoxFull()
	{
		return R.drawable.checkbox_full_grey;
	}
	@Override
	protected int getCheckBoxEmpty()
	{
		return R.drawable.checkbox_empty_grey;
	}
	
	
	public ArchiveAdapter(Context context, ArrayList<ToDo> toDos) {
		super(context, toDos);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// inflate a view if we don't have one already
		if (view == null) {
			LayoutInflater inflator = LayoutInflater.from(this.getContext());
			view = inflator.inflate(R.layout.archived_item, null);
		}
		
		final ToDo item = getItem(position);
		EditText toDoText =  (EditText) view.findViewById(R.id.to_do_item);
		toDoText.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				item.toggleIsSelected();
				setItemBackground((View)v.getParent(), item.getIsSelected());
				return true;
			}
		});

        toDoText.setText(item.getText());
        ImageButton checkBoxImage = (ImageButton) view.findViewById(R.id.complete_checkbox);
        setCheckBoxImage(checkBoxImage, item.getIsComplete());
        setItemBackground(view, item.getIsSelected());

		return view;
	}
}
