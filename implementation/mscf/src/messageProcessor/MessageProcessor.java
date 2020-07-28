package messageProcessor;

import messages.Message;

@FunctionalInterface
public interface MessageProcessor {
	
	public void onMessage(Message message);
}
