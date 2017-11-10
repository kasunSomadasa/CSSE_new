package com.example.kasun.busysms.callBlock;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by madupoorna
 */
public class PhoneCallStateListenerTest {

    Context context;
    callReceiver.PhoneCallStateListener callReceiver =new callReceiver().new PhoneCallStateListener(context);

    @Test
    public void isBetween() throws Exception {

        String from ="20:00";
        String to ="21:40";

        boolean output = callReceiver.isBetween(from,to);

        boolean expected = true;

        assertEquals(expected,output);
    }

}