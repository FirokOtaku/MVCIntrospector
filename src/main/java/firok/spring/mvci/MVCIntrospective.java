package firok.spring.mvci;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static firok.spring.mvci.Constants.*;

/**
 * <p>标记在 JavaBean 上, 标明需要为这个类生成 MVC 结构.</p>
 * <p>
 *     <h2>{@code xxxName} 字段和 {@code xxxPackage} 字段</h2>
 *     <h3>使用 {@code Constants.PREFER_DEFAULT} 值</h3>
 *     如果没有上层配置, 此字段将使用默认模板生成;
 *     否则将会使用上层配置指定的值. <br>
 *
 *     <h3>使用 {@code Constants.DEFAULT} 值</h3>
 *     此字段将使用默认模板生成. <br>
 *
 *     <h3>使用自定义字符串</h3>
 *     提供的字符串将会作为生成模板. <br>
 *
 *     <br>
 *
 *     <h2>{@code generateXXX} 字段</h2>
 *     <h3>使用 {@code Constants.PREFER_TRUE} 或 {@code Constants.PREFER_FALSE} 值</h3>
 *     如果没有上层配置, 此字段将使用 {@code true} 或 {@code false} 值;
 *     否则将会使用上层配置指定的值. <br>
 * </p>
 * <p>marked on JavaBean class to indicate that we should generate MVC structure for it.</p>
 * <p>
 *     <h2>{@code xxxName} fields and {@code xxxPackage} fields</h2>
 *     <h3>use {@code Constants.PREFER_DEFAULT}</h3>
 *     if no other configs exist, this field will be generated from default template;
 *     otherwise, the template specified in the other config will be used.
 *
 *     <h3>use {@code Constants.DEFAULT}</h3>
 *     this field will be generated from default template. <br>
 *
 *     <h3>use customized string</h3>
 *     given value will be used as template. <br>
 *
 *     <br>
 *
 *     <h2>{@code generateXXX} field</h2>
 *     <h3>use {@code Constants.PREFER_TRUE} or {@code Constants.PREFER_FALSE}</h3>
 *     if no other configs exist, this field will use {@code true} or {@code false};
 *     otherwise, value specified in the other config will be used. <br>
 * </p>
 *
 * @since 1.0.0
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface MVCIntrospective
{

	/**
	 * <p>包含若干键值对数据, 生成结构时将会按顺序遍历此列表替换模板内容.</p>
	 * <p>you could provide some key-value-pairs for template replacements</p>
	 */
	Param[] extraParams() default {};

	/* == 实体信息 bean information == */

	/**
	 * <p>标明为此实体生成 MVC 结构时的目标包.</p>
	 * <p>indicate the target package when generating MVC structures for this bean.</p>
	 * <p>
	 *     <code>firok.spring.demo.bean.TestBean</code> → <code>firok.spring.demo</code>
	 * </p>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String basePackage() default PREFER_DEFAULT;

	/**
	 * <p>标明为此实体生成 MVC 结构时使用的短名称.</p>
	 * <p>indicate the short name to be used when generating MVC structures for this bean.</p>
	 * <p>
	 *     <code>firok.spring.demo.bean.TestBean</code> → <code>Test</code>
	 * </p>
	 */
	@AvailableValues({ DEFAULT, CUSTOM })
	String beanNameShort() default DEFAULT;

	/**
	 * <p>标明为此实体生成 MVC 结构时使用的全名.</p>
	 * <p>indicate the full name to be used when generating MVC structures for this bean.</p>
	 * <p>
	 *     <code>firok.spring.demo.bean.TestBean</code> → <code>TestBean</code>
	 * </p>
	 */
	@AvailableValues({ DEFAULT, CUSTOM })
	String beanNameFull() default DEFAULT;

	/**
	 * <p>是否生成 mapper.</p>
	 * <p>whether should we generate mapper for this bean.</p>
	 */
	@AvailableValues({ PREFER_TRUE, PREFER_FALSE, TRUE, FALSE })
	String generateMapper() default PREFER_TRUE;

	/**
	 * <p>是否生成 service.</p>
	 * <p>whether should we generate service for this bean.</p>
	 */
	@AvailableValues({ PREFER_TRUE, PREFER_FALSE, TRUE, FALSE })
	String generateService() default PREFER_TRUE;

	/**
	 * <p>是否生成 service impl.</p>
	 * <p>whether should we generate service impl for this bean.</p>
	 */
	@AvailableValues({ PREFER_TRUE, PREFER_FALSE, TRUE, FALSE })
	String generateServiceImpl() default PREFER_TRUE;

	/**
	 * <p>是否生成 controller.</p>
	 * <p>whether should we generate controller for this bean.</p>
	 */
	@AvailableValues({ PREFER_TRUE, PREFER_FALSE, TRUE, FALSE })
	String generateController() default PREFER_TRUE;

	/* == 结构名称 structure names == */

	/**
	 * <p>标明为此实体生成 mapper 时使用的名称模板.</p>
	 * <p>indicate the name template to be used when generating mapper for this bean.</p>
	 * <p>
	 *     <i>resources/mapper.name.txt</i>
	 *     <code>##BEAN_NAME_SHORT##Mapper</code> : <code>TestBean</code> → <code>TestMapper</code>
	 * </p>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateMapperName() default PREFER_DEFAULT;

	/**
	 * <p>标明为此实体生成 service 时使用的名称模板.</p>
	 * <p>indicate the name template to be used when generating service for this bean.</p>
	 * <p>
	 *     <i>resources/service.name.txt</i>
	 *     <code>##BEAN_NAME_SHORT##Service</code> : <code>TestBean</code> → <code>TestService</code>
	 * </p>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateServiceName() default DEFAULT;

	/**
	 * <p>标明为此实体生成 service impl 时使用的名称模板.</p>
	 * <p>indicate the name to be used when generating service impl for this bean.</p>
	 * <p>
	 *     <i>resources/service_impl.name.txt</i>
	 *     <code>##BEAN_NAME_SHORT##ServiceImpl</code> : <code>TestBean</code> → <code>TestServiceImpl</code>
	 * </p>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateServiceImplName() default PREFER_DEFAULT;

	/**
	 * <p>标明为此实体生成 controller 时使用的名称模板.</p>
	 * <p>indicate the name to be used when generating controller for this bean.</p>
	 * <p>
	 *     <i>resources/controller.name.txt</i>
	 *     <code>##BEAN_NAME_SHORT##Controller</code> : <code>TestBean</code> → <code>TestController</code>
	 * </p>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateControllerName() default PREFER_DEFAULT;

	/* == 结构位置 structure locations == */

	/**
	 * <p>标明为此实体生成 mapper 时的目标包位置.</p>
	 * <p>indicate the target package when generating mapper for this bean.</p>
	 *
	 * <i>resources/mapper.package.txt</i>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateMapperPackage() default PREFER_DEFAULT;

	/**
	 * <p>标明为此实体生成 mapper 时的目标包位置.</p>
	 * <p>indicate the target package when generating mapper for this bean.</p>
	 *
	 * <i>resources/service.package.txt</i>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateServicePackage() default PREFER_DEFAULT;

	/**
	 * <p>标明为此实体生成 service 时的目标包位置.</p>
	 * <p>indicate the target package when generating service for this bean.</p>
	 *
	 * <i>resources/service_impl.package.txt</i>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateServiceImplPackage() default PREFER_DEFAULT;

	/**
	 * <p>标明为此实体生成 controller 时的目标包位置.</p>
	 * <p>indicate the target package when generating controller for this bean.</p>
	 *
	 * <i>resources/controller.package.txt</i>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateControllerPackage() default PREFER_DEFAULT;

	/* == 结构模板 structure templates == */

	/**
	 * <p>标明为此实体生成 mapper 时使用的模板.</p>
	 * <p>indicate the template to be used when generating mapper for this bean.</p>
	 *
	 * <i>resources/mapper.java.txt</i>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateMapperContent() default PREFER_DEFAULT;

	/**
	 * <p>标明为此实体生成 service 时使用的模板.</p>
	 * <p>indicate the template to be used when generating service for this bean.</p>
	 *
	 * <i>resources/service.java.txt</i>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateServiceContent() default PREFER_DEFAULT;

	/**
	 * <p>标明为此实体生成 service impl 时使用的模板.</p>
	 * <p>indicate the template to be used when generating service impl for this bean.</p>
	 *
	 * <i>resources/service_impl.java.txt</i>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateServiceImplContent() default PREFER_DEFAULT;

	/**
	 * <p>标明为此实体生成 controller 时使用的模板.</p>
	 * <p>indicate the template to be used when generating controller for this bean.</p>
	 *
	 * <i>resources/controller.java.txt</i>
	 */
	@AvailableValues({ PREFER_DEFAULT, DEFAULT, CUSTOM })
	String templateControllerContent() default PREFER_DEFAULT;
}
