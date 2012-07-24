package com.skype.api;

import com.skype.api.Conversation;


public interface ConversationListener {
	/** This event gets called when there are changes to Conversation properties defined in Conversation.Property */
	public void onPropertyChange(Conversation object, Conversation.Property p, int value, String svalue);
	/** This callback gets fired when participants join or leave the conversation.  */
	public void onParticipantListChange(Conversation object);
	/** Called for each message in currently loaded conversations * @param message
	 */
	public void onMessage(Conversation object, Message message);
	/** This callback gets fired when a new Conversation is created using SpawnConference.  * @param spawned Conversation object that got created. 
	 */
	public void onSpawnConference(Conversation object, Conversation spawned);
}
