/*hold your domain model for the ExpandableListView.*/
package com.mod.cps630app;

import java.util.ArrayList;
import java.util.List;

public class Group {

  public String string;
  public final List<String> children = new ArrayList<String>();

  public Group(String string) {
    this.string = string;
  }

} 