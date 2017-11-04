package com.example.kasun.busysms.callBlock;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.kasun.busysms.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by madupoorna on 11/3/17.
 */
public class callBlockerHomeTest {

    @Rule
    public ActivityTestRule<callBlockerHome> callBlockerHomeActivityTestRule =
            new ActivityTestRule<callBlockerHome>(callBlockerHome.class);

    private callBlockerHome callBlockerHomeActivity = null;

    @Before
    public void setUp() throws Exception {
        callBlockerHomeActivity = callBlockerHomeActivityTestRule.getActivity();
    }

    @Test
    public void TestLaunch(){
        View view = callBlockerHomeActivity.findViewById(R.id.appbar);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        callBlockerHomeActivity = null;
    }

}