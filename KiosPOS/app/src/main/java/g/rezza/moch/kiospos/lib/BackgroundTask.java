package g.rezza.moch.kiospos.lib;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import g.rezza.moch.kiospos.component.MwaProgressDialog;

public class BackgroundTask {

	public static final int PENDING = 0;
	public static final int RUNNING = 1;
	public static final int CLOSING = 2; 
	public static final int CLOSED  = 3; 
	public static final int CANCELLED = 4; 
	
	private MessageHandler mHandler;
	private Thread mThread;
	private int mState = PENDING;
	private MwaProgressDialog mMwaProgressDialog;
		
	public BackgroundTask(Context p_context) {
		this (p_context, false);
	}

	public BackgroundTask(Context p_context, boolean p_showLoading) {
		mHandler = new MessageHandler(this);
		if (p_showLoading) {
			mMwaProgressDialog = new MwaProgressDialog(p_context);
			mMwaProgressDialog.setCancelable(false);
			mMwaProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface p_dialog) {
					cancel();
				}
			});
		}
	}
	
	public final void setMessage(CharSequence p_message) {
		mMwaProgressDialog.setMessage(p_message);
	}
	
	public final void setCancelable(boolean p_cancelable) {
		mMwaProgressDialog.setCancelable(p_cancelable);
	}
	
	public final synchronized void stop() {
		mState = CLOSING;
		if (mThread != null) {
			mThread.interrupt();
		}
	}
	
	public final int getState() {
		return mState;
	}

	private synchronized final void cancel() {
		mState = CANCELLED;
		onCancelled();
		if (mThread != null) {
			mThread.interrupt();
		}
	}
	
	public final synchronized void execute() throws IllegalStateException {
		execute((Object[]) null);
	}
	
	public final synchronized void execute(final Object p_object) throws IllegalStateException {
		if (mState == PENDING) {
			mState = RUNNING;
			onPreExecute();
			openProgress();
			mThread = new Thread(new Runnable() {
				@Override
				public void run() {
					Object result = doInBackground(p_object);
					Message message = new Message();
					message.what = 1;
					message.obj  = result;
					mHandler.sendMessage(message);				
				}
			});
			mThread.start();
		}
		else if (mState == RUNNING) {
			throw new IllegalStateException("Cannot execute task: the task is already running.");
		}
		else if (mState == CLOSING) {
			throw new IllegalStateException("Cannot execute task: the task is going to close");
		}
		else if (mState == CLOSED) {
			throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
		}
	}

	protected void onPreExecute() {
		
	}

	protected Object doInBackground(Object p_params) {
		return null;
	}

	protected void onPostExecute(Object p_result) {
		
	}

	protected void onProgressUpdate(Object p_value) {

	}
	
	protected void onCancelled() {
		
	}

	public final void publishProgress(Object p_value) {
		Message message = new Message();
		message.what = 2;
		message.obj = p_value;
		mHandler.sendMessage(message);
	}

	private void openProgress() {
		if (mMwaProgressDialog != null) {
			mMwaProgressDialog.show();
		}
	}
	
	private void closeProgress() {
		if (mMwaProgressDialog != null && mMwaProgressDialog.isShowing()) {
			mMwaProgressDialog.dismiss();
		}
	}
	
	private static class MessageHandler extends Handler {
		
		private BackgroundTask mTask;
		
		private MessageHandler(BackgroundTask p_task) {
			mTask = p_task;
		}
		@Override
		public void handleMessage(Message p_message) {
			switch (p_message.what) {
			case 1:
				mTask.closeProgress();				
				mTask.onPostExecute(p_message.obj);
				mTask.mState = CLOSED;
				break;
			case 2:
				mTask.onProgressUpdate(p_message.obj);
				break;
			default:
				break;
			}
		}
	}
	
}