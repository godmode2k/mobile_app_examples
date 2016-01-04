/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 14, 2015
 * Filename:	CAlarmService.java
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

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by admin on 2016-01-04.
 */
public class CAlarmService extends Service {
    private static final String TAG = "CAlarmBroadcastReceiver";
    private App m_main_app = null;

    final RemoteCallbackList<IRemoteServiceCallback> m_callbacks =
            new RemoteCallbackList<IRemoteServiceCallback>();


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
    public void onCreate() {
        super.onCreate();

        m_main_app = (App)getApplicationContext();

        __DEBUG__( "onCreate()" );
    }

    @Override
    public void onDestroy() {
        __DEBUG__( "onDestroy()" );
        super.onDestroy();

        if ( m_callbacks != null ) {
            m_callbacks.kill();
        }

        /*
        if ( m_handler != null ) {
            m_handler.sendEmptyMessage( REPORT_MSG );
        }
        */

        {
            NotificationManager m_nm = (NotificationManager) getSystemService( NOTIFICATION_SERVICE );
            if ( m_nm != null ) {
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        __DEBUG__( "onStartCommand()" );
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        __DEBUG__( "onBind()" );

        if ( IRemoteService.class.getName().equals(intent.getAction()) ) {
            return m_binder;
        }
        /*
        if ( ISecondary.class.getName().equals(intent.getAction()) ) {
            return m_secondary_binder;
        }
        */

        return null;
    }

    private final IRemoteService.Stub m_binder = new IRemoteService.Stub() {
        public void registerCallback(IRemoteServiceCallback cb) {
            if ( cb != null )
                m_callbacks.register( cb );
        }
        public void unregisterCallback(IRemoteServiceCallback cb) {
            if ( cb != null )
                m_callbacks.unregister( cb );
        }
    };

    /*
    private final ISecondary.Stub mSecondaryBinder = new ISecondary.Stub() {
        public int getPid() {
            return Process.myPid();
        }
        public void basicTypes(int anInt, long aLong, boolean aBoolean,
                float aFloat, double aDouble, String aString) {
        }
    };
    */

    /*
    private static final int REPORT_MSG = 1;
    private final Handler m_handler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch ( msg.what ) {

                // It is time to bump the value!
                case REPORT_MSG:
                    {
                        // Up it goes.
                        int value = ++mValue;

                        // Broadcast to all clients the new value.
                        final int N = m_callbacks.beginBroadcast();
                        for ( int i = 0; i < N; i++ ) {
                            try {
                                m_callbacks.getBroadcastItem(i).valueChanged( value );
                            }
                            catch ( RemoteException e ) {
                                // The RemoteCallbackList will take care of removing
                                // the dead object for us.
                            }
                        }
                        m_callbacks.finishBroadcast();

                        // Repeat every 1 second.
                        sendMessageDelayed( obtainMessage(REPORT_MSG), 1*1000 );
                    } break;
                default:
                    super.handleMessage( msg );
            }
        }
    };
    */
}
