package firok.spring.mvci.comment;

/**
 * @since 17.2.0
 */
@SuppressWarnings("all")
public final class CurrentBeanClasses
{
	private CurrentBeanClasses() throws InstantiationException { throw new InstantiationException("不允许初始化"); }



	/**
	 * <p>当前项目中所有标注了 {@code @MVCIntrospective} 的实体类 Class 对象</p>
	 * <p>An array contains Class instances of all bean-class marked with {@code @MVCIntrospective}</p>
	 *
	 * @implNote 此字段会在编译期填充内容 this field will be filled during compiling phase
	 */
	public static final Class<?>[] CLASSES = new Class<?>[] {};



	/**
	 * <p>如果不希望使用 {@code Class.forName(xxx)} 获取实体类 Class 对象, 可以使用此方法.</p>
	 * <p>This method is available for the case to get Class instances without {@code Class.forName(xxx)}</p>
	 *
	 * @param fullQualifiedBeanName 类完整限定名
	 * @return 类 Class 对象
	 * @implNote 此方法会在编译期填充内容 this method will be filled during compiling phase
	 */
	public static Class<?> getByFullQualifiedBeanName(String fullQualifiedBeanName)
	{
		return switch (fullQualifiedBeanName)
		{
			default -> null;
		};
	}
}
