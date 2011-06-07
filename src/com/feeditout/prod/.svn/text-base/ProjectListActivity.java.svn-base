package com.feeditout.prod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProjectListActivity extends ListActivity implements Runnable
{
	private ProgressDialog pd;
	private Thread thread;
	private String downloadThis;
	private CountDownTimer timer;
	private ListAdapter adapter;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.projectsview );

		this.getListView();
		this.adapter = new ProjectsAdapter( this );
		this.setListAdapter( this.adapter );
	}
	
	@Override
	public void onResume()
	{
		this.getPackages();
		this.adapter = new ProjectsAdapter( ProjectListActivity.this );
		this.setListAdapter( ProjectListActivity.this.adapter );
		
		super.onResume();
	}

	public boolean Update( String apkurl )
	{
		try
		{
			URL url = new URL( apkurl );
			HttpURLConnection c = ( HttpURLConnection ) url.openConnection();
			c.setRequestMethod( "GET" );
			c.setDoOutput( true );
			c.connect();

			String PATH = Environment.getExternalStorageDirectory() + "/download/";
			File file = new File( PATH );
			file.mkdirs();
			File outputFile = new File( file , "app.apk" );
			FileOutputStream fos = new FileOutputStream( outputFile );

			InputStream is = c.getInputStream();

			byte[] buffer = new byte[ 1024 ];
			int len1 = 0;
			while ( ( len1 = is.read( buffer ) ) != -1 )
			{
				fos.write( buffer , 0 , len1 );
			}
			fos.close();
			is.close();
			
			ProjectListActivity.this.downloadHandler.sendEmptyMessage( 0 );

			Intent intent = new Intent( Intent.ACTION_VIEW );
			intent.setDataAndType( Uri.fromFile( new File( Environment.getExternalStorageDirectory() + "/download/" + "app.apk" ) ) , "application/vnd.android.package-archive" );
			startActivity( intent );
			
			return true;

		}
		catch( IOException e )
		{
			Message msg = new Message();
			msg.obj = "Error downloading application";
			ProjectListActivity.this.downloadHandler.sendMessage( msg );
			return false;
		}
	}

	@Override
	protected void onListItemClick( ListView l , View v , int position , long id )
	{
		
	}
	
	public void installSelected( View v )
	{				
		LinearLayout opt = ( LinearLayout ) v.getParent();
		TextView name = ( TextView ) opt.findViewById( R.id.projectId );
		this.downloadThis = name.getText().toString();	
		
		this.thread = new Thread( this );
		this.createCancelProgressDialog( "Fetching application" , "Please standby while we fetch " + this.downloadThis );
		this.thread.start();	
	}
	
	
	public void updateSelected( View v )
	{				
		LinearLayout opt = ( LinearLayout ) v.getParent();
		TextView name = ( TextView ) opt.findViewById( R.id.projectId );
		this.downloadThis = name.getText().toString();	
				
		this.thread = new Thread( this );
		this.createCancelProgressDialog( "Fetching application" , "Please standby while we fetch " + this.downloadThis );
		this.thread.start();		
	}
	
	
	private void createCancelProgressDialog( String title , String message )
	{
		this.pd = new ProgressDialog( this );
		this.pd.setTitle( title );
		this.pd.setMessage( message );
		this.pd.setButton( "Cancel" , new DialogInterface.OnClickListener()
		{
			public void onClick( DialogInterface dialog , int which )
			{
				try
				{
					ProjectListActivity.this.thread.interrupt();
				}
				catch( Exception e )
				{

				}
				return;
			}
		} );
		this.pd.show();
	}

	private Handler downloadHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			ProjectListActivity.this.pd.dismiss();			
		}
	};
	

	@Override
	public void run()
	{
		this.Update("http://www.feeditout.com/android/android.php?install=true&short=" + this.downloadThis );		
	}	
		
	
	class PInfo
	{
		private String appname = "";
		private String pname = "";
		private String versionName = "";
		private int versionCode = 0;

		private void prettyPrint()
		{
			Project.addInstalledPackage( appname , pname , versionName , String.valueOf( versionCode ) );
		}
	}

	private ArrayList< PInfo > getPackages()
	{
		ArrayList< PInfo > apps = this.getInstalledApps( false );
		final int max = apps.size();
		for ( int i = 0; i < max; i++ )
		{
			apps.get( i ).prettyPrint();
		}
		return apps;
	}

	private ArrayList< PInfo > getInstalledApps( boolean getSysPackages )
	{
		ArrayList< PInfo > res = new ArrayList< PInfo >();
		List< PackageInfo > packs = getPackageManager().getInstalledPackages( 0 );
		for ( int i = 0; i < packs.size(); i++ )
		{
			PackageInfo p = packs.get( i );
			if ( ( !getSysPackages ) && ( p.versionName == null ) )
			{
				continue;
			}
			PInfo newInfo = new PInfo();
			newInfo.appname = p.applicationInfo.loadLabel( getPackageManager() ).toString();
			newInfo.pname = p.packageName;
			newInfo.versionName = p.versionName;
			newInfo.versionCode = p.versionCode;
			res.add( newInfo );
		}
		return res;
	}
}
