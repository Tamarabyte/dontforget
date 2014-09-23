/*
 * Contains functionality unique to the Archive To Do Fragment
 */


package com.wildgamerappears.dontforget;

public class ArchiveListFragment extends BaseToDoFragment {
	// ------------------------------------------
	// Implement abstract methods
	// ------------------------------------------
	
	@Override
	protected Boolean getIsActiveList() {
		return false;
	}

	@Override
	protected int getSubtitleId() {
		return R.string.archive_subtitle;
	}

	@Override
	protected int getMenuId() {
		return R.menu.archive_list_menu;
	}
	
	@Override
	protected BaseToDoAdapter getAdapter() {
		return new ArchiveAdapter(getActivity(), mToDos);
	}

	@Override
	protected boolean otherMenuItemsSelected(int menuItemID, ToDoManager manager) {
		return false;
	}
	
	
}
