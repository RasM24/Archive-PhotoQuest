package ru.endroad.panorama

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.math.hypot

class PanoramaView constructor(context: Context?, attrs: AttributeSet?) : GLSurfaceView(context, attrs), LifecycleObserver {
	private var xstart = 0f
	private var ystart = 0f
	var forZoom = 0f
	var renderZoom = 0f
	var timeClick: Long = 0
	private var render: Render? = null

	fun start(lifecycleOwner: LifecycleOwner, pathTexture: String) {
		lifecycleOwner.lifecycle.addObserver(this)
		setEGLContextClientVersion(2)
		//TODO Сделать вариативность выбора рендера. Как минимум для сферической панорамы
		render = Render(context).apply { path = pathTexture }

		super.setRenderer(render)
	}

	override fun onTouchEvent(event: MotionEvent): Boolean {
		if (event.action == MotionEvent.ACTION_DOWN) {
			onTouchDown(event)
		}
		when (event.actionMasked) {
			MotionEvent.ACTION_POINTER_DOWN -> onTouchPointerDown(event)
			MotionEvent.ACTION_POINTER_UP   -> onTouchPointerUp(event)
			MotionEvent.ACTION_MOVE         -> onTouchPointerMove(event)
		}
		return true
	}

	private fun onDoubleClickListener() {
		render?.let {
			if (it.zoom > 6)
				it.zoom = it.zoom - 3.9f
			else
				it.zoom = 10f
		}
	}

	private fun onTouchDown(event: MotionEvent) {
		if (System.currentTimeMillis() - timeClick < 200) {
			onDoubleClickListener()
		}
		timeClick = System.currentTimeMillis()
		xstart = event.x
		ystart = event.y
	}

	private fun onTouchPointerDown(event: MotionEvent) {
		xstart = (event.getX(FIRST_FINGER) + event.getX(SECOND_FINGER)) / 2
		ystart = (event.getY(FIRST_FINGER) + event.getY(SECOND_FINGER)) / 2
		forZoom = hypot(event.getX(FIRST_FINGER) - event.getX(SECOND_FINGER).toDouble(),
						event.getY(FIRST_FINGER) - event.getY(SECOND_FINGER).toDouble()).toFloat()
		render?.let { renderZoom = it.zoom }
	}

	private fun onTouchPointerUp(event: MotionEvent) {
		if (event.actionIndex == SECOND_FINGER) {
			xstart = event.getX(FIRST_FINGER)
			ystart = event.getY(FIRST_FINGER)
		}
		if (event.actionIndex == FIRST_FINGER) {
			xstart = event.getX(SECOND_FINGER)
			ystart = event.getY(SECOND_FINGER)
		}
	}

	private fun onTouchPointerMove(event: MotionEvent) {
		val xx: Float
		val yy: Float
		if (event.pointerCount == 1) {
			xx = event.x
			yy = event.y
		} else {
			xx = (event.getX(FIRST_FINGER) + event.getX(SECOND_FINGER)) / 2
			yy = (event.getY(FIRST_FINGER) + event.getY(SECOND_FINGER)) / 2
			var bb = renderZoom * forZoom / hypot(event.getX(FIRST_FINGER) - event.getX(
				SECOND_FINGER).toDouble(),
												  event.getY(FIRST_FINGER) - event.getY(
													  SECOND_FINGER).toDouble()).toFloat()
			if (bb > 10) bb = 10f
			if (bb < 2) bb = 2f
			render?.zoom = bb
		}
		render?.run { turnVision(-(xx - xstart) * zoom / 3000, (yy - ystart) * zoom / 3000) }
		xstart = xx
		ystart = yy
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
	private fun runRender() = onResume()

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
	private fun stopRender() = onPause()

	companion object {
		const val FIRST_FINGER = 0
		const val SECOND_FINGER = 1

	}
}