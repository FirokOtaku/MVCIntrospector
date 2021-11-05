package firok.spring.mvci;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({})
public @interface Param
{
	/**
	 * 建议使用 "##" 将键包裹起来.
	 */
	String key();

	String value();
}
