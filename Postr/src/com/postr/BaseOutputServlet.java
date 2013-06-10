
package com.postr;
import com.postr.DataTypes.Json;

@SuppressWarnings("serial")
public abstract class BaseOutputServlet extends BaseJSONServlet {


	@Override
	protected Json ProcessRequest(Json parameters) throws Exception
	{
		String method = parameters.getString("method");

		MethodTypes methodType = MethodTypes.valueOf(method);
		switch (methodType) {
		case VerifyPassword:
			return VerifyPassword(parameters);
		case SaveData:
			return SaveData(parameters);
		case RemoveData:
			return RemoveData(parameters);
		case UpdateData:
			return UpdateData(parameters);
		default:
			throw new Exception("No such method found: "+method);
		}

	}

	protected abstract Json UpdateData	(Json parameters) throws Exception;

	protected abstract Json RemoveData	(Json parameters) throws Exception;
	
	protected abstract Json VerifyPassword(Json parameters) throws Exception;
	
	protected abstract Json SaveData(Json parameters) throws Exception;
}
