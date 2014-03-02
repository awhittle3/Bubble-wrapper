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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class Unpopped {

	   private final String vertexShaderCode =
	            // This matrix member variable provides a hook to manipulate
	            // the coordinates of the objects that use this vertex shader
	            "uniform mat4 uMVPMatrix;" +
	            "attribute vec4 vPosition;" +
	            "void main() {" +
	            // The matrix must be included as a modifier of gl_Position.
	            // Note that the uMVPMatrix factor *must be first* in order
	            // for the matrix multiplication product to be correct.
	            "  gl_Position = uMVPMatrix * vPosition;" +
	            "}";

	    private final String fragmentShaderCode =
	            "precision mediump float;" +
	            "uniform vec4 vColor;" +
	            "void main() {" +
	            "  gl_FragColor = vColor;" +
	            "}";

	    private final FloatBuffer vertexBuffer;
	    private final ShortBuffer drawListBuffer;
	    private final int mProgram;
	    private int mPositionHandle;
	    private int mColorHandle;
	    private int mMVPMatrixHandle;
	    public static float[] unpoppedCoords;

	    // number of coordinates per vertex in this array
	    static final int COORDS_PER_VERTEX = 3;
	    private static final float unpoppedTemplate[] = {
	        0.0f, 0.0f, 0.0f,	//0
	        0.1f, 0.6f, 0.0f,	//1
	        0.3f, 0.5f, 0.0f,//2
	        0.5f, 0.3f, 0.0f,//3
	        0.6f, 0.1f, 0.0f,//4
	        
	        0.1f, -0.6f, 0.0f,//5
	        0.3f, -0.5f, 0.0f,//6
	        0.5f, -0.3f, 0.0f,//7
	        0.6f, -0.1f, 0.0f,//8
	        
	        -0.1f, 0.6f, 0.0f,//9
	        -0.3f, 0.5f, 0.0f,//10
	        -0.5f, 0.3f, 0.0f,//11
	        -0.6f, 0.1f, 0.0f,//12
	        
	        -0.1f, -0.6f, 0.0f,//13
	        -0.3f, -0.5f, 0.0f,//14
	        -0.5f, -0.3f, 0.0f,//15
	        -0.6f, -0.1f, 0.0f,};	//16
	    
	    public static final float initUnpoppedCoords[] = ShapeTools.scaleMatrix(unpoppedTemplate, 0.25f);
	    
	    private static final short drawOrder[] =  { 0,1,2, 0,2,3, 0,3,4, 0,4,8, 
	    											0,8,7, 0,7,6, 0,6,5, 0,5,13,
	    											0,13,14, 0,14,15, 0,15,16, 0,16,12,
	    											0,12,11, 0,11,10, 0,10,9, 0,9,1}; // order to draw vertices

	    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

	    float color[] = { 0.8f, 0.8f, 0.8f, 1.0f };

	    /**
	     * Sets up the drawing object data for use in an OpenGL ES context.
	     */
	    public Unpopped(float[] unpoppedLocation) {
	    	unpoppedCoords = ShapeTools.translateMatrix(initUnpoppedCoords, unpoppedLocation[0], unpoppedLocation[1]);
	    	
	        // initialize vertex byte buffer for shape coordinates
	        ByteBuffer bb = ByteBuffer.allocateDirect(
	        // (# of coordinate values * 4 bytes per float)
	                unpoppedCoords.length * 4);
	        bb.order(ByteOrder.nativeOrder());
	        vertexBuffer = bb.asFloatBuffer();
	        vertexBuffer.put(unpoppedCoords);
	        vertexBuffer.position(0);

	        // initialize byte buffer for the draw list
	        ByteBuffer dlb = ByteBuffer.allocateDirect(
	                // (# of coordinate values * 2 bytes per short)
	                drawOrder.length * 2);
	        dlb.order(ByteOrder.nativeOrder());
	        drawListBuffer = dlb.asShortBuffer();
	        drawListBuffer.put(drawOrder);
	        drawListBuffer.position(0);

	        // prepare shaders and OpenGL program
	        int vertexShader = MyGLRenderer.loadShader(
	                GLES20.GL_VERTEX_SHADER,
	                vertexShaderCode);
	        int fragmentShader = MyGLRenderer.loadShader(
	                GLES20.GL_FRAGMENT_SHADER,
	                fragmentShaderCode);

	        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
	        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
	        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
	        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
	    }

	    /**
	     * Encapsulates the OpenGL ES instructions for drawing this shape.
	     *
	     * @param mvpMatrix - The Model View Project matrix in which to draw
	     * this shape.
	     */
	    public void draw(float[] mvpMatrix) {
	        // Add program to OpenGL environment
	        GLES20.glUseProgram(mProgram);

	        // get handle to vertex shader's vPosition member
	        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

	        // Enable a handle to the triangle vertices
	        GLES20.glEnableVertexAttribArray(mPositionHandle);

	        // Prepare the triangle coordinate data
	        GLES20.glVertexAttribPointer(
	                mPositionHandle, COORDS_PER_VERTEX,
	                GLES20.GL_FLOAT, false,
	                vertexStride, vertexBuffer);

	        // get handle to fragment shader's vColor member
	        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

	        // Set color for drawing the triangle
	        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

	        // get handle to shape's transformation matrix
	        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	        MyGLRenderer.checkGlError("glGetUniformLocation");

	        // Apply the projection and view transformation
	        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
	        MyGLRenderer.checkGlError("glUniformMatrix4fv");

	        // Draw the square
	        GLES20.glDrawElements(
	                GLES20.GL_TRIANGLES, drawOrder.length,
	                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

	        // Disable vertex array
	        GLES20.glDisableVertexAttribArray(mPositionHandle);
	    }
}
