package com.anuja.sqlite;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UpdateDeleteUndergraduateActivity extends Activity implements OnClickListener {
	
	private EditText uGraduateNameEditText;
	private EditText uGraduateUniIdEditText;
	private EditText uGraduateGpaEditText;
	private Button cancelButton;
	private Button updateButton;
	private Button deleteButton;
	private String bundledUgraduateName;
	private String bundledUgraduateUniId;
	private String bundledUgraduateGpa;
	private Double bundleUgraduateGpaDouble;
	private String ugraduateNameEditTextValue;
	private String ugraduateUniIdEditTextValue;
	private Double ugraduateGpaEditTextValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_delete_ugraduate);
		
		uGraduateNameEditText = (EditText) findViewById(R.id.updateDelateUgraduateNameEditText);
		uGraduateUniIdEditText = (EditText) findViewById(R.id.updateDeleteUgraduateUniIdEditText);
		uGraduateGpaEditText = (EditText) findViewById(R.id.updateDeleteUgaduateGpaEditText);
		
		cancelButton = (Button) findViewById(R.id.updateDeleteUgraduate_cancel_button);
		cancelButton.setOnClickListener(this);
		updateButton = (Button) findViewById(R.id.updateDeleteUgraduate_update_button);
		updateButton.setOnClickListener(this);
		deleteButton = (Button) findViewById(R.id.updateDeleteUgraduate_delete_button);
		deleteButton.setOnClickListener(this);
		
		Bundle takeBundledData = getIntent().getExtras();
		
		// First we need to get the bundle data that pass from the UndergraduateListActivity
		bundledUgraduateName = takeBundledData.getString("clickedUgraduateName");
		bundledUgraduateUniId = takeBundledData.getString("clickedUgraduateUniId");
		// setText method ask for a String value
		//But getDouble method returns a Double value
		bundleUgraduateGpaDouble = takeBundledData.getDouble("clickedUgraduateGpa");
		//So we need to convert that Double value to String value
		bundledUgraduateGpa = Double.toString(bundleUgraduateGpaDouble);
		
		// Set the values that we extracted from the Bundle in the EditText fields
		uGraduateNameEditText.setText(bundledUgraduateName);
		uGraduateUniIdEditText.setText(bundledUgraduateUniId);
		uGraduateGpaEditText.setText(bundledUgraduateGpa);
	}

	@Override
	public void onClick(View v) {
		
		// We need to update or delete details which is in the EditText fields after user edit the values		
		// These values are the ContentValues that we are going to use in future
		ugraduateNameEditTextValue = uGraduateNameEditText.getText().toString();
		ugraduateUniIdEditTextValue = uGraduateUniIdEditText.getText().toString();
		
		String ugraduateGpaEditTextValueStr = uGraduateGpaEditText.getText().toString();
		ugraduateGpaEditTextValue = Double.valueOf(ugraduateGpaEditTextValueStr);
		
		// It is easy to set values to the POJO class and pass the class instance to the updateUgraduateDetails() method
		UndergraduateDetailsPojo undergraduateDetailsPojo = new UndergraduateDetailsPojo();
		
		undergraduateDetailsPojo.setuGraduateName(bundledUgraduateName);
		undergraduateDetailsPojo.setuGraduateUniId(bundledUgraduateUniId);
		
		// POJO class ask for a double value for the GPA
		//So we have to convert the EditText value to Double value
		Double ugraduateGpaDoubleValue = Double.parseDouble(bundledUgraduateGpa);
		undergraduateDetailsPojo.setuGraduateGpa(ugraduateGpaDoubleValue);
		
		if(v.getId() == R.id.updateDeleteUgraduate_cancel_button){
			finish();
		}else if(v.getId() == R.id.updateDeleteUgraduate_update_button){
			updateUgraduateDetails(undergraduateDetailsPojo);
		}else if(v.getId() == R.id.updateDeleteUgraduate_delete_button){
			deleteUgraduateDetails(undergraduateDetailsPojo);
		}
	}

	private void updateUgraduateDetails(UndergraduateDetailsPojo undergraduateDetailsPojo) {
		
		AndroidOpenDbHelper androidOpenDbHelper = new AndroidOpenDbHelper(this);
		SQLiteDatabase sqliteDatabase = androidOpenDbHelper.getWritableDatabase();

		// ContentValues class is used to store a set of values
		//It is like name-value pairs
		// "value" part contains the values that we are going to UPDATE
		ContentValues contentValues = new ContentValues();
		contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_UNDERGRADUATE_NAME, ugraduateNameEditTextValue);
		contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_UNDERGRADUATE_UNI_ID, ugraduateUniIdEditTextValue);
		contentValues.put(AndroidOpenDbHelper.COLLUMN_NAME_UNDERGRADUATE_GPA, ugraduateGpaEditTextValue);

		// If we are using multiple whereClauseArguments, array size should have to change
		String[] whereClauseArgument = new String[1];
		whereClauseArgument[0] = undergraduateDetailsPojo.getuGraduateName();
		
		System.out.println("whereClauseArgument[0] is :" + whereClauseArgument[0]);
		
		/**
		 * This is the normal SQL query for UPDATE
		UPDATE table_name 
		SET column1=value, column2=value2,...
		WHERE some_column=some_value
		*/		
		
		sqliteDatabase.update(AndroidOpenDbHelper.TABLE_NAME_GPA, contentValues, AndroidOpenDbHelper.COLUMN_NAME_UNDERGRADUATE_NAME+"=?", whereClauseArgument);
		// For two whereClauseArguments
		//sqliteDatabase.update(AndroidOpenDbHelper.TABLE_NAME_GPA, contentValues, BaseColumns._ID+"=? AND name=?", whereClauseArgument);
		
		sqliteDatabase.close();
		finish();
	}

	private void deleteUgraduateDetails(UndergraduateDetailsPojo deleteUndergraduateDetailsPojo) {

		AndroidOpenDbHelper androidOpenDbHelper = new AndroidOpenDbHelper(this);
		SQLiteDatabase sqliteDatabase = androidOpenDbHelper.getWritableDatabase();
		
		String[] whereClauseArgument = new String[1];
		whereClauseArgument[0] = deleteUndergraduateDetailsPojo.getuGraduateName();
		
		// Only difference between UPDATE and DELETE is
		//DELETE does not have ContentValues part
		sqliteDatabase.delete(AndroidOpenDbHelper.TABLE_NAME_GPA, AndroidOpenDbHelper.COLUMN_NAME_UNDERGRADUATE_NAME+"=?", whereClauseArgument);
		
		sqliteDatabase.close();
		finish();
	}
}
