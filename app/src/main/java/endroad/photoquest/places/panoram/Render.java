package endroad.photoquest.places.panoram;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import endroad.photoquest.R;

import static endroad.photoquest.places.panoram.RawResourceReaderKt.readTextFileFromRawResource;
import static endroad.photoquest.places.panoram.ShaderHelperKt.compileShader;
import static endroad.photoquest.places.panoram.ShaderHelperKt.createAndLinkProgram;
import static endroad.photoquest.places.panoram.TextureHelperKt.loadTexture;


public class Render implements GLSurfaceView.Renderer {
	private final Context mActivityContext;
	/**
	 * Store the accumulated rotation.
	 */
	private final float[] mAccumulatedRotation = new float[16];
	/**
	 * Store the current rotation.
	 */
	private final float[] mCurrentRotation = new float[16];
	/**
	 * Store our model data in a float buffer.
	 */
	private final FloatBuffer mCubePositions;
	private final FloatBuffer mCubeTextureCoordinates;
	String path;
	float zoom = 10;
	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];
	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];
	/**
	 * Store the projection matrix. This is used to project the scene onto a 2D viewport.
	 */
	private float[] mProjectionMatrix = new float[16];
	/**
	 * Allocate storage for the final combined matrix. This will be passed into the shader program.
	 */
	private float[] mMVPMatrix = new float[16];
	/**
	 * A temporary matrix.
	 */
	private float[] mTemporaryMatrix = new float[16];
	/**
	 * This will be used to pass in the transformation matrix.
	 */
	private int mMVPMatrixHandle;
	/**
	 * This will be used to pass in the modelview matrix.
	 */
	private int mMVMatrixHandle;
	/**
	 * This will be used to pass in model position information.
	 */
	private int mPositionHandle;
	/**
	 * This is a handle to our cube shading program.
	 */
	private int mProgramHandle;
	/**
	 * These are handles to our texture data.
	 */
	private int mTop;
	private int mBot;
	private int mLeft;
	private int mRight;

	//float mDeltaX;
	//float mDeltaY;
	private int mFront;
	private int mBack;
	private float vision_fi = 0f; // азимутальный угол камеры
	private float vision_psy = 0f; // зенитный угол камеры
	private float ratio_;

	/**
	 * Initialize the model data.
	 */
	Render(final Context activityContext) {
		mActivityContext = activityContext;

		// X, Y, Z
		final float[] cubePositionData =
				{

						// Front face
						-1.0f, 1.0f, 1.0f,
						1.0f, 1.0f, 1.0f,
						-1.0f, -1.0f, 1.0f,
						-1.0f, -1.0f, 1.0f,
						1.0f, 1.0f, 1.0f,
						1.0f, -1.0f, 1.0f,

						// Left face
						-1.0f, 1.0f, -1.0f,
						-1.0f, 1.0f, 1.0f,
						-1.0f, -1.0f, -1.0f,
						-1.0f, -1.0f, -1.0f,
						-1.0f, 1.0f, 1.0f,
						-1.0f, -1.0f, 1.0f,

						// Back face
						1.0f, 1.0f, -1.0f,
						-1.0f, 1.0f, -1.0f,
						1.0f, -1.0f, -1.0f,
						1.0f, -1.0f, -1.0f,
						-1.0f, 1.0f, -1.0f,
						-1.0f, -1.0f, -1.0f,

						// Right face
						1.0f, 1.0f, 1.0f,
						1.0f, 1.0f, -1.0f,
						1.0f, -1.0f, 1.0f,
						1.0f, -1.0f, 1.0f,
						1.0f, 1.0f, -1.0f,
						1.0f, -1.0f, -1.0f,

						// Top face
						-1.0f, 1.0f, -1.0f,
						1.0f, 1.0f, -1.0f,
						-1.0f, 1.0f, 1.0f,
						-1.0f, 1.0f, 1.0f,
						1.0f, 1.0f, -1.0f,
						1.0f, 1.0f, 1.0f,

						// Bottom face
						-1.0f, -1.0f, 1.0f,
						1.0f, -1.0f, 1.0f,
						-1.0f, -1.0f, -1.0f,
						-1.0f, -1.0f, -1.0f,
						1.0f, -1.0f, 1.0f,
						1.0f, -1.0f, -1.0f,
				};

		// S, T (or X, Y)
		// Texture coordinate data.
		// Because images have a Y axis pointing downward (values increase as you move down the image) while
		// OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
		// What's more is that the texture coordinates are the same for every face.
		final float[] cubeTextureCoordinateData =
				{
						// Front face
						1.0f, 0.0f,
						0.0f, 0.0f,
						1.0f, 1.0f,
						1.0f, 1.0f,
						0.0f, 0.0f,
						0.0f, 1.0f,

						// Right face
						1.0f, 0.0f,
						0.0f, 0.0f,
						1.0f, 1.0f,
						1.0f, 1.0f,
						0.0f, 0.0f,
						0.0f, 1.0f,

						// Back face
						1.0f, 0.0f,
						0.0f, 0.0f,
						1.0f, 1.0f,
						1.0f, 1.0f,
						0.0f, 0.0f,
						0.0f, 1.0f,

						// Left face
						1.0f, 0.0f,
						0.0f, 0.0f,
						1.0f, 1.0f,
						1.0f, 1.0f,
						0.0f, 0.0f,
						0.0f, 1.0f,

						// Top face
						1.0f, 0.0f,
						0.0f, 0.0f,
						1.0f, 1.0f,
						1.0f, 1.0f,
						0.0f, 0.0f,
						0.0f, 1.0f,

						// Bottom face
						1.0f, 0.0f,
						0.0f, 0.0f,
						1.0f, 1.0f,
						1.0f, 1.0f,
						0.0f, 0.0f,
						0.0f, 1.0f,
				};

		// Initialize the buffers.

		int mBytesPerFloat = 4; //How many bytes per float.
		mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubePositions.put(cubePositionData).position(0);

		mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0);

	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		// Set the background clear color to black.
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);

		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		// The below glEnable() call is a holdover from OpenGL ES 1, and is not needed in OpenGL ES 2.
		// Enable texture mapping
		// GLES20.glEnable(GLES20.GL_TEXTURE_2D);

		// Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f);

		final String vertexShader = readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader_tex_and_light);
		final String fragmentShader = readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader_tex_and_light);

		final int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
		final int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

		mProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
				new String[]{"a_Position", "a_TexCoordinate"});

		// Load the texture

//        mTop = TextureHelper.loadTexture(mActivityContext, R.drawable.tup);
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
//        mBot = TextureHelper.loadTexture(mActivityContext, R.drawable.tdown);
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
//        mRight = TextureHelper.loadTexture(mActivityContext, R.drawable.tright);
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
//        mLeft = TextureHelper.loadTexture(mActivityContext, R.drawable.tleft);
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
//        mFront = TextureHelper.loadTexture(mActivityContext, R.drawable.tfront);
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
//        mBack = TextureHelper.loadTexture(mActivityContext, R.drawable.tback);
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

		mTop = loadTexture(mActivityContext, path + "top.jpg");
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		mBot = loadTexture(mActivityContext, path + "bottom.jpg");
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		mRight = loadTexture(mActivityContext, path + "right.jpg");
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		mLeft = loadTexture(mActivityContext, path + "left.jpg");
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		mFront = loadTexture(mActivityContext, path + "front.jpg");
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		mBack = loadTexture(mActivityContext, path + "back.jpg");
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);


		// Initialize the accumulated rotation matrix
		Matrix.setIdentityM(mAccumulatedRotation, 0);
		onDrawFrame(glUnused);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		ratio_ = ratio;

		Matrix.frustumM(mProjectionMatrix, 0, -ratio / 10, ratio / 10, -0.1f, 0.1f, 1f / zoom, 5.0f);
	}

	void zoom(float aa) {
		zoom = aa;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio_ / 10, ratio_ / 10, -0.1f, 0.1f, 1f / zoom, 5.0f);

	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 0f, (float) (Math.sin(vision_fi) * Math.cos(vision_psy)), (float) (Math.sin(vision_psy)), -(float) (Math.cos(vision_psy) * Math.cos(vision_fi)), 0f, 1f / zoom, 0f);

		// Set our per-vertex lighting program.
		GLES20.glUseProgram(mProgramHandle);

		// Set program handles for cube drawing.
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
		mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix");
		int mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture"); // This will be used to pass in the texture.
		mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
		int mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate"); //  This will be used to pass in model texture coordinate information.

		// Draw a cube.
		// Translate the cube into the screen.
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.setIdentityM(this.mCurrentRotation, 0);
		//Matrix.rotateM(this.mCurrentRotation, 0, this.mDeltaX, 0.0F, 1.0F, 0.0F);
		//Matrix.rotateM(this.mCurrentRotation, 0, this.mDeltaY, 1.0F, 0.0F, 0.0F);
		//this.mDeltaX = 0.0F;
		//this.mDeltaY = 0.0F;

		// Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
		Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
		System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);

		// Rotate the cube taking the overall rotation into account.
		Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
		System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);

		// Set the active texture unit to texture unit 0.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFront);

		// Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
		GLES20.glUniform1i(mTextureUniformHandle, 0);

		// Pass in the texture coordinate information
		mCubeTextureCoordinates.position(0);

		int mTextureCoordinateDataSize = 2;
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,
				0, mCubeTextureCoordinates); // Size of the texture coordinate data in elements.

		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

		drawCube();


	}

	void turnVision(float _dfi, float _dpsy) {
		vision_fi += _dfi;
		vision_psy += _dpsy;
		if (vision_psy > 1.5f) {
			vision_psy = 1.5f;
		}
		if (vision_psy < -1.5f) {
			vision_psy = -1.5f;
		}
	}

	/**
	 * Draws a cube.
	 */
	private void drawCube() {
		// Pass in the position information
		mCubePositions.position(0);

		int mPositionDataSize = 3;
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
				0, mCubePositions); // Size of the position data in elements.

		GLES20.glEnableVertexAttribArray(mPositionHandle);


		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
		// (which currently contains model * view).
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

		// Pass in the modelview matrix.
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

		// This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
		// (which now contains model * view * projection).
		Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
		System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

		// Pass in the combined matrix.
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		// Draw the cube.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mRight);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 6, 6);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBack);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 12, 6);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mLeft);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 18, 6);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTop);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 24, 6);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBot);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 30, 6);
	}
}
