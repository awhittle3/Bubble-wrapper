/*The following code was borrowed from
 * http://developer.android.com/training/graphics/opengl/index.html
 * and modified to suit the needs of the application.
 * 
 * 
 * Copyright (C) 2011 The Android Open Source Project
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
 */

package com.awhittle.bubblewrapper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
        float x = event.getX();
        float y = event.getY();
        
        //OpenGL screen measured in a ratio against width in x and y
        x = (x - MainActivity.width/2)/ MainActivity.width;
        y = (y - MainActivity.height/2)/ MainActivity.width;
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	
            	if (x < 0.2f && x > -0.2f && y < 0.2f && y > -0.2f){
            		setPop();
            		requestRender();
            	}

        }
        return true;
    }

	private void setPop() {
		MyGLRenderer.isPopped = true;
		
	}
	
}