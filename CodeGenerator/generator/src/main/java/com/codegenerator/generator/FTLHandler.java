package com.codegenerator.generator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FTLHandler {
	private Configuration templateConfiguration;

	private FTLHandler() {
		configurationSet();
	}

	public static FTLHandler getInstance() {
		return LazyHolder.INSTANCE;
	}

	private static class LazyHolder {
		private static final FTLHandler INSTANCE = new FTLHandler();
	}

	private void configurationSet() {
		templateConfiguration = new Configuration(Configuration.VERSION_2_3_27);
		templateConfiguration.setDefaultEncoding("UTF-8");
		templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		templateConfiguration.setLogTemplateExceptions(false);
		templateConfiguration.setWrapUncheckedExceptions(true);
		try {
			templateConfiguration.setTemplateLoader(new FileTemplateLoader(
					new File(CodeGeneratorConstant.TEMPLATE_DIRECTORY.toString())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateCode(Path templatePath, Path outputFilePath, Map<String, Object> rootHash) {
		try {
			Template template = templateConfiguration.getTemplate(templatePath.toString());
			Writer out = new OutputStreamWriter(new PrintStream(outputFilePath.toFile()));
			template.process(rootHash, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
