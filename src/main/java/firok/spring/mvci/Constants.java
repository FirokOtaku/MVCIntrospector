package firok.spring.mvci;

import firok.spring.mvci.internal.DefaultControllerTemplate;
import firok.spring.mvci.internal.DefaultMapperTemplate;
import firok.spring.mvci.internal.DefaultServiceImplTemplate;
import firok.spring.mvci.internal.DefaultServiceTemplate;

public final class Constants
{
	public static final String DEFAULT_MAPPER_TEMPLATE = DefaultMapperTemplate.VALUE;

	public static final String DEFAULT_SERVICE_TEMPLATE = DefaultServiceTemplate.VALUE;

	public static final String DEFAULT_SERVICE_IMPL_TEMPLATE = DefaultServiceImplTemplate.VALUE;

	public static final String DEFAULT_CONTROLLER_TEMPLATE = DefaultControllerTemplate.VALUE;

	/**
	 * 如果标注为 DISABLE, 将不会生成指定结构
	 */
	public static final String DISABLE = "##DISABLE##";

	public static final String DEFAULT = "##DEFAULT##";
}
