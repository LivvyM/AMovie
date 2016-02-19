package com.ekuaizhi.library.util.handler;

import android.os.Message;

/**
 * 用于取代 Handler 的消息管理器
 */
public abstract class MessageHandler {
	public MessageHandler(){
	}

	public abstract void handleMessage(Message msg);

	/**
     * Pushes a message onto the end of the message queue after all pending messages
     * before the current time. It will be received in {@link #handleMessage},
     * in the thread attached to this handler.
     *  
     * @return Returns true if the message was successfully placed in to the 
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
	public final boolean sendMessage(Message msg) {
        return MessageSender.sendMessage(this, msg);
	}

    /**
     * Sends a Message containing only the what value.
     *  
     * @return Returns true if the message was successfully placed in to the 
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
	public final boolean sendEmptyMessage(int what) {
		return MessageSender.sendEmptyMessage(this, what);
	}

    /**
     * Sends a Message containing only the what value, to be delivered
     * after the specified amount of time elapses.
     * @see #sendMessageDelayed(Message, long)
     *
     * @return Returns true if the message was successfully placed in to the
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
	public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
		return MessageSender.sendEmptyMessageDelayed(this, what, delayMillis);
    }

	/**
     * Enqueue a message into the message queue after all pending messages
     * before (current time + delayMillis). You will receive it in
     * {@link #handleMessage}, in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.  Note that a
     *         result of true does not mean the message will be processed -- if
     *         the looper is quit before the delivery time of the message
     *         occurs then the message will be dropped.
     */
    public final boolean sendMessageDelayed(Message msg, long delayMillis){
    	return MessageSender.sendMessageDelayed(this, msg, delayMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages
     * before the absolute time (in milliseconds) <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * You will receive it in {@link #handleMessage}, in the thread attached
     * to this handler.
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
	public final boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		return MessageSender.sendMessageAtTime(this, msg, uptimeMillis);
	}

	/**
     * Returns a new {@link Message Message} from the global message pool. More efficient than
     * creating and allocating new instances. The retrieved message has its handler set to this instance (Message.target == this).
     *  If you don't want that facility, just call Message.obtain() instead.
     */
    public final Message obtainMessage(){
        return MessageSender.obtainMessage();
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what member of the returned Message.
     * 
     * @param what Value to assign to the returned Message.what field.
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what){
        return MessageSender.obtainMessage(what);
    }
}
