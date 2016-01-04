/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 14, 2015
 * Filename:	Common.java
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
import android.content.Context;

/**
 * Created by admin on 2015-12-14.
 */
public class Common {
    public static final String __FRAGMENT_TAG_0__ = "fragment_tag_0";
    public static final String __FRAGMENT_TAG_1__ = "fragment_tag_1";
    public static final String __FRAGMENT_TAG_2__ = "fragment_tag_2";

    private static final int __REQUEST_CODE__FRAGMENT_TAG_0__ = 10;
    private static final int __REQUEST_CODE__FRAGMENT_TAG_1__ = 20;
    private static final int __REQUEST_CODE__FRAGMENT_TAG_2__ = 30;
    private static final int __REQUEST_CODE__ALARM_APP__ = 40;

    public static final int __REQUEST_CODE__FRAGMENT_TAG_1__PICK_PHOTO__ = __REQUEST_CODE__FRAGMENT_TAG_0__ + 1;
    public static final int __REQUEST_CODE__FRAGMENT_TAG_1__PICK_CONTACT__ = __REQUEST_CODE__FRAGMENT_TAG_0__ + 2;
    public static final int __REQUEST_CODE__APP_LIST__PICK_PHOTO__ = __REQUEST_CODE__ALARM_APP__ + 1;
    public static final int __REQUEST_CODE__APP_LIST__PICK_CONTACT__ = __REQUEST_CODE__ALARM_APP__ + 2;

    public static final String __ALARM_LIST__KEY__TYPE__ = "type";
    public static final int __ALARM_LIST__KEY__TYPE__UNKNOWN__ = -1;
    public static final int __ALARM_LIST__KEY__TYPE__APP__ = 0;
    public static final int __ALARM_LIST__KEY__TYPE__MSG__ = 1;
    public static final int __ALARM_LIST__KEY__TYPE__CALL__ = 2;
    //
    public static final String __ALARM_LIST__KEY__REQUEST_CODE__ = "req_code";
    public static final int __ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__ = -1;
    public static final int __MAX_REQUEST_ALARM_CODE__ = 1000;
    public static final String __ALARM_LIST__KEY__CHECKED__ = "checked";
    public static final String __ALARM_LIST__KEY__ALARM_ON__ = "alarm_on";
    public static final String __ALARM_LIST__KEY__REPEAT_DAILY__ = "repeat_daily";
    public static final String __ALARM_LIST__KEY__APP_NAME__ = "app_name";
    public static final String __ALARM_LIST__KEY__APP_PKG_NAME__ = "app_pkg_name";
    public static final String __ALARM_LIST__KEY__APP_ICON__ = "app_icon";
    //public static final String __ALARM_LIST__KEY__APP_ICON_ID__ = "app_icon_id";
    public static final String __ALARM_LIST__KEY__APP_ICON_BITMAP__ = "app_icon_bitmap";
    public static final String __ALARM_LIST__KEY__MSG__ = "msg";
    public static final String __ALARM_LIST__KEY__DATETIME__ = "datetime";
    public static final String __ALARM_LIST__KEY__DATETIME_CR__ = "datetime_cr";
    public static final String __ALARM_LIST__KEY__DATETIME_CR_HTML__ = "datetime_cr_html";
    public static final String __ALARM_LIST__KEY__DATE__ = "date";
    public static final String __ALARM_LIST__KEY__TIME__ = "time";
    public static final String __ALARM_LIST__KEY__DATETIME__YEAR__ = "datetime_year";
    public static final String __ALARM_LIST__KEY__DATETIME__MONTH__ = "datetime_month";
    public static final String __ALARM_LIST__KEY__DATETIME__DAY__ = "datetime_day";
    public static final String __ALARM_LIST__KEY__DATETIME__HOUR__ = "datetime_hour";
    public static final String __ALARM_LIST__KEY__DATETIME__MIN__ = "datetime_min";
    public static final String __ALARM_LIST__KEY__DATETIME__SEC__ = "datetime_sec";
    public static final String __ALARM_LIST__KEY__CALL_NAME__ = "call_name";
    public static final String __ALARM_LIST__KEY__CALL_NUMBER__ = "call_number";

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


    // Broadcast Receiver, Notification
    public static final int __NOTIFY_ID__ = 1;
    public static int notify_count = 0;

    // Service
    public static final int __NOTIFY_ID__SERVICE__ = 2;


    // ------------------------------------------------------------------------

    public static void notification_clear(Context context) {
        notify_count = 0;

        {
            NotificationManager nm = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
            if ( nm != null ) {
                nm.cancel( Common.__NOTIFY_ID__ );

                // leave this, auto canceled already
                //nm.cancelAll();
            }
        }
    }
}
