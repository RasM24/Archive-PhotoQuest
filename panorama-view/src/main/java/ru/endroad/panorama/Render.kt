package ru.endroad.panorama

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

private const val ONE = 1.0f
private const val ZERO = 0.0f
private const val POSITION_DATA_SIZE = 3
private const val TEXTURE_COORDINATE_DATA_SIZE = 2
private const val BYTES_PER_FLOAT = 4
// X, Y, Z
private val cubePositionData = floatArrayOf(
	// Front face
	-ONE, ONE, ONE, ONE, ONE, ONE, -ONE, -ONE, ONE, -ONE, -ONE, ONE, ONE, ONE, ONE, ONE, -ONE, ONE,
	// Left face
	-ONE, ONE, -ONE, -ONE, ONE, ONE, -ONE, -ONE, -ONE, -ONE, -ONE, -ONE, -ONE, ONE, ONE, -ONE, -ONE, ONE,
	// Back face
	ONE, ONE, -ONE, -ONE, ONE, -ONE, ONE, -ONE, -ONE, ONE, -ONE, -ONE, -ONE, ONE, -ONE, -ONE, -ONE, -ONE,
	// Right face
	ONE, ONE, ONE, ONE, ONE, -ONE, ONE, -ONE, ONE, ONE, -ONE, ONE, ONE, ONE, -ONE, ONE, -ONE, -ONE,
	// Top face
	-ONE, ONE, -ONE, ONE, ONE, -ONE, -ONE, ONE, ONE, -ONE, ONE, ONE, ONE, ONE, -ONE, ONE, ONE, ONE,
	// Bottom face
	-ONE, -ONE, ONE, ONE, -ONE, ONE, -ONE, -ONE, -ONE, -ONE, -ONE, -ONE, ONE, -ONE, ONE, ONE, -ONE, -ONE)

// S, T (or X, Y)
private val cubeTextureCoordinateData = floatArrayOf(
	ONE, ZERO, ZERO, ZERO, ONE, ONE, ONE, ONE, ZERO, ZERO, ZERO, ONE, // Front face
	ONE, ZERO, ZERO, ZERO, ONE, ONE, ONE, ONE, ZERO, ZERO, ZERO, ONE, // Right face
	ONE, ZERO, ZERO, ZERO, ONE, ONE, ONE, ONE, ZERO, ZERO, ZERO, ONE, // Back face
	ONE, ZERO, ZERO, ZERO, ONE, ONE, ONE, ONE, ZERO, ZERO, ZERO, ONE, // Left face
	ONE, ZERO, ZERO, ZERO, ONE, ONE, ONE, ONE, ZERO, ZERO, ZERO, ONE, // Top face
	ONE, ZERO, ZERO, ZERO, ONE, ONE, ONE, ONE, ZERO, ZERO, ZERO, ONE // Bottom face
)

internal class Render internal constructor(private val mActivityContext: Context) : GLSurfaceView.Renderer, OnTurnVisionListener {
	/**
	 * Store the accumulated rotation.
	 */
	private val mAccumulatedRotation = FloatArray(16)
	/**
	 * Store the current rotation.
	 */
	private val mCurrentRotation = FloatArray(16)

	private val mCubePositions = ByteBuffer.allocateDirect(cubePositionData.size * BYTES_PER_FLOAT)
		.order(ByteOrder.nativeOrder())
		.asFloatBuffer()
		.apply { put(cubePositionData).position(0) }

	private val mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.size * BYTES_PER_FLOAT)
		.order(ByteOrder.nativeOrder())
		.asFloatBuffer()
		.apply { put(cubeTextureCoordinateData).position(0) }

	var path: String? = null
	var zoom = 10f
		set(value) {
			Matrix.frustumM(mProjectionMatrix, 0, -ratio / 10, ratio / 10, -0.1f, 0.1f, 1f / value, 5.0f)
			field = value
		}
	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private val mModelMatrix = FloatArray(16)
	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	private val mViewMatrix = FloatArray(16)
	/**
	 * Store the projection matrix. This is used to project the scene onto a 2D viewport.
	 */
	private val mProjectionMatrix = FloatArray(16)
	/**
	 * Allocate storage for the final combined matrix. This will be passed into the shader program.
	 */
	private val mMVPMatrix = FloatArray(16)
	/**
	 * A temporary matrix.
	 */
	private val mTemporaryMatrix = FloatArray(16)
	/**
	 * This will be used to pass in the transformation matrix.
	 */
	private var mMVPMatrixHandle = 0
	/**
	 * This will be used to pass in the modelview matrix.
	 */
	private var mMVMatrixHandle = 0
	/**
	 * This will be used to pass in model position information.
	 */
	private var mPositionHandle = 0
	/**
	 * This is a handle to our cube shading program.
	 */
	private var mProgramHandle = 0
	/**
	 * These are handles to our texture data.
	 */
	private var mTop = 0
	private var mBot = 0
	private var mLeft = 0
	private var mRight = 0
	private var mFront = 0
	private var mBack = 0

	private var visionFi = 0f // азимутальный угол камеры
	private var visionPsy = 0f // зенитный угол камеры
	private var ratio = 0f

	override fun onSurfaceCreated(glUnused: GL10,
								  config: EGLConfig) { // Set the background clear color to black.
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE)
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST)
		// The below glEnable() call is a holdover from OpenGL ES 1, and is not needed in OpenGL ES 2.
// Enable texture mapping
// GLES20.glEnable(GLES20.GL_TEXTURE_2D);
// Set the view matrix. This matrix can be said to represent the camera position.
// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f)
		val vertexShader = mActivityContext.readTextFileFromRawResource(R.raw.per_pixel_vertex_shader_tex_and_light)
		val fragmentShader = mActivityContext.readTextFileFromRawResource(R.raw.per_pixel_fragment_shader_tex_and_light)
		val vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader)
		val fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader)
		mProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, arrayOf("a_Position", "a_TexCoordinate"))

		loadTexture(path)
		// Initialize the accumulated rotation matrix
		Matrix.setIdentityM(mAccumulatedRotation, 0)
		onDrawFrame(glUnused)
	}

	//TODO убрать загрузку текстур из рендера
	private fun loadTexture(pathTexture: String?) {
		mTop = loadTexture(mActivityContext, "${pathTexture}top.jpg")
		mBot = loadTexture(mActivityContext, "${pathTexture}bottom.jpg")
		mRight = loadTexture(mActivityContext, "${pathTexture}right.jpg")
		mLeft = loadTexture(mActivityContext, "${pathTexture}left.jpg")
		mFront = loadTexture(mActivityContext, "${pathTexture}front.jpg")
		mBack = loadTexture(mActivityContext, "${pathTexture}back.jpg")
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
	}

	override fun onSurfaceChanged(glUnused: GL10, width: Int, height: Int) { // Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height)
		// Create a new perspective projection matrix. The height will stay the same
// while the width will vary as per aspect ratio.
		val ratio = width.toFloat() / height
		this.ratio = ratio
		Matrix.frustumM(mProjectionMatrix, 0, -ratio / 10, ratio / 10, -0.1f, 0.1f, 1f / zoom, 5.0f)
	}

	override fun onDrawFrame(glUnused: GL10) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

		Matrix.setLookAtM(mViewMatrix, 0,
						  0f, 0f, 0f,
						  sin(visionFi) * cos(visionPsy), sin(visionPsy), -(cos(visionPsy) * cos(visionFi)),
						  0f, 1f / zoom, 0f)
		// Set our per-vertex lighting program.
		GLES20.glUseProgram(mProgramHandle)
		// Set program handles for cube drawing.
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix")
		mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix")
		val mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture") // This will be used to pass in the texture.
		mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position")
		val mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle,
																  "a_TexCoordinate") //  This will be used to pass in model texture coordinate information.

		// Draw a cube.
// Translate the cube into the screen.
		Matrix.setIdentityM(mModelMatrix, 0)
		Matrix.setIdentityM(mCurrentRotation, 0)
		//Matrix.rotateM(this.mCurrentRotation, 0, this.mDeltaX, 0.0F, 1.0F, 0.0F);
//Matrix.rotateM(this.mCurrentRotation, 0, this.mDeltaY, 1.0F, 0.0F, 0.0F);
//this.mDeltaX = 0.0F;
//this.mDeltaY = 0.0F;
// Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
		Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0)
		System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16)
		// Rotate the cube taking the overall rotation into account.
		Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0)
		System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16)
		// Set the active texture unit to texture unit 0.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
		// Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
		GLES20.glUniform1i(mTextureUniformHandle, 0)
		// Pass in the texture coordinate information
		mCubeTextureCoordinates.position(0)
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle,
									 TEXTURE_COORDINATE_DATA_SIZE,
									 GLES20.GL_FLOAT,
									 false,
									 0,
									 mCubeTextureCoordinates) // Size of the texture coordinate data in elements.
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle)

		passInThePositionInformation()
		drawCube()
	}

	/**
	 * Draws a cube.
	 */
	private fun drawCube() {

		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
// (which currently contains model * view).
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)
		// Pass in the modelview matrix.
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0)
		// This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
// (which now contains model * view * projection).
		Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0)
		System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16)
		// Pass in the combined matrix.
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0)
		// Draw the cube.
		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFront)
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6) // Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mRight)
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 6, 6)
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBack)
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 12, 6)
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mLeft)
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 18, 6)
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTop)
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 24, 6)
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBot)
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 30, 6)
	}

	private fun passInThePositionInformation() {
		mCubePositions.position(0)
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, mCubePositions)
		GLES20.glEnableVertexAttribArray(mPositionHandle)
	}

	override fun onTurnVision(deltaFi: Float, deltaPsy: Float) {
		visionFi += deltaFi
		visionPsy += deltaPsy

		if (visionPsy > 1.5f) {
			visionPsy = 1.5f
		}
		if (visionPsy < -1.5f) {
			visionPsy = -1.5f
		}
	}
}
