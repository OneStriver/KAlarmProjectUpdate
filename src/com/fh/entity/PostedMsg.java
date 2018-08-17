package com.fh.entity;

public class PostedMsg 
{
	private int type;
	private Object msg;
	private String topic;
	
	public PostedMsg(int type, Object msg, String topic)
	{
		this.type=type;
		this.msg=msg;
		this.topic=topic;
	}
	
	public PostedMsg(Object msg, String topic)
	{
		this.msg=msg;
		this.topic=topic;
	}
	
	public PostedMsg(int type, Object msg)
	{
		this.type=type;
		this.msg=msg;
	}
	
	public PostedMsg(Object msg)
	{
		this.msg=msg;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
}
