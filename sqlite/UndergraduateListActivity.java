package com.anuja.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UndergraduateListActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	private ListView uGraduateNamesListView;
	private Button addNewUndergraduateButton;
	
	// We need some kind of Adapter to made the connection between ListView UI component and SQLite data set.
	private ListAdapter uGraduateListAdapter;
	
	// We need this while we read the query using Cursor and pass data
	private ArrayList<UndergraduateDetailsPojo> pojoArrayList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.names_listview);
        
        // Initialize UI components
        uGraduateNamesListView = (ListView) findViewById(R.id.uGraduateListView);
        uGraduateNamesListView.setOnItemClickListener(this);
        
        addNewUndergraduateButton = (Button) findViewById(R.id.namesListViewAddButton);
        addNewUndergraduateButton.setOnClickListener(this);
        
        pojoArrayList = new ArrayList<UndergraduateDetailsPojo>();
        
        // For the third argument, we need a List that contains Strings.
        //We decided to display undergraduates names on the ListView.
        //Therefore we need to create List that contains undergraduates names
        uGraduateListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, populateList());
        
        uGraduateNamesListView.setAdapter(uGraduateListAdapter);
        
    }

	@Override
	public void onClick(View v) {
		Intent addNewUndergraduateIntent = new Intent(this, AddNewUndergraduateActivity.class);
		startActivity(addNewUndergraduateIntent);
	}
	
	// To create a List that contains undergraduate names, we have to read the SQLite database
	//We are going to do it in the separate method
	public List<String> populateList(){
		
		// We have to return a List which contains only String values. Lets create a List first
		List<String> uGraduateNamesList = new ArrayList<String>();
		
		// First we need to make contact with the database we have created using the DbHelper class
		AndroidOpenDbHelper openHelperClass = new AndroidOpenDbHelper(this);
		
		// Then we need to get a readable database
		SQLiteDatabase sqliteDatabase = openHelperClass.getReadableDatabase();
		
		// We need a a guy to read the database query. Cursor interface will do it for us
		//(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
		Cursor cursor = sqliteDatabase.query(AndroidOpenDbHelper.TABLE_NAME_GPA, null, null, null, null, null, null);
		// Above given query, read all the columns and fields of the table
		
		startManagingCursor(cursor);
		
		// Cursor object read all the fields. So we make sure to check it will not miss any by looping through a while loop
		while (cursor.moveToNext()) {
			// In one loop, cursor read one undergraduate all details
			// Assume, we also need to see all the details of each and every undergraduate
			// What we have to do is in each loop, read all the values, pass them to the POJO class
			//and create a ArrayList of undergraduates
			
			String ugName = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_UNDERGRADUATE_NAME));
			String ugUniId = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_UNDERGRADUATE_UNI_ID));
			double ugGpa = cursor.getDouble(cursor.getColumnIndex(AndroidOpenDbHelper.COLLUMN_NAME_UNDERGRADUATE_GPA));
			
			// Finish reading one raw, now we have to pass them to the POJO
			UndergraduateDetailsPojo ugPojoClass = new UndergraduateDetailsPojo();
			ugPojoClass.setuGraduateName(ugName);
			ugPojoClass.setuGraduateUniId(ugUniId);
			ugPojoClass.setuGraduateGpa(ugGpa);
			
			// Lets pass that POJO to our ArrayList which contains undergraduates as type
			pojoArrayList.add(ugPojoClass);
			
			// But we need a List of String to display in the ListView also.
			//That is why we create "uGraduateNamesList"
			uGraduateNamesList.add(ugName);
		}
		
		// If you don't close the database, you will get an error
		sqliteDatabase.close();
		
		return uGraduateNamesList;
	}

	// If you don't write the following code, you wont be able to see what you have just insert to the database 
	@Override
	protected void onResume() {
		super.onResume();
		pojoArrayList = new ArrayList<UndergraduateDetailsPojo>();
        uGraduateListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, populateList());        
        uGraduateNamesListView.setAdapter(uGraduateListAdapter);		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		pojoArrayList = new ArrayList<UndergraduateDetailsPojo>();
        uGraduateListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, populateList());        
        uGraduateNamesListView.setAdapter(uGraduateListAdapter);	
	}

	// On ListView you just see the name of the undergraduate, not any other details
	// Here we provide the solution to that. When the user click on a list item, he will redirect to a page where
	//he can see all the details of the undergraduate
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		Toast.makeText(getApplicationContext(), "Clicked on :" + arg2, Toast.LENGTH_SHORT).show();
		
		// We want to redirect to another Activity when the user click an item on the ListView
		Intent updateDeleteUgraduateIntent = new Intent(this, UpdateDeleteUndergraduateActivity.class);
		
		// We have to identify what object, does the user clicked, because we are going to pass only clicked object details to the next activity
		// What we are going to do is, get the ID of the clicked item and get the values from the ArrayList which has
		//same array id.
		UndergraduateDetailsPojo clickedObject =  pojoArrayList.get(arg2);
		
		// We have to bundle the data, which we want to pass to the other activity from this activity
		Bundle dataBundle = new Bundle();
		dataBundle.putString("clickedUgraduateName", clickedObject.getuGraduateName());
		dataBundle.putString("clickedUgraduateUniId", clickedObject.getuGraduateUniId());
		dataBundle.putDouble("clickedUgraduateGpa", clickedObject.getuGraduateGpa());
		
		// Attach the bundled data to the intent
		updateDeleteUgraduateIntent.putExtras(dataBundle);
		
		// Start the Activity
		startActivity(updateDeleteUgraduateIntent);
		
	}
}