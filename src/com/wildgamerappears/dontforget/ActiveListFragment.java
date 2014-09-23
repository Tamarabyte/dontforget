/*
 * Contains functionality unique to the Active To Do Fragment
 */

package com.wildgamerappears.dontforget;

import android.content.Intent;

public class ActiveListFragment extends BaseToDoFragment {
	
	// ------------------------------------------
	// Implement abstract methods
	// ------------------------------------------
	@Override
	protected Boolean getIsActiveList() {
		return true;
	}

	@Override
	protected int getSubtitleId() {
		return R.string.to_do_list_subtitle;
	}

	@Override
	protected int getMenuId() {
		return R.menu.to_do_list_menu;
	}
	
	@Override
	protected BaseToDoAdapter getAdapter() {
		return new ActiveAdapter(getActivity(), mToDos);
	}
	
	@Override
	protected boolean otherMenuItemsSelected(int menuItemID, ToDoManager manager) {
		// Adds a new to do
		if (menuItemID == R.id.menu_new_item) {
			ToDo newToDo = new ToDo();
			manager.addToDo(newToDo);
			((ActiveAdapter) getListAdapter()).notifyDataSetChanged();
			return true;
		}

		// Launches the archive Activity/Fragment
		if (menuItemID == R.id.menu_go_to_archive) {
			// Start Archive Activity
			Intent i = new Intent(getActivity(), ArchiveListActivity.class);
			startActivity(i);
			return true;
		}

		return false;
	}

	// The Archive fragment can send ToDos back to the Active List
	// so we should refresh our list when we return to the Active Fragment
	@Override
	public void onResume() {
		super.onResume();
		((ActiveAdapter) getListAdapter()).notifyDataSetChanged();
	}
}
