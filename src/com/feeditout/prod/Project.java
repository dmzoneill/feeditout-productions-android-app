package com.feeditout.prod;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Project
{
	private static ArrayList< Project > projects;
	private ArrayList< String > images;
	private String title;
	private String shortname;
	private String description;
	private String downloads;
	private String version;
	private String url;
	private Drawable imageIcon;
	private String icon;
	private String namespace;
	private boolean updateAvailable;
	private boolean installed;
	
	static
	{
		Project.projects = new ArrayList< Project >(); 
	}
	
	public Project()
	{
		this.images = new ArrayList< String >();
		Project.projects.add( this );
		this.updateAvailable = false;
		this.installed = false;
	}

	public ArrayList< String > getImages()
	{
		return this.images;
	}

	public void addImage( String image )
	{
		this.images.add( image );
	}

	public String getTitle()
	{
		return this.title + "";
	}

	public void setTitle( String title )
	{
		this.title = title + "";
	}

	public String getShortname()
	{
		return this.shortname + "";
	}

	public void setShortname( String shortname )
	{
		this.shortname = shortname + "";
	}

	public String getDescription()
	{
		return this.description + "";
	}

	public void setDescription( String description )
	{
		this.description = description + "";
	}

	public String getDownloads()
	{
		return this.downloads + "";
	}

	public void setDownloads( String downloads )
	{
		this.downloads = downloads + "";
	}

	public String getVersion()
	{
		return this.version + "";
	}

	public void setVersion( String version )
	{
		this.version = version + "";
	}

	public String getUrl()
	{
		return this.url + "";
	}

	public void setUrl( String url )
	{
		this.url = url + "";
	}

	public String getIcon()
	{
		return this.icon + "";
	}

	public void setIcon( String icon )
	{
		this.icon = icon;		
	}

	public Drawable getImageIcon( Context context )
	{
		if( this.imageIcon == null )
		{
			this.imageIcon = ImageOperations( context , "http://www.feeditout.com/android/apps/" + this.shortname + "/" + this.icon , "image.jpg" );
		}
		return imageIcon;
	}

	public static void clearProjects()
	{
		Project.projects.clear();
	}
	
	public static ArrayList< Project > getProjects()
	{
		return Project.projects;
	}

	public static int getProjectCount()
	{
		return Project.projects.size();
	}
		
	public String getNamespace()
	{
		return this.namespace;
	}

	public boolean isUpdateAvailable()
	{
		return this.updateAvailable;
	}

	public void setUpdateAvailable( boolean updateAvailable )
	{
		this.updateAvailable = updateAvailable;
	}

	public boolean isInstalled()
	{
		return this.installed;
	}

	public void setInstalled( boolean installed )
	{
		this.installed = installed;
	}

	public void setNamespace( String namespace )
	{
		this.namespace = namespace;
	}

	private Drawable ImageOperations( Context ctx , String url , String saveFilename )
	{
		try
		{
			InputStream is = ( InputStream ) this.fetch( url );
			Drawable d = Drawable.createFromStream( is , "src" );
			return d;
		}
		catch( MalformedURLException e )
		{
			e.printStackTrace();
			return null;
		}
		catch( IOException e )
		{
			e.printStackTrace();
			return null;
		}
	}	

	public Object fetch( String address ) throws MalformedURLException , IOException
	{
		URL url = new URL( address );
		Object content = url.getContent();
		return content;
	}
	
	public static void addInstalledPackage( String name , String namespace , String version , String codeversion )
	{
		for( int h = 0; h < Project.getProjects().size(); h++ )
		{
			Project proj = Project.getProjects().get( h );
						
			if( proj.getTitle().trim().compareTo( name.trim() ) == 0 && proj.getNamespace().trim().compareTo( namespace.trim() ) == 0 )
			{
				proj.setInstalled( true );
				Log.e( namespace , "installed" );
				
				if( proj.getVersion().trim().compareTo( version ) != 0 )
				{
					proj.setUpdateAvailable( true );
					Log.e( namespace , "update available" );
				}
			}						
		}
	}
	
}
