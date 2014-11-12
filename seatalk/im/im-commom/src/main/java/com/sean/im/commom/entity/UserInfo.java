package com.sean.im.commom.entity;

import java.io.Serializable;

/**
 * 用户信息
 * @author sean
 */
public class UserInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	private long id;
	private String username;
	private String password;
	private String nickname;
	private String signature;
	private int country;
	private int sex;
	private int age;
	private String tel;
	private String mail;
	private int language;
	private String description;
	private int head;
	private int status;
	private String translator;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getSignature()
	{
		return signature;
	}

	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	public int getCountry()
	{
		return country;
	}

	public void setCountry(int country)
	{
		this.country = country;
	}

	public int getSex()
	{
		return sex;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public String getTel()
	{
		return tel;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public String getMail()
	{
		return mail;
	}

	public void setMail(String mail)
	{
		this.mail = mail;
	}

	public int getLanguage()
	{
		return language;
	}

	public void setLanguage(int language)
	{
		this.language = language;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getHead()
	{
		return head;
	}

	public void setHead(int head)
	{
		this.head = head;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getTranslator()
	{
		return translator;
	}

	public void setTranslator(String translator)
	{
		this.translator = translator;
	}

}
