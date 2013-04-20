
package com.postr;
import com.postr.DataTypes.Parameters;
import com.postr.DataTypes.StringResult;

@SuppressWarnings("serial")
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
		case RemoveData:
			return RemoveData(parameters);
		default:
			throw new Exception("No such method found: "+method);
		}

	}

	protected abstract StringResult RemoveData	(Parameters parameters) throws Exception;
	
	protected abstract StringResult VerifyPassword(Parameters parameters) throws Exception;
	
	protected abstract StringResult SaveData(Parameters parameters) throws Exception;
}
