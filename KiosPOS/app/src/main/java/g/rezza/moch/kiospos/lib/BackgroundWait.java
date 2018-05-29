package g.rezza.moch.kiospos.lib;

import android.content.Context;

public class BackgroundWait extends BackgroundTask {
	
	private int mTimeout = 0;
	
	public BackgroundWait(Context p_context, int p_timeout) {
		super (p_context, true);
		mTimeout = p_timeout;
	}
		
	@Override
	protected final Object doInBackground(Object p_params) {
		while (getState() == RUNNING) {
			try {
				Thread.sleep(mTimeout);
			} catch (InterruptedException ie) {
			}
		}
		return p_params;
	}

	
}