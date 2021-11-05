package firok.spring.mvci.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class RegexPipeline
{
	public static String pipelineAll(String content, Map<String, String> mapParams)
	{
		for(var entryParam : mapParams.entrySet())
		{
			String regexString = entryParam.getKey();
			String replacement = entryParam.getValue();

			Pattern regexPattern = getPattern(regexString);

			content = regexPattern.matcher(content).replaceAll(replacement);
		}
		return content;
	}
	public static String pipelineFirst(String content, Map<String, String> mapParams)
	{
		for(var entryParam : mapParams.entrySet())
		{
			String regexString = entryParam.getKey();
			String replacement = entryParam.getValue();

			Pattern regexPattern = getPattern(regexString);

			content = regexPattern.matcher(content).replaceFirst(replacement);
		}
		return content;
	}

	// 为正则创建缓存
	private static final Map<String, Pattern> mapCachedPattern = new ConcurrentHashMap<>();
	public static Pattern getPattern(String regex)
	{
		return mapCachedPattern.computeIfAbsent(regex, Pattern::compile);
	}
	public static void cleanPatterns()
	{
		mapCachedPattern.clear();
	}
}
