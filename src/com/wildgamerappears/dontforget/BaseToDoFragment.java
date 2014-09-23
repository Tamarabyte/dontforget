/*
 * Contains functionality shared between the Archive and Active Fragments.
 * 
 */
package com.wildgamerappears.dontforget;

import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class BaseToDoFragment extends ListFragment {
	protected ArrayList<ToDo> mToDos;

	// ------------------------------------------
	// These abstract methods allow us
	// to get different variables for our Archive/Active views 
	// ------------------------------------------
	protected abstract Boolean getIsActiveList();

	protected abstract int getSubtitleId();

	protected abstract int getMenuId();

	protected abstract boolean otherMenuItemsSelected(int menuItemID,
			ToDoManager manager);

	protected abstract BaseToDoAdapter getAdapter();

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		getActivity().getActionBar().setSubtitle(getSubtitleId());
		setHasOptionsMenu(true);

		// getIsActiveList is a flag to the ToDoManager get method
		// telling it to return the static manager associated with
		// the archive or active list
		ToDoManager manager = ToDoManager.get(getActivity(), getIsActiveList());
		mToDos = manager.getToDoItems();

		// We use a slightly different adapter for archive/active views
		BaseToDoAdapter adapter = getAdapter();
		setListAdapter(adapter);
	}

	// save the list when we pause the App
	@Override
	public void onPause() {
		super.onPause();
		ToDoManager manager = ToDoManager.get(getActivity(), getIsActiveList());
		manager.saveList();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflator) {
		super.onCreateOptionsMenu(menu, menuInflator);
		menuInflator.inflate(getMenuId(), menu);
	}

	// menu items shared between both active/archived fragments
	// calls otherMenuItemsSelected for fragment specific items
	// uses if statements - I'm not a fan of huge switch statements
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		int menuItemID = menuItem.getItemId();
		ToDoManager manager = ToDoManager.get(getActivity(), getIsActiveList());
		
		// Regardless of whether we're sending to archive or unarchive we can use
		// the same code. (We'll just add the item to our opposite list of items)
		if (menuItemID == R.id.menu_archive || menuItemID == R.id.menu_unarchive) {
			ToDoManager altManager = ToDoManager.get(getActivity(),!getIsActiveList());
			ArrayList<ToDo> selected = manager.popSelected();
			
			if (selected.size() == 0) {
				showNoneSelectedToast(R.string.no_items_selected_error);
				return true;
			}
			
			altManager.addToDos(selected);
			altManager.saveList();
			((BaseToDoAdapter) getListAdapter()).notifyDataSetChanged();
			return true;
		}

		// Remove selected items from our list
		// If no items were selected, alert the user
		if (menuItemID == R.id.menu_delete) {
			if (manager.popSelected().size() == 0) {
				showNoneSelectedToast(R.string.no_items_selected_error);
				return true;
			}
				
			((BaseToDoAdapter) getListAdapter()).notifyDataSetChanged();
			return true;
		}

		// Set the state of all items selected
		// Convenience method for the user, so they don't
		// have to select every item on the list
		if (menuItemID == R.id.select_all) {
			manager.setSelectedAll(true);
			((BaseToDoAdapter) getListAdapter()).notifyDataSetChanged();
			return true;
		}

		// Opposite of the above
		if (menuItemID == R.id.unselect_all) {
			manager.setSelectedAll(false);
			((BaseToDoAdapter) getListAdapter()).notifyDataSetChanged();
			return true;
		}

		// Email the selected items
		if (menuItemID == R.id.menu_email) {
			String body = manager.emailBodySelected();
			return emailSelected(body);
		}

		// Email all items, this requires us
		// including all items on the opposing list (be that archive or active)
		if (menuItemID == R.id.email_all) {
			manager.setSelectedAll(true);
			String active_body = manager.emailBodySelected();
			
			ToDoManager altManager = ToDoManager.get(getActivity(),!getIsActiveList());
			altManager.setSelectedAll(true);
			String alt_body = altManager.emailBodySelected();
			
			String combined_body = active_body + alt_body;
			return emailSelected(combined_body);
		}

		// Used a toast because we only have a few statistics to keep track of
		// and this allows users to browse statics without removing them from the
		// ToDos they're making
		if (menuItemID == R.id.show_stats) {

			ToDoManager altManager = ToDoManager.get(getActivity(),!getIsActiveList());
			
			int total_archived;
			int total_archived_checked;
			int total_archived_unchecked;
			int total_checked;
			int total_unchecked;
			
			if (manager.isArchiveList()) {
				total_archived = manager.getCount();
				total_archived_checked = manager.getCompletedCount();
				total_archived_unchecked = total_archived - total_archived_checked;
				total_checked = altManager.getCompletedCount() + total_archived_checked;
				total_unchecked = (altManager.getCount() - altManager.getCompletedCount()) + total_archived_unchecked;
			} else {
				total_archived = altManager.getCount();
				total_archived_checked = altManager.getCompletedCount();
				total_archived_unchecked = total_archived - total_archived_checked;
				total_checked = manager.getCompletedCount() + total_archived_checked;
				total_unchecked = (manager.getCount() - manager.getCompletedCount()) + total_archived_unchecked;
			}
			
			String preFormat = getResources().getString(R.string.statistics);
			String postFormat = String.format(preFormat, total_checked, total_unchecked, total_archived, total_archived_checked, total_archived_unchecked);
			Toast.makeText(getActivity(), postFormat, Toast.LENGTH_LONG).show();
			return true;
		}

		// if we make it to here without hitting any of the above menu items
		// check the archive/active specific menu items
		boolean ret = otherMenuItemsSelected(menuItemID, manager);
		return ret || super.onOptionsItemSelected(menuItem);
	}

	
	private boolean emailSelected(String body) {
		if (body.equals("")){
			showNoneSelectedToast(R.string.no_items_email_error);
			return true;
		}
		
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_TEXT, body);
		i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
		startActivity(Intent.createChooser(i, "Choose an Email Client:"));
		return true;
	}
	
	
	private void showNoneSelectedToast(int resourceID) {
		Toast.makeText(getActivity(), resourceID, Toast.LENGTH_SHORT).show();
	}

}
