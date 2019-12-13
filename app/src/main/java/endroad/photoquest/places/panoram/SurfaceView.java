package endroad.photoquest.places.panoram;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.jetbrains.annotations.NotNull;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class SurfaceView extends GLSurfaceView implements LifecycleObserver {
	float xstart = 0f;
	float ystart = 0f;
	float forZoom;
	float zoom;
	long timeClick;
	private Render mRenderer;

	public SurfaceView(Context context) {
		super(context);
		//setEGLContextClientVersion(2);
		//mRenderer = new Render(context);
		//setRenderer(mRenderer);
	}

	public SurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (System.currentTimeMillis() - timeClick < 200) {
				if (mRenderer.zoom > 6)
					mRenderer.zoom(mRenderer.zoom - 3.9f);
				else mRenderer.zoom(10);
				return true; //zoom
			}
			timeClick = System.currentTimeMillis();
			xstart = event.getX();
			ystart = event.getY();
		}
		if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
			xstart = (event.getX(0) + event.getX(1)) / 2;
			ystart = (event.getY(0) + event.getY(1)) / 2;
			forZoom = (float) Math.hypot(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1));
			zoom = mRenderer.zoom;

		}
		if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
			if (event.getActionIndex() == 1) {
				xstart = event.getX(0);
				ystart = event.getY(0);
			}
			if (event.getActionIndex() == 0) {

				xstart = event.getX(1);
				ystart = event.getY(1);
			}
		}
		if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
			float xx;
			float yy;
			if (event.getPointerCount() == 1) {
				xx = event.getX();
				yy = event.getY();
			} else {
				xx = (event.getX(0) + event.getX(1)) / 2;
				yy = (event.getY(0) + event.getY(1)) / 2;
				float bb = zoom * forZoom / (float) Math.hypot(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1));
				if (bb > 10) bb = 10;
				if (bb < 2) bb = 2;
				mRenderer.zoom(bb);
			}
			mRenderer.turnVision(-(xx - xstart) * mRenderer.zoom / 3000, (yy - ystart) * mRenderer.zoom / 3000);
			xstart = xx;
			ystart = yy;
		}

		return true;
	}

	// Hides superclass method.
	public void setRenderer(Render renderer) {
		mRenderer = renderer;
		super.setRenderer(renderer);
	}

	public void setTexturePath(@NotNull String pathTexture) {
		mRenderer.path = pathTexture;
	}

	public void start(LifecycleOwner lifecycleOwner) {
		lifecycleOwner.getLifecycle().addObserver(this);

		setEGLContextClientVersion(2);
		setRenderer(new Render(getContext()));
	}


	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
	private void runRender() {
		onResume();
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
	private void stopRender() {
		onPause();
	}
}
