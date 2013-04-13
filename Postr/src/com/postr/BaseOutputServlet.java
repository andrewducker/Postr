
package com.postr;
import com.postr.DataTypes.Parameters;
import com.postr.DataTypes.StringResult;

public abstract class BaseOutputServlet extends BaseJSONServlet {


	@Override
	protected StringResult ProcessRequest(Parameters parameters) throws Exception
	{
		String method = parameters.getStringParameter("method");

		MethodTypes methodType = MethodTypes.valueOf(method);
		switch (methodType) {
		case VerifyPassword:
			return VerifyPassword(parameters);
		case SaveData:
			return SaveData(parameters);
		default:
			throw new Exception("No such method found: "+method);
		}

	}

	protected abstract StringResult VerifyPassword(Parameters parameters) throws Exception;
	
	protected abstract StringResult SaveData(Parameters parameters) throws Exception;
}
