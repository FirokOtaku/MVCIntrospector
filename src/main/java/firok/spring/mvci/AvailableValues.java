package firok.spring.mvci;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     标注于注解字段, 标明此字段可接受的值.
 * </p>
 * <p>
 *     marked on annotation fields to indicate what values could be taken.
 * </p>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface AvailableValues
{
	String[] value();
}
