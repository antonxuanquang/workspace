package com.sean.im.commom.entity;

import java.util.Map;

import com.sean.im.commom.constant.MessageEnum;

/**
 * 消息
 * @author sean
 */
public class Message
{
	private long id;
	private String content;
	private long sendTime;
	private long senderId;
	private long receiverId;
	private long flockId;
	private int type;
	private Object singleParam;
	private Map<String, Object> mulParams;

	public boolean isChatMessage()
	{
		return flockId == 0
				&& (type == MessageEnum.Message_Text || type == MessageEnum.Message_Image || type == MessageEnum.Message_Audio || type == MessageEnum.Message_Video);
	}

	public boolean isFlockChatMessage()
	{
		return flockId != 0
				&& (type == MessageEnum.Message_Text || type == MessageEnum.Message_Image || type == MessageEnum.Message_Audio || type == MessageEnum.Message_Video);
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public long getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(long sendTime)
	{
		this.sendTime = sendTime;
	}

	public long getSenderId()
	{
		return senderId;
	}

	public void setSenderId(long senderId)
	{
		this.senderId = senderId;
	}

	public long getReceiverId()
	{
		return receiverId;
	}

	public void setReceiverId(long receiverId)
	{
		this.receiverId = receiverId;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public Object getSingleParam()
	{
		return singleParam;
	}

	public void setSingleParam(Object singleParam)
	{
		this.singleParam = singleParam;
	}

	public Map<String, Object> getMulParams()
	{
		return mulParams;
	}

	public void setMulParams(Map<String, Object> mulParams)
	{
		this.mulParams = mulParams;
	}

	public long getFlockId()
	{
		return flockId;
	}

	public void setFlockId(long flockId)
	{
		this.flockId = flockId;
	}

}
