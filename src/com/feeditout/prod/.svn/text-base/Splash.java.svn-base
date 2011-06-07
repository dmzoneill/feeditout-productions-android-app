package com.feeditout.prod;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Splash extends Activity implements Runnable
{

	private ProgressDialog pd;
	private Thread thread;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );
	}

	@Override
	public void run()
	{
		Project.clearProjects();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse( new URL( "http://www.feeditout.com/android/android.php?list=true" ).openStream() );
			doc.getDocumentElement().normalize();

			NodeList applist = doc.getElementsByTagName( "app" );

			for ( int i = 0; i < applist.getLength(); i++ )
			{
				Project p = new Project();

				Element element = ( Element ) applist.item( i );

				NodeList icon = element.getElementsByTagName( "icon" );
				Element iconString = ( Element ) icon.item( 0 );
				p.setIcon( iconString.getTextContent() );

				NodeList shortname = element.getElementsByTagName( "shortname" );
				Element shortnameString = ( Element ) shortname.item( 0 );
				p.setShortname( shortnameString.getTextContent() );

				NodeList title = element.getElementsByTagName( "title" );
				Element titleString = ( Element ) title.item( 0 );
				p.setTitle( titleString.getTextContent() );

				NodeList description = element.getElementsByTagName( "description" );
				Element descriptionString = ( Element ) description.item( 0 );
				p.setDescription( descriptionString.getTextContent() );

				NodeList version = element.getElementsByTagName( "version" );
				Element versionString = ( Element ) version.item( 0 );
				p.setVersion( versionString.getTextContent() );

				NodeList url = element.getElementsByTagName( "url" );
				Element urlString = ( Element ) url.item( 0 );
				p.setUrl( urlString.getTextContent() );

				NodeList downloads = element.getElementsByTagName( "downloads" );
				Element downloadsString = ( Element ) downloads.item( 0 );
				p.setDownloads( downloadsString.getTextContent() );

				NodeList namespace = element.getElementsByTagName( "namespace" );
				Element namespaceString = ( Element ) namespace.item( 0 );
				p.setNamespace( namespaceString.getTextContent() );

				NodeList images = element.getElementsByTagName( "image" );
				for ( int h = 0; h < images.getLength(); h++ )
				{
					Element entry = ( Element ) images.item( h );
					p.addImage( entry.getTextContent() );
				}

				p.getImageIcon( this );
			}
		}
		catch( Exception e )
		{
			Message msg = new Message();
			msg.obj = e.getMessage();
			Log.e( "MYAPP" , "exception" , e );
			Splash.this.listHandler.sendMessage( msg );
		}

		this.getPackages();

		Splash.this.listHandler.sendEmptyMessage( 0 );
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
					Splash.this.thread.interrupt();
				}
				catch( Exception e )
				{

				}
				return;
			}
		} );
		this.pd.show();
	}

	private Handler listHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			Splash.this.pd.dismiss();

			try
			{
				Toast.makeText( Splash.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				Intent myIntent = new Intent( Splash.this , ProjectListActivity.class );
				Splash.this.startActivity( myIntent );
			}
		}
	};

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

	public void accept( View v )
	{
		int result = Settings.Secure.getInt( getContentResolver() , Settings.Secure.INSTALL_NON_MARKET_APPS , 0 );

		if ( result == 0 )
		{
			AlertDialog alertDialog = new AlertDialog.Builder( this ).create();
			alertDialog.setTitle( "Unknown Sources" );
			alertDialog.setMessage( "You cannot install non market applications untill you enable 'unknown sources' in\n\nmenu > settings > applications" );
			alertDialog.setButton( "OK" , new DialogInterface.OnClickListener()
			{
				public void onClick( DialogInterface dialog , int which )
				{
					// here you can add functions
				}
			} );
			alertDialog.setIcon( R.drawable.icon );
			alertDialog.show();
		}
		else
		{
			this.createCancelProgressDialog( "Feeditout projects" , "Please standby while we fetch a list of feeditout projects" );
			this.thread = new Thread( this );
			this.thread.start();
		}
	}

	public void visit( View v )
	{
		Uri uri = Uri.parse( "http://www.feeditout.com" );
		startActivity( new Intent( Intent.ACTION_VIEW , uri ) );
	}

	public void email( View v )
	{
		Intent msg = new Intent( Intent.ACTION_SEND );
		String[] recipients = { "David O Neill <dave@feeditout.com>" };

		msg.putExtra( Intent.EXTRA_EMAIL , recipients );
		msg.putExtra( Intent.EXTRA_TEXT , "Hey Dave,\n" );
		msg.putExtra( Intent.EXTRA_SUBJECT , "Feeditout Projects" );
		msg.setType( "message/rfc822" );
		startActivity( Intent.createChooser( msg , "Email dave@feeditout.com" ) );
	}
}
