package firok.spring.mvci.internal;

import firok.spring.mvci.MVCIntrospective;

import javax.lang.model.element.TypeElement;
import java.util.regex.Pattern;

import static firok.spring.mvci.Constants.DEFAULT;
import static java.util.regex.Pattern.compile;

public class IntrospectContext
{
	public static final Pattern ptnBeanOrEntity = Pattern.compile("entity|bean");

	public IntrospectContext(final TypeElement element, final MVCIntrospective anno)
	{
		final String beanQualifiedName = element.getQualifiedName().toString();
		final int beanQualifiedNameLastDot = beanQualifiedName.lastIndexOf(".");

		beanNameFull = DEFAULT.equals(anno.beanNameFull()) ?
				beanQualifiedName.substring(beanQualifiedNameLastDot + 1):
				anno.beanNameFull();

		if(DEFAULT.equals(anno.beanNameShort()))
		{
			if(beanNameFull.startsWith("Bean"))
				beanNameShort = beanNameFull.substring(4);
			else if(beanNameFull.startsWith("Entity"))
				beanNameShort = beanNameFull.substring(6);
			else if(beanNameFull.endsWith("Bean"))
				beanNameShort = beanNameFull.substring(0,beanNameFull.length()-4);
			else if(beanNameFull.endsWith("Entity"))
				beanNameShort = beanNameFull.substring(0,beanNameFull.length()-6);
			else beanNameShort = beanNameFull;
		}
		else beanNameShort = anno.beanNameShort();

		beanPackage = DEFAULT.equals(anno.beanPackage()) ?
				beanQualifiedName.substring(0,beanQualifiedNameLastDot):
				anno.beanPackage();

		mapperName = DEFAULT.equals(anno.mapperName()) ? beanNameShort + "Mapper" : anno.mapperName();
		mapperPackage = DEFAULT.equals(anno.mapperPackage()) ?
			ptnBeanOrEntity.matcher(beanPackage).replaceFirst("mapper"):
			anno.mapperPackage();

		serviceName = DEFAULT.equals(anno.serviceName()) ? beanNameShort + "Service" : anno.serviceName();
		servicePackage = DEFAULT.equals(anno.servicePackage()) ?
			ptnBeanOrEntity.matcher(beanPackage).replaceFirst("service"):
			anno.servicePackage();

		serviceImplName = DEFAULT.equals(anno.serviceImplName()) ? beanNameShort + "ServiceImpl" : anno.serviceImplName();
		serviceImplPackage = DEFAULT.equals(anno.serviceImplPackage()) ?
			ptnBeanOrEntity.matcher(beanPackage).replaceFirst("service.impl"):
			anno.serviceImplPackage();

		controllerName = DEFAULT.equals(anno.controllerName()) ? beanNameShort + "Controller" : anno.controllerName();
		controllerPackage = DEFAULT.equals(anno.controllerPackage()) ?
			ptnBeanOrEntity.matcher(beanPackage).replaceFirst("controller"):
			anno.controllerPackage();
	}
	/**
	 * bean全名
	 * @implNote DemoBean -> DemoBean
	 * @implSpec ##BEAN_NAME_FULL##
	 */
	public String beanNameFull = "";

	private static final Pattern ptnBeanNameFull = compile("##BEAN_NAME_FULL##");

	/**
	 * bean短名
	 * @implNote DemoBean -> Demo
	 * @implSpec ##BEAN_NAME_SHORT##
	 */
	public String beanNameShort = "";

	private static final Pattern ptnBeanNameShort = compile("##BEAN_NAME_SHORT##");

	/**
	 * bean包
	 * @implNote firok.spring.bean
	 * @implSpec ##BEAN_PACKAGE##
	 */
	public String beanPackage = "";

	private static final Pattern ptnBeanPackage = compile("##BEAN_PACKAGE##");

	/**
	 * mapper包
	 * @implNote firok.spring.mapper
	 * @implSpec ##MAPPER_PACKAGE##
	 */
	public String mapperPackage = "";

	private static final Pattern ptnMapperPackage = compile("##MAPPER_PACKAGE##");

	/**
	 * service包
	 * @implNote firok.spring.service
	 * @implSpec ##SERVICE_PACKAGE##
	 */
	public String servicePackage = "";

	private static final Pattern ptnServicePackage = compile("##SERVICE_PACKAGE##");

	/**
	 * service impl包
	 * @implNote firok.spring.service.impl
	 * @implSpec ##SERVICE_IMPL_PACKAGE##
	 */
	public String serviceImplPackage = "";

	private static final Pattern ptnServiceImplPackage = compile("##SERVICE_IMPL_PACKAGE##");

	/**
	 * controller包
	 * @implNote firok.spring.controller
	 * @implSpec ##CONTROLLER_PACKAGE##
	 */
	public String controllerPackage = "";

	private static final Pattern ptnControllerPackage = compile("##CONTROLLER_PACKAGE##");

	/**
	 * mapper名称
	 * @implNote DemoBean -> DemoMapper
	 * @implSpec ##MAPPER_NAME##
	 */
	public String mapperName = "";

	private static final Pattern ptnMapperName = compile("##MAPPER_NAME##");

	/**
	 * service名称
	 * @implNote DemoBean -> DemoService
	 * @implSpec ##SERVICE_NAME##
	 */
	public String serviceName = "";

	private static final Pattern ptnServiceName = compile("##SERVICE_NAME##");

	/**
	 * service impl名称
	 * @implNote DemoBean -> DemoServiceImpl
	 * @implSpec ##SERVICE_IMPL_NAME##
	 */
	public String serviceImplName = "";

	private static final Pattern ptnServiceImplName = compile("##SERVICE_IMPL_NAME##");

	/**
	 * controller名称
	 * @implNote DemoBean -> DemoController
	 * @implSpec ##CONTROLLER_NAME##
	 */
	public String controllerName = "";

	private static final Pattern ptnControllerName = compile("##CONTROLLER_NAME##");

	private String pl(String origin,Pattern ptn,String replacement)
	{
		return ptn.matcher(origin).replaceAll(replacement);
	}
	public String pipeline(String raw)
	{
		raw = pl(raw,ptnBeanNameFull,beanNameFull);
		raw = pl(raw,ptnBeanNameShort,beanNameShort);
		raw = pl(raw,ptnMapperName,mapperName);
		raw = pl(raw,ptnServiceName,serviceName);
		raw = pl(raw,ptnServiceImplName,serviceImplName);
		raw = pl(raw,ptnControllerName,controllerName);

		raw = pl(raw,ptnBeanPackage,beanPackage);
		raw = pl(raw,ptnMapperPackage,mapperPackage);
		raw = pl(raw,ptnServicePackage,servicePackage);
		raw = pl(raw,ptnServiceImplPackage,serviceImplPackage);
		raw = pl(raw,ptnControllerPackage,controllerPackage);
		return raw;
	}
}
