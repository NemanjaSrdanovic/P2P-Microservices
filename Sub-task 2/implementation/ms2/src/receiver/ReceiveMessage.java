package receiver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import messages.Message;

/**
 * The ReceiveMessage class is...
 *
 * @author Nemanja Srdanovic
 * @version 1.0
 * @since 2020-06-09
 */
public class ReceiveMessage implements messageProcessor.MessageProcessor {

	/*-------------------------------------VARIABLES-------------------------------------*/

	private List<Message> messageList;
	boolean messageInList = false;
	private SearchMessage search;

	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/

	public ReceiveMessage() {

		this.messageList = new CopyOnWriteArrayList<Message>();
		this.search = new SearchMessage(this);
	}

	/**
	  @param Message
	  
	  Recieving messages forwarded from the UDPServer(mscfFramework), and placing
	  them into a messageList from which their objects are given to the correct
	  function in MS2Controller
	*/
	@Override
	public synchronized void onMessage(Message message) {

		messageList.add(message);
		messageInList = true;

	}

	/*-------------------------------------GETTERS AND SETTER METHODS-------------------------------------*/

	/**/
	public synchronized boolean getMessageInList() {
		return messageInList;
	}

	/**/
	public synchronized void setMessageInList(boolean setter) {

		this.messageInList = setter;
	}

	/**/
	public synchronized List<Message> getMessageList() {
		return messageList;
	}

	/**/
	public synchronized void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	/**/
	public SearchMessage getSearchMessage() {

		return search;
	}

	/**/
	public synchronized void removeMessageFromList(Message message) {

		messageList.remove(message);

	}

}
