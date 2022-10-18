package firok.spring.mvci.comment;

/**
 * @since 17.2.0
 */
@SuppressWarnings("all")
public final class CurrentBeanNames
{
	private CurrentBeanNames() throws InstantiationException { throw new InstantiationException("不允许初始化"); }



	/**
	 * <p>当前项目中由 MVCI 读取到的所有实体类完整限定名</p>
	 * <p>An array contains full qualified names of all bean-class marked with {@code @MVCIntrospective}</p>
	 *
	 * @implNote 此字段会在编译期填充内容 this field will be filled during compiling phase
	 */
	public static final String[] NAMES = new String[] {};
}
