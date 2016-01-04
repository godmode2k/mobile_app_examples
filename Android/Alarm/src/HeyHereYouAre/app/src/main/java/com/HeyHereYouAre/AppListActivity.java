/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 12, 2015
 * Filename:	AppListActivity.java
 *
 * Last modified:	Dec 29, 2015
 * License:
 *
 *
 * Copyright (C) 2014 Ho-Jung Kim (godmode2k@hotmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Source:
 * Note: {
  }
 * -----------------------------------------------------------------
 * TODO:
 *
 * URGENT!!!
 * TODO:
 *
 */

package com.HeyHereYouAre;

//import android.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2015-12-12.
 */
public class AppListActivity extends Activity {
    private static final String TAG = "AppListActivity";
    private App m_main_app = null;

    private ListView m_listview;
    private appListArrayAdapter m_listviewAdapter;
    private ArrayList<HashMap<String, Object>> m_list_items = null;


    // ------------------------------------------------------------------------


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_main_app = (App)getApplicationContext();

        __DEBUG__("onCreate()");

        setContentView(R.layout.activity_app_list);

        init();
    }


    // ------------------------------------------------------------------------

    @Override
    public void onDestroy() {
        __DEBUG__("onDestroy()");
        super.onDestroy();

        release();

        System.gc();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        __DEBUG__( "onConfigurationChanged()" );
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        __DEBUG__("onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        __DEBUG__( "onPause()" );
        super.onPause();
    }

    @Override
    public void onResume() {
        __DEBUG__( "onResume()" );
        super.onResume();

        /*
        if ( m_main_app != null ) {
            // Notification
            m_main_app.notification_clear();
        }
        */
        Common.notification_clear( AppListActivity.this );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        __DEBUG__( "onActivityResult()" );

        if ( resultCode == Activity.RESULT_OK ) {
        }
        else {
            __DEBUG__("onActivityResult(): Unknown resultCode = " + resultCode);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    // ------------------------------------------------------------------------

    public void __DEBUG__(String str) {
        if ( m_main_app != null ) {
            m_main_app.__DEBUG__( TAG, str );
        }
    }

    public void __DEBUG_ERROR__(String str, StackTraceElement[] val) {
        if ( m_main_app != null ) {
            m_main_app.__DEBUG_ERROR__(false, TAG, str, val);
        }
    }

    // ------------------------------------------------------------------------

    private void init() {
        __DEBUG__( "init()" );

        m_list_items = new ArrayList<HashMap<String, Object>>();
        load_app_list();

        {
            m_listviewAdapter = new appListArrayAdapter( this,
                    //R.layout.activity_app_list_listview_item, new String[] {"dummy1", "dummy2"} );
                    R.layout.activity_app_list_listview_item, new Object[] {m_list_items} );
            m_listview = (ListView)findViewById( R.id.ListView_apps );

            if ( (m_listview != null) && (m_listviewAdapter != null) ) {
                m_listview.setAdapter( m_listviewAdapter );
                __DEBUG__( "init(): done..." );

                m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText( getContext(), "position = #" + position, Toast.LENGTH_SHORT ).show();

                        String app_name = null;
                        String app_pkg_name = null;
                        Drawable d = null;

                        if ( (m_list_items != null) && !m_list_items.isEmpty() ) {
                            HashMap<String, Object> map = m_list_items.get( position );

                            if (map != null) {
                                app_name = (String)map.get( Common.__ALARM_LIST__KEY__APP_NAME__ );
                                app_pkg_name = (String)map.get( Common.__ALARM_LIST__KEY__APP_PKG_NAME__ );
                                d = (Drawable)map.get( Common.__ALARM_LIST__KEY__APP_ICON__ );
                            }
                        }

                        if ( (app_name == null) || (app_pkg_name == null) || (d == null) )
                            return;

                        try {
                            Intent intent = new Intent();

                            if ( intent != null ) {
                                Bitmap bmp = ((BitmapDrawable) d).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte buff[] = null;

                                if ( (bmp != null) && (baos != null) ) {
                                    bmp.compress( Bitmap.CompressFormat.PNG, 100, baos );
                                    buff = baos.toByteArray();

                                    //! DO NOT RELEASE Bitmap from ApplicationInfo.loadIcon()
                                    //bmp.recycle();
                                    //bmp = null;
                                }

                                if ( buff != null ) {
                                    intent.putExtra( Common.__ALARM_LIST__KEY__APP_NAME__, app_name );
                                    intent.putExtra( Common.__ALARM_LIST__KEY__APP_PKG_NAME__, app_pkg_name );
                                    intent.putExtra( Common.__ALARM_LIST__KEY__APP_ICON__, buff );

                                    AppListActivity.this.setResult( Activity.RESULT_OK, intent );
                                }
                                else {
                                    AppListActivity.this.setResult( Activity.RESULT_CANCELED, intent );
                                }
                                AppListActivity.this.finish();
                            }
                        }
                        catch ( Exception e ) {
                            __DEBUG_ERROR__( "", e.getStackTrace() );
                        }
                    }
                });

                m_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        return false;
                    }
                });
            }
        }
    }

    private void release() {
        __DEBUG__( "release()" );

        m_listviewAdapter = null;
        if ( m_listview != null ) {
            m_listview.setAdapter( null );
            m_listview = null;
        }
    }

    // ------------------------------------------------------------------------

    public void load_app_list() {
        __DEBUG__( "load_app_list()" );

        if ( m_list_items != null ) {
            __DEBUG__("load_app_list(): adding... ");

            final android.content.pm.PackageManager pm = getPackageManager();
            List<ApplicationInfo> packages = null;

            if ( pm == null )
                return;

            packages = pm.getInstalledApplications( android.content.pm.PackageManager.GET_META_DATA );
            if ( (packages == null) || packages.isEmpty() )
                return;

            /*
            for ( android.content.pm.ApplicationInfo packageInfo : packages ) {
                __DEBUG__( "load_app_list(): Installed package :" + packageInfo.packageName );
                __DEBUG__( "load_app_list(): Source dir : " + packageInfo.sourceDir );
                __DEBUG__( "load_app_list(): Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName) );
            }
            // the getLaunchIntentForPackage returns an intent that you can use with startActivity()
            */

            __DEBUG__( "load_app_list(): package size = " + packages.size() );
            for ( int i = 0; i < packages.size(); i++ ) {
                if ( (packages.get(i).flags & ApplicationInfo.FLAG_SYSTEM) != 0 ) continue;

                //__DEBUG__( "load_app_list(): list = " + i + " {" );
                //__DEBUG__( "load_app_list():     Installed package = " + packages.get(i).packageName );
                //__DEBUG__( "load_app_list():     name = " + (String)packages.get(i).loadLabel(pm) );
                //__DEBUG__( "load_app_list():     Source dir = " + packages.get(i).sourceDir );
                //__DEBUG__( "load_app_list():     Launch Activity = " + pm.getLaunchIntentForPackage(packages.get(i).packageName) );
                //__DEBUG__( "load_app_list():     icon id = " + packages.get(i).icon );
                //__DEBUG__( "load_app_list(): }" );

                HashMap<String, Object> map = new HashMap<String, Object>();
                if ( map != null ) {
                    map.put( Common.__ALARM_LIST__KEY__APP_NAME__, (String)packages.get(i).loadLabel(pm) );
                    map.put( Common.__ALARM_LIST__KEY__APP_PKG_NAME__, (String)packages.get(i).packageName );
                    map.put( Common.__ALARM_LIST__KEY__APP_ICON__, (Drawable)packages.get(i).loadIcon(pm) );
                    //map.put( Common.__ALARM_LIST__KEY__APP_ICON_ID__, packages.get(i).icon );

                    m_list_items.add( map );
                }
            }
        }
    }

    // ------------------------------------------------------------------------

    private class appListArrayAdapter extends ArrayAdapter {
        private Context m_context = null;
        private LayoutInflater m_inflater = null;
        private ArrayList<HashMap<String, Object>> m_list_items = null;
        private int m_list_items_count = 0;

        public appListArrayAdapter(Context context, int textViewResourceId, Object[] objects) {
            super(context, textViewResourceId, objects);

            //ArrayList<HashMap<String, Object>> entryHashList;
            //int listCount;

            m_context = context;
            m_list_items = (ArrayList<HashMap<String, Object>>)objects[0];
            m_inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        }

        @Override
        public int getCount() {
            //return super.getCount();

            m_list_items_count = 0;
            if ( m_list_items != null )
                m_list_items_count = m_list_items.size();

            return m_list_items_count;
        }

        /*
        @Override
        public Object getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
        */

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            View v = convertView;

            if ( m_inflater == null ) {
                return null;
            }

            if ( (m_list_items == null) || (m_list_items.isEmpty()) )
                return null;

            if ( convertView == null ) {
                v = m_inflater.inflate( R.layout.activity_app_list_listview_item, null );
                if ( v == null ) return null;

                v.setTag( R.id.ImageView_thumb_app_icon, v.findViewById(R.id.ImageView_thumb_app_icon) );
                v.setTag( R.id.TextView_app_name, v.findViewById(R.id.TextView_app_name) );
            }

            HashMap<String, Object> map = m_list_items.get( position );

            if ( map != null ) {
                TextView tv_app_name = (TextView)v.getTag( R.id.TextView_app_name );
                ImageView iv_app_icon = (ImageView)v.getTag( R.id.ImageView_thumb_app_icon );

                String app_name = (String)map.get( Common.__ALARM_LIST__KEY__APP_NAME__ );
                Drawable app_icon = (Drawable)map.get( Common.__ALARM_LIST__KEY__APP_ICON__ );
                //int icon_id = (Integer)map.get( Common.__ALARM_LIST__KEY__APP_ICON_ID__ );

                if ( (app_name != null) && (tv_app_name != null) ) {
                    tv_app_name.setText( app_name );
                }

                if ( app_icon != null ) {
                    if ( iv_app_icon != null ) {
                        iv_app_icon.setImageDrawable( app_icon );
                    }
                    else {
                        iv_app_icon.setImageResource( R.mipmap.ic_launcher );
                    }
                }

                /*
                // Application Info Icon Resource Id
                if ( iv_app_icon != null ) {
                    // USE Bitmap
                    String package_name = "";
                    android.content.pm.PackageManager pm = getActivity().getPackageManager();
                    android.content.pm.ApplicationInfo appinfo =
                            pm.getApplicationInfo( package_name, android.content.pm.PackageManager.GET_META_DATA );
                    android.content.res.Resources resource = pm.getResourcesForApplication( appinfo );
                    int icon_res_id = appinfo.icon;
                    android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeResource( resource, icon_res_id );

                    iv_icon.setImageBitmap( bitmap );
                }
                */
            }



            return v;
        }
    }
}
