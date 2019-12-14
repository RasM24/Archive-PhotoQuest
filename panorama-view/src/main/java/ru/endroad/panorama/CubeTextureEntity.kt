package ru.endroad.panorama

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20

internal class CubeTextureEntity(
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
		fun fromBitmap(context: Context, texturePathes: TexturePathes): CubeTextureEntity {
			val top = context.loadBitmap(texturePathes.top)
			val bottom = context.loadBitmap(texturePathes.bottom)
			val right = context.loadBitmap(texturePathes.right)
			val left = context.loadBitmap(texturePathes.left)
			val front = context.loadBitmap(texturePathes.front)
			val back = context.loadBitmap(texturePathes.back)

			return CubeTextureEntity(top, bottom, left, right, front, back)
		}
	}
}

class TexturePathes(val top: String, val bottom: String, val left: String, val right: String, val front: String, val back: String)