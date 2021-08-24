package firok.spring.mvci.internal;

public class DefaultServiceImplTemplate
{
	public static final String VALUE = """
			package ##SERVICE_IMPL_PACKAGE##;
			   
			import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
			import ##MAPPER_PACKAGE##.##MAPPER_NAME##;
			import ##SERVICE_PACKAGE##.##SERVICE_NAME##;
			import ##BEAN_PACKAGE##.##BEAN_NAME_FULL##;
			import org.springframework.stereotype.Service;
			   
			@Service
			public class ##SERVICE_IMPL_NAME##
			extends ServiceImpl<##MAPPER_NAME##, ##BEAN_NAME_FULL##> implements ##SERVICE_NAME##
			{ }
			""";
}
