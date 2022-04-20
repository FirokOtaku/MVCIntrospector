package firok.spring.mvci.internal;

import firok.spring.mvci.Constants;
import firok.spring.mvci.MVCIntrospectProcessor;
import firok.spring.mvci.MVCIntrospective;
import firok.spring.mvci.Param;
import lombok.SneakyThrows;

import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static firok.spring.mvci.Constants.*;

public class BeanContext
{
	final TypeElement eleBean;
	final List<MVCIntrospective> configs;
	final MVCIntrospectProcessor mvci;

	public Map<String, String> mapExtraParams = new HashMap<>();

//	String basePackage;
//	String beanNameShort;
//	String beanNameFull;

	public boolean shouldMapper;
	public boolean shouldService;
	public boolean shouldServiceImpl;
	public boolean shouldController;
	public String templateMapperName;
	public String templateServiceName;
	public String templateServiceImplName;
	public String templateControllerName;
	public String templateMapperPackage;
	public String templateServicePackage;
	public String templateServiceImplPackage;
	public String templateControllerPackage;
	public String templateMapper;
	public String templateService;
	public String templateServiceImpl;
	public String templateController;


	public BeanContext(List<MVCIntrospective> configs, TypeElement eleBean, MVCIntrospectProcessor mvci)
	{
		this.configs = configs;
		this.eleBean = eleBean;
		this.mvci = mvci;

		// 为当前实体创建参数上下文
		for(var config : configs)
			readExtraParams(config.extraParams());

		final String beanQualifiedName = eleBean.getQualifiedName().toString();
		final int beanQualifiedNameLastDot = beanQualifiedName.lastIndexOf(".");

		String beanNameFull = nonDefault(configs, MVCIntrospective::beanNameFull, ()-> beanQualifiedName.substring(beanQualifiedNameLastDot + 1));
		String beanNameShort;
		if(beanNameFull.startsWith("Bean"))
			beanNameShort = beanNameFull.substring(4);
		else if(beanNameFull.startsWith("Entity"))
			beanNameShort = beanNameFull.substring(6);
		else if(beanNameFull.endsWith("Bean"))
			beanNameShort = beanNameFull.substring(0,beanNameFull.length()-4);
		else if(beanNameFull.endsWith("Entity"))
			beanNameShort = beanNameFull.substring(0,beanNameFull.length()-6);
		else beanNameShort = beanNameFull;
		String beanPackage = beanQualifiedName.substring(0, beanQualifiedNameLastDot);

		mapExtraParams.put(BEAN_NAME_FULL, beanNameFull);
		mapExtraParams.put(BEAN_NAME_SHORT, beanNameShort);
		mapExtraParams.put(BEAN_PACKAGE, beanPackage);

		// 根据配置 处理各种参数
		shouldMapper = should(configs, MVCIntrospective::generateMapper);
		shouldService = should(configs, MVCIntrospective::generateService);
		shouldServiceImpl = should(configs, MVCIntrospective::generateServiceImpl);
		shouldController = should(configs, MVCIntrospective::generateController);

		// 根据配置 加载模板
		templateMapperName = templateOrResource(configs, MVCIntrospective::templateMapperName, "mapper.name");
		templateServiceName = templateOrResource(configs, MVCIntrospective::templateServiceName, "service.name");
		templateServiceImplName = templateOrResource(configs, MVCIntrospective::templateServiceImplName, "service_impl.name");
		templateControllerName = templateOrResource(configs, MVCIntrospective::templateControllerName, "controller.name");

		templateMapperPackage = templateOrResource(configs, MVCIntrospective::templateMapperPackage, "mapper.package");
		templateServicePackage = templateOrResource(configs, MVCIntrospective::templateServicePackage, "service.package");
		templateServiceImplPackage = templateOrResource(configs, MVCIntrospective::templateServiceImplPackage, "service_impl.package");
		templateControllerPackage = templateOrResource(configs, MVCIntrospective::templateControllerPackage, "controller.package");

		templateMapper = shouldMapper ? templateOrResource(configs, MVCIntrospective::templateMapperContent, "mapper.java") : null;
		templateService = shouldService ? templateOrResource(configs, MVCIntrospective::templateServiceContent, "service.java") : null;
		templateServiceImpl = shouldServiceImpl ? templateOrResource(configs, MVCIntrospective::templateServiceImplContent, "service_impl.java") : null;
		templateController = shouldController ? templateOrResource(configs, MVCIntrospective::templateControllerContent, "controller.java") : null;

		single(MAPPER_NAME, templateMapperName);
		single(SERVICE_NAME, templateServiceName);
		single(SERVICE_IMPL_NAME, templateServiceImplName);
		single(CONTROLLER_NAME, templateControllerName);

		single(MAPPER_PACKAGE, templateMapperPackage, paramMapper);
		single(SERVICE_PACKAGE, templateServicePackage, paramService);
		single(SERVICE_IMPL_PACKAGE, templateServiceImplPackage, paramServiceImpl);
		single(CONTROLLER_PACKAGE, templateControllerPackage, paramController);
	}



	private void readExtraParams(Param[] customValues)
	{
		for(var customValue : customValues)
		{
			this.mapExtraParams.putIfAbsent(customValue.key(), customValue.value());
		}
	}

	/**
	 * 根据配置注解判断所需字段是否为真
	 */
	private static boolean should(List<MVCIntrospective> configs, Function<MVCIntrospective, String> mapper)
	{
		boolean ret = false;
		for(var config : configs)
		{
			String value = mapper.apply(config);
			switch (value)
			{
				case TRUE: return true;
				case FALSE: return false;
				case PREFER_TRUE:
					ret = true;
					break;
				case PREFER_FALSE:
					ret = false;
					break;
			}
		}
		return ret;
	}
	/**
	 * 根据配置注解判断所需字段模板值
	 */
	private static String templateOrResource(
			List<MVCIntrospective> configs,
			Function<MVCIntrospective, String> mapper,
			String defaultLocation)
	{
		FOR: for(var config : configs)
		{
			String value = mapper.apply(config);
			switch (value)
			{
				case PREFER_DEFAULT:
					break;
				case DEFAULT:
					break FOR;
				default:
					return value;
			}
		}
		return ResourceCache.getResourceString(defaultLocation);
	}

	private static String nonDefault(List<MVCIntrospective> configs, Function<MVCIntrospective, String> mapper, Supplier<String> defaultValue)
	{
		FOR: for(var config : configs)
		{
			String value = mapper.apply(config);
			switch (value)
			{
				case PREFER_DEFAULT:
					break;
				case DEFAULT:
					break FOR;
				default:
					return value;
			}
		}
		return defaultValue.get();
	}

	private void single(String valueKey, String valueTemplate, Map<String, String> extras)
	{
		String value = RegexPipeline.pipelineAll(valueTemplate, mapExtraParams);
		if(extras != null) value = RegexPipeline.pipelineAll(value, extras);
		mapExtraParams.put(valueKey, value);
	}
	private void single(String valueKey, String valueTemplate)
	{
		mapExtraParams.put(valueKey, RegexPipeline.pipelineAll(valueTemplate, mapExtraParams));
	}
	private void write(String valueKeyLocation, String valueKeyName, String templateContent) throws Exception
	{
		String valueLocation = mapExtraParams.get(valueKeyLocation);
		String valueName = mapExtraParams.get(valueKeyName);
		String valueContent = RegexPipeline.pipelineAll(templateContent, mapExtraParams);
		try(var writer = mvci.createSourceFileWrite(valueLocation + "." + valueName))
		{
			writer.write(valueContent);
			writer.flush();
		}
	}
	private static final Map<String, String> paramMapper = new HashMap<>();
	private static final Map<String, String> paramService = new HashMap<>();
	private static final Map<String, String> paramServiceImpl = new HashMap<>();
	private static final Map<String, String> paramController = new HashMap<>();
	static
	{
		paramMapper.put("\\.entity|\\.bean",".mapper");
		paramService.put("\\.entity|\\.bean",".service");
		paramServiceImpl.put("\\.entity|\\.bean",".service_impl");
		paramController.put("\\.entity|\\.bean",".controller");
	}

	@SneakyThrows
	public void generate()
	{
		// 生成代码文件
		if(shouldMapper) write(MAPPER_PACKAGE, MAPPER_NAME, templateMapper);
		if(shouldService) write(SERVICE_PACKAGE, SERVICE_NAME, templateService);
		if(shouldServiceImpl) write(SERVICE_IMPL_PACKAGE, SERVICE_IMPL_NAME, templateServiceImpl);
		if(shouldController) write(CONTROLLER_PACKAGE, CONTROLLER_NAME, templateController);
	}

	private String cacheBean, cacheMapper, cacheService, cacheServiceImpl, cacheController;
	public String getBeanFullQualifiedName()
	{
		return cacheBean != null ?
				cacheBean :
				(cacheBean = mapExtraParams.get(BEAN_PACKAGE) + "." + mapExtraParams.get(BEAN_NAME_FULL));
	}
	public String getMapperFullQualifiedName()
	{
		return cacheMapper != null ?
				cacheMapper :
				(cacheMapper = mapExtraParams.get(MAPPER_PACKAGE) + "." + mapExtraParams.get(MAPPER_NAME));
	}
	public String getServiceFullQualifiedName()
	{
		return cacheService != null ?
				cacheService :
				(cacheService = mapExtraParams.get(SERVICE_PACKAGE) + "." + mapExtraParams.get(SERVICE_NAME));
	}
	public String getServiceImplFullQualifiedName()
	{
		return cacheServiceImpl != null ? cacheServiceImpl :
				(cacheServiceImpl = mapExtraParams.get(SERVICE_IMPL_PACKAGE) + "." + mapExtraParams.get(SERVICE_IMPL_NAME));
	}
	public String getControllerFullQualifiedName()
	{
		return cacheController != null ? cacheController :
				(cacheController = mapExtraParams.get(CONTROLLER_PACKAGE) + "." + mapExtraParams.get(CONTROLLER_NAME));
	}
}
