package com.sean.im.server.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.sean.log.core.LogFactory;

/**
 * 上传图片Servlet
 * @author sean
 */
@WebServlet(urlPatterns = "/UploadImage")
@MultipartConfig(location = "/home/sean/upload/")
public class UploadImage extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogFactory.getFrameworkLogger();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			request.setCharacterEncoding("utf-8");
			Part part = request.getParts().iterator().next();
			String path = "upload/image/" + UUID.randomUUID().toString();

			String root = request.getServletContext().getRealPath("");
			InputStream input = part.getInputStream();
			FileOutputStream output = new FileOutputStream(new File(root + path));
			byte[] buf = new byte[10240];
			int len = 0;
			while ((len = input.read(buf)) != -1)
			{
				output.write(buf, 0, len);
			}

			// 返回文件ID
			response.getWriter().write(path);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("上传图片失败", e);
		}
	}

}
