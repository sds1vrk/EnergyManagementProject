package com.mir.vtn.profile;

public class EventDetail {

	private String venID, dtStart, market_context_id, response_required_type_id, vtn_comment, signal_name_id, signal_type_id;
	private int duration, priority;
	private double payload_value;
	private boolean test_event, event_flag= false;

	public String getVenID() {
		return venID;
	}

	public EventDetail setVenID(String venID) {
		this.venID = venID;
		return this;
	}

	public String getDtStart() {
		return dtStart;
	}

	public EventDetail setDtStart(String dtStart) {
		this.dtStart = dtStart;
		return this;
	}

	public String getMarket_context_id() {
		return market_context_id;
	}

	public EventDetail setMarket_context_id(String market_context_id) {
		this.market_context_id = market_context_id;
		return this;
	}

	public String getResponse_required_type_id() {
		return response_required_type_id;
	}

	public EventDetail setResponse_required_type_id(String response_required_type_id) {
		this.response_required_type_id = response_required_type_id;
		return this;
	}

	public String getVtn_comment() {
		return vtn_comment;
	}

	public EventDetail setVtn_comment(String vtn_comment) {
		this.vtn_comment = vtn_comment;
		return this;
	}

	public String getSignal_name_id() {
		return signal_name_id;
	}

	public EventDetail setSignal_name_id(String signal_name_id) {
		this.signal_name_id = signal_name_id;
		return this;
	}

	public String getSignal_type_id() {
		return signal_type_id;
	}

	public EventDetail setSignal_type_id(String signal_type_id) {
		this.signal_type_id = signal_type_id;
		return this;
	}

	public int getDuration() {
		return duration;
	}

	public EventDetail setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public EventDetail setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public double getPayload_value() {
		return payload_value;
	}

	public EventDetail setPayload_value(double payload_value) {
		this.payload_value = payload_value;
		return this;
		
	}

	public boolean isTest_event() {
		return test_event;
	}

	public EventDetail setTest_event(boolean test_event) {
		this.test_event = test_event;
		return this;
	}

	public boolean isEvent_flag() {
		return event_flag;
	}

	public EventDetail setEvent_flag(boolean event_flag) {
		this.event_flag = event_flag;
		return this;
	}

	@Override
	public String toString() {
		return "EventDetail [venID=" + venID + ", dtStart=" + dtStart + ", market_context_id=" + market_context_id
				+ ", response_required_type_id=" + response_required_type_id + ", vtn_comment=" + vtn_comment
				+ ", signal_name_id=" + signal_name_id + ", signal_type_id=" + signal_type_id + ", duration=" + duration
				+ ", priority=" + priority + ", payload_value=" + payload_value + ", test_event=" + test_event
				+ ", event_flag=" + event_flag + "]";
	}

}
