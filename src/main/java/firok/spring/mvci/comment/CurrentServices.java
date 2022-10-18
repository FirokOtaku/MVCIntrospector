package firok.spring.mvci.comment;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

/**
 * 当前项目中所有的 service
 * @implNote 此类会在编译期填充内容 this class will be filled during compiling phase
 * @since 17.6.0
 * */
@Configuration
@ConditionalOnExpression("${firok.spring.mvci.runtime.enable-service-config:false}")
public class CurrentServices
{
	/**
	 * <p>获取某个实体类对应的 service 实例</p>
	 * <p>Get service instance of a bean</p>
	 *
	 * @param fullQualifiedBeanName 类完整限定名
	 * @return service 实例
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
	 * <p>获取所有 service 实例</p>
	 * <p>Get all service instances of a bean</p>
	 *
	 * @return service 实例
	 * @implNote 此方法会在编译期填充内容 this method will be filled during compiling phase
	 */
	public Object[] getAllInstances()
	{
		return new Object[] {
		};
	}
}
