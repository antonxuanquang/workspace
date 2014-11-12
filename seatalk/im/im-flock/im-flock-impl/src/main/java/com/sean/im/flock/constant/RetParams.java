package com.sean.im.flock.constant;

import com.sean.im.account.dic.UserDic;
import com.sean.im.flock.entity.FlockCardEntity;
import com.sean.im.flock.entity.FlockEntity;
import com.sean.im.flock.entity.FlockMemberEntity;
import com.sean.service.annotation.ReturnParameterConfig;
import com.sean.service.annotation.ReturnParameterProviderConfig;
import com.sean.service.annotation.UseDicConfig;
import com.sean.service.enums.Format;

@ReturnParameterProviderConfig(description = "return parameter list of flock module")
public class RetParams
{	
	@ReturnParameterConfig(description = "flock ID", 
	format = Format.Numeric)
	public static final String flockId = "flockId";
	
	@ReturnParameterConfig(description = "flock member ID", 
	format = Format.Numeric)
	public static final String flockMemberId = "flockMemberId";
	
	@ReturnParameterConfig(description = "flock brief infomation", 
	format = Format.EntityList, entity = FlockEntity.class, 
	fields = { "id", "name", "signature", "creater" })
	public static final String flockList = "flockList";
	
	@ReturnParameterConfig(description = "flock detail infomation", 
	format = Format.Entity, entity = FlockEntity.class,
	fields = { "id", "name", "signature", "description", "createTime", "creater" })
	public static final String flockFull = "flockFull";
	
	@ReturnParameterConfig(description = "flock brief infomation", 
	format = Format.Entity, entity = FlockEntity.class,
	fields = { "id", "name", "signature", "creater" })
	public static final String flockBrief = "flockBrief";
	
	@ReturnParameterConfig(description = "infomation of flock creater",
	format = Format.Entity, entity = FlockEntity.class, 
	fields = { "creater", "username", "nickname" },
	dics = 
	{
		@UseDicConfig(dic = UserDic.class, field = "creater")
	})
	public static final String flockCreater = "flockCreater";
	
	@ReturnParameterConfig(description = "flock member list",
	format = Format.EntityList, entity = FlockMemberEntity.class, 
	fields = { "id", "userId", "joinTime", "isAdmin", "head", "username", "nickname", "status" },
	dics = 
	{
		@UseDicConfig(dic = UserDic.class, field = "userId")
	})
	public static final String flockMemberList = "flockMemberList";
	
	@ReturnParameterConfig(description = "flock card",
	format = Format.Entity, entity = FlockCardEntity.class, 
	fields = { "name", "tel", "email", "description" })
	public static final String flockCard = "flockCard";
}
