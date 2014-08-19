package com.mnt.core.ui.component;

import java.util.List;

public interface TreeNode {

	public String name();
	public boolean isExpandable();
	public List<TreeNode> node();
	public int positionID();
	public int _type();
	public long refID();
	

}
