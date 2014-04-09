package com.mod.cps630app;


import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ExpandableListView;

public class StaticListActivity extends Activity {
  // more efficient than HashMap for mapping integers to objects
  SparseArray<Group> groups = new SparseArray<Group>();
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    createData();
    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
    MyExpandableListAdapter adapter = new MyExpandableListAdapter(this,
        groups);
    listView.setAdapter(adapter);
  }

  
  /*Add Data to The list*/
  
  public void createData() {
	 
	 	  
    for (int j = 0; j < 5; j++) {
  	  /*Add Data to The Main list <The Buildings>*/
      Group group = new Group("Ryerson Building " + j + " 0.0 Km");
      for (int i = 0; i < 2; i++) {
    	  
    	  /*Add Data to The sub list <Shops in The Buildings>*/    	  
        group.children.add("Shop " + i);
      }
      groups.append(j, group);
    }
  }

} 

/*
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class StaticListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_static_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.static_list, menu);
		return true;
	}

	// This should be called by whatever should switch to the menu page
	// This isn't called anywhere yet
	public void onSelectLocation(View view) {
		Intent intent = new Intent(this, MenuDisplayActivity.class);
		intent.putExtra(MainActivity.LOCATION_DATA,
				"string representation of stuff");
	}

}
*/