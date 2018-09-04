package com.lel.utils.freemarker;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import com.lel.Result;
import com.lel.utils.file.FileUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerUtils {
	
	private static final String ENCODING_UTF8 = "UTF-8";
	
	private static Configuration cfg = null;
	
	private static StringTemplateLoader stl;
	
	private static Configuration getConfiguration(){
		if (null == cfg) {
			cfg = new Configuration();
			// 设置编码格式，防止中文乱码
			cfg.setEncoding(Locale.getDefault(), ENCODING_UTF8);
			cfg.setDefaultEncoding(ENCODING_UTF8);
			// 设置对象包装器
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			// 设置异常处理器，在使用${a.b.c.d}时没有属性也不会出错
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
			cfg.setClassicCompatible(true);//处理空值为空字符串
			cfg.setTemplateUpdateDelay(0);
		}
		if (null == stl) {
			stl =  new StringTemplateLoader();
			cfg.setTemplateLoader(stl);
		}
		return cfg;
	}
	
	
	/**
	 * 生成模板文件-写入文件
	 * @param tempPath 
	 * @param tempFtl
	 * @param obj
	 * @param dealFile
	 * @return
	 */
	public static Result<String> processToFile(String tempPath, String tempFtl, Object obj, String dealFile){
		Writer writer = null;
		Result<String> result = new Result<String>();
		try {
			Configuration conf = getConfiguration();
			conf.setDirectoryForTemplateLoading(FileUtils.toFile(tempPath));
			Template template = conf.getTemplate(tempFtl);
			Result<String> createRes = FileUtils.createFileOrDir(dealFile, true);
			if (createRes.exeSuccess()) {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dealFile), "UTF-8"));
				template.process(obj, writer);
				result.success();
			}else{
				result.fail(createRes.getMsg());
			}
			
		} catch (Exception e) {
			result.fail(e.getMessage());
		}finally{
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					
				}
			}
		}
		return result;
	}
	
	/**
	 * 生成模板文件-返回结果字符串
	 * @param tempPath
	 * @param tempFtl
	 * @param obj
	 * @return
	 */
	public static Result<String> processToStr(String tempPath, String tempFtl, Object obj){
		Result<String> result = new Result<String>();
		Writer writer = null;
		try {
			Configuration conf = getConfiguration();
			conf.setDirectoryForTemplateLoading(FileUtils.toFile(tempPath));
			Template template = conf.getTemplate(tempFtl);
			writer = new StringWriter();  
			template.process(obj, writer);
			result.successT(writer.toString());
		} catch (IOException | TemplateException e) {
			result.fail(e.getMessage());
		}finally{
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					
				}
			}
		}
		return result;
	}
}
