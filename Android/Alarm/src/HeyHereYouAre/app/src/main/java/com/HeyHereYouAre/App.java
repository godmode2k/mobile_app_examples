/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 12, 2015
 * Filename:	App.java
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

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * Created by admin on 2015-12-14.
 */
public class App extends Application {
    private static final String TAG = "App";
    private static final boolean m_debug_on = true;
    private Context m_context = null;


    // for compatible version
    // GAS: start version code 4, GASFree: start version code 5
    // USE: get_app_version_code_fixed()
    final int m_gas_version_code = 4;
    //final int gasfree_version_code = 5;	// DO NOT USE
    final int m_xml_version_code_inc = 1;        // Start from App Version Code 4 (xml_version_code_inc(0) == AppVersionCode(4))
    final int m_xml_version_code = m_gas_version_code + m_xml_version_code_inc;        //! increment 'xml_version_code_inc' if modified XML structure


    // Common
    private static final String m_strDefaultResPath = "ListenMyStory";
    //! DO NOT USE: Currently using cache directory [
    private static final String m_strDefaultThumbPath = "_thumb_";
    private static final String m_strDefaultResThumbPath = "ListenMyStory" + "/" + m_strDefaultThumbPath;
    //! DO NOT USE: Currently using cache directory ]
    private static final String m_strConfXmlFile = "conf.xml";
    private static final String m_strTAG_UseDebug = "usedebug=";
    private static final String m_strTAG_FontSize = "fontsize=";
    private static final String m_strTAG_ResPath = "respath=";
    private static final String m_strTAG_LastFile = "lastfile=";
    private static final String m_strTAG_Orientation = "orientation=";
    private static final String m_strTAG_Password = "password=";


    //! Alarm
    private CAlarmToDo m_alarm_todo = null;
    private boolean m_alarm_fired = false;
    private int m_alarm_fired_request_code = Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__;
    private static final String m_alarm_save_file = "alarm_list.xml";
    private final String __DELIM__ = "|";
    private final String __DELIM_REGEXP__ = "\\|";

    //! Notification
    // SEE: Common::notify_count, Common::notification_clear()
    private int m_notify_count = 0;


    public class CConfManager {
        // Common
        private boolean m_updated = false;
        private boolean m_useDebug = true;
        private int m_FontSize = 14;
        // NOTE:
        //	- Prefixed mounted external storage directory such as "/sdcard/" or "/mnt/sdcard/"
        //	- It will be creates like: "/sdcard/xxx" or "/mnt/sdcard/xxx"
        private String m_strResPath = "ListenMyStory";
        private String m_strLastFile = "";
        private String m_strOrientation = "landscape";    // "portrait", "landscape"
        private String m_strMD5EncryptedPassword = "";    // MD5 encrypted string

        // Common
        public CConfManager() {
        }

        public void ClearDefaultAll() {
            __DEBUG__("ClearDefaultAll()");

            // Common
            m_updated = false;
            m_useDebug = true;
            m_FontSize = 14;

            // NOTE:
            //	- Prefixed mounted external storage directory such as "/sdcard/" or "/mnt/sdcard/"
            //	- It will be creates like: "/sdcard/xxx" or "/mnt/sdcard/xxx"
            m_strResPath = "ListenMyStory";
            m_strLastFile = "";
            m_strOrientation = "landscape";    // "portrait", "landscape"
            m_strMD5EncryptedPassword = "";    // MD5 encryped string
        }

        public void ClearCacheListDefault() {
        }

        public void SetUpdated(boolean updated) {
            m_updated = updated;
        }

        public void SetUseDebug(boolean useDebug) {
            m_useDebug = useDebug;
        }

        public void SetFontSize(int fontSize) {
            m_FontSize = fontSize;
        }

        public void SetResLocation(String strResPath) {
            m_strResPath = strResPath;
        }

        public void SetLastFile(String strLastFile) {
            m_strLastFile = strLastFile;
        }

        public void SetOrientation(String orientation) {
            m_strOrientation = orientation;
        }

        public boolean GetUpdated() {
            return m_updated;
        }

        //		public boolean GetUseDebug() { return m_useDebug; }
        public boolean GetUseDebug() {
            return false;
        }    //! Fixed to "FALSE" for release

        public String GetFontSize() {
            return Integer.toString(m_FontSize);
        }

        public String GetResPath() {
            return m_strResPath;
        }

        public String GetResFullPath() {
            return GetExternalStoragePath() + "/" + GetResPath();
        }

        ;

        //! DO NOT USE: Currently using cache directory [
        public String GetResThumbPath() {
            return m_strResPath + "/" + m_strDefaultThumbPath;
        }

        public String GetResThumbFullPath() {
            return GetExternalStoragePath() + "/" + GetResThumbPath();
        }

        //! DO NOT USE: Currently using cache directory ]
        public String GetLastFile() {
            return m_strLastFile;
        }

        public String GetOrientation() {
            return m_strOrientation;
        }

        // Password
        public String GetMD5Hash(String strMessage) {
            // Source:
            //	- http://stackoverflow.com/questions/3934331/android-how-to-encrypt-a-string
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
                digest.update(strMessage.getBytes(), 0, strMessage.length());
                String hash = new BigInteger(1, digest.digest()).toString(16);

				/*
				// Unicode
				digest = MessageDigest.getInstance( "MD5" );
				byte utf8_bytes[] = strMessage.getBytes();
				digest.update( utf8_bytes, 0, utf8_bytes.length );
				hash = new BigInteger(1, digest.digest()).toString(16);
				*/

                return hash;
            } catch (NoSuchAlgorithmException e) {
                __DEBUG_ERROR__(false, TAG, "GetMD5Hash(): NoSuchAlgorithmException: " + e.getMessage(), e.getStackTrace());
                return null;
            } catch (Exception e) {
                __DEBUG_ERROR__(false, TAG, "GetMD5Hash(): Exception: " + e.getMessage(), e.getStackTrace());
                return null;
            }
        }

        public boolean SetPassword(String strPassword, boolean encode) {
            if (encode) {
                String strHash = GetMD5Hash(strPassword);
                if (strHash != null) {
                    m_strMD5EncryptedPassword = strHash;
                    __DEBUG__("SetPassword(): Set a password [TRUE]");
                    return true;
                }

                __DEBUG__("SetPassword(): Cannot get new encrypted password");
                return false;
            } else {
                m_strMD5EncryptedPassword = strPassword;
            }

            return true;
        }

        public String GetPassword() {
            if (m_strMD5EncryptedPassword != null)
                return m_strMD5EncryptedPassword;

            return "";
        }

        public boolean GetCheckPassword(String strPassword) {
            __DEBUG__("GetCheckPassword()");

            if (m_strMD5EncryptedPassword != null) {
                String strHash = GetMD5Hash(strPassword);
                if (strHash != null) {
                    __DEBUG__("GetCheckPassword(): src = " + m_strMD5EncryptedPassword + ", new = " + strHash);
                    if (m_strMD5EncryptedPassword.equals(strHash)) {
                        __DEBUG__("GetCheckPassword(): EQUALS");
                        return true;
                    }

                    __DEBUG__("GetCheckPassword(): Cannot get new encrypted password");
                    return false;
                }
            }

            __DEBUG__("GetCheckPassword(): Encrypted Password doesn't found");
            return false;
        }
    }

    private CConfManager m_ConfManager = new CConfManager();

    // ------------------------------------------------------------------------

    @Override
    public void onCreate() {
        __DEBUG__( TAG, "onCreate()" );
        super.onCreate();

        m_context = this.getApplicationContext();
        init();
    }

    @Override
    public void onTerminate() {
        __DEBUG__( TAG, "onTerminate()" );
        super.onTerminate();

        release();
    }

    public void __DEBUG__(String str) {
        if (m_debug_on)
            Log.d(TAG, str);
    }

    public void __DEBUG__(String strTAG, String str) {
        if (m_debug_on)
            Log.d(strTAG, str);
    }

    public void __DEBUG_ERROR__(String str, StackTraceElement[] excptVal) {
        Log.d(TAG, "Exception: " + str);

        if (excptVal != null)
            __DEBUG__GetStackTrace(TAG, excptVal);
    }

    public void __DEBUG_ERROR__(boolean force, String strTAG, String str, StackTraceElement[] excptVal) {
        if (m_debug_on) {
            Log.d(strTAG, "Exception: " + str);

            if (excptVal != null)
                __DEBUG__GetStackTrace(strTAG, excptVal);
        }
    }

    public void __DEBUG__GetStackTrace(String strTAG, StackTraceElement[] val) {
        //__DEBUG__( "__DEBUG__GetStackTrace()" );

        if (val != null) {
            for (int i = 0; i < val.length; i++) {
                Log.d(strTAG, "Exception: [" + i + "] " + val[i]);
            }
        }
    }

    // ------------------------------------------------------------------------

    public void init() {
        __DEBUG__("init()");

        m_alarm_todo = new CAlarmToDo( m_context );

        // load a saved alarm list
        get_alarm_list_save();
    }

    public void release() {
        __DEBUG__("release()");

        if ( m_alarm_todo != null ) {
            m_alarm_todo.release();
            m_alarm_todo = null;
        }

        //! DO NOT USE THIS HERE
        //notification_clear();
    }


    public int GetPixel(int dip) {
//		float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;
//	    float scale = getResources().getDisplayMetrics().density;
//
//	    return (int)(dip / scale * DEFAULT_HDIP_DENSITY_SCALE);

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    public int GetDIP(int pixel) {
        float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;
        float scale = getResources().getDisplayMetrics().density;

        return (int) (pixel / DEFAULT_HDIP_DENSITY_SCALE * scale);
    }

    public float GetPixel2(float dip) {
        android.util.DisplayMetrics metrics = getResources().getDisplayMetrics();
        float pixel = dip * (metrics.densityDpi / 160f);

        //float pixel = dip * getResources().getDisplayMetrics().density;

        return pixel;
    }

    public float GetDIP2(float pixel) {
        android.util.DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dip = pixel / (metrics.densityDpi / 160f);

        //float dip = pixel / getResources().getDisplayMetrics().density;

        return dip;
    }

    public String GetExternalStoragePath() {
        //__DEBUG__( "GetExternalStoragePath()" );

        // path = /mnt/sdcard/AAA
        return Environment.getExternalStorageDirectory().getPath();
    }

    public boolean SetCreatesResDirectory() {
        __DEBUG__("SetCreatesResDirectory()");

        String strFilePath = "./";
        String strFilePathInt = "";
        //String strFilePathExt = "/sdcard/";
        String strFilePathExt = android.os.Environment.getExternalStorageDirectory().getPath();
        strFilePathExt = strFilePathExt + "/";
        //File path = Environment.getExternalStorageDirectory();

        __DEBUG__("SetCreatesResDirectory(): External Storage = " + Environment.getExternalStorageState());

        // Checks Mount
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            //Toast.makeText(this, getResources().getString(R.string.Export_ExternalStorageHasNotBeenMounted), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "External Storage Has Not Been Mounted", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Default Resource Directory
        // /mnt/sdcard/ListenMyStory
        strFilePath = strFilePathExt + m_strDefaultResPath;

        __DEBUG__("SetCreatesResDirectory(): External Storage Directory = " + strFilePath);

        if (!(new File(strFilePath).exists())) {
            // Create a directory
            if ((new File(strFilePath).mkdir())) {
                __DEBUG__("SetCreatesResDirectory(): \"" + strFilePath + "\" created");
                //Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                Toast.makeText(this, "\"" + strFilePath + "\" " + "Created directory", Toast.LENGTH_SHORT).show();
            } else {
                __DEBUG_ERROR__(false, TAG, "SetCreatesResDirectory(): Fail to a created directory \"" + strFilePath + "\"", null);
                //Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                Toast.makeText(this, "\"" + strFilePath + "\" " + "Created directory [FALSE]", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // DO NOT USE: Currently using cache directory [
        // Default Resource Thumbnail Directory
        // /mnt/sdcard/ListenMyStory/_thumb_
		/*
		strFilePath = strFilePathExt + m_strDefaultResThumbPath;

		__DEBUG__( "SetCreatesResDirectory(): External Storage Directory = " + strFilePath );

		if ( !(new File(strFilePath).exists()) ) {
			// Create a directory
			if ( (new File(strFilePath).mkdir()) ) {
				__DEBUG__( "SetCreatesResDirectory(): \"" + strFilePath + "\" created" );
				Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
			}
			else {
				__DEBUG_ERROR__( "SetCreatesResDirectory(): Fail to a created directory \"" + strFilePath + "\"" );
				Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
				return false;
			}
		}
		*/
        // DO NOT USE: Currently using cache directory ]

        return true;
    }

    public boolean SetCreatesNomediaFile() {
        __DEBUG__("SetCreatesNomediaFile()");

        String strFilePath = "./";
        String strFilePathInt = "";
        //String strFilePathExt = "/sdcard/";
        String strNomediaFile = ".nomedia";
        String strFilePathExt = android.os.Environment.getExternalStorageDirectory().getPath();
        strFilePathExt = strFilePathExt + "/";
        //File path = Environment.getExternalStorageDirectory();

        __DEBUG__("SetCreatesNomediaFile(): External Storage = " + Environment.getExternalStorageState());

        // Checks Mount
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            //Toast.makeText( this, getResources().getString(R.string.Export_ExternalStorageHasNotBeenMounted), Toast.LENGTH_SHORT ).show();
            Toast.makeText(this, "ExternalStorageHasNotBeenMounted", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Default Resource Directory
        // /mnt/sdcard/ListenMyStory
        strFilePath = strFilePathExt + m_strDefaultResPath;
        strNomediaFile = strFilePath + "/" + strNomediaFile;

        __DEBUG__("SetCreatesNomediaFile(): External Storage Directory = " + strFilePath);

        if (!(new File(strFilePath).exists())) {
            // Create a directory
            if ((new File(strFilePath).mkdir())) {
                __DEBUG__("SetCreatesNomediaFile(): \"" + strFilePath + "\" created");
                //Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                Toast.makeText(this, "\"" + strFilePath + "\" " + "CreatedDirectory", Toast.LENGTH_SHORT).show();

                // Create a ".nomedia" file
                if (!(new File(strNomediaFile).exists())) {
                    try {
                        if ((new File(strNomediaFile).createNewFile())) {
                            __DEBUG__("SetCreatesNomediaFile(): \"" + strNomediaFile + "\" created");
                            //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                            Toast.makeText(this, "\"" + strNomediaFile + "\" " + "CreatedDirectory", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        __DEBUG_ERROR__(false, TAG, "SetCreatesNomediaFile(): Fail to a created directory \"" + strNomediaFile + "\"", null);
                        //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                        Toast.makeText(this, "\"" + strNomediaFile + "\" " + "Error_CreatedDirectory", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            } else {
                __DEBUG_ERROR__(false, TAG, "SetCreatesNomediaFile(): Fail to a created directory \"" + strFilePath + "\"", null);
                //Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                Toast.makeText(this, "\"" + strFilePath + "\" " + "Error_CreatedDirectory", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            __DEBUG__("SetCreatesNomediaFile(): \"" + strFilePath + "\" existed");

            // Create a ".nomedia" file
            if (!(new File(strNomediaFile).exists())) {
                try {
                    if ((new File(strNomediaFile).createNewFile())) {
                        __DEBUG__("SetCreatesNomediaFile(): \"" + strNomediaFile + "\" created");
                        //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                        Toast.makeText(this, "\"" + strNomediaFile + "\" " + "CreatedDirectory", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    __DEBUG_ERROR__(false, TAG, "SetCreatesNomediaFile(): Fail to a created directory \"" + strNomediaFile + "\"", null);
                    //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                    Toast.makeText(this, "\"" + strNomediaFile + "\" " + "Error_CreatedDirectory", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                __DEBUG__("SetCreatesNomediaFile(): \"" + strNomediaFile + "\" existed");
            }
        }

        return true;
    }

    // Create a Directory with a ".nomedia" file
    public boolean SetCreatesNomediaFile(String strDir) {
        __DEBUG__("SetCreatesNomediaFile()");

        String strFilePath = "./";
        String strFilePathInt = "";
        //String strFilePathExt = "/sdcard/";
        String strNomediaFile = ".nomedia";
        String strFilePathExt = android.os.Environment.getExternalStorageDirectory().getPath();
        strFilePathExt = strFilePathExt + "/";
        //File path = Environment.getExternalStorageDirectory();

        __DEBUG__("SetCreatesNomediaFile(): External Storage = " + Environment.getExternalStorageState());

        // Checks Mount
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            //Toast.makeText( this, getResources().getString(R.string.Export_ExternalStorageHasNotBeenMounted), Toast.LENGTH_SHORT ).show();
            Toast.makeText(this, "ExternalStorageHasNotBeenMounted", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Default Resource Directory
        // /mnt/sdcard/ListenMyStory + "/ + strDir"
        strFilePath = strFilePathExt + m_strDefaultResPath + "/" + strDir;
        strNomediaFile = strFilePath + "/" + strNomediaFile;

        __DEBUG__("SetCreatesNomediaFile(): External Storage Directory = " + strFilePath);

        if (!(new File(strFilePath).exists())) {
            // Create a directory
            if ((new File(strFilePath).mkdir())) {    //! NOTE: .mkdirs()
                __DEBUG__("SetCreatesNomediaFile(): \"" + strFilePath + "\" created");
                //Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                Toast.makeText(this, "\"" + strFilePath + "\" " + "CreatedDirectory", Toast.LENGTH_SHORT).show();

                // Create a ".nomedia" file
                if (!(new File(strNomediaFile).exists())) {
                    try {
                        if ((new File(strNomediaFile).createNewFile())) {
                            __DEBUG__("SetCreatesNomediaFile(): \"" + strNomediaFile + "\" created");
                            //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                            Toast.makeText(this, "\"" + strNomediaFile + "\" " + "CreatedDirectory", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        __DEBUG_ERROR__(false, TAG, "SetCreatesNomediaFile(): Fail to a created directory \"" + strNomediaFile + "\"", null);
                        //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                        Toast.makeText(this, "\"" + strNomediaFile + "\" " + "Error_CreatedDirectory", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            } else {
                __DEBUG_ERROR__(false, TAG, "SetCreatesNomediaFile(): Fail to a created directory \"" + strFilePath + "\"", null);
                //Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                Toast.makeText(this, "\"" + strFilePath + "\" " + "Error_CreatedDirectory", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            __DEBUG__("SetCreatesNomediaFile(): \"" + strFilePath + "\" existed");

            // Create a ".nomedia" file
            if (!(new File(strNomediaFile).exists())) {
                try {
                    if ((new File(strNomediaFile).createNewFile())) {
                        __DEBUG__("SetCreatesNomediaFile(): \"" + strNomediaFile + "\" created");
                        //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                        Toast.makeText(this, "\"" + strNomediaFile + "\" " + "CreatedDirectory", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    __DEBUG_ERROR__(false, TAG, "SetCreatesNomediaFile(): Fail to a created directory \"" + strNomediaFile + "\"", null);
                    //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                    Toast.makeText(this, "\"" + strNomediaFile + "\" " + "Error_CreatedDirectory", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                __DEBUG__("SetCreatesNomediaFile(): \"" + strNomediaFile + "\" existed");
            }
        }

        return true;
    }

    // Create a sub-directory with a ".nomedia" file
    public boolean setCreatesSubDirWithNomediaFile(final String strPath, final String strSubDir, final boolean useToast) {
        __DEBUG__("setCreatesSubDirWithNomediaFile()");

        String strFilePath = "./";
        String strFilePathInt = "";
        //String strFilePathExt = "/sdcard/";
        String strNomediaFile = ".nomedia";
        String strFilePathExt = android.os.Environment.getExternalStorageDirectory().getPath();
        strFilePathExt = strFilePathExt + "/";
        //File path = Environment.getExternalStorageDirectory();

        __DEBUG__("setCreatesSubDirWithNomediaFile(): External Storage = " + Environment.getExternalStorageState());

        // Checks Mount
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            //__DEBUG__( "setCreatesSubDirWithNomediaFile(): " + getResources().getString(R.string.Export_ExternalStorageHasNotBeenMounted) );
            __DEBUG__("setCreatesSubDirWithNomediaFile(): " + "ExternalStorageHasNotBeenMounted");
            if (useToast)
                //Toast.makeText( this, getResources().getString(R.string.Export_ExternalStorageHasNotBeenMounted), Toast.LENGTH_SHORT ).show();
                Toast.makeText(this, "ExternalStorageHasNotBeenMounted", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Default Resource Directory
        // /mnt/sdcard/ListenMyStory + "/ + strDir"
        //strFilePath = strFilePathExt + m_strDefaultResPath + "/" + strSubDir;
        strFilePath = strPath + "/" + strSubDir;
        strNomediaFile = strFilePath + "/" + strNomediaFile;

        strFilePath = strFilePath.replace("//", "/");
        strNomediaFile = strNomediaFile.replace("//", "/");

        __DEBUG__("setCreatesSubDirWithNomediaFile(): External Storage Directory = " + strFilePath);

        if ((strFilePath == null) || (strNomediaFile == null)) {
            __DEBUG__("setCreatesSubDirWithNomediaFile(): Path || Nomedia file is NULL");
            return false;
        }

        if (!(new File(strFilePath).exists())) {
            // Create a directory
            if ((new File(strFilePath).mkdir())) {    //! NOTE: .mkdirs()
                __DEBUG__("setCreatesSubDirWithNomediaFile(): \"" + strFilePath + "\" created");
                if (useToast)
                    //Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                    Toast.makeText(this, "\"" + strFilePath + "\" " + "CreatedDirectory", Toast.LENGTH_SHORT).show();

                // Create a ".nomedia" file
                if (!(new File(strNomediaFile).exists())) {
                    try {
                        if ((new File(strNomediaFile).createNewFile())) {
                            __DEBUG__("setCreatesSubDirWithNomediaFile(): \"" + strNomediaFile + "\" created");
                            if (useToast)
                                //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                                Toast.makeText(this, "\"" + strNomediaFile + "\" " + "CreatedDirectory", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        __DEBUG_ERROR__(false, TAG, "setCreatesSubDirWithNomediaFile(): Fail to a created directory \"" + strNomediaFile + "\"", null);
                        if (useToast)
                            //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                            Toast.makeText(this, "\"" + strNomediaFile + "\" " + "Error_CreatedDirectory", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            } else {
                __DEBUG_ERROR__(false, TAG, "setCreatesSubDirWithNomediaFile(): Fail to a created directory \"" + strFilePath + "\"", null);
                if (useToast)
                    //Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                    Toast.makeText(this, "\"" + strFilePath + "\" " + "Error_CreatedDirectory", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            __DEBUG__("setCreatesSubDirWithNomediaFile(): \"" + strFilePath + "\" existed");

            File fp = new File(strFilePath);
            if (fp != null) {
                String[] str = fp.list(null);

                if (str != null) {
                    boolean isAllDeleted = true;
                    for (int i = 0; i < str.length; i++) {
                        // NOTE: "deleteFile" doesn't support a path separator
                        //deleteFile( fp.getPath() + "/" + str[i] );

                        if (new File(fp, str[i]).delete())
                            __DEBUG__("setCreatesSubDirWithNomediaFile(): DELETED: " + fp.getAbsolutePath() + "/" + str[i]);
                        else {
                            isAllDeleted = false;
                            __DEBUG_ERROR__(false, TAG, "setCreatesSubDirWithNomediaFile(): FAIL TO DELETED: " + fp.getAbsolutePath() + "/" + str[i], null);
                        }
                    }

                    if (isAllDeleted)
                        __DEBUG__("setCreatesSubDirWithNomediaFile(): Deleted All Cached files");
                    else
                        __DEBUG_ERROR__(false, TAG, "setCreatesSubDirWithNomediaFile(): Fail to Deleted All Cached files", null);
                }

                fp = null;
            }

            // Create a ".nomedia" file
            if (!(new File(strNomediaFile).exists())) {
                try {
                    if ((new File(strNomediaFile).createNewFile())) {
                        __DEBUG__("setCreatesSubDirWithNomediaFile(): \"" + strNomediaFile + "\" created");
                        if (useToast)
                            //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                            Toast.makeText(this, "\"" + strNomediaFile + "\" " + "CreatedDirectory", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    __DEBUG_ERROR__(false, TAG, "setCreatesSubDirWithNomediaFile(): Fail to a created directory \"" + strNomediaFile + "\"", null);
                    if (useToast)
                        //Toast.makeText( this, "\"" + strNomediaFile + "\" " + getResources().getString(R.string.Export_Error_CreatedDirectory), Toast.LENGTH_SHORT ).show();
                        Toast.makeText(this, "\"" + strNomediaFile + "\" " + "Error_CreatedDirectory", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                __DEBUG__("setCreatesSubDirWithNomediaFile(): \"" + strNomediaFile + "\" existed");
            }
        }

        return true;
    }

    // Reference: UUID Usage
    // Source: http://stackoverflow.com/questions/617414/create-a-temporary-directory-in-java
    public String SetCreateRandomFileStr(String strPath) {
        __DEBUG__("SetCreateRandomFileStr()");

        final File fpDefaultPath = new File(strPath);
        File newUniqueFile;
        final int maxAttempts = 9;
        int attemptCount = 0;

        do {
            attemptCount++;

            if (attemptCount > maxAttempts) {
                __DEBUG__("SetCreateRandomFileStr(): Fail to generate an unique id");
                return null;
            }

            String dirName = UUID.randomUUID().toString();
            newUniqueFile = new File(fpDefaultPath, dirName);
        } while (newUniqueFile.exists());

        // Expected result will be like this: "1bf3f20f-b820-4eb0-ad24-ea1fe71d0b7e"
        __DEBUG__("SetCreateRandomFileStr(): New id = " + newUniqueFile.getName());

        return newUniqueFile.getName();
    }

    public String GetSDcardPath() {
        __DEBUG__("GetSDcardPath()");

        try {
            String strFilePath = "./";
            String strFilePathInt = "";
            //String strFilePathExt = "/sdcard/";
            String strFilePathExt = Environment.getExternalStorageDirectory().getPath();

            __DEBUG__("GetSDcardPath(): External Storage = " + Environment.getExternalStorageState());

            // Get absolute path of the SD card
            // e.g., "/mnt/sdcard/"
            strFilePath = strFilePathExt + "/";

            return strFilePath;
        } catch (Exception e) {
            __DEBUG_ERROR__(false, TAG, "GetSDcardPath(): Exception: " + e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public String[] GetResourceFilesFromSDcard(String strNewFilePath) {
        __DEBUG__("GetResourceFilesFromSDcard()");

        if (m_ConfManager == null) {
            __DEBUG_ERROR__(false, TAG, "GetLoadConf(): m_ConfManager == NULL", null);
            return null;
        }

        try {
            String strFilePath = "./";
            String strFilePathInt = "";
            //String strFilePathExt = "/sdcard/";
            String strFilePathExt = Environment.getExternalStorageDirectory().getPath();
            //String strResFilename = "";
            strFilePathExt = strFilePathExt + "/";

            __DEBUG__("GetResourceFilesFromSDcard(): External Storage = " + Environment.getExternalStorageState());


            // Get resource filename with absolute path
            // e.g., "/mnt/sdcard/" + "ListenMyStory" + "/"
            ////strFilePath = strFilePathExt;
            //strFilePath = strFilePathExt + m_ConfManager.GetResPath() + "/";
            if ((strNewFilePath == null) || (strNewFilePath.equals("")))
                strFilePath = strFilePathExt + m_ConfManager.GetResPath() + "/";
            else
                strFilePath = strFilePathExt + strNewFilePath + "/";

            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                File fp = new File(strFilePath);

                if ((fp != null) && (fp.exists()) && (fp.isDirectory())) {
                    __DEBUG__("GetResourceFilesFromSDcard(): Found: \"" + strFilePath + "\"");
                    //Toast.makeText( this, "\"" + strFilePath + "\" " + getResources().getString(R.string.Export_FoundSuchFileDirectory), Toast.LENGTH_SHORT ).show();

                    try {
                        String[] strFilelist = new String[fp.list().length];
                        for (int i = 0; i < fp.list().length; i++) {
                            __DEBUG__("GetResourceFilesFromSDcard(): Index = " + i + ", Filename = " + fp.list()[i]);
                            //strFilelist[i] = strFilePath + fp.list()[i];
                            strFilelist[i] = fp.list()[i];
                        }

                        fp = null;
                        return strFilelist;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                        __DEBUG_ERROR__(false, TAG, "GetResourceFilesFromSDcard(): Exception: " + e.getMessage(), e.getStackTrace());
                        return null;
                    }
                } else {
                    __DEBUG__("GetResourceFilesFromSDcard(): Not Found: \"" + strFilePath + "\"");
                    //Toast.makeText( this, "\"" + strFilePath + "\" "  + getResources().getString(R.string.Export_Error_NoFoundSuchFileDirectory), Toast.LENGTH_SHORT ).show();
                    Toast.makeText(this, "\"" + strFilePath + "\" " + "Error_NoFoundSuchFileDirectory", Toast.LENGTH_SHORT).show();
                    fp = null;
                    return null;
                }
            }
        } catch (Exception e) {
            __DEBUG_ERROR__(false, TAG, "GetResourceFilesFromSDcard(): Exception: " + e.getMessage(), e.getStackTrace());
            return null;
        }

        return null;
    }

    public void SetClearDataCacheAll() {
        __DEBUG__("SetClearDataCacheAll()");

        //boolean ret = true;

        File fpCacheDir = getCacheDir();
        File fpCacheDirExternal = getExternalCacheDir();
        File fpFilesDir = getFilesDir();

        try {
            if (fpCacheDir != null) {
                String[] str = fpCacheDir.list(null);
                __DEBUG__("SetClearDataCacheAll(): Cache Files count: " + str.length);
                for (int i = 0; i < str.length; i++) {
                    //__DEBUG__( "SetClearDataCacheAll(): Cache Files: [" + i + "] = " + fpCacheDir.getAbsolutePath() + "/" + str[i] );

                    if (new File(fpCacheDir, str[i]).delete())
                        __DEBUG__("SetClearDataCacheAll(): Cache Files: DELETED: [" + i + "] = " + fpCacheDir.getAbsolutePath() + "/" + str[i]);
                    else {
                        __DEBUG_ERROR__(false, TAG, "SetClearDataCacheAll(): Cache Files: FAIL TO DELETED: [" + i + "] = " + fpCacheDir.getAbsolutePath() + "/" + str[i], null);
                    }
                }
            }
            if (fpCacheDirExternal != null) {
                String[] str = fpCacheDirExternal.list(null);
                __DEBUG__("SetClearDataCacheAll(): External Cache Files Count: " + str.length);
                for (int i = 0; i < str.length; i++) {
                    //__DEBUG__( "SetClearDataCacheAll(): External Cache Files: [" + i + "] = " + fpCacheDirExternal.getAbsolutePath() + "/" + str[i] );

                    if (new File(fpCacheDirExternal, str[i]).delete())
                        __DEBUG__("SetClearDataCacheAll(): External Cache Files: DELETED: [" + i + "] = " + fpCacheDirExternal.getAbsolutePath() + "/" + str[i]);
                    else {
                        __DEBUG_ERROR__(false, TAG, "SetClearDataCacheAll(): External Cache Files: FAIL TO DELETED: [" + i + "] = " + fpCacheDirExternal.getAbsolutePath() + "/" + str[i], null);
                    }
                }
            }
            if (fpFilesDir != null) {
                String[] str = fpFilesDir.list(null);
                __DEBUG__("SetClearDataCacheAll(): FilesDir(Internal Files Directory) Files Count: " + str.length);
                for (int i = 0; i < str.length; i++) {
                    //__DEBUG__( "SetClearDataCacheAll(): FilesDir(Internal Files Directory) Files: [" + i + "] = " + fpFilesDir.getAbsolutePath() + "/" + str[i] );

                    if (new File(fpFilesDir, str[i]).delete())
                        __DEBUG__("SetClearDataCacheAll(): FilesDir(Internal Files Directory) Files: DELETED: [" + i + "] = " + fpFilesDir.getAbsolutePath() + "/" + str[i]);
                    else {
                        __DEBUG_ERROR__(false, TAG, "SetClearDataCacheAll(): FilesDir(Internal Files Directory) Files: FAIL TO DELETED: [" + i + "] = " + fpFilesDir.getAbsolutePath() + "/" + str[i], null);
                    }
                }
            }

            fpCacheDir = null;
            fpCacheDirExternal = null;
            fpFilesDir = null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "SetClearDataCacheAll(): Exception: " + e.getMessage(), e.getStackTrace());

            fpCacheDir = null;
            fpCacheDirExternal = null;
            fpFilesDir = null;

            //return false;
            return;
        }

        // Clear Cache List a default
        if (m_ConfManager != null)
            m_ConfManager.ClearCacheListDefault();

        //return ret;
    }

    public void SetRemoveData(String strFile) {
        __DEBUG__("SetRemoveData()");

        //boolean ret = false;
        File fpFile = new File(strFile);

        try {
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                if ((fpFile != null) && (fpFile.exists()) && (!fpFile.isDirectory())) {
                    //__DEBUG__( "SetClearDataAllTargetDir(): Target File: = " + fpFilesDir.getAbsolutePath() );

                    if (fpFile.delete())
                        __DEBUG__("SetRemoveData(): Target File: DELETED: " + fpFile.getAbsolutePath());
                    else {
                        __DEBUG_ERROR__(false, TAG, "SetRemoveData(): Target File: FAIL TO DELETED: " + fpFile.getAbsolutePath() + "/" + fpFile.getName(), null);
                    }
                }
            }

            fpFile = null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "SetRemoveData(): Exception: " + e.getMessage(), e.getStackTrace());

            fpFile = null;
            return;
        }
    }

    public void SetRemoveDataAllTargetDir(String strPath, boolean removeDir) {
        __DEBUG__("SetClearDataAllTargetDir()");

        //boolean ret = false;
        File fpFilesDir = new File(strPath);

        try {
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                if ((fpFilesDir != null) && (fpFilesDir.exists()) && (fpFilesDir.isDirectory())) {
                    String[] str = fpFilesDir.list(null);

                    if (str != null) {
                        __DEBUG__("SetClearDataAllTargetDir(): Target Directory Files Count: " + str.length);
                        for (int i = 0; i < str.length; i++) {
                            //__DEBUG__( "SetClearDataAllTargetDir(): Target Directory Files: [" + i + "] = " + fpFilesDir.getAbsolutePath() + "/" + str[i] );

                            if (new File(fpFilesDir, str[i]).delete())
                                __DEBUG__("SetClearDataAllTargetDir(): Target Directory Files: DELETED: [" + i + "] = " + fpFilesDir.getAbsolutePath() + "/" + str[i]);
                            else {
                                __DEBUG_ERROR__(false, TAG, "SetClearDataAllTargetDir(): Target Directory Files: FAIL TO DELETED: [" + i + "] = " + fpFilesDir.getAbsolutePath() + "/" + str[i], null);
                            }
                        }
                    }

                    if (removeDir) {
                        if (fpFilesDir.delete()) {
                            __DEBUG__("SetClearDataAllTargetDir(): Target Directory DELETED: " + fpFilesDir.getAbsolutePath());
                        } else {
                            __DEBUG_ERROR__(false, TAG, "SetClearDataAllTargetDir(): Target Directory FAIL TO DELETED: " + fpFilesDir.getAbsolutePath(), null);
                        }
                    }
                }
            }

            fpFilesDir = null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "SetClearDataAllTargetDir(): Exception: " + e.getMessage(), e.getStackTrace());

            fpFilesDir = null;
            return;
        }
    }

    // ------------------------------------------------------------------------

    public boolean SetSaveConf() {
        __DEBUG__("SetSaveConf()");

        boolean ret = false;

        if (m_ConfManager != null) {
            String strConf = null;

/*
    		strConf = "<rss version=\"2.0\">";
			strConf += "    <contents>";
			strConf += "        <item>";
			// Common
			strConf += "            <usedebug>";
			strConf += m_ConfManager.GetUseDebug();
			strConf += "</usedebug>";
			strConf += "            <fontsize>";
			strConf += m_ConfManager.GetFontSize();
			strConf += "</fontsize>";
			strConf += "            <respath>";
			strConf += m_ConfManager.GetResLocation();
			strConf += "</respath>";
			strConf += "            <lastfile>";
			strConf += m_ConfManager.GetLastFile();
			strConf += "</lastfile>";
			strConf += "			<orientation>";
			strConf += m_ConfManager.GetOrientation();
			strConf += </orientation>";
			//
			strConf += "        </item>";
			strConf += "    </contents>";
			strConf += "</rss>";
*/

            // Common
            strConf = m_strTAG_UseDebug;
            strConf += m_ConfManager.GetUseDebug();
            strConf += "\n";
            strConf += m_strTAG_FontSize;
            strConf += m_ConfManager.GetFontSize();
            strConf += "\n";
            strConf += m_strTAG_ResPath;
            strConf += m_ConfManager.GetResPath();
            strConf += "\n";
            strConf += m_strTAG_LastFile;
            strConf += m_ConfManager.GetLastFile();
            strConf += "\n";
            strConf += m_strTAG_Orientation;
            strConf += m_ConfManager.GetOrientation();
            strConf += "\n";
            strConf += m_strTAG_Password;
            strConf += m_ConfManager.GetPassword();
            strConf += "\n";

            __DEBUG__("SetSaveConf(): {\n" + strConf + "\n}");

            // Save as a xml
            try {
                //! DEPRECATED: MODE_WORLD_WRITEABLE
                //FileOutputStream f = openFileOutput( m_strConfXmlFile, MODE_WORLD_WRITEABLE );
                FileOutputStream f = openFileOutput(m_strConfXmlFile, MODE_PRIVATE);
                f.write(strConf.getBytes());
                f.close();
                //deleteFile( m_strConfXmlFile );

                ret = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                __DEBUG_ERROR__(false, TAG, "SetSaveConf(): IOException: " + e.getMessage(), e.getStackTrace());
                return false;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                __DEBUG_ERROR__(false, TAG, "SetSaveConf(): Exception: " + e.getMessage(), e.getStackTrace());
                return false;
            }
        }

        return ret;
    }

    public boolean GetLoadConf() {
        __DEBUG__("GetLoadConf()");

        boolean ret = true;

        if (m_ConfManager == null) {
            __DEBUG_ERROR__(false, TAG, "GetLoadConf(): m_ConfManager == NULL", null);
            return false;
        }

        try {
            int MAX_BUF_SIZE = 2048;
            FileInputStream f = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            String strGetLine;
            StringBuilder sb = new StringBuilder();

            // Common
            String strUseDebug = "";
            String strFontSize = "";
            String strResPath = "";
            String strLastFile = "";
            String strOrientation = "";
            String strPassword = "";

            // Common
            boolean flagUseDebug = false;
            boolean flagFontSize = false;
            boolean flagResPath = false;
            boolean flagLastFile = false;
            boolean flagOrientation = false;
            boolean flagPassword = false;

            f = openFileInput(m_strConfXmlFile);
            if (f.available() <= 0) {
                __DEBUG_ERROR__(false, TAG, "GetLoadConf(): \"" + m_strConfXmlFile + "\" not available", null);
                f.close();
                //return false;

                __DEBUG_ERROR__(false, TAG, "GetLoadConf(): \"" + m_strConfXmlFile + "\" not available, Try to save a configuration", null);
                if (!SetSaveConf()) {
                    __DEBUG_ERROR__(false, TAG, "GetLoadConf(): \"" + m_strConfXmlFile + "\" not available, return", null);
                    return false;
                }

                //f = openFileInput( m_picsOnly_strConfResCacheXmlFile );
            }

            if (f.available() <= 0) {
                __DEBUG_ERROR__(false, TAG, "GetLoadConf(): TRY #ONCE \"" + m_strConfXmlFile + "\" not available", null);
                f.close();
                return false;
            }

            isr = new InputStreamReader(f);
            br = new BufferedReader(isr);

            try {
                while ((strGetLine = br.readLine()) != null) {
                    //sb.append( strGetLine );
                    //sb.append( "\n" );

                    __DEBUG__("GetLoadConf(): GETLINE = " + strGetLine);

                    // Common
                    if (!flagUseDebug) {
                        if (strGetLine.contains(m_strTAG_UseDebug)) {
                            strUseDebug = strGetLine.replace(m_strTAG_UseDebug, "");
                            flagUseDebug = true;
                            continue;
                        }
                    }
                    if (!flagFontSize) {
                        if (strGetLine.contains(m_strTAG_FontSize)) {
                            strFontSize = strGetLine.replace(m_strTAG_FontSize, "");
                            flagFontSize = true;
                            continue;
                        }
                    }
                    if (!flagResPath) {
                        if (strGetLine.contains(m_strTAG_ResPath)) {
                            strResPath = strGetLine.replace(m_strTAG_ResPath, "");
                            flagResPath = true;
                        }
                    }
                    if (!flagLastFile) {
                        if (strGetLine.contains(m_strTAG_LastFile)) {
                            strLastFile = strGetLine.replace(m_strTAG_LastFile, "");
                            flagLastFile = true;
                            continue;
                        }
                    }
                    if (!flagOrientation) {
                        if (strGetLine.contains(m_strTAG_Orientation)) {
                            strOrientation = strGetLine.replace(m_strTAG_Orientation, "");
                            flagOrientation = true;
                            continue;
                        }
                    }
                    if (!flagPassword) {
                        if (strGetLine.contains(m_strTAG_Password)) {
                            strPassword = strGetLine.replace(m_strTAG_Password, "");
                            flagPassword = true;
                            continue;
                        }
                    }
                }
                br.close();
                isr.close();

                // Common
                //strUseDebug = strUseDebug.replace( m_strTAG_UseDebug, "" );
                //strFontSize = strFontSize.replace( m_strTAG_FontSize, "" );
                //strResPath = strResPath.replace( m_strTAG_ResPath, "" );
                //strLastFile = strLastFile.replace( m_strTAG_LastFile, "" );
                //strOrientation = strOrientation.replace( m_strTAG_Orientation, "" );
                //strPassword = strPassword.replace( m_strTAG_Password, "" );

                // Common
                __DEBUG__("GetLoadConf(): " + "Use Debug = " + strUseDebug);
                __DEBUG__("GetLoadConf(): " + "Font Size = " + strFontSize);
                __DEBUG__("GetLoadConf(): " + "Resource Path = " + strResPath);
                __DEBUG__("GetLoadConf(): " + "Last Opened File = " + strLastFile);
                __DEBUG__("GetLoadConf(): " + "Orientation = " + strOrientation);
                __DEBUG__("GetLoadConf(): " + "Password = " + strPassword);

                // Common
                m_ConfManager.SetUseDebug(Boolean.parseBoolean(strUseDebug));
                m_ConfManager.SetFontSize(Integer.parseInt(strFontSize));
                m_ConfManager.SetResLocation(strResPath);
                m_ConfManager.SetLastFile(strLastFile);
                m_ConfManager.SetOrientation(strOrientation);
                m_ConfManager.SetPassword(strPassword, false);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                __DEBUG_ERROR__(false, TAG, "GetLoadConf(): IOException: " + e.getMessage(), e.getStackTrace());
                return false;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                __DEBUG_ERROR__(false, TAG, "GetLoadConf(): Exception: " + e.getMessage(), e.getStackTrace());
                return false;
            }

            f.close();
        } catch (FileNotFoundException e) {
            __DEBUG_ERROR__(false, TAG, "GetLoadConf(): FileNotFoundException: " + e.getMessage(), e.getStackTrace());
            __DEBUG_ERROR__(false, TAG, "GetLoadConf(): FileNotFoundException: Create a default configuration", null);
            SetSaveConf();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "GetLoadConf(): IOException: " + e.getMessage(), e.getStackTrace());
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "GetLoadConf(): Exception: " + e.getMessage(), e.getStackTrace());
            return false;
        }

        return ret;
    }

    // ------------------------------------------------------------------------

    // App Version Information
    public int get_app_version_code() {
        //__DEBUG__( "get_app_version_code()" );

        int appVersionCode = -1;
        String strAppVersionName = null;

        try {
            PackageInfo pkgInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            if (pkgInfo != null) {
                appVersionCode = pkgInfo.versionCode;
                strAppVersionName = pkgInfo.versionName;

                __DEBUG__("get_app_version_code(): version code = " + appVersionCode + ", version name = " + strAppVersionName);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "get_app_version_code(): Exception: " + e.getMessage(), e.getStackTrace());
            return -1;
        }

        return appVersionCode;
    }

    public String get_app_version_name() {
        //__DEBUG__( "get_app_version()" );

        int appVersionCode = -1;
        String strAppVersionName = null;

        try {
            PackageInfo pkgInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            if (pkgInfo != null) {
                appVersionCode = pkgInfo.versionCode;
                strAppVersionName = pkgInfo.versionName;

                __DEBUG__("get_app_version(): version code = " + appVersionCode + ", version name = " + strAppVersionName);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "get_app_version(): Exception: " + e.getMessage(), e.getStackTrace());
            return null;
        }

        return strAppVersionName;
    }

    public int get_app_version_code_fixed() {
        //__DEBUG__( "get_app_version_code_fixed()" );

        // for compatible version

        return m_xml_version_code;
    }

    // System Configuration
    public boolean GetAutoRotateScreen() {
        try {
            int val = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION);

            // 0 if Auto-Rotate is off and 1 if Auto-Rotate is on
            return ((val == 0) ? false : true);
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "GetAutoRotateScreen(): Exception: " + e.getMessage(), e.getStackTrace());
            return false;
        }
    }

    // Network
    public boolean GetNetStateMobile() {
        boolean ret = true;

        ConnectivityManager connect = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            // Available
            // ...
            ret = true;
        } else {
            // No Available
            // ...
            ret = false;
        }

        return ret;
    }

    /*
    //! DEPRECATED API Level 21: ActivityManager.getRunningTasks()
    //
    // Get Application state running under background
    // Source: http://stackoverflow.com/questions/4414171/how-to-detect-when-an-android-app-goes-to-the-background-and-come-back-to-the-fo
    public boolean isAppBackground(Context context) {
        Log.d( TAG,  "isAppBackground()" );

        ActivityManager am = (ActivityManager)context.getSystemService( Context.ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks( 1 );

        if( !tasks.isEmpty() ) {
            ComponentName topActivity = tasks.get(0).topActivity;

            if( !topActivity.getPackageName().equals(context.getPackageName()) ) {
                Log.d( TAG,  "isAppBackground(): " + context.getPackageName() + " == Background" );
                return true;
            }
        }

        return false;
    }
    */

    // ------------------------------------------------------------------------

    public String GetMD5Hash(String strMessage) {
        // Source:
        //	- http://stackoverflow.com/questions/3934331/android-how-to-encrypt-a-string
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(strMessage.getBytes(), 0, strMessage.length());
            String hash = new BigInteger(1, digest.digest()).toString(16);

				/*
				// Unicode
				digest = MessageDigest.getInstance( "MD5" );
				byte utf8_bytes[] = strMessage.getBytes();
				digest.update( utf8_bytes, 0, utf8_bytes.length );
				hash = new BigInteger(1, digest.digest()).toString(16);
				*/

            return hash;
        } catch (NoSuchAlgorithmException e) {
            __DEBUG_ERROR__(false, TAG, "GetMD5Hash(): NoSuchAlgorithmException: " + e.getMessage(), e.getStackTrace());
            return null;
        } catch (Exception e) {
            __DEBUG_ERROR__(false, TAG, "GetMD5Hash(): Exception: " + e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public boolean GetCheckMD5Hash(final String src_hash, final String message) {
        __DEBUG__("GetCheckMD5Hash()");

        if ((src_hash != null) && (message != null)) {
            String strHash = GetMD5Hash(message);
            if (strHash != null) {
                __DEBUG__("GetCheckMD5Hash(): src hash = " + src_hash + ", target = " + strHash);
                if (src_hash.equals(strHash)) {
                    __DEBUG__("GetCheckMD5Hash(): EQUALS");
                    return true;
                }

                __DEBUG__("GetCheckMD5Hash(): Cannot get new encrypted password");
                return false;
            }
        }

        __DEBUG__("GetCheckMD5Hash(): Encrypted Password doesn't found");
        return false;
    }

    // ------------------------------------------------------------------------

    //! Notification
    // ------------------------------------------------------------------------
    public int notification_inc() {
        return ++m_notify_count;
    }

    public void notification_clear() {
        m_notify_count = 0;

        {
            NotificationManager nm = (NotificationManager) m_context.getSystemService( Context.NOTIFICATION_SERVICE );
            if ( nm != null ) {
                nm.cancel( Common.__NOTIFY_ID__ );

                // leave this, auto canceled already
                nm.cancelAll();
            }
        }
    }

    //! Alarm test
    // ------------------------------------------------------------------------
    public void test_add_booking_alarm() {
        Intent intent = new Intent( m_context, CAlarmBroadcastReceiver.class );
        PendingIntent pending = null;
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarm_mgr = (AlarmManager)m_context.getSystemService( Context.ALARM_SERVICE );
        int request_code = 0;

        if ( (intent != null) && (calendar != null) && (alarm_mgr != null) ) {
            intent.putExtra("request_code", request_code);
            pending = PendingIntent.getBroadcast( m_context, request_code, intent, 0 );
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

    //! Data structures
    // ------------------------------------------------------------------------
    // Alarm
    public CAlarmToDo get_alarm_todo() {
        if ( m_alarm_todo == null )
            m_alarm_todo = new CAlarmToDo( m_context );

        return m_alarm_todo;
    }

    public void set_alarm_fired_clear() {
        set_alarm_fired( false, Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__ );
    }

    public void set_alarm_fired(boolean fired, int request_code) {
        m_alarm_fired = fired;
        m_alarm_fired_request_code = request_code;
    }

    public boolean get_alarm_fired() {
        return m_alarm_fired;
    }

    public int get_alarm_fired_request_code() {
        return m_alarm_fired_request_code;
    }

    public boolean set_alarm_list_save() {
        __DEBUG__("set_alarm_list_save()");

        boolean ret = false;
        String strConf = "";
        ArrayList<HashMap<String, Object>> list = null;

        if (m_ConfManager == null) {
            return false;
        }

        if (m_alarm_todo == null) {
            return false;
        }

        list = m_alarm_todo.get_alarm_list();
        if ( (list == null) || list.isEmpty() ) {
            return false;
        }

        /*
            type|req_code|alarm_on|repeat_daily|app_pkg_name|message|call_name|call_number|year|month|day|hour|min|sec

            e.g.,
            1|1|1|1|com.HeyHereYouAre|test_message|||2016|1|1|10|10|0    <CRLF>
            2|2|0|0||test_message|||2016|1|1|10|10|0    <CRLF>
            3|3|0|0||test_message|friend|xxx-1234-5678|2016|1|1|10|10|0    <CRLF>
            ...
         */

        for ( int i = 0; i < list.size(); i++ ) {
            HashMap<String, Object> map = list.get( i );

            if ( map == null ) continue;

            int type = (Integer)map.get( Common.__ALARM_LIST__KEY__TYPE__ );
            int req_code = (Integer)map.get( Common.__ALARM_LIST__KEY__REQUEST_CODE__ );
            boolean alarm_on = (Boolean)map.get( Common.__ALARM_LIST__KEY__ALARM_ON__ );
            boolean repeat_daily = (Boolean)map.get( Common.__ALARM_LIST__KEY__REPEAT_DAILY__ );
            String app_pkg_name = (String)map.get( Common.__ALARM_LIST__KEY__APP_PKG_NAME__ );
            String message = (String)map.get( Common.__ALARM_LIST__KEY__MSG__ );
            String call_name = (String)map.get( Common.__ALARM_LIST__KEY__CALL_NAME__ );
            String call_number = (String)map.get( Common.__ALARM_LIST__KEY__CALL_NUMBER__ );
            int year = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__YEAR__ );
            int month = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__MONTH__ );
            int day = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__DAY__ );
            int hour = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__HOUR__ );
            int min = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__MIN__ );
            int sec = (Integer)map.get( Common.__ALARM_LIST__KEY__DATETIME__SEC__ );

            if ( app_pkg_name == null )
                app_pkg_name = "";
            else
                app_pkg_name = Base64.encodeToString( app_pkg_name.getBytes(), Base64.NO_WRAP );

            if ( message == null )
                message = "";
            else
                message = Base64.encodeToString( message.getBytes(), Base64.NO_WRAP );

            if ( call_name == null )
                call_name = "";
            else
                call_name = Base64.encodeToString( call_name.getBytes(), Base64.NO_WRAP );

            if ( call_number == null )
                call_number = "";
            else
                call_number = Base64.encodeToString( call_number.getBytes(), Base64.NO_WRAP );


            strConf += type + __DELIM__;
            strConf += req_code + __DELIM__;
            strConf += (alarm_on ? 1 : 0) + __DELIM__;
            strConf += (repeat_daily ? 1 : 0) + __DELIM__;
            strConf += app_pkg_name + __DELIM__;
            strConf += message + __DELIM__;
            strConf += call_name + __DELIM__;
            strConf += call_number + __DELIM__;
            strConf += year + __DELIM__;
            strConf += month + __DELIM__;
            strConf += day + __DELIM__;
            strConf += hour + __DELIM__;
            strConf += min + __DELIM__;
            strConf += sec + __DELIM__;
            strConf += "\n";
        }

        __DEBUG__("set_alarm_list_save(): {\n" + strConf + "\n}");


        // Save as a xml
        try {
            //! DEPRECATED: MODE_WORLD_WRITEABLE
            //FileOutputStream f = openFileOutput( m_alarm_save_file, MODE_WORLD_WRITEABLE );
            FileOutputStream f = openFileOutput(m_alarm_save_file, MODE_PRIVATE);
            f.write(strConf.getBytes());
            f.close();

            ret = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "set_alarm_list_save(): IOException: " + e.getMessage(), e.getStackTrace());
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "set_alarm_list_save(): Exception: " + e.getMessage(), e.getStackTrace());
            return false;
        }

        return ret;
    }

    public boolean get_alarm_list_save() {
        __DEBUG__("get_alarm_list_save()");

        boolean ret = true;

        if (m_ConfManager == null) {
            __DEBUG_ERROR__(false, TAG, "get_alarm_list_save(): m_ConfManager == NULL", null);
            return false;
        }

        if (m_alarm_todo == null) {
            return false;
        }
        m_alarm_todo.clear_alarm_list();


        try {
            int MAX_BUF_SIZE = 2048;
            FileInputStream f = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            String strGetLine;
            StringBuilder sb = new StringBuilder();

            f = openFileInput(m_alarm_save_file);
            if (f.available() <= 0) {
                __DEBUG_ERROR__(false, TAG, "get_alarm_list_save(): \"" + m_alarm_save_file + "\" not available", null);
                f.close();
                return false;
            }

            isr = new InputStreamReader(f);
            br = new BufferedReader(isr);

            try {
                while ((strGetLine = br.readLine()) != null) {
                    //sb.append( strGetLine );
                    //sb.append( "\n" );

                    __DEBUG__("get_alarm_list_save(): GETLINE = " + strGetLine);

                    {
                        int type = Common.__ALARM_LIST__KEY__TYPE__UNKNOWN__;
                        int req_code = Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__;
                        boolean alarm_on = false;
                        boolean repeat_daily = false;
                        String app_name = null;
                        String app_pkg_name = null;
                        byte app_icon[] = null;
                        String message = null;
                        String call_name = null;
                        String call_number = null;
                        int year = -1;
                        int month = -1;
                        int day = -1;
                        int hour = -1;
                        int min = -1;
                        int sec = -1;

                        String[] strRowList = strGetLine.split( __DELIM_REGEXP__ );
                        if ( strRowList == null ) continue;
                        __DEBUG__( "get_alarm_list_save(): Split" +
                                    ": [0] type = " + strRowList[0] +
                                    ", [1] request code = " + strRowList[1] +
                                    ", [2] alarm on = " + strRowList[2] +
                                    ", [3] repeat daily = " + strRowList[3] +
                                    ", [4] app_pkg_name = " + strRowList[4] +
                                    ", [5] message = " + strRowList[5] +
                                    ", [6] call_name = " + strRowList[6] +
                                    ", [7] call_number = " + strRowList[7] +
                                    ", [8] year = " + strRowList[8] +
                                    ", [9] month = " + strRowList[9] +
                                    ", [10] day = " + strRowList[10] +
                                    ", [11] hour = " + strRowList[11] +
                                    ", [12] min = " + strRowList[12] +
                                    ", [13] sec = " + strRowList[13] );

                        type = Integer.parseInt( strRowList[0] );
                        req_code = Integer.parseInt( strRowList[1] );
                        alarm_on = ( Integer.parseInt(strRowList[2]) > 0 ? true : false );
                        repeat_daily = ( Integer.parseInt(strRowList[3]) > 0 ? true : false );
                        app_pkg_name = strRowList[4];
                        message = strRowList[5];
                        call_name = strRowList[6];
                        call_number = strRowList[7];
                        year = Integer.parseInt( strRowList[8] );
                        month = Integer.parseInt( strRowList[9] );
                        day = Integer.parseInt( strRowList[10] );
                        hour = Integer.parseInt( strRowList[11] );
                        min = Integer.parseInt( strRowList[12] );
                        sec = Integer.parseInt( strRowList[13] );

                        if ( (app_pkg_name != null) || !app_pkg_name.isEmpty() )
                            app_pkg_name = new String( Base64.decode(app_pkg_name, Base64.NO_WRAP) );
                        else
                            app_pkg_name = "";

                        if ( (message != null) || !message.isEmpty() )
                            message = new String( Base64.decode(message, Base64.NO_WRAP) );
                        else
                            message = "";

                        if ( (call_name != null) || !call_name.isEmpty() )
                            call_name = new String( Base64.decode(call_name, Base64.NO_WRAP) );
                        else
                            call_name = "";

                        if ( (call_number != null) || !call_number.isEmpty() )
                            call_number = new String( Base64.decode(call_number, Base64.NO_WRAP) );
                        else
                            call_number = "";

                        __DEBUG__( "get_alarm_list_save(): decoded" +
                                ": [0] type = " + type +
                                ", [1] request code = " + req_code +
                                ", [2] alarm on = " + alarm_on +
                                ", [3] repeat daily = " + repeat_daily +
                                ", [4] app_pkg_name = " + app_pkg_name +
                                ", [5] message = " + message +
                                ", [6] call_name = " + call_name +
                                ", [7] call_number = " + call_number +
                                ", [8] year = " + year +
                                ", [9] month = " + month +
                                ", [10] day = " + day +
                                ", [11] hour = " + hour +
                                ", [12] min = " + min +
                                ", [13] sec = " + sec );



                        if ( type == Common.__ALARM_LIST__KEY__TYPE__APP__ ) {
                            if ( (app_pkg_name == null) || app_pkg_name.isEmpty() ) continue;

                            {
                                final android.content.pm.PackageManager pm = getPackageManager();
                                List<ApplicationInfo> packages = null;

                                if ( pm == null ) continue;

                                packages = pm.getInstalledApplications( android.content.pm.PackageManager.GET_META_DATA );
                                if ( (packages == null) || packages.isEmpty() ) continue;

                                /*
                                for ( android.content.pm.ApplicationInfo packageInfo : packages ) {
                                    __DEBUG__( "load_app_list(): Installed package :" + packageInfo.packageName );
                                    __DEBUG__( "load_app_list(): Source dir : " + packageInfo.sourceDir );
                                    __DEBUG__( "load_app_list(): Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName) );
                                }
                                // the getLaunchIntentForPackage returns an intent that you can use with startActivity()
                                */

                                for ( int i = 0; i < packages.size(); i++ ) {
                                    if ( (packages.get(i).flags & ApplicationInfo.FLAG_SYSTEM) != 0 ) continue;

                                    //__DEBUG__( "load_app_list(): list = " + i + " {" );
                                    //__DEBUG__( "load_app_list():     Installed package = " + packages.get(i).packageName );
                                    //__DEBUG__( "load_app_list():     name = " + (String)packages.get(i).loadLabel(pm) );
                                    //__DEBUG__( "load_app_list():     Source dir = " + packages.get(i).sourceDir );
                                    //__DEBUG__( "load_app_list():     Launch Activity = " + pm.getLaunchIntentForPackage(packages.get(i).packageName) );
                                    //__DEBUG__( "load_app_list():     icon id = " + packages.get(i).icon );
                                    //__DEBUG__( "load_app_list(): }" );

                                    // Common.__ALARM_LIST__KEY__APP_PKG_NAME__
                                    String _app_pkg_name = (String)packages.get(i).packageName;
                                    Drawable d = null;

                                    if ( (_app_pkg_name != null) && _app_pkg_name.equals(app_pkg_name) ) {
                                        // Common.__ALARM_LIST__KEY__APP_NAME__
                                        app_name = (String) packages.get(i).loadLabel( pm );
                                        // Common.__ALARM_LIST__KEY__APP_ICON__
                                        d = (Drawable) packages.get(i).loadIcon( pm );
                                        // Common.__ALARM_LIST__KEY__APP_ICON_ID__
                                        // packages.get(i).icon );

                                        if ( d != null ) {
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

                                            app_icon = buff;
                                        }

                                        break;
                                    }
                                }

                                if ( (app_name == null) || (app_icon == null) )
                                    continue;
                            }
                        }
                        else if ( type == Common.__ALARM_LIST__KEY__TYPE__CALL__ ) {
                            //
                        }
                        else if ( type == Common.__ALARM_LIST__KEY__TYPE__MSG__ ) {
                            //
                        }
                        else {
                            continue;
                        }

                        m_alarm_todo.add_alarm_list( type, req_code, alarm_on, repeat_daily, app_name, app_pkg_name, app_icon, message,
                                                    year, month, day, hour, min, sec,
                                                    call_name, call_number );
                    }
                }
                br.close();
                isr.close();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                __DEBUG_ERROR__(false, TAG, "get_alarm_list_save(): Exception: " + e.getMessage(), e.getStackTrace());
                return false;
            }

            f.close();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            __DEBUG_ERROR__(false, TAG, "get_alarm_list_save(): Exception: " + e.getMessage(), e.getStackTrace());
            return false;
        }

        return ret;
    }
}
