/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 12, 2015
 * Filename:	slidemenu_1_fragment.java
 *
 * Last modified:	Dec 14, 2015
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
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class slidemenu_1_fragment extends Fragment {
    private static final String TAG = "slidemenu_1_fragment";
    private App m_main_app = null;

    View m_rootview;
    private ListView m_listview;
    private todoListArrayAdapter m_listviewAdapter;
    private ArrayList<HashMap<String, Object>> m_list_items = null;
    private static final String __KEY__APP_NAME__ = "app_name";
    private static final String __KEY__APP_ICON__ = "app_icon";
    //private static final String __KEY__APP_ICON_ID__ = "app_icon_id";
    private static final String __KEY__APP_PKG_NAME__ = "app_pkg_name";


    // ------------------------------------------------------------------------


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        m_main_app = (App)getActivity().getApplicationContext();

        __DEBUG__( "onCreateView()" );

        m_rootview = inflater.inflate( R.layout.slidemenu_1_layout, container, false );
        return m_rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        __DEBUG__( "onActivityCreated()" );

        m_list_items = new ArrayList<HashMap<String, Object>>();
        load_app_list();

        if ( m_rootview != null ) {
            m_listviewAdapter = new todoListArrayAdapter( getActivity(),
                    //R.layout.slidemenu_1_layout_listview_item, new String[] {"dummy1", "dummy2"} );
                    R.layout.slidemenu_1_layout_listview_item, new Object[] {m_list_items} );
            m_listview = (ListView)m_rootview.findViewById( R.id.ListView_apps );

            if ( (m_listview != null) && (m_listviewAdapter != null) ) {
                m_listview.setAdapter( m_listviewAdapter );
                __DEBUG__("onActivityCreated(): done...");

                m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText( getContext(), "position = #" + position, Toast.LENGTH_SHORT ).show();

                        {
                            if ( (m_list_items != null) && !m_list_items.isEmpty() ) {
                                HashMap<String, Object> map = m_list_items.get( position );
                                String pkg_name = null;

                                if ( map != null ) {
                                    pkg_name = (String)map.get( __KEY__APP_PKG_NAME__ );
                                    if ( pkg_name != null ) {
                                        Intent intent = null;
                                        PackageManager pm = getActivity().getPackageManager();

                                        if ( pm != null ) {
                                            intent = pm.getLaunchIntentForPackage(pkg_name);
                                            if ( intent != null ) {
                                                startActivity( intent );
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        /*
                        Drawable d = null;

                        if ( (m_list_items != null) && !m_list_items.isEmpty() ) {
                            HashMap<String, Object> map = m_list_items.get( position );

                            if ( map != null ) {
                                d = (Drawable) map.get( __KEY__APP_ICON__ );
                            }
                        }

                        if ( d == null )
                            return;

                        try {
                            Intent intent = new Intent();

                            if ( intent != null ) {
                                String app_name = "";
                                Bitmap bmp = ((BitmapDrawable)d).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte buff[] = null;

                                if ( (bmp != null) && (baos != null) ) {
                                    bmp.compress( Bitmap.CompressFormat.PNG, 100, baos );
                                    buff = baos.toByteArray();
                                }

                                if ( buff != null ) {
                                    intent.putExtra( Common.__APP_LIST__KEY__APP_NAME__, app_name );
                                    intent.putExtra( Common.__APP_LIST__KEY__APP_ICON__, buff );

                                    getActivity().setResult( Activity.RESULT_OK, intent );
                                }
                                else {
                                    getActivity().setResult( Activity.RESULT_CANCELED, intent );
                                }
                                getActivity().finish();
                            }
                        }
                        catch ( Exception e ) {
                            __DEBUG_ERROR__( "", e.getStackTrace() );
                        }
                        */
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

    @Override
    public void onDestroy() {
        __DEBUG__("onDestroy()");
        super.onDestroy();

        m_listviewAdapter = null;
        if ( m_listview != null ) {
            m_listview.setAdapter( null );
            m_listview = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        __DEBUG__( "onSaveInstanceState()" );
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        __DEBUG__( "onPause()" );
        super.onPause();
    }

    @Override
    public void onResume() {
        __DEBUG__("onResume()");
        super.onResume();

        /*
        if ( m_main_app != null ) {
            // Notification
            m_main_app.notification_clear();
        }
        */
        Common.notification_clear( getActivity() );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        __DEBUG__( "onActivityResult()" );

        if ( resultCode == Activity.RESULT_OK ) {
            if ( requestCode == Common.__REQUEST_CODE__FRAGMENT_TAG_1__PICK_CONTACT__ ) {
                Uri uri = data.getData();
                //String proj[] = { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                //                    ContactsContract.CommonDataKinds.Phone.NUMBER };
                //String proj[] = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                //        ContactsContract.PhoneLookup.NUMBER };
                Cursor c = null;

                if ( uri != null ) {
                    c = getActivity().getContentResolver().query( uri, null, null, null, null );

                    if ( c != null ) {
                        if ( c.moveToFirst() ) {
                            String name = c.getString( c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) );
                            String number = c.getString( c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER) );
                            //String name = c.getString( c.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME) );
                            //String number = c.getString( c.getColumnIndexOrThrow(ContactsContract.PhoneLookup.NUMBER) );

                            __DEBUG__( "onActivityResult(): name = " + name );
                            __DEBUG__( "onActivityResult(): number = " + number );
                        }

                        c.close();
                    }
                }
            }
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

    public void load_app_list() {
        __DEBUG__( "load_app_list()" );

        if ( m_list_items != null ) {
            __DEBUG__( "load_app_list(): adding... " );

            final android.content.pm.PackageManager pm = getActivity().getPackageManager();
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
                    map.put( __KEY__APP_NAME__, (String)packages.get(i).loadLabel(pm) );
                    map.put( __KEY__APP_PKG_NAME__, (String)packages.get(i).packageName);
                    map.put( __KEY__APP_ICON__, packages.get(i).loadIcon(pm) );
                    //map.put( __KEY__ICON_ID__, packages.get(i).icon );

                    m_list_items.add(map);
                }
            }
        }
    }

    // ------------------------------------------------------------------------

    private class todoListArrayAdapter extends ArrayAdapter {
        private Context m_context = null;
        private LayoutInflater m_inflater = null;
        private ArrayList<HashMap<String, Object>> m_list_items = null;
        private int m_list_items_count = 0;

        public todoListArrayAdapter(Context context, int textViewResourceId, Object[] objects) {
            super(context, textViewResourceId, objects);

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
                v = m_inflater.inflate( R.layout.slidemenu_1_layout_listview_item, null );
                if ( v == null ) return null;

                v.setTag( R.id.ImageView_thumb_app_icon, v.findViewById(R.id.ImageView_thumb_app_icon) );
                v.setTag( R.id.TextView_title, v.findViewById(R.id.TextView_title) );
            }

            HashMap<String, Object> map = m_list_items.get( position );

            if ( map != null ) {
                TextView tv_app_name = (TextView)v.getTag( R.id.TextView_title );
                ImageView iv_app_icon = (ImageView)v.getTag(R.id.ImageView_thumb_app_icon);

                String app_name = (String)map.get(__KEY__APP_NAME__);
                android.graphics.drawable.Drawable app_icon =
                        (android.graphics.drawable.Drawable)map.get( __KEY__APP_ICON__ );
                //int icon_id = (Integer)map.get( __KEY__APP_ICON_ID__ );

                if ( (app_name != null) && (tv_app_name != null) ) {
                    tv_app_name.setText( app_name );
                }

                if ( (app_icon != null) && (iv_app_icon != null) ) {
                    iv_app_icon.setImageDrawable( app_icon );
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
