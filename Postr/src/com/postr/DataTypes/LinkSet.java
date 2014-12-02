package com.postr.DataTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public class LinkSet extends ArrayList<LinkEntry> {

	public List<String> getTagNames() {
		List<String> tags = new ArrayList<String>();

		for (LinkEntry entry : this) {
			for (LinkTag linkTag : entry.getTags()) {
				if (!tags.contains(linkTag.getTag())) {
					tags.add(linkTag.getTag());
				}
			}
		}
		Collections.sort(tags);
		return tags;
	}
}