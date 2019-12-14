package ru.endroad.panorama

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20

class CubeTextureEntity(
	private val topBitmap: Bitmap?,
	private val bottomBitmap: Bitmap?,
	private val leftBitmap: Bitmap?,
	private val rightBitmap: Bitmap?,
	private val frontBitmap: Bitmap?,
	private val backBitmap: Bitmap?
) {

	var top: Int = 0
	var bottom: Int = 0
	var left: Int = 0
	var right: Int = 0
	var front: Int = 0
	var back: Int = 0

	fun init() {
		top = topBitmap.toTexture()
		bottom = bottomBitmap.toTexture()
		right = rightBitmap.toTexture()
		left = leftBitmap.toTexture()
		front = frontBitmap.toTexture()
		back = backBitmap.toTexture()
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
	}

	companion object {
		fun fromBitmap(context: Context, pathTexture: String): CubeTextureEntity {
			val top = context.loadBitmap("${pathTexture}top.jpg")
			val bottom = context.loadBitmap("${pathTexture}bottom.jpg")
			val right = context.loadBitmap("${pathTexture}right.jpg")
			val left = context.loadBitmap("${pathTexture}left.jpg")
			val front = context.loadBitmap("${pathTexture}front.jpg")
			val back = context.loadBitmap("${pathTexture}back.jpg")

			return CubeTextureEntity(top, bottom, left, right, front, back)
		}
	}
}