/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 12, 2015
 * Filename:	MainActivity.java
 *
 * Last modified:	Jan 3, 2016
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
//import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import javax.sql.CommonDataSource;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private App m_main_app = null;

    private android.graphics.Bitmap m_bmp_app_icon = null;


    // ------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_main_app = (App)getApplicationContext();
        if ( m_main_app == null ) {
            this.finish();
        }
        m_main_app.init();

        __DEBUG__("onCreate()");


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        init();
    }

    @Override
    protected void onDestroy() {
        __DEBUG__("onDestroy()");
        super.onDestroy();

        if ( m_main_app != null ) {
            m_main_app.release();
        }

        release();


        System.gc();
    }

    // ------------------------------------------------------------------------

    public void __DEBUG__(String str) {
        if ( m_main_app != null ) {
            m_main_app.__DEBUG__( TAG, str );
        }
    }

    public void __DEBUG_ERROR__(String str, StackTraceElement[] val) {
        if ( m_main_app != null ) {
            m_main_app.__DEBUG_ERROR__( false, TAG, str, val );
        }
    }

    // ------------------------------------------------------------------------

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        __DEBUG__("onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        __DEBUG__("onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        __DEBUG__("onResume()");
        super.onResume();

        /*
        if ( m_main_app != null ) {
            // Notification
            m_main_app.notification_clear();
        }
        */
        Common.notification_clear( this );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        __DEBUG__("onActivityResult()");

        switch ( requestCode ) {
            case Common.__REQUEST_CODE__FRAGMENT_TAG_1__PICK_PHOTO__:
            case Common.__REQUEST_CODE__FRAGMENT_TAG_1__PICK_CONTACT__:
            {
                if ( requestCode == Common.__REQUEST_CODE__FRAGMENT_TAG_1__PICK_CONTACT__ ) {
                    //FragmentManager fragmentManager = getFragmentManager();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = null;

                    if ( fragmentManager != null ) {
                        fragment = fragmentManager.findFragmentByTag( Common.__FRAGMENT_TAG_1__ );

                        if ( fragment != null ) {
                            ((slidemenu_1_fragment) fragment).onActivityResult( requestCode, resultCode, data );
                        }
                    }
                }
            } break;
            //case ...:
            //{
            //} break;
            default:
                {
                    if ( resultCode == RESULT_OK ) {
                    }
                    else {
                        __DEBUG__( "onActivityResult(): Unknown resultCode = " + resultCode );
                    }
                } break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    //! We can use onNewIntent() here due to "Intent.FLAG_ACTIVITY_NEW_TASK" in a class "CAlarmBroadcastReceiver"
    /*
    @Override
    protected void onNewIntent(Intent intent) {
        __DEBUG__( "onNewIntent()" );
        super.onNewIntent(intent);

        setIntent( intent );

        set_intent_data();
    }
    */

    // ------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        /*
        // BackStack count
        //getFragmentManager().getBackStackEntryCount();
        //getSupportFragmentManager().getBackStackEntryCount();

        // previous Fragment
        //getFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            //FragmentManager fragmentManager = getFragmentManager();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new slidemenu_1_fragment();

            if ( fragment != null && fragmentManager != null ) {
                fragmentManager.beginTransaction()
                        //.replace(R.id.container, PlaceholderNetworkFragment.newInstance(position + 1))
                        .replace(R.id.container, fragment)
                                //.addToBackStack("flagBack")
                        .commit();
            }
        } else if (id == R.id.nav_send) {
            //FragmentManager fragmentManager = getFragmentManager();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new slidemenu_2_fragment();

            if ( fragment != null && fragmentManager != null ) {
                fragmentManager.beginTransaction()
                        //.replace(R.id.container, PlaceholderNetworkFragment.newInstance(position + 1))
                        .replace(R.id.container, fragment)
                                //.addToBackStack("flagBack")
                        .commit();
            }
        }
        */

        {
            LinearLayout alarm_show_layout = (LinearLayout)findViewById( R.id.LinearLayout_main_activity_show_alarm );
            if ( alarm_show_layout != null ) {
                alarm_show_layout.setVisibility( View.GONE );
            }
        }

        // Alarm App
        if ( id == R.id.nav_slidemenu_0 ) {
            //FragmentManager fragmentManager = getFragmentManager();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new slidemenu_0_fragment();

            if ( fragment != null && fragmentManager != null ) {
                fragmentManager.beginTransaction()
                        //.replace(R.id.container, PlaceholderNetworkFragment.newInstance(position + 1))
                        .replace(R.id.container, fragment, Common.__FRAGMENT_TAG_0__)
                        //.addToBackStack("flagBack")
                        .commit();
            }
        }
        // App list test
        else if ( id == R.id.nav_slidemenu_1 ) {
            //FragmentManager fragmentManager = getFragmentManager();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new slidemenu_1_fragment();

            if ( fragment != null && fragmentManager != null ) {
                fragmentManager.beginTransaction()
                        //.replace(R.id.container, PlaceholderNetworkFragment.newInstance(position + 1))
                        .replace(R.id.container, fragment, Common.__FRAGMENT_TAG_1__)
                        //.addToBackStack("flagBack")
                        .commit();
            }
        }
        /*
        else if ( id == R.id.nav_slidemenu_2 ) {
            //FragmentManager fragmentManager = getFragmentManager();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new slidemenu_2_fragment();

            if ( fragment != null && fragmentManager != null ) {
                fragmentManager.beginTransaction()
                        //.replace(R.id.container, PlaceholderNetworkFragment.newInstance(position + 1))
                        .replace(R.id.container, fragment, Common.__FRAGMENT_TAG_2__)
                        //.addToBackStack("flagBack")
                        .commit();
            }
        }
        */
        /*
        //! TEST: contact
        else if ( id == R.id.nav_req_contact ) {
            // Contact list
            //Intent intent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
            Intent intent = new Intent( Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI );
            if ( intent != null ) {
                startActivityForResult( intent, Common.__REQUEST_CODE__FRAGMENT_TAG_1__PICK_CONTACT__ );
            }
        }
        */



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // ------------------------------------------------------------------------

    private void init() {
        __DEBUG__("init()");

        // Prevent soft-keyboard slide-out
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );

        set_intent_data();
    }

    private void release() {
        __DEBUG__( "release()" );

        if ( m_bmp_app_icon != null ) {
            m_bmp_app_icon.recycle();
            m_bmp_app_icon = null;
        }
    }

    // ------------------------------------------------------------------------

    //! DO NOT USE getIntent() here.
    // SEE: MainActivity::onNewIntent() and CAlarmBroadcastReceiver::onReceive()
    private void set_intent_data() {
        __DEBUG__( "set_intent_data()" );

        LinearLayout alarm_show_layout = (LinearLayout)findViewById( R.id.LinearLayout_main_activity_show_alarm );
        LinearLayout layout_app = (LinearLayout)findViewById( R.id.LinearLayout_main_activity_show_alarm_app );
        LinearLayout layout_call = (LinearLayout)findViewById( R.id.LinearLayout_main_activity_show_alarm_call );
//        Intent intent = getIntent();

        if ( m_main_app == null )
            return;


//        if ( intent != null ) {
//            int req_code = intent.getIntExtra( Common.__ALARM_LIST__KEY__REQUEST_CODE__, Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__ );
        if ( m_main_app.get_alarm_fired() ) {
            int req_code = m_main_app.get_alarm_fired_request_code();

            m_main_app.set_alarm_fired_clear();

            __DEBUG__( "set_intent_data(): request code = " + req_code );

            if ( req_code == Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__ ) {
                if ( alarm_show_layout != null ) {
                    alarm_show_layout.setVisibility( View.GONE );
                }

                return;
            }


            {
                __DEBUG__( "set_intent_data(): change layout for main" );

                //FragmentManager fragmentManager = getFragmentManager();
                FragmentManager fragmentManager = getSupportFragmentManager();
                List<Fragment> fragment_list = null;

                if ( fragmentManager != null ) {
                    fragment_list = fragmentManager.getFragments();

                    if ( fragment_list != null ) {
                        __DEBUG__( "set_intent_data(): remove fragments; size =  " + fragment_list.size() );

                        for ( int i = 0; i < fragment_list.size(); i++ ) {
                            Fragment fragment = fragment_list.get( i );

                            if ( fragment != null ) {
                                __DEBUG__( "set_intent_data(): [" + i + "] removing fragment tag = " + fragment.getTag() );

                                fragmentManager.beginTransaction()
                                        .remove(fragment)
                                        .commit();
                            }
                        }
                    }
                }
            }

            if ( alarm_show_layout != null ) {
                alarm_show_layout.setVisibility( View.VISIBLE );
            }
            if ( layout_app != null ) {
                layout_app.setVisibility( View.GONE );
            }
            if ( layout_call != null ) {
                layout_call.setVisibility( View.GONE );
            }
            {
                CAlarmToDo todo = m_main_app.get_alarm_todo();
                int type = Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__;
                String app_name = null;
                String app_pkg_name = null;
                byte app_icon[] = null;
                String call_name = null;
                String call_number = null;
                String message = null;

                if ( todo == null )
                    return;

                {
                    HashMap<String, Object> map = todo.get_alarm_list_from_request_code( req_code );
                    if ( map != null ) {
                        type = (Integer)map.get( Common.__ALARM_LIST__KEY__TYPE__ );
                        app_name = (String)map.get( Common.__ALARM_LIST__KEY__APP_NAME__ );
                        app_pkg_name = (String)map.get( Common.__ALARM_LIST__KEY__APP_PKG_NAME__ );
                        app_icon = (byte[])map.get( Common.__ALARM_LIST__KEY__APP_ICON__ );
                        call_name = (String)map.get( Common.__ALARM_LIST__KEY__CALL_NAME__ );
                        call_number = (String)map.get( Common.__ALARM_LIST__KEY__CALL_NUMBER__ );
                        message = (String)map.get( Common.__ALARM_LIST__KEY__MSG__ );
                    }
                }

                if ( type == Common.__ALARM_LIST__KEY__TYPE__APP__ ) {
                    if ( layout_app != null ) {
                        layout_app.setVisibility( View.VISIBLE );
                    }

                    TextView tv_app_name = (TextView)findViewById( R.id.TextView_app_name );
                    ImageView iv_app_icon = (ImageView)findViewById( R.id.ImageView_thumb_app_icon );
                    Button btn_app_launch = (Button)findViewById( R.id.Button_app_launch );

                    if ( tv_app_name != null ) {
                        if ( app_name != null ) {
                            tv_app_name.setText( app_name );
                        }
                    }

                    if ( iv_app_icon != null ) {
                        if ( app_icon != null ) {
                            m_bmp_app_icon = android.graphics.BitmapFactory.decodeByteArray( app_icon, 0, app_icon.length );
                            iv_app_icon.setImageBitmap( m_bmp_app_icon );
                        }
                    }

                    if ( btn_app_launch != null ) {
                        final String launch_app_pkg_name = app_pkg_name;
                        btn_app_launch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = null;
                                PackageManager pm = getPackageManager();

                                if ( pm != null ) {
                                    intent = pm.getLaunchIntentForPackage( launch_app_pkg_name );
                                    if ( intent != null ) {
                                        startActivity( intent );
                                    }
                                }
                            }
                        });
                    }
                }
                else if ( type == Common.__ALARM_LIST__KEY__TYPE__CALL__ ) {
                    if ( layout_call != null ) {
                        layout_call.setVisibility( View.VISIBLE );
                    }

                    TextView tv_call_name = (TextView)findViewById( R.id.TextView_call_name );
                    TextView tv_call_number = (TextView)findViewById( R.id.TextView_call_number );
                    Button btn_call_number = (Button)findViewById( R.id.Button_call_number );
                    final String add_call_number = call_number;

                    if ( tv_call_name != null ) {
                        if ( call_name != null ) {
                            tv_call_name.setText( call_name );
                        }
                    }

                    if ( tv_call_number != null ) {
                        if ( call_number != null ) {
                            tv_call_number.setText( call_number );
                        }
                    }

                    if ( btn_call_number != null ) {
                        btn_call_number.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ( add_call_number != null ) {
                                    Intent intent = new Intent( Intent.ACTION_DIAL, Uri.parse("tel:" + add_call_number) );
                                    if ( intent != null ) {
                                        startActivity( intent );
                                    }
                                }
                            }
                        });
                    }
                }

                if ( message != null ) {
                    EditText et_message = (EditText)findViewById( R.id.EditText_msg );

                    if ( et_message != null ) {
                        et_message.setText( message );
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------------
}
