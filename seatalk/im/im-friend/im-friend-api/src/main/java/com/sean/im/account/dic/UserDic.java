package com.sean.im.account.dic;

import com.sean.persist.dictionary.DictionaryConfig;
import com.sean.persist.dictionary.DictionaryKeyConfig;

@DictionaryConfig(description = "好友字典", keys = 
{
	@DictionaryKeyConfig(key = "username", description = "用户名"),
	@DictionaryKeyConfig(key = "nickname", description = "昵称"),
	@DictionaryKeyConfig(key = "head", description = "头像"),
	@DictionaryKeyConfig(key = "signature", description = "签名"),
	@DictionaryKeyConfig(key = "status", description = "状态")
})
public interface UserDic{}
