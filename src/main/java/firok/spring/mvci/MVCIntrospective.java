package firok.spring.mvci;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static firok.spring.mvci.Constants.*;

/**
 * 标记在 JavaBean 上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MVCIntrospective
{
	/* == 实体信息 == */

	String beanPackage() default DEFAULT;

	String beanNameFull() default DEFAULT;

	String beanNameShort() default DEFAULT;

	/* == 结构名称 == */

	String mapperName() default DEFAULT;

	String serviceName() default DEFAULT;

	String serviceImplName() default DEFAULT;

	String controllerName() default DEFAULT;

	/* == 结构模板 == */

	String mapperTemplate() default DEFAULT_MAPPER_TEMPLATE;

	String serviceTemplate() default DEFAULT_SERVICE_TEMPLATE;

	String serviceImplTemplate() default DEFAULT_SERVICE_IMPL_TEMPLATE;

	String controllerTemplate() default DEFAULT_CONTROLLER_TEMPLATE;

	/* == 结构位置 == */

	String mapperPackage() default DEFAULT;

	String servicePackage() default DEFAULT;

	String serviceImplPackage() default DEFAULT;

	String controllerPackage() default DEFAULT;
}
