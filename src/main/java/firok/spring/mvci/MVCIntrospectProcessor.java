package firok.spring.mvci;

import firok.spring.mvci.internal.BeanContext;
import firok.spring.mvci.internal.RuntimeGenerate;
import lombok.SneakyThrows;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@SupportedAnnotationTypes({"firok.spring.mvci.MVCIntrospective"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MVCIntrospectProcessor extends AbstractProcessor
{
	private Types typeUtils;
	private Elements elementUtils;
	private Filer filer;
	private Messager messager;
	public void printNote(Object obj)
	{
		messager.printMessage(Diagnostic.Kind.NOTE,"[MVCI] " + obj);
	}
	public void printWarning(Object obj)
	{
		messager.printMessage(Diagnostic.Kind.WARNING,"[MVCI] " + obj);
	}
	public void printError(Object obj)
	{
		messager.printMessage(Diagnostic.Kind.ERROR,"[MVCI] " + obj);
	}

	public MVCIntrospectProcessor()
	{ }

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		typeUtils = processingEnv.getTypeUtils();
		elementUtils = processingEnv.getElementUtils();
		filer = processingEnv.getFiler();
		messager = processingEnv.getMessager();

		messager.printMessage(Diagnostic.Kind.NOTE,"MVC Introspector ADT 初始化完成");
	}

	/**
	 * 省得出问题 还是加上个锁吧
	 */
	private static final Object LOCK_JFO_API = new Object();
	public Writer createSourceFileWrite(String location) throws IOException
	{
		JavaFileObject jfo;
		synchronized (LOCK_JFO_API) { jfo = filer.createSourceFile(location); return jfo.openWriter(); }

	}

	@SuppressWarnings("ReflectionForUnavailableAnnotation")
	@SneakyThrows
	private void checkAnnotation(Annotation anno)
	{
		var classAnno = anno.getClass();
		for(var field : classAnno.getDeclaredMethods()) // 注解字段 = 方法
		{
//			printNote("field:"+field.getName());
			AvailableValues annoAVs;
			synchronized (LOCK_REFLECT_API) { annoAVs = field.getAnnotation(AvailableValues.class); }
			if(annoAVs == null) {
//				printNote("continue;");
				continue;
			}

			Object fieldValue = field.invoke(anno);
			assert fieldValue != null;

			boolean passCheck = false;
			for(var av : annoAVs.value())
			{
				if(Constants.CUSTOM.equals(av) || Objects.equals(av, fieldValue))
				{
					passCheck = true;
					break;
				}
			}

			if(!passCheck)
			{
				printError("错误的值[%s]于[%s]注解的[%s]字段".formatted(
						fieldValue,
						classAnno.getSimpleName(),
						field.getName()
				));
			}
		}
	}

	/**
	 * JDK的反射api看起来不是线程安全的, 加一个全局锁省得炸裂
	 */
	public static final Object LOCK_REFLECT_API = new Object();

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
	{
		try
		{
			var setAnno = roundEnv.getElementsAnnotatedWith(MVCIntrospective.class);
			printNote("本轮找到 %d 个 MVCI 注解".formatted(setAnno.size()));
			if(setAnno.isEmpty()) return true;

			// 所有生成的实体数据
			var listContext = new Vector<BeanContext>();

			setAnno.stream().parallel().forEach(ele -> {
				if(ele instanceof TypeElement eleBean)
				{
					List<MVCIntrospective> configs = new ArrayList<>();

					MVCIntrospective annoBean;
					synchronized (LOCK_REFLECT_API) { annoBean = eleBean.getAnnotation(MVCIntrospective.class); }
					checkAnnotation(annoBean);
					configs.add(annoBean);

					// 读取相关的配置

					final String beanFullName = eleBean.getQualifiedName().toString(); // a.b.c.d.E
					String beanPackageName = beanFullName.substring(0, beanFullName.lastIndexOf('.')); // a.b.c.d
					if(beanPackageName.lastIndexOf('.') > 0) do
					{
						// 寻找配置注解
						PackageElement elePackage;
						synchronized (LOCK_REFLECT_API) { elePackage = elementUtils.getPackageElement(beanPackageName); }

						if(elePackage != null)
						{
							MVCIntrospective annoConfig;
							synchronized (LOCK_REFLECT_API) { annoConfig = elePackage.getAnnotation(MVCIntrospective.class); }

							if(annoConfig != null)
							{
								checkAnnotation(annoConfig);
								configs.add(annoConfig);
							}
						}

						// 准备寻找上级package
						final int indexLastDot = beanPackageName.lastIndexOf('.');
						if(indexLastDot > 0)
							beanPackageName = beanPackageName.substring(0, indexLastDot);
						else // 当前package限定名已经是根package了
							beanPackageName = null;
					}
					while(beanPackageName != null);

					// 此时, 列表内的配置是由近而远
					// 在根据配置调整生成时, 前面的配置会覆盖后面的配置

					// 初始化上下文
					var context = new BeanContext(configs, eleBean, this);

					// 开始生成
					context.generate();
					listContext.add(context);
				}
			});

			// 根据实体数据生成runtime类
			printNote("开始生成运行时内容...");
			RuntimeGenerate.startGenAll(listContext, this);

			printNote("完成生成");
			printWarning("本次共为 %d 个实体生成结构: [%s]".formatted(
					listContext.size(),
					listContext.stream().map(BeanContext::getBeanFullQualifiedName).collect(Collectors.toList())
			));
		}
		catch (Exception e)
		{
			printError(e);
		}

		return true;
	}

}
