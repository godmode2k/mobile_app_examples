/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 12, 2015
 * Filename:	AddAlarmActivity.java
 *
 * Last modified:	Jan 4, 2016
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

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by admin on 2015-12-15.
 */
//public class AddAlarmActivity extends AppCompatActivity {
public class AddAlarmActivity extends Activity {
    private static final String TAG = "AddAlarmActivity";
    private App m_main_app = null;

    private String m_app_name = null;
    private String m_app_pkg_name = null;
    private byte m_app_icon[] = null;
    private android.graphics.Bitmap m_bmp_app_icon = null;
    private int m_alarm_type = Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__;
    private boolean m_update_mode = false;
    private int m_update_alarm_by_request_code = Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__;


    // ------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_main_app = (App)getApplicationContext();

        __DEBUG__("onCreate()");

        setContentView(R.layout.activity_add_alarm);

        // Prevent soft-keyboard slide-out
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );

        init();
    }

    @Override
    protected void onDestroy() {
        __DEBUG__( "onDestroy()" );
        super.onDestroy();

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
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        __DEBUG__("onRestoreInstanceState()");
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
        Common.notification_clear( AddAlarmActivity.this );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        __DEBUG__("onActivityResult()");

        m_app_name = null;
        m_app_pkg_name = null;
        m_app_icon = null;
        {
            if ( m_bmp_app_icon != null ) {
                m_bmp_app_icon.recycle();
                m_bmp_app_icon = null;
            }

            ImageView iv_icon = (ImageView)findViewById( R.id.ImageView_thumb_app_icon );
            if ( iv_icon != null ) {
                iv_icon.setImageBitmap( null );
                iv_icon.setImageResource( R.mipmap.ic_launcher );
            }
        }

        if ( resultCode == RESULT_OK ) {
            if ( requestCode == Common.__REQUEST_CODE__APP_LIST__PICK_PHOTO__ ) {
                __DEBUG__( "onActivityResult(): App name and icon" );

                if ( data != null ) {
                    String app_name = data.getStringExtra( Common.__ALARM_LIST__KEY__APP_NAME__ );
                    String app_pkg_name = data.getStringExtra( Common.__ALARM_LIST__KEY__APP_PKG_NAME__ );
                    byte buff[] = data.getByteArrayExtra( Common.__ALARM_LIST__KEY__APP_ICON__ );


                    if ( app_name != null ) {
                        TextView tv_app_name = (TextView)findViewById( R.id.TextView_app_name );

                        m_app_name = app_name;

                        if ( tv_app_name != null ) {
                            tv_app_name.setText( app_name );
                        }
                    }

                    m_app_pkg_name = app_pkg_name;

                    if ( buff != null ) {
                        m_app_icon = buff;

                        m_bmp_app_icon = android.graphics.BitmapFactory.decodeByteArray( buff, 0, buff.length );
                        if ( m_bmp_app_icon != null ) {
                            ImageView iv_icon = (ImageView)findViewById( R.id.ImageView_thumb_app_icon );
                            if ( iv_icon != null ) {
                                iv_icon.setImageBitmap( m_bmp_app_icon );
                            }
                        }
                    }
                }
            }
            else if ( requestCode == Common.__REQUEST_CODE__APP_LIST__PICK_CONTACT__ ) {
                __DEBUG__( "onActivityResult(): Call name and number" );

                Uri uri = data.getData();
                //String proj[] = { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                //                    ContactsContract.CommonDataKinds.Phone.NUMBER };
                //String proj[] = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                //        ContactsContract.PhoneLookup.NUMBER };
                Cursor c = null;
                String name = null;
                String number = null;

                if ( uri != null ) {
                    c = getContentResolver().query( uri, null, null, null, null );

                    if ( c != null ) {
                        if ( c.moveToFirst() ) {
                            name = c.getString( c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) );
                            number = c.getString( c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER) );
                            //String name = c.getString( c.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME) );
                            //String number = c.getString( c.getColumnIndexOrThrow(ContactsContract.PhoneLookup.NUMBER) );

                            __DEBUG__( "onActivityResult(): name = " + name );
                            __DEBUG__( "onActivityResult(): number = " + number );

                            {
                                TextView tv_call_name = (TextView)findViewById( R.id.TextView_call_name );
                                TextView tv_call_number = (TextView)findViewById( R.id.TextView_call_number );

                                if ( (name != null) && (number != null) ) {
                                    if ( (tv_call_name != null) && (tv_call_number != null) ) {
                                        tv_call_name.setText( name );
                                        tv_call_number.setText( number );
                                    }
                                }
                                else {
                                    __DEBUG__( "onActivityResult(): name or number == NULL" );
                                }
                            }
                        }

                        c.close();
                    }
                }

                if ( (name == null) || (number == null) ) {
                    __DEBUG__( "onActivityResult(): name or number == NULL" );
                }
            }
        }
        else {
            __DEBUG__( "onActivityResult(): Unknown resultCode = " + resultCode );

            {
                TextView tv_call_name = (TextView)findViewById( R.id.TextView_call_name );
                TextView tv_call_number = (TextView)findViewById( R.id.TextView_call_number );

                if ( (tv_call_name != null) && (tv_call_number != null) ) {
                    tv_call_name.setText( "Name" );
                    tv_call_number.setText( "Number" );
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // ------------------------------------------------------------------------

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        // BackStack count
        //getFragmentManager().getBackStackEntryCount();
        //getSupportFragmentManager().getBackStackEntryCount();

        // previous Fragment
        //getFragmentManager().popBackStack();
        //getSupportFragmentManager().popBackStack();
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

        // ...

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    */

    // ------------------------------------------------------------------------

    private void init() {
        __DEBUG__( "init()" );

        final CheckBox cb_app = (CheckBox)findViewById( R.id.CheckBox_app );
        final CheckBox cb_call = (CheckBox)findViewById( R.id.CheckBox_call );
        final CheckBox cb_memo = (CheckBox)findViewById( R.id.CheckBox_memo );

        final ImageView iv_app_icon = (ImageView)findViewById( R.id.ImageView_thumb_app_icon );
        final TextView tv_app_name = (TextView)findViewById( R.id.TextView_app_name );
        final TextView tv_call_name = (TextView)findViewById( R.id.TextView_call_name );
        final TextView tv_call_number = (TextView)findViewById( R.id.TextView_call_number );
        final EditText et_msg = (EditText)findViewById( R.id.EditText_msg );

        final CheckBox cb_repeat_daily = (CheckBox)findViewById( R.id.CheckBox_repeat_daily );
        final Button btn_current_datetime = (Button)findViewById( R.id.Button_current_datetime );

        final DatePicker datepicker = (DatePicker)findViewById( R.id.DatePicker );
        final TimePicker timepicker = (TimePicker)findViewById( R.id.TimePicker );

        final Button btn_add = (Button)findViewById( R.id.Button_add );
        final Button btn_cancel = (Button)findViewById( R.id.Button_cancel );

        m_alarm_type = Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__;
        m_update_mode = false;


        // Edit the alarm request if have the request code
        set_intent_data();


        if ( cb_app != null ) {
            cb_app.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if ( isChecked ) {
                        m_alarm_type = Common.__ALARM_LIST__KEY__TYPE__APP__;

                        if ( cb_call != null ) {
                            cb_call.setChecked( false );
                        }

                        if ( cb_memo != null ) {
                            cb_memo.setChecked( false );
                        }

                        if ( tv_call_name != null ) {
                            tv_call_name.setText("Name");
                        }
                        if ( tv_call_number != null ) {
                            tv_call_number.setText( "Number" );
                        }

                        //if ( m_update_mode ) {
                        //    return;
                        //}

                        {
                            Intent intent = new Intent( AddAlarmActivity.this, AppListActivity.class );
                            if ( intent != null ) {
                                startActivityForResult( intent, Common.__REQUEST_CODE__APP_LIST__PICK_PHOTO__ );
                            }
                        }
                    }
                    else {
                        if (iv_app_icon != null) {
                            iv_app_icon.setImageResource(R.mipmap.ic_launcher);
                        }

                        if ( tv_app_name != null ) {
                            tv_app_name.setText( "App name" );
                        }
                    }
                }
            });
        }

        if ( cb_call != null ) {
            cb_call.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if ( isChecked ) {
                        m_alarm_type = Common.__ALARM_LIST__KEY__TYPE__CALL__;

                        if ( cb_app != null ) {
                            cb_app.setChecked( false );
                        }

                        if ( cb_memo != null ) {
                            cb_memo.setChecked( false );
                        }

                        if ( iv_app_icon != null ) {
                            iv_app_icon.setImageResource(R.mipmap.ic_launcher);
                        }
                        if ( tv_app_name != null ) {
                            tv_app_name.setText( "App name" );
                        }

                        {
                            Intent intent = new Intent( Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI );
                            if ( intent != null ) {
                                startActivityForResult( intent, Common.__REQUEST_CODE__APP_LIST__PICK_CONTACT__ );
                            }
                        }
                    }
                    else {
                        if ( tv_call_name != null ) {
                            tv_call_name.setText("Name");
                        }

                        if ( tv_call_number != null ) {
                            tv_call_number.setText( "number" );
                        }
                    }
                }
            });
        }

        if ( cb_memo != null ) {
            cb_memo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if ( isChecked ) {
                        m_alarm_type = Common.__ALARM_LIST__KEY__TYPE__MSG__;

                        if ( cb_app != null ) {
                            cb_app.setChecked( false );
                        }

                        if ( cb_call != null ) {
                            cb_call.setChecked( false );
                        }

                        if ( iv_app_icon != null ) {
                            iv_app_icon.setImageResource( R.mipmap.ic_launcher );
                        }
                        if ( tv_app_name != null ) {
                            tv_app_name.setText( "App name" );
                        }

                        if ( tv_call_name != null ) {
                            tv_call_name.setText( "Name" );
                        }
                        if ( tv_call_number != null ) {
                            tv_call_number.setText( "number" );
                        }
                    }
                }
            });
        }

        if ( cb_repeat_daily != null ) {
            cb_repeat_daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // NOP
                }
            });
        }

        if ( btn_current_datetime != null ) {
            btn_current_datetime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( (datepicker != null) && (timepicker != null) ) {
                        Calendar calendar = Calendar.getInstance();

                        if ( calendar != null ) {
                            int year = calendar.get( Calendar.YEAR );
                            int month = calendar.get( Calendar.MONTH );
                            int day = calendar.get( Calendar.DAY_OF_MONTH );
                            int hour = calendar.get( Calendar.HOUR_OF_DAY );
                            int min = calendar.get( Calendar.MINUTE );

                            datepicker.updateDate( year, month, day );

                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                                // API Level 23 (M)
                                timepicker.setHour( hour );
                                timepicker.setMinute( min );
                            }
                            else {
                                //! DEPRECATED: setCurrentHour(), setCurrentMinute() in API Level 23 (M)
                                timepicker.setCurrentHour( hour );
                                timepicker.setCurrentMinute( min );
                            }
                        }
                    }
                }
            });
        }

        if ( btn_add != null ) {
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    //! TEST ----- [
                    if (0 != 1) {
                        if ( (datepicker != null) && (timepicker != null) ) {
                            int request_code = 0;
                            int year = datepicker.getYear();
                            int month = datepicker.getMonth(); + 1
                            int day = datepicker.getDayOfMonth();
                            int hour = -1;
                            int min = -1;
                            int sec = 0;

                            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                                // API Level 23 (M)
                                hour = timepicker.getHour();
                                min = timepicker.getMinute();
                            }
                            else {
                                //! DEPRECATED: getCurrentHour(), getCurrentMinute() in API Level 23 (M)
                                hour = timepicker.getCurrentHour();
                                min = timepicker.getCurrentMinute();
                            }

                            __DEBUG__( "init(): yyyy-MM-dd HH:mm:ss = " +
                                    year + "-" + month + "-" + day + " " +
                                    hour + ":" + min + ":" + sec );

                            add_booking_alarm( request_code, year, month, day, hour, min, sec );
                        }
                        return;
                    }
                    //! TEST ----- ]
                    */

                    m_alarm_type = Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__;
                    if ( cb_app.isChecked() )
                        m_alarm_type = Common.__ALARM_LIST__KEY__TYPE__APP__;
                    else if ( cb_call.isChecked() )
                        m_alarm_type = Common.__ALARM_LIST__KEY__TYPE__CALL__;
                    else if ( cb_memo.isChecked() )
                        m_alarm_type = Common.__ALARM_LIST__KEY__TYPE__MSG__;

                    if ( m_alarm_type == Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__ ) {
                        Toast.makeText( AddAlarmActivity.this, "Alarm type is not selected", Toast.LENGTH_SHORT ).show();
                        return;
                    }

                    if ( m_main_app != null ) {
                        CAlarmToDo todo = m_main_app.get_alarm_todo();
                        String msg = null;
                        String call_name = null;
                        String call_number = null;
                        String alarm_datetime = null;
                        boolean ret = true;

                        if ( todo != null ) {
                            if ( (et_msg != null) && (et_msg.getText() != null) ) {
                                msg = et_msg.getText().toString();
                            }

                            if ( (tv_call_name != null) && (tv_call_number != null) ) {
                                if ( tv_call_name.getText() != null )
                                    call_name = tv_call_name.getText().toString();

                                if ( tv_call_number.getText() != null )
                                    call_number = tv_call_number.getText().toString();
                            }

                            {
                                if ( m_alarm_type == Common.__ALARM_LIST__KEY__TYPE__APP__ ) {
                                    if ( (m_app_name == null) || (m_app_pkg_name == null) || (m_app_icon == null) ) {
                                        Toast.makeText( AddAlarmActivity.this, "App is not selected", Toast.LENGTH_SHORT ).show();
                                        return;
                                    }
                                }
                                else if ( m_alarm_type == Common.__ALARM_LIST__KEY__TYPE__MSG__ ) {
                                    if ( (msg == null) || (msg.isEmpty()) ) {
                                        Toast.makeText( AddAlarmActivity.this, "Fill out the message", Toast.LENGTH_SHORT ).show();
                                        return;
                                    }
                                }
                                else if ( m_alarm_type == Common.__ALARM_LIST__KEY__TYPE__CALL__ ) {
                                    if ( (call_name == null) || (call_number == null) ||
                                            call_name.isEmpty() || call_number.isEmpty() ||
                                            call_name.equals("Name") || call_number.equals("Number") ) {
                                        Toast.makeText( AddAlarmActivity.this, "Call name and number are not selected", Toast.LENGTH_SHORT ).show();
                                        return;
                                    }
                                }
                                else {
                                    return;
                                }
                            }

                            if ( (datepicker != null) && (timepicker != null) ) {
                                int request_code = Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__;
                                boolean alarm_on = true;
                                boolean repeat_daily = false;
                                int year = datepicker.getYear();
                                int month = datepicker.getMonth() + 1;
                                int day = datepicker.getDayOfMonth();
                                int hour = -1;
                                int min = -1;
                                int sec = 0;

                                if ( m_update_mode ) {
                                    request_code = m_update_alarm_by_request_code;
                                }
                                else {
                                    request_code = todo.request_code_inc();
                                }

                                if ( cb_repeat_daily != null ) {
                                    repeat_daily = cb_repeat_daily.isChecked();
                                }

                                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                                    // API Level 23 (M)
                                    hour = timepicker.getHour();
                                    min = timepicker.getMinute();
                                }
                                else {
                                    //! DEPRECATED: getCurrentHour(), getCurrentMinute() in API Level 23 (M)
                                    hour = timepicker.getCurrentHour();
                                    min = timepicker.getCurrentMinute();
                                }

                                alarm_datetime = "" + year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;

                                __DEBUG__( "init(): [" + request_code + "] yyyy-MM-dd HH:mm:ss = " +
                                            alarm_datetime +
                                            ", repeat daily = " + repeat_daily );

                                if ( m_update_mode ) {
                                    ret = todo.remove_alarm_list_from_request_code( request_code );
                                    if ( !ret ) {
                                        Toast.makeText( AddAlarmActivity.this, "Update an alarm [FALSE]", Toast.LENGTH_SHORT ).show();
                                        return;
                                    }
                                }

                                ret = todo.add_alarm_list( m_alarm_type, request_code, alarm_on, repeat_daily, m_app_name, m_app_pkg_name, m_app_icon, msg,
                                                            year, month, day, hour, min, sec, call_name, call_number );
                                if ( !ret ) {
                                    if ( m_update_mode ) {
                                        Toast.makeText( AddAlarmActivity.this, "Update an alarm [FALSE]", Toast.LENGTH_SHORT ).show();
                                    }
                                    else {
                                        Toast.makeText( AddAlarmActivity.this, "Add an alarm [FALSE]", Toast.LENGTH_SHORT ).show();
                                    }
                                    return;
                                }

                                // add an alarm
                                add_booking_alarm( repeat_daily, request_code, year, month, day, hour, min, sec );

                                // save
                                m_main_app.set_alarm_list_save();
                            }

                            //Toast.makeText( AddAlarmActivity.this, "Alarm added [" + (ret ? "TRUE" : "FALSE") + "]", Toast.LENGTH_SHORT ).show();
                            AddAlarmActivity.this.finish();
                        }
                    }
                }
            });
        }

        if ( btn_cancel != null ) {
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddAlarmActivity.this.finish();
                }
            });
        }

        // Edit the alarm request if have the request code
        //set_intent_data();
    }

    private void release() {
        __DEBUG__( "release()" );

        if ( m_bmp_app_icon != null ) {
            m_bmp_app_icon.recycle();
            m_bmp_app_icon = null;
        }
    }

    // ------------------------------------------------------------------------

    private void set_intent_data() {
        final CheckBox cb_app = (CheckBox)findViewById( R.id.CheckBox_app );
        final CheckBox cb_call = (CheckBox)findViewById( R.id.CheckBox_call );
        final CheckBox cb_memo = (CheckBox)findViewById( R.id.CheckBox_memo );

        final ImageView iv_app_icon = (ImageView)findViewById( R.id.ImageView_thumb_app_icon );
        final TextView tv_app_name = (TextView)findViewById( R.id.TextView_app_name );
        final TextView tv_call_name = (TextView)findViewById( R.id.TextView_call_name );
        final TextView tv_call_number = (TextView)findViewById( R.id.TextView_call_number );
        final EditText et_msg = (EditText)findViewById( R.id.EditText_msg );

        final CheckBox cb_repeat_daily = (CheckBox)findViewById( R.id.CheckBox_repeat_daily );
        final DatePicker datepicker = (DatePicker)findViewById( R.id.DatePicker );
        final TimePicker timepicker = (TimePicker)findViewById( R.id.TimePicker );

        final Button btn_add = (Button)findViewById( R.id.Button_add );
        final Button btn_cancel = (Button)findViewById( R.id.Button_cancel );

        Intent intent = getIntent();
        CAlarmToDo todo = null;

        m_update_alarm_by_request_code = Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__;

        {
            if ( intent == null )
                return;

            m_update_alarm_by_request_code = intent.getIntExtra( Common.__ALARM_LIST__KEY__REQUEST_CODE__,
                    Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__ );
            __DEBUG__( "set_intent_data(): request code = " + m_update_alarm_by_request_code );
            if ( m_update_alarm_by_request_code == Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__ )
                return;

            m_update_mode = true;
        }

        {
            // Cancel the alarm request
            cancel_booking_alarm( m_update_alarm_by_request_code );

            if ( (cb_app == null) || (cb_call == null) || (cb_memo == null) ||
                    (iv_app_icon == null) || (tv_app_name == null) || (tv_call_name == null) ||
                    (tv_call_number == null) || (et_msg == null) ||
                    (cb_repeat_daily == null) || (datepicker == null) || (timepicker == null) ||
                    (btn_add == null) || (btn_cancel == null) ) {
                return;
            }
            cb_app.setChecked( false );
            cb_memo.setChecked( false );
            cb_call.setChecked( false );
            cb_repeat_daily.setChecked( false );

            // Update the alarm request
            if ( m_main_app != null ) {
                todo = m_main_app.get_alarm_todo();

                if ( todo != null ) {
                    int type = Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__;
                    HashMap<String, Object> map = todo.get_alarm_list_from_request_code( m_update_alarm_by_request_code );

                    if ( map == null )
                        return;

                    type = (Integer)map.get( Common.__ALARM_LIST__KEY__TYPE__ );
                    if ( type == Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__ )
                        return;

                    if ( type == Common.__ALARM_LIST__KEY__TYPE__APP__ ) {
                        String app_name = (String)map.get( Common.__ALARM_LIST__KEY__APP_NAME__ );
                        String app_pkg_name = (String)map.get( Common.__ALARM_LIST__KEY__APP_PKG_NAME__ );

                        if ( (app_name == null) || (app_pkg_name == null) )
                            return;

                        m_app_name = app_name;
                        m_app_pkg_name = app_pkg_name;
                        tv_app_name.setText( app_name );

                        {
                            byte buff[] = null;

                            m_app_icon = null;
                            if ( m_bmp_app_icon != null ) {
                                m_bmp_app_icon.recycle();
                                m_bmp_app_icon = null;
                            }

                            buff = (byte[])map.get( Common.__ALARM_LIST__KEY__APP_ICON__ );
                            if ( buff != null ) {
                                m_app_icon = buff;
                                m_bmp_app_icon = android.graphics.BitmapFactory.decodeByteArray( buff, 0, buff.length );

                                if ( iv_app_icon != null ) {
                                    if ( m_bmp_app_icon != null ) {
                                        iv_app_icon.setImageBitmap( m_bmp_app_icon );
                                    }
                                    else {
                                        iv_app_icon.setImageResource( R.mipmap.ic_launcher );
                                    }
                                }
                            }
                        }

                        cb_app.setChecked( true );

                    }
                    else if ( type == Common.__ALARM_LIST__KEY__TYPE__MSG__ ) {
                        /*
                        String msg = (String)map.get( Common.__ALARM_LIST__KEY__MSG__ );

                        if ( msg == null )
                            return;

                        et_msg.setText( msg );
                        */

                        cb_memo.setChecked( true );
                    }
                    else if ( type == Common.__ALARM_LIST__KEY__TYPE__CALL__ ) {
                        String call_name = (String)map.get( Common.__ALARM_LIST__KEY__CALL_NAME__ );
                        String call_number = (String)map.get( Common.__ALARM_LIST__KEY__CALL_NUMBER__ );

                        if ( (call_name == null) || (call_number == null) )
                            return;

                        tv_call_name.setText( call_name );
                        tv_call_number.setText( call_number );

                        cb_call.setChecked( true );
                    }
                    else {
                        return;
                    }

                    // Message
                    {
                        String msg = (String)map.get( Common.__ALARM_LIST__KEY__MSG__ );

                        if ( msg == null )
                            return;

                        et_msg.setText( msg );
                    }

                    if ( cb_repeat_daily != null ) {
                        boolean repeat_daily_enabled = (Boolean)map.get( Common.__ALARM_LIST__KEY__REPEAT_DAILY__ );
                        cb_repeat_daily.setChecked( repeat_daily_enabled );
                    }

                    if ( (datepicker != null) && (timepicker != null) ) {
                        int year = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__YEAR__ );
                        int month = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__MONTH__ );
                        int day = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__DAY__ );
                        int hour = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__HOUR__ );
                        int min = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__MIN__ );
                        //int sec = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__SEC__ );

                        datepicker.updateDate(year, (month - 1), day);

                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                            // API Level 23 (M)
                            timepicker.setHour( hour );
                            timepicker.setMinute( min );
                        }
                        else {
                            //! DEPRECATED: setCurrentHour(), setCurrentMinute() in API Level 23 (M)
                            timepicker.setCurrentHour( hour );
                            timepicker.setCurrentMinute( min );
                        }
                    }

                    if ( btn_add != null ) {
                        btn_add.setText("Update");
                    }
                }
            }
        }
    }

    private void test_add_booking_alarm() {
        Intent intent = new Intent( AddAlarmActivity.this, CAlarmBroadcastReceiver.class );
        PendingIntent pending = null;
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarm_mgr = (AlarmManager)getSystemService( Context.ALARM_SERVICE );
        int request_code = 0;

        if ( (intent != null) && (calendar != null) && (alarm_mgr != null) ) {
            intent.putExtra( Common.__ALARM_LIST__KEY__REQUEST_CODE__, request_code );
            pending = PendingIntent.getBroadcast( AddAlarmActivity.this, request_code, intent, 0 );
            calendar.setTimeInMillis( System.currentTimeMillis() );
            calendar.add( Calendar.SECOND, 1 );    // after 1 second

            alarm_mgr.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending );


            /*
            //! NOTE
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                // API Level 21
                AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo( calendar.getTimeInMillis(), pending );
                alarm_mgr.setAlarmClock( info, pending );
            }
            else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
                // API Level 19
                //AlarmManager.RTC
                alarm_mgr.setExact( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending );
            }
            */


            /*
            // Repeat
            {
                long start = SystemClock.elapsedRealtime() + (1 * 1000);    // start after 1 second
                long repeat = 5 * 1000;     // 5 seconds
                alarm_mgr.setRepeating( AlarmManager.ELAPSED_REALTIME_WAKEUP, start, repeat, pending );
            }
            */

            // Cancel
            //alarm_mgr.cancel( pending );
        }
    }

    //! SEE ALSO: CAlarmToDo::add_booking_alarm()
    private void add_booking_alarm(int request_code, int year, int month, int day, int hour, int min, int sec) {
        add_booking_alarm( false, request_code, year, month, day, hour, min, sec );
    }
    private void add_booking_alarm(boolean repeat_daily, int request_code, int year, int month, int day, int hour, int min, int sec) {
        if ( request_code < 0 )
            return;

        Intent intent = new Intent( AddAlarmActivity.this, CAlarmBroadcastReceiver.class );
        PendingIntent pending = null;
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarm_mgr = (AlarmManager)getSystemService( Context.ALARM_SERVICE );


        if ( (intent != null) && (calendar != null) && (alarm_mgr != null) ) {
            intent.putExtra( Common.__ALARM_LIST__KEY__REQUEST_CODE__, request_code );
            pending = PendingIntent.getBroadcast( AddAlarmActivity.this, request_code, intent, 0 );
            calendar.setTimeInMillis( System.currentTimeMillis() );
            //calendar.add( Calendar.SECOND, 1 );    // after 1 second
            //calendar.set( year, (month - 1), day, hour, min, sec );     // January start from 0


            if ( !repeat_daily ) {
                calendar.set( year, (month - 1), day, hour, min, sec );     // January start from 0

                if ( System.currentTimeMillis() > calendar.getTimeInMillis() ) {
                    return;
                }

                //alarm_mgr.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending );

                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                    // API Level 21
                    AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo( calendar.getTimeInMillis(), pending );
                    alarm_mgr.setAlarmClock( info, pending );

                    __DEBUG__( "add_booking_alarm(): Version >= LOLLIPOP (API Level 21)" );
                }
                else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
                    // API Level 19
                    //AlarmManager.RTC
                    alarm_mgr.setExact( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending );

                    __DEBUG__( "add_booking_alarm(): Version >= KITKAT (API Level 19)" );
                }
                else {
                    alarm_mgr.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending );

                    __DEBUG__( "add_booking_alarm(): Version < KITKAT (API Level 19)" );
                }
            }
            else {
                // Repeat
                {
                    //long trigger = SystemClock.elapsedRealtime() + (1 * 1000);    // 1 second later
                    long interval = 5 * 1000;     // 5 seconds

                    calendar.set( Calendar.HOUR, hour );
                    calendar.set( Calendar.MINUTE, min );
                    calendar.set( Calendar.SECOND, sec );

                    if ( System.currentTimeMillis() > calendar.getTimeInMillis() ) {
                        {
                            Calendar current_calendar = Calendar.getInstance();
                            if ( current_calendar != null ) {
                                calendar.set( Calendar.DAY_OF_WEEK, current_calendar.get(Calendar.DAY_OF_YEAR) + 1 );
                            }
                            else {
                                return;
                            }
                        }
                    }

                    alarm_mgr.setRepeating( AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), interval, pending );


                    /*
                    // NOTE
                    if ( System.currentTimeMillis() > calendar.getTimeInMillis() ) {
                        Calendar current_calendar = Calendar.getInstance();

                        // Everyday
                        calendar.set( Calendar.DAY_OF_WEEK, current_calendar.get(Calendar.DAY_OF_YEAR) + 1 );

                        // Every week
                        calendar.set( Calendar.WEEK_OF_YEAR, current_calendar.get(Calendar.WEEK_OF_YEAR) + 1 );
                    }
                    */
                }
            }


            // Cancel
            //alarm_mgr.cancel( pending );

            __DEBUG__("add_booking_alarm(): added");
        }
    }

    //! SEE ALSO: CAlarmToDo::cancel_booking_alarm()
    private void cancel_booking_alarm(int request_code) {
        if ( request_code < 0 )
            return;

        Intent intent = new Intent( AddAlarmActivity.this, CAlarmBroadcastReceiver.class );
        PendingIntent pending = null;
        AlarmManager alarm_mgr = (AlarmManager)getSystemService( Context.ALARM_SERVICE );

        if ( (intent != null) && (alarm_mgr != null) ) {
            intent.putExtra( Common.__ALARM_LIST__KEY__REQUEST_CODE__, request_code );
            pending = PendingIntent.getBroadcast( AddAlarmActivity.this, request_code, intent, 0 );

            // Cancel
            alarm_mgr.cancel( pending );

            __DEBUG__( "cancel_booking_alarm(): canceled" );
        }
    }
}
