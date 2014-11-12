package com.sean.im.friend.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.commom.entity.UserInfo;
import com.sean.im.friend.constant.Tables;
import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = Tables.UserInfo, dataSource = IMDataSource.class, description = "用户实体")
public class UserInfoEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "用户名")
	private String username;
	@ColumnConfig(description = "密码")
	private String password;
	@ColumnConfig(description = "昵称")
	private String nickname;
	@ColumnConfig(description = "个性签名")
	private String signature;
	@ColumnConfig(description = "国家")
	private int country;
	@ColumnConfig(description = "性别")
	private int sex;
	@ColumnConfig(description = "年龄")
	private int age;
	@ColumnConfig(description = "电话")
	private String tel;
	@ColumnConfig(description = "邮箱")
	private String mail;
	@ColumnConfig(description = "语言")
	private int language;
	@ColumnConfig(description = "个人说明")
	private String description;
	@ColumnConfig(description = "头像Id")
	private int head;
	@ColumnConfig(description = "用户状态")
	private int status;
	@ColumnConfig(description = "翻译语言")
	private String translator;

	@Override
	public Object getKey()
	{
		return id;
	}

	@Override
	public void setKey(Object key)
	{
		this.id = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<String, Object>(14);
		map.put("id", id);
		map.put("username", username);
		map.put("password", password);
		map.put("nickname", nickname);
		map.put("signature", signature);
		map.put("country", country);
		map.put("sex", sex);
		map.put("age", age);
		map.put("tel", tel);
		map.put("mail", mail);
		map.put("language", language);
		map.put("description", description);
		map.put("head", head);
		map.put("status", status);
		map.put("translator", translator);
		return map;
	}

	@Override
	public void setValues(Map<String, Object> vals)
	{
		Object o = null;
		if ((o = vals.get("id")) != null)
			this.id = Long.parseLong(o.toString());
		if ((o = vals.get("username")) != null)
			this.username = o.toString();
		if ((o = vals.get("password")) != null)
			this.password = o.toString();
		if ((o = vals.get("nickname")) != null)
			this.nickname = o.toString();
		if ((o = vals.get("signature")) != null)
			this.signature = o.toString();
		if ((o = vals.get("country")) != null)
			this.country = Integer.parseInt(o.toString());
		if ((o = vals.get("sex")) != null)
			this.sex = Integer.parseInt(o.toString());
		if ((o = vals.get("age")) != null)
			this.age = Integer.parseInt(o.toString());
		if ((o = vals.get("tel")) != null)
			this.tel = o.toString();
		if ((o = vals.get("mail")) != null)
			this.mail = o.toString();
		if ((o = vals.get("language")) != null)
			this.language = Integer.parseInt(o.toString());
		if ((o = vals.get("description")) != null)
			this.description = o.toString();
		if ((o = vals.get("head")) != null)
			this.head = Integer.parseInt(o.toString());
		if ((o = vals.get("status")) != null)
			this.status = Integer.parseInt(o.toString());
		if ((o = vals.get("translator")) != null)
			this.translator = o.toString();
	}

	public UserInfo toUserInfo()
	{
		UserInfo u = new UserInfo();
		u.setId(this.id);
		u.setUsername(username);
		u.setNickname(nickname);
		u.setPassword(password);
		u.setSignature(signature);
		u.setCountry(country);
		u.setSex(sex);
		u.setAge(age);
		u.setTel(tel);
		u.setMail(mail);
		u.setLanguage(language);
		u.setDescription(description);
		u.setHead(head);
		u.setStatus(status);
		return u;
	}

	public long getId()
	{
		return this.id;
	}

	public String getUsername()
	{
		return this.username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public String getNickname()
	{
		return this.nickname;
	}

	public String getSignature()
	{
		return this.signature;
	}

	public int getCountry()
	{
		return this.country;
	}

	public int getSex()
	{
		return this.sex;
	}

	public int getAge()
	{
		return this.age;
	}

	public String getTel()
	{
		return this.tel;
	}

	public String getMail()
	{
		return this.mail;
	}

	public int getLanguage()
	{
		return this.language;
	}

	public String getDescription()
	{
		return this.description;
	}

	public int getHead()
	{
		return this.head;
	}

	public int getStatus()
	{
		return this.status;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	public void setCountry(int country)
	{
		this.country = country;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public void setMail(String mail)
	{
		this.mail = mail;
	}

	public void setLanguage(int language)
	{
		this.language = language;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setHead(int head)
	{
		this.head = head;
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