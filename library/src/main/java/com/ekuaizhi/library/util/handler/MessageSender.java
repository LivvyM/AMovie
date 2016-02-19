package com.ekuaizhi.library.util.handler;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ekuaizhi.library.data.ObjectSessionStore;


/**
 * 全局主线程消息发送器
 */
public class MessageSender {
	private final static String mObjectName = "messageTargetObject";
	private static Handler mHandler = null;

    /**
     * Pushes a message onto the end of the message queue after all pending messages
     * before the current time.
     *  
     * @return Returns true if the message was successfully placed in to the 
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
	public static boolean sendMessage(MessageHandler messageTargetObject, Message msg) {
    	initHandler();
        return mHandler.sendMessage(getMessage(messageTargetObject, msg));
	}

    /**
     * Sends a Message containing only the what value.
     *  
     * @return Returns true if the message was successfully placed in to the 
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
	public static boolean sendEmptyMessage(MessageHandler messageTargetObject, int what) {
    	initHandler();
        Message msg = mHandler.obtainMessage();
        msg.what = what;
		return sendMessage(messageTargetObject, msg);
	}

    /**
     * Sends a Message containing only the what value, to be delivered
     * after the specified amount of time elapses.
     * 
     * @return Returns true if the message was successfully placed in to the 
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
	public static boolean sendEmptyMessageDelayed(MessageHandler messageTargetObject, int what, long delayMillis) {
    	initHandler();
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        return mHandler.sendMessageDelayed(getMessage(messageTargetObject, msg), delayMillis);
    }

	/**
     * Enqueue a message into the message queue after all pending messages
     * before (current time + delayMillis).
     *  
     * @return Returns true if the message was successfully placed in to the 
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.  Note that a
     *         result of true does not mean the message will be processed -- if
     *         the looper is quit before the delivery time of the message
     *         occurs then the message will be dropped.
     */
    public static boolean sendMessageDelayed(MessageHandler messageTargetObject, Message msg, long delayMillis){
    	initHandler();
        return mHandler.sendMessageDelayed(getMessage(messageTargetObject, msg), delayMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages
     * before the absolute time (in milliseconds) <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * 
     * @param uptimeMillis The absolute time at which the message should be
     *         delivered, using the
     *         {@link android.os.SystemClock#uptimeMillis} time-base.
     *         
     * @return Returns true if the message was successfully placed in to the 
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.  Note that a
     *         result of true does not mean the message will be processed -- if
     *         the looper is quit before the delivery time of the message
     *         occurs then the message will be dropped.
     */
	public static boolean sendMessageAtTime(MessageHandler messageTargetObject, Message msg, long uptimeMillis) {
    	initHandler();
		return mHandler.sendMessageAtTime(getMessage(messageTargetObject, msg), uptimeMillis);
	}

    /**
     * Returns a new {@link Message Message} from the global message pool. More efficient than
     * creating and allocating new instances. The retrieved message has its handler set to this instance (Message.target == this).
     *  If you don't want that facility, just call Message.obtain() instead.
     */
    public static Message obtainMessage(){
    	initHandler();
        return Message.obtain(mHandler);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what member of the returned Message.
     * 
     * @param what Value to assign to the returned Message.what field.
     * @return A Message from the global message pool.
     */
    public static Message obtainMessage(int what){
    	initHandler();
        return Message.obtain(mHandler, what);
    }

	private static Message getMessage(MessageHandler messageTargetObject, Message msg) {
		if (null == messageTargetObject) {
			return msg;
		}

		if (null == msg) {
			msg = obtainMessage();
		}

		Bundle data = msg.getData();

		if (null == data) {
			data = new Bundle();
		}

		data.putString(mObjectName, ObjectSessionStore.insertObject(messageTargetObject));

		msg.setData(data);

		return msg;
	}

    @SuppressLint("HandlerLeak")
    @SuppressWarnings("HandlerLeak")
	private static synchronized void initHandler(){
		if(null != mHandler){
			return;
		}

		mHandler = new Handler(Looper.getMainLooper()){
			public void handleMessage(Message msg) {
				if(null == msg){
					return;
				}

				Bundle data = msg.getData();
				if(null == data){
					return;
				}

				MessageHandler object = (MessageHandler) ObjectSessionStore.popObject(data.getString(mObjectName));
				if(null == object){
					return;
				}

					object.handleMessage(msg);

			}
		};
	}
}
