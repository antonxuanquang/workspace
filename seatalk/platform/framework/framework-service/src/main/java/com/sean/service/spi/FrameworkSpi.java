package com.sean.service.spi;

import java.util.Map;

import com.sean.service.core.Session;
import com.sean.service.entity.ActionEntity;

/**
 * framework service provider interface 
 * @author sean
 */
public abstract class FrameworkSpi
{
	/**
	 * global exception handle
	 * @param session
	 * @param action
	 * @param e
	 */
	public abstract void exceptionHandle(Session session, ActionEntity action, Exception e);

	/**
	 * encode uid
	 * @param userId
	 * @return
	 */
	public abstract String encodeUid(long userId);

	/**
	 * decode uid, this method will be invoked when client request if the request action need permission check
	 * @param uid
	 * @return							all field in the encoded uid
	 */
	public abstract Map<String, String> decodeUid(String uid);

	/**
	 * check permission
	 * @param session					request session
	 * @param permissionId				permission id
	 * @return
	 */
	public abstract boolean checkPermission(Session session, int permissionId);

	/**
	 * execute before action run
	 * @param session							request session
	 * @param action							action entity
	 */
	public abstract void preAction(Session session, ActionEntity action);

	/**
	 * execute after action run finished
	 * @param session							request session
	 * @param action							action entity
	 * @param milliSeconds						the time that action spend					
	 */
	public abstract void afterAction(Session session, ActionEntity action, long millSeconds);
}
