/*
 * Copyright (c) 2017 Gowtham Parimelazhagan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gm.goldencity.activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.gm.glog.library.GLog;
import com.gm.goldencity.ApplicationComponent;
import com.gm.goldencity.GoldenCityApplication;
import com.gm.goldencity.R;
import com.gm.goldencity.activity.search.SearchActivity;
import com.gm.goldencity.base.basic.BaseNavigationActivity;
import com.gm.goldencity.util.AppConstants;
import com.gm.goldencity.util.session.SessionConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends BaseNavigationActivity {

    private static final int RC_SEARCH = 0;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.content_dash_board);
        ButterKnife.bind(this);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        //To show helpDescriptions
        helpScreenData(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_search) {

            View searchMenuView = getToolbar().findViewById(R.id.menu_search);
            Bundle options = ActivityOptions.makeSceneTransitionAnimation(this, searchMenuView,
                    getString(R.string.transition_search_back)).toBundle();
            startActivityForResult(new Intent(this, SearchActivity.class), RC_SEARCH, options);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    protected void injectDependencies(GoldenCityApplication application, ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void onBackPressed() {
        if (backPressed + AppConstants.BACK_PRESS_TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(context, R.string.toast_double_back, Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    /**
     * HelpScreen Contents
     *
     * @param show is userd to show the help screen if true
     */
    private void helpScreenData(boolean show) {

        if (show) {
            // For Sequence

            final TapTargetSequence tapTargetSequence = new TapTargetSequence(this)
                    .targets(
                            TapTarget.forToolbarNavigationIcon(getToolbar(), "This is ToolBar").id(1),
//                        TapTarget.forToolbarMenuItem(getToolbar(),R.id.action_settings,"Setting","This is a Setting Menu").id(2),
                            TapTarget.forToolbarOverflow(getToolbar(), "This will show more").id(2)
                    ).listener(new TapTargetSequence.Listener() {
                        @Override
                        public void onSequenceFinish() {
                            Toast.makeText(context, "Congrats! Now u know about the app usage", Toast.LENGTH_SHORT).show();
                            session.putBooleanValues(SessionConstants.TAG_FIRST_TIME, true);
                        }

                        @Override
                        public void onSequenceStep(TapTarget lastTarget) {
                            GLog.e("TapTargetView", "Clicked on " + lastTarget.id());
                        }

                        @Override
                        public void onSequenceCanceled(TapTarget lastTarget) {
                            final AlertDialog dialog = new AlertDialog.Builder(DashBoardActivity.this)
                                    .setTitle("Uh oh")
                                    .setMessage("You Cancled the Scequence")
                                    .setPositiveButton("Oops", null).show();
                            TapTargetView.showFor(dialog, TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You cancled the sequence at step " + lastTarget.id())
                                    .cancelable(false)
                                    .tintTarget(false), new TapTargetView.Listener() {
                                @Override
                                public void onTargetClick(TapTargetView view) {
                                    super.onTargetClick(view);
                                    dialog.dismiss();
                                }
                            });
                        }
                    });


            if (!session.getBooleanValues(SessionConstants.TAG_FIRST_TIME)) {
                tapTargetSequence.start();
            }
            // For Single

       /* final SpannableString spannableString = new SpannableString("This is a fab");
        spannableString.setSpan(new UnderlineSpan(), spannableString.length() - "fab".length(), spannableString.length(), 0);
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.fab), "Hi How are u", spannableString)
                .cancelable(false)
                .drawShadow(true)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                tapTargetSequence.start();
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(context, "Clicked Outer Circle", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                super.onTargetDismissed(view, userInitiated);
            }
        });*/
        }
    }

}
