package com.example.kasun.busysms.callBlock;

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
public class CallBlockerHomeTest {

    @Rule
    public ActivityTestRule<CallBlockerHome> callBlockerHomeActivityTestRule =
            new ActivityTestRule<CallBlockerHome>(CallBlockerHome.class);

    private CallBlockerHome callBlockerHomeActivity = null;

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