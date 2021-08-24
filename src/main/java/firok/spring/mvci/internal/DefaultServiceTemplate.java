package firok.spring.mvci.internal;

public class DefaultServiceTemplate
{
	public static final String VALUE = """
			package ##SERVICE_PACKAGE##;
			   
			import com.baomidou.mybatisplus.extension.service.IService;
			import ##BEAN_PACKAGE##.##BEAN_NAME_FULL##;
			
			public interface ##SERVICE_NAME## extends IService<##BEAN_NAME_FULL##> { }
			""";
}
