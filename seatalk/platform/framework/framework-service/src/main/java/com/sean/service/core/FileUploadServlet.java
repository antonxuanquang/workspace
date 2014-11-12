package com.sean.service.core;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * 上传文件Servlet
 * @author sean
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = "/FileUploadServlet")
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024, maxFileSize = 1024 * 1024 * 1)
public class FileUploadServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final String SAVE_PATH = "/upload";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");
		Part part = request.getPart("file");

		// 创建目录
		File dir = new File(SAVE_PATH + File.separator);
		if (!dir.exists())
		{
			dir.mkdirs();
		}

		String h = part.getHeader("content-disposition");
		String filename = h.substring(h.lastIndexOf("=") + 2, h.length() - 1);
		part.write(SAVE_PATH + File.separator + filename);
	}

}
