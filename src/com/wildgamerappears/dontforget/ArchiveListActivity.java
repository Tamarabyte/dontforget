/*
 * Activity using the ArchiveListFragment
 */


package com.wildgamerappears.dontforget;

import android.app.Fragment;

public class ArchiveListActivity extends BaseFragmentActivity {

	@Override
	protected Fragment newFragmentType() {
		return new ArchiveListFragment();
	}

}
