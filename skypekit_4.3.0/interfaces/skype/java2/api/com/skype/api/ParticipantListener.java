package com.skype.api;

import com.skype.api.Participant;
import com.skype.api.Participant;


public interface ParticipantListener {
	/** This event gets called when there are changes to Participant properties defined in Participant.Property */
	public void onPropertyChange(Participant object, Participant.Property p, int value, String svalue);
	/** This event gets fired on receiving a DTMF signal sent by Participant. Note that this event will only fire if the Participant is also using a Skype client. Skype audio library does not monitor incoming voice streams for dial tones. DTMF events are propagated to remote participants via data channel. Incoming DTMF tones transmitted from, for example, mobile phones, will not cause this event to fire. In case of incoming DTMF signals from Skype clients, DTMF tones are also inserted into the audio stream. You don't have to inject those into local audio playback yourself.  * @param dtmf Returns Participant.DTMF value. 
	 */
	public void onIncomingDtmf(Participant object, Participant.Dtmf dtmf);
	/**
	 * onLiveSessionVideosChanged
	 * @param object
	 */
	public void onLiveSessionVideosChanged(Participant object);
}
