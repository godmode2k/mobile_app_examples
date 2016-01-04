/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 17, 2015
 * Filename:	CAlarmToDo.java
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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2015-12-17.
 */
public class CAlarmToDo {
    private static final String TAG = "CAlarmToDo";
    private App m_main_app = null;

    private ArrayList<HashMap<String, Object>> m_alarm_list = null;

    /*
    Common.__ALARM_LIST__KEY__TYPE__            : Integer
    Common.__ALARM_LIST__KEY__REQUEST_CODE__    : Integer
    Common.__ALARM_LIST__KEY__CHECKED__         : Boolean
    Common.__ALARM_LIST__KEY__ALARM_ON__        : Boolean
    Common.__ALARM_LIST__KEY__REPEAT_DAILY__    : Boolean
    Common.__ALARM_LIST__KEY__APP_NAME__        : String
    Common.__ALARM_LIST__KEY__APP_PKG_NAME__    : String
    Common.__ALARM_LIST__KEY__APP_ICON__        : byte[]
    Common.__ALARM_LIST__KEY__MSG__             : String
    Common.__ALARM_LIST__KEY__DATETIME__        : String (yyyy-MM-dd HH:mm:ss | 2015-00-00 00:00:00)
    Common.__ALARM_LIST__KEY__DATETIME_CR__     : String (yyyy-MM-dd HH:mm:ss | 2015-00-00\n00:00:00)
    Common.__ALARM_LIST__KEY__DATETIME__YEAR__  : integer
    Common.__ALARM_LIST__KEY__DATETIME__MONTH__ : integer
    Common.__ALARM_LIST__KEY__DATETIME__DAY__   : integer
    Common.__ALARM_LIST__KEY__DATETIME__HOUR__  : integer
    Common.__ALARM_LIST__KEY__DATETIME__MIN__   : integer
    Common.__ALARM_LIST__KEY__DATETIME__SEC__   : integer
    Common.__ALARM_LIST__KEY__CALL_NAME__       : String
    Common.__ALARM_LIST__KEY__CALL_NUMBER__     : String
    */

    // Common.__MAX_REQUEST_ALARM_CODE__: 1000
    private int m_req_code = 0;


    // ------------------------------------------------------------------------


    protected CAlarmToDo(Context context) {
        if ( context == null )
            return;

        m_main_app = (App)context;
        if ( m_main_app == null )
            return;

        init();
    }

    public void init() {
        m_alarm_list = new ArrayList<HashMap<String, Object>>();
        m_req_code = 0;
    }

    public void release() {
        if ( (m_alarm_list != null) && (!m_alarm_list.isEmpty()) ) {
            for ( int i = 0; i < m_alarm_list.size(); i++ ) {
                HashMap<String, Object> map = m_alarm_list.get( i );
                if ( map != null ) {
                    Bitmap bitmap = (Bitmap)map.get( Common.__ALARM_LIST__KEY__APP_ICON_BITMAP__ );
                    if ( bitmap != null ) {
                        bitmap.recycle();
                        bitmap = null;

                        map.put( Common.__ALARM_LIST__KEY__APP_ICON_BITMAP__, null );
                    }
                }
            }

            m_alarm_list.clear();
            m_alarm_list = null;
        }
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

    //! return: -1 (m_req_code) if not increased, positive value otherwise
    // -1: Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__
    public int request_code_inc() {
        /*
        if ( (m_req_code + 1) >= Common.__MAX_REQUEST_ALARM_CODE__ )
            m_req_code = 0;
        else
            ++m_req_code;
        */

        {
            List<Integer> req_code_list = new ArrayList<Integer>();
            boolean found = false;
            boolean found_new_inc_req_code = false;
            //int new_inc_req_code = m_req_code;
            int new_inc_req_code = Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__;

            if ( (m_alarm_list != null) && (req_code_list != null) ) {
                for ( int i = 0; i < m_alarm_list.size(); i++ ) {
                    HashMap<String, Object> map = m_alarm_list.get( i );

                    if ( map != null ) {
                        int id = (Integer)map.get( Common.__ALARM_LIST__KEY__REQUEST_CODE__ );

                        req_code_list.add( id );

                        //if ( m_req_code == id )
                        //    found = true;
                    }
                }

                if ( req_code_list.isEmpty() ) {
                    m_req_code = 0;
                }
                else {
                //if ( found ) {
                    Collections.sort( req_code_list );

                    /*
                    for ( int i = 0; i < req_code_list.size(); i++ ) {
                        if ( m_req_code <= req_code_list.get(i) )
                            continue;
                        if ( (new_inc_req_code + 1) == req_code_list.get(i) )
                            ++new_inc_req_code;
                        else {
                            found_new_inc_req_code = true;
                            break;
                        }
                    }
                    */

                    for ( int i = 0; i < Common.__MAX_REQUEST_ALARM_CODE__; i++ ) {
                        for ( int j = 0; j < req_code_list.size(); j++ ) {
                            if ( i == req_code_list.get(j) ) {
                                found_new_inc_req_code = false;
                                break;
                            }

                            found_new_inc_req_code = true;
                        }

                        if ( found_new_inc_req_code ) {
                            new_inc_req_code = i;
                            break;
                        }
                    }

                    if ( found_new_inc_req_code ) {
                        m_req_code = new_inc_req_code;
                    }
                    else {
                        m_req_code = Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__;
                    }
                }
            }

            if ( (req_code_list != null) && !req_code_list.isEmpty() ) {
                req_code_list.clear();
                req_code_list = null;
            }

            __DEBUG__( "request_code_inc(): request code = " + m_req_code );
        }

        return m_req_code;
    }

    public ArrayList<HashMap<String, Object>> get_alarm_list() {
        if ( m_alarm_list == null ) {
            m_alarm_list = new ArrayList<HashMap<String, Object>>();
        }

        return m_alarm_list;
    }

    public void clear_alarm_list() {
        release();

        m_alarm_list = new ArrayList<HashMap<String, Object>>();
    }

    public boolean add_alarm_list(HashMap<String, Object> map) {
        if ( m_alarm_list == null || map == null )
            return false;

        return m_alarm_list.add( map );
    }

    public boolean add_alarm_list(int type, int request_code, boolean alarm_on, boolean repeat_daily,
                                  String app_name, String app_pkg_name, byte[] icon, String msg,
                                  int year, int month, int day, int hour, int min, int sec) {
        return add_alarm_list(type, request_code, alarm_on, repeat_daily, app_name, app_pkg_name, icon, msg,
                year, month, day, hour, min, sec,
                null, null);
    }

    public boolean add_alarm_list(int type, int request_code, boolean alarm_on, boolean repeat_daily,
                                  String app_name, String app_pkg_name, byte[] icon, String msg,
                                  int year, int month, int day, int hour, int min, int sec,
                                  String call_name, String call_number) {
        if ( request_code < 0 )
            return false;

        if ( m_alarm_list == null ) {
            m_alarm_list = new ArrayList<HashMap<String, Object>>();
        }

        if ( m_alarm_list == null )
            return false;

        HashMap<String, Object> map = new HashMap<String, Object>();
        if ( map == null )
            return false;

        map.put( Common.__ALARM_LIST__KEY__TYPE__, (Integer)type );
        map.put( Common.__ALARM_LIST__KEY__REQUEST_CODE__, (Integer)request_code );
        map.put( Common.__ALARM_LIST__KEY__CHECKED__, (Boolean)false );
        map.put( Common.__ALARM_LIST__KEY__ALARM_ON__, (Boolean)alarm_on );
        map.put( Common.__ALARM_LIST__KEY__REPEAT_DAILY__, (Boolean)repeat_daily );
        map.put( Common.__ALARM_LIST__KEY__MSG__, (String)msg );

        {
            map.put( Common.__ALARM_LIST__KEY__DATETIME__YEAR__, (Integer)year );
            map.put( Common.__ALARM_LIST__KEY__DATETIME__MONTH__, (Integer)month );
            map.put( Common.__ALARM_LIST__KEY__DATETIME__DAY__, (Integer)day );
            map.put( Common.__ALARM_LIST__KEY__DATETIME__HOUR__, (Integer)hour );
            map.put( Common.__ALARM_LIST__KEY__DATETIME__MIN__, (Integer)min );
            map.put( Common.__ALARM_LIST__KEY__DATETIME__SEC__, (Integer)sec );

            String date = "" + year + "-" + month + "-" + day;
            String time = "" + hour + ":" + min + ":" + sec;
            //String datetime_cr_html = date + "<br/>" + time;
            String datetime_cr = date + "\n" + time;
            map.put( Common.__ALARM_LIST__KEY__DATE__, (String)date );
            map.put( Common.__ALARM_LIST__KEY__TIME__, (String)time );
            map.put( Common.__ALARM_LIST__KEY__DATETIME__, (String)(date + " " + time) );
            map.put( Common.__ALARM_LIST__KEY__DATETIME_CR__, (String)datetime_cr );
        }


        if ( type == Common.__ALARM_LIST__KEY__TYPE__APP__ ) {
            map.put( Common.__ALARM_LIST__KEY__APP_NAME__, (String)app_name );
            map.put( Common.__ALARM_LIST__KEY__APP_PKG_NAME__, (String)app_pkg_name );
            map.put( Common.__ALARM_LIST__KEY__APP_ICON__, (byte[])icon );
        }
        else if ( type == Common.__ALARM_LIST__KEY__TYPE__MSG__ ) {}
        else if ( type == Common.__ALARM_LIST__KEY__TYPE__CALL__ ) {
            map.put( Common.__ALARM_LIST__KEY__CALL_NAME__, (String)call_name );
            map.put( Common.__ALARM_LIST__KEY__CALL_NUMBER__, (String)call_number );
        }
        else {
            // Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__

            map.clear();
            map = null;

            return false;
        }

        return m_alarm_list.add( map );
    }

    public boolean set_alarm_list(int index, HashMap<String, Object> map) {
        if ( m_alarm_list == null || map == null )
            return false;

        return ( m_alarm_list.set(index, map) != null );
    }

    public HashMap<String, Object> get_alarm_list_from_request_code(int request_code) {
        int pos = -1;

        if ( m_alarm_list == null )
            return null;

        for ( int i = 0; i < m_alarm_list.size(); i++ ) {
            HashMap<String, Object> map = m_alarm_list.get( i );

            if ( map != null ) {
                int _req_code = (Integer)map.get( Common.__ALARM_LIST__KEY__REQUEST_CODE__ );

                if ( _req_code == request_code ) {
                    Bitmap bitmap = (Bitmap)map.get( Common.__ALARM_LIST__KEY__APP_ICON_BITMAP__ );
                    if ( bitmap != null ) {
                        bitmap.recycle();
                        bitmap = null;

                        map.put( Common.__ALARM_LIST__KEY__APP_ICON_BITMAP__, null );
                    }
                    pos = i;
                    break;
                }
            }
        }

        if ( (m_alarm_list.size() < pos) || (pos < 0) )
            return null;

        return m_alarm_list.get(pos);
    }

    public boolean remove_alarm_list(int index) {
        if ( m_alarm_list == null )
            return false;

        if ( (m_alarm_list.size() < index) || (index < 0) )
            return false;

        {
            HashMap<String, Object> map = m_alarm_list.get( index );
            if ( map != null ) {
                Bitmap bitmap = (Bitmap)map.get( Common.__ALARM_LIST__KEY__APP_ICON_BITMAP__ );
                if ( bitmap != null ) {
                    bitmap.recycle();
                    bitmap = null;

                    map.put( Common.__ALARM_LIST__KEY__APP_ICON_BITMAP__, null );
                }
            }
        }

        return ( m_alarm_list.remove(index) != null );
    }

    public boolean remove_alarm_list_and_add_from_request_code(int request_code, HashMap<String, Object> map) {
        if ( remove_alarm_list_from_request_code(request_code) ) {
            return add_alarm_list( map );
        }

        return false;
    }

    public boolean remove_alarm_list_from_request_code(int request_code) {
        int pos = -1;

        if ( m_alarm_list == null )
            return false;

        for ( int i = 0; i < m_alarm_list.size(); i++ ) {
            HashMap<String, Object> map = m_alarm_list.get( i );

            if ( map != null ) {
                int _req_code = (Integer)map.get( Common.__ALARM_LIST__KEY__REQUEST_CODE__ );

                if ( _req_code == request_code ) {
                    Bitmap bitmap = (Bitmap)map.get( Common.__ALARM_LIST__KEY__APP_ICON_BITMAP__ );
                    if ( bitmap != null ) {
                        bitmap.recycle();
                        bitmap = null;

                        map.put( Common.__ALARM_LIST__KEY__APP_ICON_BITMAP__, null );
                    }
                    pos = i;
                    break;
                }
            }
        }

        if ( (m_alarm_list.size() < pos) || (pos < 0) )
            return false;

        return ( m_alarm_list.remove(pos) != null );
    }

    public boolean remove_alarm_list_from_request_code(List<Integer> request_code_list) {
        boolean ret = true;

        if ( (m_alarm_list == null) || (m_alarm_list.isEmpty()) )
            return false;

        if ( (request_code_list == null) || (request_code_list.isEmpty()) )
            return false;

        if ( m_alarm_list.size() < request_code_list.size() )
            return false;

        for ( int i = 0; i < request_code_list.size(); i++ ) {
            __DEBUG__( "remove_alarm_list_from_request_code(): i = " + i + ", list val = " + request_code_list.get(i) );
            ret = remove_alarm_list_from_request_code( request_code_list.get(i) );

            if ( !ret ) break;
        }

        return ret;
    }

    public boolean remove_alarm_list_checked_already() {
        return remove_alarm_list_from_request_code( get_checked_list() );
    }


    //
    // Alarm On/Off
    //

    public boolean onoff_alarm_list_from_request_code(int request_code, boolean turn_on) {
        return onoff_alarm_list_from_request_code( null, request_code, turn_on );
    }
    public boolean onoff_alarm_list_from_request_code(Context context, int request_code, boolean turn_on) {
        int pos = -1;

        if ( m_alarm_list == null )
            return false;

        for ( int i = 0; i < m_alarm_list.size(); i++ ) {
            HashMap<String, Object> map = m_alarm_list.get( i );

            if ( map != null ) {
                int _req_code = (Integer)map.get( Common.__ALARM_LIST__KEY__REQUEST_CODE__ );

                if ( _req_code == request_code ) {
                    map.put( Common.__ALARM_LIST__KEY__ALARM_ON__, (Boolean)turn_on );
                    pos = i;

                    if ( turn_on ) {
                        int year = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__YEAR__ );
                        int month = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__MONTH__ );
                        int day = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__DAY__ );
                        int hour = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__HOUR__ );
                        int min = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__MIN__ );
                        int sec = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__SEC__ );

                        add_booking_alarm( context, request_code, year, month, day, hour, min, sec );
                    }
                    else {
                        cancel_booking_alarm( context, request_code );
                    }

                    break;
                }
            }
        }

        if ( (m_alarm_list.size() < pos) || (pos < 0) )
            return false;

        return true;
    }

    public boolean onoff_alarm_list_from_request_code(List<Integer> request_code_list, boolean turn_on) {
        return onoff_alarm_list_from_request_code( null, request_code_list, turn_on );
    }
    public boolean onoff_alarm_list_from_request_code(Context context, List<Integer> request_code_list, boolean turn_on) {
        boolean ret = true;

        if ( (m_alarm_list == null) || (m_alarm_list.isEmpty()) )
            return false;

        if ( (request_code_list == null) || (request_code_list.isEmpty()) )
            return false;

        if ( m_alarm_list.size() < request_code_list.size() )
            return false;

        for ( int i = 0; i < request_code_list.size(); i++ ) {
            __DEBUG__( "onoff_alarm_list_from_request_code(): i = " + i + ", list val = " + request_code_list.get(i) );
            ret = onoff_alarm_list_from_request_code( context, request_code_list.get(i), turn_on );

            if ( !ret ) break;
        }

        return ret;
    }

    public boolean onoff_alarm_list_checked_already(boolean turn_on) {
        return onoff_alarm_list_checked_already( null, turn_on );
    }
    public boolean onoff_alarm_list_checked_already(Context context, boolean turn_on) {
        return onoff_alarm_list_from_request_code( context, get_checked_list(), turn_on );
    }


    //
    // Repeat daily
    //

    public boolean repeat_daily_alarm_list_from_request_code(int request_code, boolean repeat) {
        return repeat_daily_alarm_list_from_request_code( null, request_code, repeat );
    }
    public boolean repeat_daily_alarm_list_from_request_code(Context context, int request_code, boolean repeat) {
        int pos = -1;

        if ( m_alarm_list == null )
            return false;

        for ( int i = 0; i < m_alarm_list.size(); i++ ) {
            HashMap<String, Object> map = m_alarm_list.get( i );

            if ( map != null ) {
                int _req_code = (Integer)map.get( Common.__ALARM_LIST__KEY__REQUEST_CODE__ );

                if ( _req_code == request_code ) {
                    map.put( Common.__ALARM_LIST__KEY__REPEAT_DAILY__, (Boolean)repeat );
                    pos = i;

                    if ( repeat ) {
                        int year = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__YEAR__ );
                        int month = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__MONTH__ );
                        int day = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__DAY__ );
                        int hour = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__HOUR__ );
                        int min = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__MIN__ );
                        int sec = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__SEC__ );

                        //add_booking_alarm( context, request_code, year, month, day, hour, min, sec );
                        add_booking_alarm( context, repeat, request_code, year, month, day, hour, min, sec );
                    }
                    else {
                        cancel_booking_alarm( context, request_code );
                    }

                    break;
                }
            }
        }

        if ( (m_alarm_list.size() < pos) || (pos < 0) )
            return false;

        return true;
    }

    public boolean repeat_daily_alarm_list_from_request_code(List<Integer> request_code_list, boolean repeat) {
        return repeat_daily_alarm_list_from_request_code( null, request_code_list, repeat );
    }
    public boolean repeat_daily_alarm_list_from_request_code(Context context, List<Integer> request_code_list, boolean repeat) {
        boolean ret = true;

        if ( (m_alarm_list == null) || (m_alarm_list.isEmpty()) )
            return false;

        if ( (request_code_list == null) || (request_code_list.isEmpty()) )
            return false;

        if ( m_alarm_list.size() < request_code_list.size() )
            return false;

        for ( int i = 0; i < request_code_list.size(); i++ ) {
            __DEBUG__( "repeat_daily_alarm_list_from_request_code(): i = " + i + ", list val = " + request_code_list.get(i) );
            ret = repeat_daily_alarm_list_from_request_code( context, request_code_list.get(i), repeat );

            if ( !ret ) break;
        }

        return ret;
    }

    public boolean repeat_daily_alarm_list_checked_already(boolean repeat) {
        return repeat_daily_alarm_list_checked_already( null, repeat );
    }
    public boolean repeat_daily_alarm_list_checked_already(Context context, boolean repeat) {
        return repeat_daily_alarm_list_from_request_code( context, get_checked_list(), repeat );
    }



    // List<Integer> involves alarm request code
    public List<Integer> get_checked_list() {
        if ( (m_alarm_list == null) || (m_alarm_list.isEmpty()) )
            return null;

        boolean ret = true;
        List<Integer> list = new ArrayList<Integer>();

        if ( list == null )
            return null;

        for ( int i = 0; i < m_alarm_list.size(); i++ ) {
            HashMap<String, Object> map = m_alarm_list.get( i );
            if ( map != null ) {
                boolean checked = (Boolean)map.get( Common.__ALARM_LIST__KEY__CHECKED__ );
                int req_code = (Integer)map.get( Common.__ALARM_LIST__KEY__REQUEST_CODE__ );

                __DEBUG__( "get_checked_list(): pos = " + i + ", request code = " + req_code + ", checked = " + checked );

                if ( checked ) {
                    ret = list.add( req_code );
                }

                if ( !ret ) break;
            }
        }

        return ( ret ? list : null );
    }

    public void show_list() {
        if ( m_alarm_list != null ) {
            if ( m_alarm_list.isEmpty() ) {
                __DEBUG__( "show_list(): no data found" );
                return;
            }

            __DEBUG__( "show_list(): size = " + m_alarm_list.size() );
            for ( int i = 0; i < m_alarm_list.size(); i++ ) {
                HashMap<String, Object> map = m_alarm_list.get( i );

                __DEBUG__( "show_list(): index = " + i + " {" );
                if ( map != null ) {
                    __DEBUG__( "show_list():     type = " + (Integer)map.get(Common.__ALARM_LIST__KEY__TYPE__) );
                    __DEBUG__( "show_list():     request code = " + (Integer)map.get(Common.__ALARM_LIST__KEY__REQUEST_CODE__) );
                    __DEBUG__( "show_list():     checked = " + (Boolean)map.get(Common.__ALARM_LIST__KEY__CHECKED__) );
                    __DEBUG__( "show_list():     alarm on = " + (Boolean)map.get(Common.__ALARM_LIST__KEY__ALARM_ON__) );
                    __DEBUG__( "show_list():     repeat daily = " + (Boolean)map.get(Common.__ALARM_LIST__KEY__REPEAT_DAILY__) );
                    __DEBUG__( "show_list():     message = " + (String)map.get(Common.__ALARM_LIST__KEY__MSG__) );
                    __DEBUG__( "show_list():     datetime = " + (String)map.get(Common.__ALARM_LIST__KEY__DATETIME__) );
                    __DEBUG__( "show_list():     app name = " + (String)map.get(Common.__ALARM_LIST__KEY__APP_NAME__) );
                    __DEBUG__( "show_list():     app package name = " + (String)map.get(Common.__ALARM_LIST__KEY__APP_PKG_NAME__) );
                    if ( map.get(Common.__ALARM_LIST__KEY__APP_ICON__) != null ) {
                        __DEBUG__( "show_list():     app icon size (byte) = " + ((byte[])map.get(Common.__ALARM_LIST__KEY__APP_ICON__)).length );
                    }
                    else {
                        __DEBUG__( "show_list():     app icon size (byte) = 0" );
                    }
                    __DEBUG__( "show_list():     call name = " + (String)map.get(Common.__ALARM_LIST__KEY__CALL_NAME__) );
                    __DEBUG__( "show_list():     call number = " + (String)map.get(Common.__ALARM_LIST__KEY__CALL_NUMBER__) );
                }
                __DEBUG__( "}" );
            }
        }
    }

    private void add_booking_alarm(Context context, int request_code, int year, int month, int day, int hour, int min, int sec) {
        add_booking_alarm( context, false, request_code, year, month, day, hour, min, sec );
    }
    private void add_booking_alarm(Context context, boolean repeat_daily, int request_code, int year, int month, int day, int hour, int min, int sec) {
        if ( request_code < 0 )
            return;

        Intent intent = new Intent( context, CAlarmBroadcastReceiver.class );
        PendingIntent pending = null;
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarm_mgr = (AlarmManager)context.getSystemService( Context.ALARM_SERVICE );


        if ( (intent != null) && (calendar != null) && (alarm_mgr != null) ) {
            intent.putExtra( Common.__ALARM_LIST__KEY__REQUEST_CODE__, request_code );
            pending = PendingIntent.getBroadcast( context, request_code, intent, 0 );
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

            __DEBUG__( "add_booking_alarm(): added" );
        }
    }

    private void cancel_booking_alarm(Context context, int request_code) {
        if ( request_code < 0 )
            return;

        Intent intent = new Intent( context, CAlarmBroadcastReceiver.class );
        PendingIntent pending = null;
        AlarmManager alarm_mgr = (AlarmManager)context.getSystemService( Context.ALARM_SERVICE );

        if ( (intent != null) && (alarm_mgr != null) ) {
            intent.putExtra( Common.__ALARM_LIST__KEY__REQUEST_CODE__, request_code );
            pending = PendingIntent.getBroadcast( context, request_code, intent, 0 );

            // Cancel
            alarm_mgr.cancel( pending );

            __DEBUG__( "cancel_booking_alarm(): canceled" );
        }
    }
}
