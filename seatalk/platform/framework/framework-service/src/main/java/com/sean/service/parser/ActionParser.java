package com.sean.service.parser;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.sean.persist.core.Entity;
import com.sean.persist.core.PersistContext;
import com.sean.persist.dictionary.Dictionary;
import com.sean.persist.entity.DictionaryKeyEntity;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.core.InterceptorInvoker;
import com.sean.service.core.PermissionProvider;
import com.sean.service.core.Version;
import com.sean.service.entity.ActionEntity;
import com.sean.service.entity.ParameterEntity;
import com.sean.service.entity.ReturnParameterEntity;
import com.sean.service.entity.UseDicEntity;
import com.sean.service.spi.FrameworkSpi;
import com.sean.service.worker.ActionWorker;
import com.sean.service.worker.CrossDomainWorker;
import com.sean.service.worker.InterceptWorker;
import com.sean.service.worker.ParamCheckWorker;
import com.sean.service.worker.PermissionWorker;
import com.sean.service.worker.TransactionWorker;
import com.sean.service.worker.Worker;

/**
 * Action解析器
 * @author sean
 *
 */
public class ActionParser
{
	private FrameworkSpi userInterface;
	private InterceptorInvoker interceptorInvoker;
	private Map<String, Version> versions = new HashMap<String, Version>();

	public ActionParser(FrameworkSpi userInterface, InterceptorInvoker interceptorInvoker)
	{
		this.userInterface = userInterface;
		this.interceptorInvoker = interceptorInvoker;
	}

	public ActionEntity parse(Class<?> cls, Map<String, ParameterEntity> params, Map<String, ReturnParameterEntity> retparams) throws Exception
	{
		ActionConfig ac = cls.getAnnotation(ActionConfig.class);

		// 解析必填参数
		String[] mustPc = ac.mustParams();
		ParameterEntity[] mustParams = new ParameterEntity[mustPc.length];
		for (int i = 0; i < mustPc.length; i++)
		{
			ParameterEntity param = params.get(mustPc[i]);
			if (param == null)
			{
				throw new RuntimeException("the request parameter " + mustPc[i] + " defined in " + cls.getName() + " was not found");
			}
			else
			{
				mustParams[i] = param;
			}
		}

		// 解析可选参数
		String[] optionalPc = ac.optionalParams();
		ParameterEntity[] optionalParams = new ParameterEntity[optionalPc.length];
		for (int i = 0; i < optionalPc.length; i++)
		{
			ParameterEntity param = params.get(optionalPc[i]);
			if (param == null)
			{
				throw new RuntimeException("the request parameter " + optionalPc[i] + " defined in " + cls.getName() + " was not found");
			}
			else
			{
				optionalParams[i] = param;
			}
		}

		// 解析返回参数
		String[] rpc = ac.returnParams();
		ReturnParameterEntity[] rpe = new ReturnParameterEntity[rpc.length];
		for (int i = 0; i < rpc.length; i++)
		{
			ReturnParameterEntity param = retparams.get(rpc[i]);
			if (param == null)
			{
				throw new RuntimeException("the return parameter " + rpc[i] + " defined in " + cls.getName() + " was not found");
			}
			else
			{
				rpe[i] = param;
			}
		}

		// 开始创建工作流,完整流程为：crossdomain(must)->authenticate(optional)->intercept(must)->checkparam(must)->permission(optional)->transaction(optional)->action(must)
		Worker actionWorker = new ActionWorker(this.userInterface);
		Worker chain = actionWorker;
		// 如果需要事务
		if (ac.transaction())
		{
			Worker node = new TransactionWorker(chain);
			chain = node;
		}
		// 如果需要权限验证
		if (ac.permission() != PermissionProvider.None)
		{
			Worker node = new PermissionWorker(userInterface, chain);
			chain = node;
		}
		// 添加参数验证
		if (true)
		{
			Worker node = new ParamCheckWorker(chain);
			chain = node;
		}
		// 添加拦截器
		if (true)
		{
			Worker node = new InterceptWorker(interceptorInvoker, chain);
			chain = node;
		}
		// 如果需要认证
		if (ac.authenticate())
		{
		}
		// 添加跨域
		if (true)
		{
			Worker node = new CrossDomainWorker(chain);
			chain = node;
		}

		Version version = this.versions.get(ac.version().getName());
		if (version == null)
		{
			version = (Version) ac.version().newInstance();
			versions.put(ac.version().getName(), version);
		}

		ActionEntity ae = new ActionEntity(ac.transaction(), rpe, mustParams, optionalParams, ac.permission(), ac.authenticate(), ac.returnType(),
				cls, ac.description(), version, chain);

		// 检查action
		checkAction(ae);

		return ae;
	}

	/**
	 * 检查action
	 * @param ae
	 */
	private void checkAction(ActionEntity ae) throws Exception
	{
		ReturnParameterEntity[] params = ae.getReturnParams();
		for (int i = 0; i < params.length; i++)
		{
			ReturnParameterEntity param = params[i];
			// 如果返回参数是实体
			if (Entity.class.isAssignableFrom(param.getEntity()))
			{
				checkEntity(ae, param);
			}
		}
	}

	/**
	 * 检查实体
	 * @param ae
	 * @param param
	 */
	private void checkEntity(ActionEntity ae, ReturnParameterEntity param) throws Exception
	{
		Map<String, Class<?>> allFields = new HashMap<String, Class<?>>();

		// 先获取实体所有域
		Field[] fields = param.getEntity().getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			allFields.put(fields[i].getName(), fields[i].getType());
		}

		// 获取数据字典所有域
		UseDicEntity[] dics = param.getDics();
		for (int i = 0; i < dics.length; i++)
		{
			Dictionary dic = PersistContext.CTX.getDictionary(dics[i].getDic());
			if (dic == null)
			{
				throw new RuntimeException("the dictionary " + dics[i].getDic() + " was not found");
			}
			DictionaryKeyEntity[] keys = dic.getEntity().getKeys();
			for (int j = 0; j < keys.length; j++)
			{
				allFields.put(keys[j].getKey(), String.class);
			}
		}

		// 判断返回参数
		String[] paramFields = param.getFields();
		for (int i = 0; i < paramFields.length; i++)
		{
			if (allFields.get(paramFields[i]) == null)
			{
				throw new RuntimeException("the return parameter field " + paramFields[i] + " of action " + ae.getCls().getName()
						+ " was not found both in entity and dictionary");
			}
		}
	}
}
