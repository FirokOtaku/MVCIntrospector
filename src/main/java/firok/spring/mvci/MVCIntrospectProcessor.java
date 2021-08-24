package firok.spring.mvci;

import firok.spring.mvci.internal.IntrospectContext;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.Set;

import static firok.spring.mvci.Constants.DISABLE;

@SupportedAnnotationTypes("firok.spring.mvci.MVCIntrospective")
@SupportedSourceVersion(SourceVersion.RELEASE_16)
public class MVCIntrospectProcessor extends AbstractProcessor
{
	private Types typeUtils;
	private Elements elementUtils;
	private Filer filer;
	private Messager messager;
	private void printNote(Object obj)
	{
		messager.printMessage(Diagnostic.Kind.NOTE,"[MVCI] " + obj);
	}
	private void printWarning(Object obj)
	{
		messager.printMessage(Diagnostic.Kind.WARNING,"[MVCI] " + obj);
	}
	private void printError(Object obj)
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

	private void genJavaSource(String location,String content) throws Exception
	{
		JavaFileObject jfo = filer.createSourceFile(location);
		try(Writer jw = jfo.openWriter())
		{
			jw.write(content);
		}
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
	{
		Set<? extends Element> setElements = roundEnv.getElementsAnnotatedWith(MVCIntrospective.class);
		printNote("读取并处理 MVC 结构");
		for(Element ele : setElements)
		{
			if(ele instanceof TypeElement te)
			{
				var anno = ele.getAnnotation(MVCIntrospective.class);
				var context = new IntrospectContext(te,anno);

				try
				{
					String content; // content to generate
					String qn; // qualified name
					GEN_MAPPER:
					{
						if(DISABLE.equals(anno.mapperTemplate())) break GEN_MAPPER;
						content = context.pipeline(anno.mapperTemplate());
						qn = context.mapperPackage + "." + context.mapperName;
						genJavaSource(qn,content);
					}
					GEN_SERVICE:
					{
						if(DISABLE.equals(anno.serviceTemplate())) break GEN_SERVICE;
						content = context.pipeline(anno.serviceTemplate());
						qn = context.servicePackage + "." + context.serviceName;
						genJavaSource(qn,content);
					}
					GEN_SERVICE_IMPL:
					{
						if(DISABLE.equals(anno.serviceImplTemplate())) break GEN_SERVICE_IMPL;
						content = context.pipeline(anno.serviceImplTemplate());
						qn = context.serviceImplPackage + "." + context.serviceImplName;
						genJavaSource(qn,content);
					}
					GEN_CONTROLLER:
					{
						if(DISABLE.equals(anno.controllerTemplate())) break GEN_CONTROLLER;
						content = context.pipeline(anno.controllerTemplate());
						qn = context.controllerPackage + "." + context.controllerName;
						genJavaSource(qn,content);
					}
				}
				catch (Exception e)
				{
					printError("为 [%s] 生成结构失败: %s".formatted(context.beanNameFull,e.getLocalizedMessage()));
				}

				printNote("为 [%s] 生成结构成功".formatted(context.beanNameFull));
			}
		}

		return true;
	}
}
