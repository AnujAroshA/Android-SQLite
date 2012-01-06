package com.anuja.sqlite;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewUndergraduateActivity extends Activity implements OnClickListener {
	
	private EditText uGraduateNameEditText;
	private EditText uGraduateUniIdEditText;
	private EditText uGraduateGpaEditText;
	private Button cancelButton;
	private Button saveButton;
	
	private ArrayList<UndergraduateDetailsPojo> undergraduateDetailsPojoObjArrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_new_ugraduate);
		
		uGraduateNameEditText = (EditText) findViewById(R.id.insertNewUgraduate_name_editText);
		uGraduateUniIdEditText = (EditText) findViewById(R.id.insertNewUgraduate_uniId_editText);
		uGraduateGpaEditText = (EditText) findViewById(R.id.insertNewUgraduate_gpa_editText);
		
		cancelButton = (Button) findViewById(R.id.insertNewUgraduate_cancel_button);
		cancelButton.setOnClickListener(this);
		saveButton = (Button) findViewById(R.id.insertNewUgraduate_save_button);
		saveButton.setOnClickListener(this);
		
		undergraduateDetailsPojoObjArrayList = new ArrayList<UndergraduateDetailsPojo>();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.insertNewUgraduate_cancel_button){
			finish();
		}else if(v.getId() == R.id.insertNewUgraduate_save_button){
			// Get the values provided by the user via the UI
			String providedUgraduateName = uGraduateNameEditText.getText().toString();
			String providedUgraduateUniId = uGraduateUniIdEditText.getText().toString();
			Double providedUgraduateGpa = Double.parseDouble(uGraduateGpaEditText.getText().toString());
			
			// Pass above values to the setter methods in POJO class
			UndergraduateDetailsPojo undergraduateDetailsPojoObj = new UndergraduateDetailsPojo();
			undergraduateDetailsPojoObj.setuGraduateName(providedUgraduateName);
			undergraduateDetailsPojoObj.setuGraduateUniId(providedUgraduateUniId);
			undergraduateDetailsPojoObj.setuGraduateGpa(providedUgraduateGpa);
			
			// Add an undergraduate with his all details to a ArrayList
			undergraduateDetailsPojoObjArrayList.add(undergraduateDetailsPojoObj);
			
			// Inserting undergraduate details to the database is doing in a separate method
			insertUndergraduate(undergraduateDetailsPojoObj);
			
			// Release from the existing UI and go back to the previous UI
			finish();
		}
	}
	
	public void insertUndergraduate(UndergraduateDetailsPojo paraUndergraduateDetailsPojoObj){
		
		// First we have to open our DbHelper class by creating a new object of that
		AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);
		
		// Then we need to get a writable SQLite database, because we are going to insert some values
		// SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
		SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
		
		// ContentValues class is used to store a set of values that the ContentResolver can process. 
		ContentValues contentValues = new ContentValues();
		
		// Get values from the POJO class and passing them to the ContentValues class
		contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_UNDERGRADUATE_NAME, paraUndergraduateDetailsPojoObj.getuGraduateName());
		contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_UNDERGRADUATE_UNI_ID, paraUndergraduateDetailsPojoObj.getuGraduateUniId());
		contentValues.put(AndroidOpenDbHelper.COLLUMN_NAME_UNDERGRADUATE_GPA, paraUndergraduateDetailsPojoObj.getuGraduateGpa());
		
		// Now we can insert the data in to relevant table
		// I am going pass the id value, which is going to change because of our insert method, to a long variable to show in Toast
		long affectedColumnId = sqliteDatabase.insert(AndroidOpenDbHelper.TABLE_NAME_GPA, null, contentValues);
		
		// It is a good practice to close the database connections after you have done with it
		sqliteDatabase.close();
		
		// I am not going to do the retrieve part in this post. So this is just a notification for satisfaction ;-)
		Toast.makeText(this, "Values inserted column ID is :" + affectedColumnId, Toast.LENGTH_SHORT).show();
		
	}
}
