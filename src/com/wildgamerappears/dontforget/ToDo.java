/*
 * This class implements a basic ToDo item.
 * 
 * A ToDo item contains text (mText), a flag indicating
 * it's completion (mIsComplete, and a flag indicating it's
 * state (mIsSelected).
 * 
 */

package com.wildgamerappears.dontforget;

public class ToDo {

	private String mText;
	private Boolean mIsComplete;
	// Tracks whether or not the To Do has been selected by the user.
	// Allows us to mass delete/email/archive/unarchive all selected ToDos.
	private Boolean mIsSelected;
	
	// Default state for a ToDo should be empty, incomplete and unselected.
	public ToDo() {
		mText = "";
		mIsComplete = false;
		mIsSelected = false;
	}
	
	// ------------------------------------------
	// Getter and Setter for private attributes
	// Toggle methods for Boolean attributes
	// ------------------------------------------
	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public Boolean getIsComplete() {
		return mIsComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		mIsComplete = isComplete;
	}

	public void toggleIsComplete() {
		setIsComplete(!getIsComplete());
	}

	public Boolean getIsSelected() {
		return mIsSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		mIsSelected = isSelected;
	}

	public void toggleIsSelected() {
		setIsSelected(!getIsSelected());
	}
}
