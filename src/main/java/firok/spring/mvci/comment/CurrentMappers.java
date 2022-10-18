package firok.spring.mvci.comment;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

/**
 * 当前项目中所有的 mapper
 * @implNote 此类会在编译期填充内容 this class will be filled during compiling phase
 * @since 17.6.0
 * */
@Configuration
@ConditionalOnExpression("${firok.spring.mvci.runtime.enable-mapper-config:false}")
public class CurrentMappers
{
	/**
	 * <p>获取某个实体类对应的 mapper 实例</p>
	 * <p>Get mapper instance of a bean</p>
	 *
	 * @param fullQualifiedBeanName 类完整限定名
	 * @return mapper 实例
	 * @implNote 此方法会在编译期填充内容 this method will be filled during compiling phase
	 */
	public Object getByFullQualifiedBeanName(String fullQualifiedBeanName)
	{
		return switch (fullQualifiedBeanName)
				{
					default -> null;
				};
	}


	/**
	 * <p>获取所有 mapper 实例</p>
	 * <p>Get all mapper instances of a bean</p>
	 *
	 * @return mapper 实例
	 * @implNote 此方法会在编译期填充内容 this method will be filled during compiling phase
	 */
	public Object[] getAllInstances()
	{
		return new Object[] {
	    };
	}
}
