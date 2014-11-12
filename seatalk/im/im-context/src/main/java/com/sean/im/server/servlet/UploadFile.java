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

import com.sean.im.server.entity.FileEntity;
import com.sean.log.core.LogFactory;
import com.sean.persist.core.EntityDao;
import com.sean.persist.core.PersistContext;

/**
 * 上传文件Servlet
 * @author sean
 */
@WebServlet(urlPatterns = "/UploadFile")
@MultipartConfig(location = "/home/sean/upload/")
public class UploadFile extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogFactory.getFrameworkLogger();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			request.setCharacterEncoding("utf-8");
			Part part = request.getParts().iterator().next();
			String filename = part.getName();
			String path = "upload/file/" + UUID.randomUUID().toString();

			String root = request.getServletContext().getRealPath("");
			InputStream input = part.getInputStream();
			FileOutputStream output = new FileOutputStream(new File(root + path));
			byte[] buf = new byte[10240];
			int len = 0;
			while ((len = input.read(buf)) != -1)
			{
				output.write(buf, 0, len);
			}

			// 保存数据库
			EntityDao<FileEntity> dao = PersistContext.CTX.getEntityDao(FileEntity.class);
			FileEntity file = new FileEntity();
			file.setFilename(filename);
			file.setLength(part.getSize());
			file.setPath(path);
			file.setType(1);
			dao.persist(file);

			// 返回文件ID
			response.getWriter().write(String.valueOf(file.getId()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("上传文件失败", e);
		}
	}

}
