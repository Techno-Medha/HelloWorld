package com.technomedha.careers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/upload")
@Produces(MediaType.APPLICATION_JSON)
public class UploadService {

	private static final Gson gson = new Gson();	
	private static final Logger logger = LoggerFactory.getLogger(UploadService.class.getName());	

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadResume(@FormDataParam("file") InputStream resume, 
			@FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
		logger.info("File Upload started");

		String location = "/tmp/resumes/";

		File directory = new File(location);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		String path = location + fileDetail.getFileName();

		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.createNewFile();
		}
		
		write(resume, filePath);
		
		String register = gson.toJson("--- File uploaded successfully---");
		
		logger.info("File Uploaded successfully");
		
		return Response.ok().entity(register).build();

	}

	private void write(InputStream resume, File file) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int read = 0;

			while ((read = resume.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			out.close();
			out.flush();

			logger.info("write function completed");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
