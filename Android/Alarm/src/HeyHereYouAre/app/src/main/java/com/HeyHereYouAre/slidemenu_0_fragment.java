/*
 * Project:		HeyHereYouAre
 * Purpose:
 * Author:		Ho-Jung Kim (godmode2k@hotmail.com)
 * Date:		Since Dec 12, 2015
 * Filename:	slidemenu_0_fragment.java
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

//import android.app.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2015-12-12.
 */
public class slidemenu_0_fragment extends Fragment {
    private static final String TAG = "slidemenu_0_fragment";
    private App m_main_app = null;

    private View m_rootview;
    private ListView m_listview;
    private todoListArrayAdapter m_listviewAdapter;


    // ------------------------------------------------------------------------


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        m_main_app = (App)getActivity().getApplicationContext();

        __DEBUG__("onCreateView()");

        m_rootview = inflater.inflate( R.layout.slidemenu_0_layout, container, false );
        return m_rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        __DEBUG__("onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        //m_list_items = new ArrayList<HashMap<String, Object>>();
        //test_load_list();

        if ( m_rootview != null ) {
            m_listviewAdapter = new todoListArrayAdapter( getActivity(),
                    //R.layout.slidemenu_0_layout_listview_item, new String[] {"dummy1", "dummy2"} );
                    //R.layout.slidemenu_0_layout_listview_item, new Object[] {m_list_items} );
                    R.layout.slidemenu_0_layout_listview_item, new Object[] {"dummy"} );
            m_listview = (ListView)m_rootview.findViewById( R.id.ListView_todo );

            if ( (m_listview != null) && (m_listviewAdapter != null) ) {
                m_listview.setAdapter( m_listviewAdapter );
                __DEBUG__("onActivityCreated(): done...");

                m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //__DEBUG__( "onItemClick(): pos = " + position );
                        //Toast.makeText( getContext(), "pos = " + position, Toast.LENGTH_SHORT ).show();

                        // Edit the alarm request
                        {
                            CAlarmToDo todo = null;
                            ArrayList<HashMap<String, Object>> list = null;
                            HashMap<String, Object> map = null;
                            Intent intent = null;
                            int request_code = Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__;

                            if ( m_main_app == null )
                                return;

                            todo = m_main_app.get_alarm_todo();
                            if ( todo == null )
                                return;

                            list = todo.get_alarm_list();
                            if ( list == null )
                                return;

                            if ( list.isEmpty() || (list.size() < position) || (position < 0) )
                                return;

                            map = list.get( position );
                            if ( map == null )
                                return;

                            request_code = (Integer)map.get( Common.__ALARM_LIST__KEY__REQUEST_CODE__ );
                            if ( request_code == Common.__ALARM_LIST__KEY__REQUEST_CODE__UNKNOWN__ )
                                return;

                            intent = new Intent( getActivity(), AddAlarmActivity.class );
                            if ( intent != null ) {
                                intent.putExtra( Common.__ALARM_LIST__KEY__REQUEST_CODE__, request_code );
                                startActivity( intent );
                            }
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

            {
                Button btn_add = (Button)m_rootview.findViewById( R.id.Button_add );
                Button btn_remove = (Button)m_rootview.findViewById( R.id.Button_remove );
                Button btn_on = (Button)m_rootview.findViewById( R.id.Button_alarm_on );
                Button btn_off = (Button)m_rootview.findViewById( R.id.Button_alarm_off );
                Button btn_repeat_daily_enabled = (Button)m_rootview.findViewById( R.id.Button_repeat_daily_enabled );
                Button btn_repeat_daily_disabled = (Button)m_rootview.findViewById( R.id.Button_repeat_daily_disabled );


                // Add an alarm
                if ( btn_add != null ) {
                    btn_add.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent( getActivity(), AddAlarmActivity.class );
                            if ( intent != null ) {
                                startActivity( intent );
                            }
                        }
                    });

                    // Remove
                    if ( btn_remove != null ) {
                        btn_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alarm_remove();
                            }
                        });
                    }

                    // Alarm Turn On
                    if ( btn_on != null ) {
                        btn_on.setOnClickListener(new View.OnClickListener() {
                            private boolean turn_on = true;
                            @Override
                            public void onClick(View v) {
                                alarm_on_off( turn_on );
                            }
                        });
                    }
                    // Alarm Turn Off
                    if ( btn_off != null ) {
                        btn_off.setOnClickListener(new View.OnClickListener() {
                            private boolean turn_on = false;
                            @Override
                            public void onClick(View v) {
                                alarm_on_off( turn_on );
                            }
                        });
                    }

                    //  repeat daily enabled
                    if ( btn_repeat_daily_enabled != null ) {
                        btn_repeat_daily_enabled.setOnClickListener(new View.OnClickListener() {
                            private boolean enabled = true;
                            @Override
                            public void onClick(View v) {
                                repeat_daily_enabled( enabled );
                            }
                        });
                    }

                    //  repeat daily disabled
                    if ( btn_repeat_daily_disabled != null ) {
                        btn_repeat_daily_disabled.setOnClickListener(new View.OnClickListener() {
                            private boolean enabled = false;
                            @Override
                            public void onClick(View v) {
                                repeat_daily_enabled( enabled );
                            }
                        });
                    }
                }
            }

            // Load alarm list
            if ( m_main_app != null ) {
                m_main_app.get_alarm_list_save();
            }
        }
    }

    @Override
    public void onDestroy() {
        __DEBUG__("onDestroy()");
        super.onDestroy();

        release();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
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
        __DEBUG__("onResume()");
        super.onResume();

        /*
        if ( m_main_app != null ) {
            // Notification
            m_main_app.notification_clear();
        }
        */
        Common.notification_clear( getActivity() );

        // Update an alarm list
        load_list();
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
            m_main_app.__DEBUG_ERROR__(false, TAG, str, val);
        }
    }

    // ------------------------------------------------------------------------

    private void init() {
        __DEBUG__( "init()" );
    }

    private void release() {
        __DEBUG__( "release()" );

        m_listviewAdapter = null;
        if ( m_listview != null ) {
            m_listview.setAdapter(null);
            m_listview = null;
        }

        //if ( m_list_items != null ) {
        //    m_list_items.clear();
        //    m_list_items = null;
        //}
    }

    // ------------------------------------------------------------------------

    private void test_layout_animation() {
        /*
        final LinearLayout layout =
                (LinearLayout)m_rootview.findViewById( R.id.LinearLayout_slidemenu_0_layout_menu_add );
        boolean slide_up = false;

        if ( layout != null ) {
            //Animation anim = AnimationUtils.loadAnimation( getActivity(), R.anim.anim_bottom_up );

            //layout.clearAnimation();
            //layout.requestLayout();

            Animation anim = null;

            {
                Button btn_add_app = (Button)layout.findViewById( R.id.Button_menu_add__app );
                Button btn_add_memo = (Button)layout.findViewById( R.id.Button_menu_add__memo );
                Button btn_add_call = (Button)layout.findViewById( R.id.Button_menu_add__call );

                if ( btn_add_app != null ) {
                    btn_add_app.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText( getActivity(), "Add an App", Toast.LENGTH_SHORT ).show();

                            Intent intent = new Intent( getActivity(), AddAlarmActivity.class );
                            if ( intent != null ) {
                                startActivity( intent );
                            }
                        }
                    });
                }

                if ( btn_add_memo != null ) {
                    btn_add_memo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText( getActivity(), "Add a Memo", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }

                if ( btn_add_call != null ) {
                    btn_add_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "Add a Call", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


            // Layout sliding
            if ( slide_up ) {
                anim = new TranslateAnimation( .0f, .0f, layout.getHeight(), .0f );
            }
            else {
                anim = new TranslateAnimation( .0f, .0f, .0f, layout.getHeight() );
            }

            if ( anim != null ) {
                anim.setDuration( 300 ) ;
                anim.setFillAfter( true );
                //anim.setStartTime( Animation.START_ON_FIRST_FRAME );
                //anim.setInterpolator( new AccelerateInterpolator(1.f) );

                anim.setAnimationListener( new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if ( layout != null ) {
                            if ( slide_up ) {
                                layout.setVisibility( View.INVISIBLE );
                            } else {
                                layout.setVisibility( View.VISIBLE );
                            }

                            //layout.requestLayout();
                            //layout.clearAnimation();
                        }

                        slide_up = !slide_up;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                layout.clearAnimation();
                layout.startAnimation( anim );
            }
        }
        */
    }

    /*
    private void test_load_list() {
        __DEBUG__( "test_load_list()" );

        if ( m_list_items != null ) {
            __DEBUG__( "test_load_list(): adding... " );

            String str_msg = "AAAAABBBBBCCCCCDDDDDEEEEEFFFFFGGGGGHHHHHIIIIIJJJJJKKKKKLLLLLMMMMM";

            for ( int i = 0; i < 100; i++ ) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                if ( map != null ) {
                    map.put( Common.__ALARM_LIST__KEY__APP_NAME__, "APP #" + i );
                    map.put( Common.__ALARM_LIST__KEY__MSG__, "MSG #" + i + ", " + str_msg );
                    map.put( Common.__ALARM_LIST__KEY__DATETIME__, "2015-XX-XX\n10:10" );

                    m_list_items.add( map );
                }
            }
        }
    }
    */

    private void load_list() {
        __DEBUG__("load_list()");

        if ( m_main_app != null ) {
            CAlarmToDo todo = m_main_app.get_alarm_todo();

            if ( todo != null ) {
                todo.show_list();
            }
        }

        if ( m_listviewAdapter != null ) {
            m_listviewAdapter.update_list();
            m_listviewAdapter.notifyDataSetChanged();
        }
    }

    private void alarm_remove() {
        __DEBUG__( "alarm_remove()" );

        if ( m_main_app != null ) {
            CAlarmToDo todo = m_main_app.get_alarm_todo();

            if ( todo != null ) {
                boolean ret = todo.remove_alarm_list_checked_already();
                if ( ret ) {
                    // save
                    m_main_app.set_alarm_list_save();
                }

                if ( m_listviewAdapter != null ) {
                    m_listviewAdapter.update_list();
                    m_listviewAdapter.notifyDataSetChanged();
                }

                todo.show_list();
            }
        }
    }

    private void alarm_on_off(boolean turn_on) {
        __DEBUG__( "alarm_on_off()" );

        if ( m_main_app != null ) {
            CAlarmToDo todo = m_main_app.get_alarm_todo();

            if ( todo != null ) {
                boolean ret = todo.onoff_alarm_list_checked_already( getActivity(), turn_on );
                if ( ret ) {
                    // save
                    m_main_app.set_alarm_list_save();
                }

                if ( m_listviewAdapter != null ) {
                    m_listviewAdapter.update_list();
                    m_listviewAdapter.notifyDataSetChanged();
                }

                todo.show_list();
            }
        }
    }

    private void repeat_daily_enabled(boolean enable) {
        __DEBUG__( "repeat_daily_enabled()" );

        if ( m_main_app != null ) {
            CAlarmToDo todo = m_main_app.get_alarm_todo();

            if ( todo != null ) {
                boolean ret = todo.repeat_daily_alarm_list_checked_already( getActivity(), enable );
                if ( ret ) {
                    // save
                    m_main_app.set_alarm_list_save();
                }

                if ( m_listviewAdapter != null ) {
                    m_listviewAdapter.update_list();
                    m_listviewAdapter.notifyDataSetChanged();
                }

                todo.show_list();
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
            m_inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            //m_list_items = (ArrayList<HashMap<String, Object>>)objects[0];


            {
                if ( m_main_app != null ) {
                    CAlarmToDo todo = m_main_app.get_alarm_todo();

                    if ( todo != null ) {
                        m_list_items = todo.get_alarm_list();
                    }
                }
            }
        }

        public void update_list() {
            if ( m_main_app != null ) {
                CAlarmToDo todo = m_main_app.get_alarm_todo();

                if ( todo != null ) {
                    m_list_items = todo.get_alarm_list();
                }
            }
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
                v = m_inflater.inflate( R.layout.slidemenu_0_layout_listview_item, null );
                if ( v == null ) return null;

                v.setTag( R.id.CheckBox_item, v.findViewById(R.id.CheckBox_item) );
                v.setTag( R.id.ImageView_alarm_enabled, v.findViewById(R.id.ImageView_alarm_enabled) );
                v.setTag( R.id.ImageView_alarm_repeat_daily, v.findViewById(R.id.ImageView_alarm_repeat_daily) );
                v.setTag( R.id.ImageView_thumb_app_icon, v.findViewById(R.id.ImageView_thumb_app_icon) );
                v.setTag( R.id.TextView_app_name, v.findViewById(R.id.TextView_app_name) );
                v.setTag( R.id.TextView_msg, v.findViewById(R.id.TextView_msg) );
                v.setTag( R.id.TextView_datetime, v.findViewById(R.id.TextView_datetime) );
            }

            HashMap<String, Object> map = m_list_items.get( position );

            if ( map != null ) {
                CheckBox cb_item = (CheckBox)v.getTag( R.id.CheckBox_item );
                ImageView iv_alarm_enabled = (ImageView)v.getTag( R.id.ImageView_alarm_enabled );
                ImageView iv_repeat_daily_enabled = (ImageView)v.getTag( R.id.ImageView_alarm_repeat_daily );
                ImageView iv_app_icon = (ImageView)v.getTag( R.id.ImageView_thumb_app_icon );
                TextView tv_app_name = (TextView)v.getTag( R.id.TextView_app_name );
                TextView tv_msg = (TextView)v.getTag( R.id.TextView_msg );
                TextView tv_datetime = (TextView)v.getTag(R.id.TextView_datetime);

                //int id = (Integer)map.get(Common.__ALARM_LIST__KEY__ID__);
                int type = (Integer)map.get( Common.__ALARM_LIST__KEY__TYPE__ );
                boolean checked = (Boolean)map.get( Common.__ALARM_LIST__KEY__CHECKED__ );
                boolean alarm_on = (Boolean)map.get( Common.__ALARM_LIST__KEY__ALARM_ON__ );
                boolean repeat_daily_enabled = (Boolean)map.get( Common.__ALARM_LIST__KEY__REPEAT_DAILY__ );
                byte app_icon[] = (byte[])map.get( Common.__ALARM_LIST__KEY__APP_ICON__ );
                Bitmap app_icon_bitmap = (Bitmap)map.get( Common.__ALARM_LIST__KEY__APP_ICON_BITMAP__ );
                String app_name = (String)map.get( Common.__ALARM_LIST__KEY__APP_NAME__ );
                String msg = (String)map.get( Common.__ALARM_LIST__KEY__MSG__ );
                String datetime = (String)map.get( Common.__ALARM_LIST__KEY__DATETIME_CR__ );


                //__DEBUG__( "getView(): pos = " + position + ", checked = " + checked );


                if ( cb_item != null ) {
                    cb_item.setTag( position );
                    cb_item.setChecked(checked);
                    cb_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            final int position = (Integer)buttonView.getTag();

                            if ( m_list_items != null ) {
                                HashMap<String, Object> map = m_list_items.get( position );

                                if ( map != null ) {
                                    map.put( Common.__ALARM_LIST__KEY__CHECKED__, (Boolean)isChecked );

                                    __DEBUG__( "onCheckedChanged(): pos = " + position + ", checked = " + isChecked );
                                    //Toast.makeText( m_context, "pos = " + position + ", checked = " + isChecked, Toast.LENGTH_SHORT ) .show();
                                }
                            }
                        }
                    });
                }

                if ( iv_alarm_enabled != null ) {
                    if ( alarm_on )
                        iv_alarm_enabled.setImageResource(R.drawable.circle_enable);
                    else
                        iv_alarm_enabled.setImageResource(R.drawable.circle_disable);
                }

                if ( iv_repeat_daily_enabled != null ) {
                    if ( repeat_daily_enabled ) {
                        //! DEPRECATED: getResources().getColor( int id );
                        iv_repeat_daily_enabled.setBackgroundColor( ContextCompat.getColor(getActivity(), R.color.green) );
                    }
                    else {
                        iv_repeat_daily_enabled.setBackgroundColor( ContextCompat.getColor(getActivity(), R.color.gray) );
                    }
                }

                if ( iv_app_icon != null ) {
                    if ( app_icon != null ) {
                        if ( app_icon_bitmap == null ) {
                            app_icon_bitmap = android.graphics.BitmapFactory.decodeByteArray( app_icon, 0, app_icon.length );
                        }

                        if ( app_icon_bitmap != null ) {
                            iv_app_icon.setImageBitmap( app_icon_bitmap );
                        }
                        else {
                            iv_app_icon.setImageResource( R.mipmap.ic_launcher );
                        }
                    }
                    else {
                        if ( type == Common.__ALARM_LIST__KEY__TYPE__CALL__ ) {
                            iv_app_icon.setImageResource( R.mipmap.call_contact );
                        }
                        else if ( type == Common.__ALARM_LIST__KEY__TYPE__MSG__ ) {
                            iv_app_icon.setImageResource( R.mipmap.ic_message );
                        }
                        else {
                            iv_app_icon.setImageResource( R.mipmap.ic_launcher );
                        }
                    }
                }

                if ( tv_app_name != null ) {
                    if ( app_name != null )
                        tv_app_name.setText(app_name);
                    else
                        tv_app_name.setText("");
                }

                if ( tv_msg != null ) {
                    if ( msg != null )
                        tv_msg.setText(msg);
                    else
                        tv_msg.setText("");
                }

                if ( tv_datetime != null ) {
                    if ( datetime != null ) {
                        //Common.__ALARM_LIST__KEY__DATETIME_CR_HTML__
                        //tv_datetime.setText( Html.fromHtml(datetime) );

                        tv_datetime.setText( datetime );
                    }
                    else {
                        tv_datetime.setText( "" );
                    }
                }
            }



            return v;
        }
    }
}
