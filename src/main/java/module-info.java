module firok.spring.mvci {
	requires lombok;
	requires java.compiler;
	requires spring.context;
	requires spring.boot.autoconfigure;
	opens firok.spring.mvci.internal to lombok, java.compiler;
	opens firok.spring.mvci.comment;
	opens firok.spring.mvci;
	exports firok.spring.mvci.comment;
	exports firok.spring.mvci;
}
