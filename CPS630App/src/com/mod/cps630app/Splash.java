package com.mod.cps630app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class Splash extends Activity{

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// Thread for displaying the Splash Screen 
		Thread splash_screen = new Thread(){
			
			public void run(){
				try{
					sleep(5000);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					startActivity(new Intent(getApplicationContext(),MainActivity.class));
					finish();
				}
			}
		};
		splash_screen.start();
		
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu){
		//Inflate the menu; this adds items to the action bar 
		//getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
}
