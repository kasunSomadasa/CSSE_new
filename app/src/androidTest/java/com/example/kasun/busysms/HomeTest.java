package com.example.kasun.busysms;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.kasun.busysms.autoSms.smsHome;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kasun on 11/1/2017.
 */
public class homeTest {

    @Rule
    public ActivityTestRule<home> activityTestRule = new ActivityTestRule<home>(home.class);
    private home homeActivity =null;

    @Before
    public void setUp() throws Exception {
        homeActivity=activityTestRule.getActivity();
    }

    @Test
    public void activityLaunch(){
        View view = homeActivity.findViewById(R.id.homeListView);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        homeActivity=null;
    }

}