/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 12, 2015
 * Filename:	slidemenu_2_fragment.java
 *
 * Last modified:	Dec 12, 2015
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
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 2015-12-12.
 */
public class slidemenu_2_fragment extends Fragment {
    private static final String TAG = "slidemenu_0_fragment";
    private App m_main_app = null;

    View rootview;


    // ------------------------------------------------------------------------


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        rootview = inflater.inflate( R.layout.slidemenu_2_layout, container, false );
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        __DEBUG__("onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        __DEBUG__("onDestroy()");
        super.onDestroy();
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
        }
        else {
            __DEBUG__( "onActivityResult(): Unknown resultCode = " + resultCode );
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
            m_main_app.__DEBUG_ERROR__( false, TAG, str, val );
        }
    }

    // ------------------------------------------------------------------------


}
