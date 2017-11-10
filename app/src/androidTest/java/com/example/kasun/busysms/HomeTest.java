package com.example.kasun.busysms;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kasun on 11/1/2017.
 */
public class HomeTest {

    @Rule
    public ActivityTestRule<Home> activityTestRule = new ActivityTestRule<Home>(Home.class);
    private Home homeActivity =null;

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