package com.sean.im.flock.action;

import com.sean.im.flock.constant.Params;
import com.sean.im.flock.entity.FlockCardEntity;
import com.sean.im.flock.service.FlockServiceImpl;
import com.sean.im.server.constant.P;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.ResourceConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "修改群名片", authenticate = false, 
mustParams = { P.loginerId, Params.flockId },
optionalParams = { P.name, P.tel, P.mail, P.description })
public class UpdateFlockCardAction extends Action
{	
	@ResourceConfig
	private FlockServiceImpl fsi;

	@Override
	public void execute(Session session) throws Exception
	{
		long loginerId = session.getLongParameter(P.loginerId);
		long flockId = session.getLongParameter(Params.flockId);
		String name = session.getParameter(P.name);
		String tel = session.getParameter(P.tel);
		String mail = session.getParameter(P.mail);
		String description = session.getParameter(P.description);

		FlockCardEntity fce = new FlockCardEntity();
		fce.setUserId(loginerId);
		fce.setFlockId(flockId);
		fce.setName(name);
		fce.setTel(tel);
		fce.setEmail(mail);
		fce.setDescription(description);
		fsi.updateFlockCard(fce);

		session.success();
	}
}
