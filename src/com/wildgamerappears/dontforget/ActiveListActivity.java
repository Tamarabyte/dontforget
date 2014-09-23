/*
 * Activity using the ActiveListFragment
 */

package com.wildgamerappears.dontforget;

import android.app.Fragment;

public class ActiveListActivity extends BaseFragmentActivity {

	@Override
	protected Fragment newFragmentType() {
		return new ActiveListFragment();
	}
	
}
