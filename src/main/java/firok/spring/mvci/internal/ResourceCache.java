package firok.spring.mvci.internal;

import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceCache
{

	private static final Map<String, String> mapCachedResources = new ConcurrentHashMap<>();
	public static String getResourceString(String location)
	{
		var fullPath = "/firok/spring/mvci/" + location + ".txt";
		return mapCachedResources.computeIfAbsent(location, loc -> {
			var ret = new StringBuilder();
			try(var is = Objects.requireNonNull(
					BeanContext.class.getResourceAsStream(fullPath),
					"resource not found: " + fullPath
			);
			    var in = new Scanner(is)
			) {
				boolean hasNextLine;
				do
				{
					ret.append(in.nextLine());
					hasNextLine = in.hasNextLine();
					if(hasNextLine) ret.append('\n');
				}
				while (hasNextLine);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			return ret.toString();
		});
	}

}
