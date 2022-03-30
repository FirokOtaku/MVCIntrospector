package firok.spring.mvci.internal;

import firok.spring.mvci.MVCIntrospectProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class RuntimeGenerate
{
	/**
	 * 生成所有运行时内容
	 */
	public static void startGenAll(
			List<BeanContext> listContext,
			MVCIntrospectProcessor processor
	) throws IOException
	{
		genOnePart("Bean", BeanContext::getBeanFullQualifiedName, listContext, processor);
		genOnePart("Mapper", BeanContext::getMapperFullQualifiedName, listContext, processor);
		genOnePart("Service", BeanContext::getServiceFullQualifiedName, listContext, processor);
		genOnePart("ServiceImpl", BeanContext::getServiceImplFullQualifiedName, listContext, processor);
		genOnePart("Controller", BeanContext::getControllerFullQualifiedName, listContext, processor);
	}

	private static void genOnePart(
			String type, Function<BeanContext, String> funGetFullQualifiedName,
			List<BeanContext> listContext,
			MVCIntrospectProcessor processor
	) throws IOException
	{
		if("Bean".equals(type)) startGenSomeNamesWithoutMappings(type, funGetFullQualifiedName, listContext, processor);
		else startGenSomeNamesWithMappings(type, funGetFullQualifiedName, listContext, processor);
		startGenSomeClasses(type, funGetFullQualifiedName, listContext, processor);
	}

	private static boolean should(BeanContext context, String type)
	{
		return switch (type)
		{
			case "Mapper" -> context.shouldMapper;
			case "Service" -> context.shouldService;
			case "ServiceImpl" -> context.shouldServiceImpl;
			case "Controller" -> context.shouldController;
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
}
