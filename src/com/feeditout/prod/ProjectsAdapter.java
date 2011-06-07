package com.feeditout.prod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProjectsAdapter extends BaseAdapter
{
	private Context context;

	public ProjectsAdapter( Context context )
	{
		this.context = context;
	}

	public int getCount()
	{
		return Project.getProjectCount();
	}

	public Project getItem( int position )
	{
		return Project.getProjects().get( position );
	}

	public long getItemId( int position )
	{
		return position;
	}

	public View getView( int position , View convertView , ViewGroup parent )
	{
		LinearLayout itemLayout;
		Project proj = Project.getProjects().get( position );

		itemLayout = ( LinearLayout ) LayoutInflater.from( context ).inflate( R.layout.projectslist , parent , false );

		TextView projectDescription = ( TextView ) itemLayout.findViewById( R.id.projectDescription );
		projectDescription.setText( proj.getDescription() + "" );

		TextView projectTitle = ( TextView ) itemLayout.findViewById( R.id.projectTitle );
		projectTitle.setText( proj.getTitle() + ""  );

		TextView projectDownloads = ( TextView ) itemLayout.findViewById( R.id.projectDownloads );
		projectDownloads.setText( proj.getDownloads() + "" );
		
		TextView projectVersion = ( TextView ) itemLayout.findViewById( R.id.projectVersion );
		projectVersion.setText( proj.getVersion() + "" );
		
		TextView projectId = ( TextView ) itemLayout.findViewById( R.id.projectId );
		projectId.setText( proj.getShortname() + "" );
		
		ImageView imgView = new ImageView( this.context );
		imgView = ( ImageView ) itemLayout.findViewById( R.id.projectIcon );
		imgView.setImageDrawable( proj.getImageIcon( this.context ) );
		
		Button updateButton = ( Button ) itemLayout.findViewById( R.id.updateSelected );
		if( proj.isUpdateAvailable() == false )
		{
			updateButton.setText( "Up to date" );
			updateButton.setEnabled( false );
		}
		
		Button installButton = ( Button ) itemLayout.findViewById( R.id.installSelected );
		if( proj.isInstalled() == true )
		{
			installButton.setText( "Installed" );
			installButton.setEnabled( false );
			updateButton.setVisibility( Button.VISIBLE );
		}
		else
		{
			updateButton.setVisibility( Button.GONE );
		}

		return itemLayout;
	}	
}
