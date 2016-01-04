/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 19, 2015
 * Filename:	CAlarmBroadcastReceiver.java
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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2015-12-19.
 */
public class CAlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "CAlarmBroadcastReceiver";
    private App m_main_app = null;
    private Context m_context = null;

    //private boolean enabled_notification = false;


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
    public void onReceive(Context context, Intent intent) {
        m_context = context;
        m_main_app = (App)context.getApplicationContext();

        __DEBUG__( "onReceive()" );

        if ( (context == null) || (intent == null) )
            return;

        String action = intent.getAction();
        String pkg_name = intent.getPackage();
        int alarm_req_code = intent.getIntExtra(
                                Common.__ALARM_LIST__KEY__REQUEST_CODE__,
                                Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__ );

        if ( action != null ) {
            __DEBUG__( "onReceive(): action = " + action );
        }

        if ( pkg_name != null ) {
            __DEBUG__( "onReceive(): package name = " + pkg_name );
        }

        __DEBUG__( "onReceive(): request code = " + alarm_req_code );



        {
            // Notification: http://developer.android.com/guide/topics/ui/notifiers/notifications.html
            // Note: Heads-up Notification (API Level 21, Android 5.0)

            String noti_ticker = "[ALARM MESSAGE] alarm test...";
            String noti_title = "[ALARM MESSAGE]";
            String noti_message = "alarm test";

            {
                boolean found = false;
                if ( m_main_app != null ) {
                    CAlarmToDo todo = m_main_app.get_alarm_todo();
                    ArrayList<HashMap<String, Object>> alarm_list = null;
                    int req_code = Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__;
                    int type = Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__;
                    String datetime = null;
                    String msg = null;

                    if ( todo == null ) {
                        __DEBUG__( "onReceive(): alarm list == NULL" );
                        return;
                    }

                    alarm_list = todo.get_alarm_list();
                    if ( alarm_list == null ) {
                        __DEBUG__( "onReceive(): alarm list == NULL" );
                        return;
                    }

                    if ( alarm_list.isEmpty() ) {
                        if ( m_main_app.get_alarm_list_save() ) {
                            todo = m_main_app.get_alarm_todo();
                            if ( todo == null ) {
                                __DEBUG__( "onReceive(): alarm list == NULL" );
                                return;
                            }

                            alarm_list = todo.get_alarm_list();
                            if ( alarm_list == null ) {
                                __DEBUG__( "onReceive(): alarm list == NULL" );
                                return;
                            }

                            if ( alarm_list.isEmpty() ) {
                                __DEBUG__( "onReceive(): alarm list is empty" );
                                return;
                            }
                        }
                    }

                    for ( int i = 0; i < alarm_list.size(); i++ ) {
                        HashMap<String, Object> map = alarm_list.get( i );

                        if ( map != null ) {
                            req_code = (Integer)map.get( Common.__ALARM_LIST__KEY__REQUEST_CODE__ );
                            type = (Integer)map.get( Common.__ALARM_LIST__KEY__TYPE__ );
                            datetime = (String)map.get( Common.__ALARM_LIST__KEY__DATETIME__ );
                            msg = (String)map.get( Common.__ALARM_LIST__KEY__MSG__ );

                            if ( alarm_req_code == req_code ) {
                                found = true;
                                noti_message = "No message";

                                if ( type == Common.__ALARM_LIST__KEY__TYPE__APP__ ) {
                                    noti_title = "[ALARM MESSAGE] APP";
                                }
                                else if ( type == Common.__ALARM_LIST__KEY__TYPE__CALL__ ) {
                                    noti_title = "[ALARM MESSAGE] CALL";
                                }
                                else if ( type == Common.__ALARM_LIST__KEY__TYPE__MSG__ ) {
                                    noti_title = "[ALARM MESSAGE] MSG";
                                }

                                if ( msg != null ) {
                                    noti_message = msg + " [" + req_code + "; " + datetime + "]";
                                }

                                break;
                            }
                        }
                    }
                }

                if ( !found ) {
                    __DEBUG__( "onReceive(): alarm not found" );
                    return;
                }
            }

            NotificationManager nm = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );
            Intent notify_intent = new Intent( context, MainActivity.class );
            PendingIntent pending = null;

            __DEBUG__( "onReceive(): Add an alarm notification" );

            if ( notify_intent == null )
                return;

            // Intent.FLAG_ACTIVITY_CLEAR_TASK: API Level 11
            //notify_intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            notify_intent.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP );

            pending = PendingIntent.getActivity( context, 0,
                    //new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT );
                    notify_intent, PendingIntent.FLAG_UPDATE_CURRENT );

            if ( pending == null )
                return;



            // Too old version
            /*
            Notification noti = new Notification( R.mipmap.ic_launcher, noti_ticker, System.currentTimeMillis() );

            if ( (nm != null) && (pending != null) ) {
                noti.setLatestEventInfo( context, noti_title, noti_message, pending );
                nm.notify( Common.__NOTIFY_ID__, noti );
            }
            */

            // Compat Build version
            if ( (nm != null) && (pending != null) ) {
                NotificationCompat.Builder compat_builder = new NotificationCompat.Builder( context );

                if ( compat_builder != null ) {
                    compat_builder.setSmallIcon( R.mipmap.ic_launcher );
                    compat_builder.setTicker( noti_ticker );
                    compat_builder.setWhen( System.currentTimeMillis() );
                    compat_builder.setNumber( ++Common.notify_count );
                    //if ( m_main_app != null ) { compat_builder.setNumber( m_main_app.notification_inc() ); }
                    compat_builder.setContentTitle( noti_title );
                    compat_builder.setContentText( noti_message );
                    compat_builder.setDefaults( Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE );
                    compat_builder.setContentIntent( pending );
                    compat_builder.setAutoCancel( true );

                    nm.notify( Common.__NOTIFY_ID__, compat_builder.build() );
                }
            }
        }

        __DEBUG__( "onReceive(): Start Alarm Screen" );
        {
            Intent intent_alarm_layout = new Intent( context, MainActivity.class );
            if ( intent_alarm_layout != null ) {
                // Notification sound, vibration
                //

                //intent_alarm_layout.putExtra( Common.__ALARM_LIST__KEY__REQUEST_CODE__, alarm_req_code );
                intent_alarm_layout.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );

                //! FLAG_ACTIVITY_SINGLE_TOP call the onNewIntent() but not here.
                //intent_alarm_layout.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP );

                // thus, use the class App
                if ( m_main_app != null ) {
                    m_main_app.set_alarm_fired( true, alarm_req_code );
                }

                context.startActivity( intent_alarm_layout );
            }
        }
    }
}
