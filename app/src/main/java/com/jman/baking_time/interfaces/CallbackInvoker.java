package com.jman.baking_time.interfaces;

import android.app.Activity;

/**
 * Created by Justin on 23/06/2018.
 *
 * This informs the fragment about the click event so it can inform the host activity
 */

public interface CallbackInvoker {

    void invokeCallback(int position);
}
