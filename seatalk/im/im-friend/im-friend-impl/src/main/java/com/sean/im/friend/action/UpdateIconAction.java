package com.sean.im.friend.action;

import com.sean.im.friend.constant.Params;
import com.sean.im.friend.entity.IconEntity;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(description = "修改", authenticate = false, 
mustParams = { Params.iconId, Params.iconImgUrl, Params.iconUrl, Params.iconVisible })
public class UpdateIconAction extends Action
{	
	@Override
	public void execute(Session session) throws Exception
	{
		long iconId = session.getLongParameter(Params.iconId);
		
		EntityDao<IconEntity> dao = PersistContext.CTX.getEntityDao(IconEntity.class);
		IconEntity icon = dao.loadById(iconId);
		if (icon != null)
		{
			icon.setImgUrl(session.getParameter(Params.iconImgUrl));
			icon.setLink(session.getParameter(Params.iconUrl));
			icon.setVisible(session.getIntParameter(Params.iconVisible));
			dao.update(icon);
		}
		session.success();
	}
}
