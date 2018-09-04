package com.lel.utils.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.lel.Result;


/**
 * 文件操作
 * @author lel
 *
 */
public class FileUtils {
	
public static final String LIST_PREFIX = "[";
	
	public static final String LIST_SUFFIX = "]";
	
	public static final String[] PATH_CANNOTHAS_CHAR = {"*","?","\"","<",">","|"};
	
	public static final String EMPTY_STR = "";
	
	public static final String FORM_SEP = "/";
	
	public static final String OLD_FORM_SEP = "\\";
	
	public static final String OLD_FORM_SEP_REG = "\\\\";
	
	/**
	 * 格式化路径分割符号<br>
	 * 替换“\\”为“/”<br>
	 */
	public static String formSeparator(String path) throws Exception {
		if (StringUtils.isBlank(path)) {
			throw new Exception("[非法]-文件路径为空");
		}
		String formPath = path;
		// 替换路径分隔符
		if (formPath.contains(OLD_FORM_SEP)) {
			formPath = formPath.replaceAll(OLD_FORM_SEP_REG, FORM_SEP);
		}
		// 防止连续多个/
		formPath = formSplit(formPath, FORM_SEP);
		return formPath;
	}

	/**
	 * 去除多余的分隔符
	 */
	public static String formSplit(String path, String split) throws Exception {
		if (StringUtils.isBlank(path)) {
			throw new Exception("[非法]-文件路径为空");
		}
		String formPath = path;
		String[] splits = formPath.split(split);
		StringBuilder sBuilder = new StringBuilder();
		for (String item : splits) {
			if (StringUtils.isNotBlank(item)) {
				sBuilder.append(item).append(FORM_SEP);
			}
		}
		formPath = sBuilder.toString();
		if (StringUtils.isBlank(formPath)) {
			throw new Exception("[非法路径]-无效路径");
		}
		return formPath;
	}

	/**
	 * 文件名或文件夹名中不可包含 *?"<>|<br>
	 * 仅可包含一个: 且必须紧跟盘符<br>
	 * 可包含多个. 可在最前面或跟在/之后,不可在/之前<br>
	 */
	public static String formPath(String path, String[] exludes) throws Exception {
		String formPath = path;
		for (int i = 0; i < exludes.length; i++) {
			formPath = formPath.replaceAll(OLD_FORM_SEP + exludes[i], EMPTY_STR);
		}
		// 格式分隔符
		formPath = formSeparator(formPath);
		formPath = formPath.replaceAll(".\\/", FORM_SEP);
		return formPath;
	}

	/**
	 * 根据文件路径获取文件<br>
	 */
	public static File toFile(String path) {
		return new File(path);
	}

	/**
	 * 文件或路径是否存在<br>
	 */
	public static boolean isExsist(String path) {
		return toFile(path).exists();
	}

	/**
	 * 创建文件夹或文件 无则创建<br>
	 */
	public static Result<String> createFileOrDir(String path, boolean isFile) {
		Result<String> result = new Result<String>();
		String formFilePath = formPath(path, isFile);
		// 1-判断文件或路径是否存在
		if (isExsist(formFilePath)) {
			File file = toFile(formFilePath);
			if (isFile && file.isFile()) {
				result.success("已存在的文件");
			} else if (!isFile && file.isDirectory()) {
				result.success("已存在的文件夹");
			} else {
				result.fail("存在同名文件或文件夹");
			}
			return result;
		}
		// 2-不存在文件或路径，则创建文件或路径
		if (isFile) {
			// 2.1、创建文件
			int last = formFilePath.lastIndexOf(FORM_SEP);
			String dirPath = formFilePath.substring(0, last);
			Result<String> createPath = createFileOrDir(dirPath, false);
			if (createPath.exeSuccess()) {
				try {
					boolean createFile = toFile(formFilePath).createNewFile();
					if (createFile) {
						result.success();
					} else {
						result.fail();
					}
				} catch (IOException e) {
					result.fail(e.getMessage());
				}
			}
		} else {
			// 2.2、创建文件路径
			boolean mkdirs = toFile(formFilePath).mkdirs();
			if (mkdirs) {
				result.success();
			} else {
				result.fail("创建失败！");
			}
		}
		return result;
	}

	/**
	 * 删除文件
	 * @param filePath
	 * @return
	 */
	public static Result<String> delFileOrDir(String path) {
		Result<String> result = new Result<String>();
		if (isExsist(path)) {
			boolean delete = toFile(path).delete();
			if (delete) {
				result.success();
			}else{
				result.fail();
			}
		}else{
			result.success("不存在的文件或文件夹");
		}
		return result;
	}

	/**
	 * 格式文件或文件夹路径<br>
	 */
	private static String formPath(String filePath, boolean isFile) {
		filePath = filePath.replace(OLD_FORM_SEP, FORM_SEP);
		return filePath;
	}

}
