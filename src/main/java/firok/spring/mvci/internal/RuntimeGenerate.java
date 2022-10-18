package firok.spring.mvci.internal;

import firok.spring.mvci.Constants;
import firok.spring.mvci.MVCIntrospectProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class RuntimeGenerate
{
	private static final String TYPE_BEAN = "Bean";
	private static final String TYPE_MAPPER = "Mapper";
	private static final String TYPE_SERVICE = "Service";
	private static final String TYPE_SERVICE_IMPL = "ServiceImpl";
	private static final String TYPE_CONTROLLER = "Controller";
	/**
	 * 生成所有运行时内容
	 */
	public static void startGenAll(
			List<BeanContext> listContext,
			MVCIntrospectProcessor processor
	) throws IOException
	{
		genOnePart(TYPE_BEAN, BeanContext::getBeanFullQualifiedName, listContext, processor);
		genOnePart(TYPE_MAPPER, BeanContext::getMapperFullQualifiedName, listContext, processor);
		genOnePart(TYPE_SERVICE, BeanContext::getServiceFullQualifiedName, listContext, processor);
		genOnePart(TYPE_SERVICE_IMPL, BeanContext::getServiceImplFullQualifiedName, listContext, processor);
		genOnePart(TYPE_CONTROLLER, BeanContext::getControllerFullQualifiedName, listContext, processor);
	}

	private static void genOnePart(
			String type, Function<BeanContext, String> funGetFullQualifiedName,
			List<BeanContext> listContext,
			MVCIntrospectProcessor processor
	) throws IOException
	{
		if(TYPE_BEAN.equals(type))
			startGenSomeNamesWithoutMappings(type, funGetFullQualifiedName, listContext, processor);
		else
		{
			startGenSomeNamesWithMappings(type, funGetFullQualifiedName, listContext, processor);
			startGenSomeInstances(type, funGetFullQualifiedName, listContext, processor);
		}
		startGenSomeClasses(type, funGetFullQualifiedName, listContext, processor);
	}

	private static boolean should(BeanContext context, String type)
	{
		return switch (type)
		{
			case TYPE_MAPPER -> context.shouldMapper;
			case TYPE_SERVICE -> context.shouldService;
			case TYPE_SERVICE_IMPL -> context.shouldServiceImpl;
			case TYPE_CONTROLLER -> context.shouldController;
			default -> true;
		};
	}

	private static void startGenSomeNamesWithoutMappings(
			String type, Function<BeanContext, String> funGetFullQualifiedName,
			List<BeanContext> listContext,
			MVCIntrospectProcessor processor
	) throws IOException
	{
		final var template = ResourceCache.getResourceString("current_names_without_mapping");
		final var bufferName = new StringBuilder();
		for(var objContext : listContext)
		{
			if(!should(objContext, type)) continue;

			final var targetRef = funGetFullQualifiedName.apply(objContext);
			bufferName.append("\n\"").append(targetRef).append("\",");
		}
		final var paramBeanName = new HashMap<String, String>();
		paramBeanName.put("##TYPE_NAME##", type);
		paramBeanName.put("##NAMES_NAME##", bufferName.toString());

		try(var writer = processor.createSourceFileWrite("firok.spring.mvci.runtime.Current" + type + "Names"))
		{
			writer.write(RegexPipeline.pipelineAll(template, paramBeanName));
		}
	}
	private static void startGenSomeNamesWithMappings(
			String type, Function<BeanContext, String> funGetFullQualifiedName,
			List<BeanContext> listContext,
			MVCIntrospectProcessor processor
	) throws IOException
	{
		final var template = ResourceCache.getResourceString("current_names_without_mapping");
		final var bufferName = new StringBuilder();
		final var bufferMapping = new StringBuilder();
		for(var objContext : listContext)
		{
			if(!should(objContext, type)) continue;

			final var beanRef = objContext.getBeanFullQualifiedName();
			final var targetRef = funGetFullQualifiedName.apply(objContext);
			bufferName.append("\n\"").append(targetRef).append("\",");
			bufferMapping.append("\ncase \"").append(beanRef).append("\" -> \"").append(targetRef).append("\";");
		}
		final var paramBeanName = new HashMap<String, String>();
		paramBeanName.put("##TYPE_NAME##", type);
		paramBeanName.put("##NAMES_NAME##", bufferName.toString());
		paramBeanName.put("##MAPPINGS_NAME##", bufferMapping.toString());

		try(var writer = processor.createSourceFileWrite("firok.spring.mvci.runtime.Current" + type + "Names"))
		{
			writer.write(RegexPipeline.pipelineAll(template, paramBeanName));
		}
	}
	private static void startGenSomeClasses(
			String type, Function<BeanContext, String> funGetFullQualifiedName,
			List<BeanContext> listContext,
			MVCIntrospectProcessor processor
	) throws IOException
	{
		final var template = ResourceCache.getResourceString("current_classes");
		final var bufferClass = new StringBuilder();
		final var bufferSwitch = new StringBuilder();
		for(final var objContext : listContext)
		{
			if(!should(objContext, type)) continue;

			final var beanRef = objContext.getBeanFullQualifiedName();
			final var targetRef = funGetFullQualifiedName.apply(objContext);
			bufferClass.append("\n").append(targetRef).append(".class,");
			bufferSwitch.append("\ncase \"").append(beanRef).append("\" -> ").append(targetRef).append(".class;");
		}
		final var paramBeanClass = new HashMap<String, String>();
		paramBeanClass.put("##TYPE_CLASS##", type);
		paramBeanClass.put("##CLASSES_CLASS##", bufferClass.toString());
		paramBeanClass.put("##SWITCHES_CLASS##", bufferSwitch.toString());

		try(var writer = processor.createSourceFileWrite("firok.spring.mvci.runtime.Current" + type + "Classes"))
		{
			writer.write(RegexPipeline.pipelineAll(template, paramBeanClass));
		}
	}
	private static void startGenSomeInstances(
			String type, Function<BeanContext, String> funGetFullQualifiedName,
			List<BeanContext> listContext,
			MVCIntrospectProcessor processor
	) throws IOException
	{
		final var template = ResourceCache.getResourceString("current_instances");
		final var bufferFieldInstances = new StringBuilder();
		final var bufferSwitchInstances = new StringBuilder();
		final var bufferArrayInstances = new StringBuilder();
		for(var objContext : listContext)
		{
			if(!should(objContext, type)) continue;

			final var beanRef = objContext.getBeanFullQualifiedName();
			final var targetRef = funGetFullQualifiedName.apply(objContext);
			final var targetName = switch (type) {
				case TYPE_MAPPER -> objContext.mapExtraParams.get(Constants.MAPPER_NAME);
				case TYPE_SERVICE -> objContext.mapExtraParams.get(Constants.SERVICE_NAME);
				case TYPE_SERVICE_IMPL -> objContext.mapExtraParams.get(Constants.SERVICE_IMPL_NAME);
				case TYPE_CONTROLLER -> objContext.mapExtraParams.get(Constants.CONTROLLER_NAME);
				default -> null;
			};

			bufferFieldInstances.append("@Autowired\npublic ").append(targetRef).append(" ").append(targetName).append(";\n");
			bufferSwitchInstances.append("case \"").append(beanRef).append("\" -> ").append(targetName).append(";\n");
			bufferArrayInstances.append(targetName).append(",\n");
		}
		final var paramBeanInstances = new HashMap<String, String>();
		paramBeanInstances.put("##TYPE_CLASS##", type);
		paramBeanInstances.put("##CONFIG_KEY##", switch (type) {
			case TYPE_CONTROLLER -> "enable-controller-config";
			case TYPE_MAPPER -> "enable-mapper-config";
			case TYPE_SERVICE -> "enable-service-config";
			case TYPE_SERVICE_IMPL -> "enable-service-impl-config";
			default -> null;
		});
		paramBeanInstances.put("##SWITCHES_INSTANCES##", bufferSwitchInstances.toString());
		paramBeanInstances.put("##FIELD_INSTANCES##", bufferFieldInstances.toString());
		paramBeanInstances.put("##ARRAY_INSTANCES##", bufferArrayInstances.toString());
		try(var writer = processor.createSourceFileWrite("firok.spring.mvci.runtime.Current" + type + "s"))
		{
			writer.write(RegexPipeline.pipelineAll(template, paramBeanInstances));
		}
	}
}
