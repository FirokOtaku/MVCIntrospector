package firok.spring.mvci.internal;

public class DefaultMapperTemplate
{
	public static final String VALUE = """
			package ##MAPPER_PACKAGE##;
						
			import com.baomidou.mybatisplus.core.mapper.BaseMapper;
			import ##BEAN_PACKAGE##.##BEAN_NAME_FULL##;
			
			public interface ##MAPPER_NAME## extends BaseMapper<##BEAN_NAME_FULL##> { }
			""";
}
