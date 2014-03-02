package com.awhittle.bubblewrapper;

public class ShapeTools {
	
	public static float[] translateMatrix(float A[], float transX, float transY){

		float[] B = A;
		
		for(int i=0; i<B.length; i+=3 ){
			B[i] = A[i] + transX;
		}
		
		for(int j=1; j<B.length; j+=3 ){
			B[j] = A[j] + transY;
		}
		
		return B;
	}

	public static float[] scaleMatrix(float A[], float scale){

		float[] B;
		B = new float[A.length];
		
		for(int i=0; i<A.length; i++ )
			B[i] = A[i] * scale;
		
		return B;
	}
}
