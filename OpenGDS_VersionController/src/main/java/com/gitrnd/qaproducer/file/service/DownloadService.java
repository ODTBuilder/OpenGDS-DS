package com.gitrnd.qaproducer.file.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import com.gitrnd.qaproducer.common.security.LoginUser;


@Service
@PropertySources({ @PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true),
		@PropertySource(value = "file:./application.yml", ignoreResourceNotFound = true) })
public class DownloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

	@Value("${gitrnd.apache.host}")
	private String apacheHost;

	@Value("${gitrnd.apache.port}")
	private String apachePort;

	public void downloadZip(HttpServletResponse response, String time, String file, String user)
			throws UnsupportedEncodingException {
		String encodeName = URLEncoder.encode(file, "UTF-8");
		String path = "http://" + apacheHost + ":" + apachePort + "/" + user + "/upload/" + time + "/" + encodeName;
		try {
			URL url = new URL(path);
			LOGGER.info("{} has been requested [origin layer]", file);
			InputStream in = url.openStream();
			response.setContentType("application/octet-stream");
			String txt = file;
			char[] txtChar = txt.toCharArray();
			for (int j = 0; j < txtChar.length; j++) {
				if (txtChar[j] >= '\uAC00' && txtChar[j] <= '\uD7A3') {
					String targetText = String.valueOf(txtChar[j]);
					try {
						txt = txt.replace(targetText, URLEncoder.encode(targetText, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
			response.setHeader("Content-disposition", "attachment; filename=" + txt);
			IOUtils.copy(in, response.getOutputStream());
			response.flushBuffer();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void downloadError(HttpServletResponse response, String time, String file, LoginUser loginUser)
			throws UnsupportedEncodingException {
		String encodeName = URLEncoder.encode(file, "UTF-8");
		String path = "http://" + apacheHost + ":" + apachePort + "/" + loginUser.getUsername() + "/error/" + time + "/"
				+ encodeName;
		try {
			URL url = new URL(path);
			LOGGER.info("{} has been requested [error layer]", file);
			InputStream in = url.openStream();
			response.setContentType("application/octet-stream");
			String txt = file;
			char[] txtChar = txt.toCharArray();
			for (int j = 0; j < txtChar.length; j++) {
				if (txtChar[j] >= '\uAC00' && txtChar[j] <= '\uD7A3') {
					String targetText = String.valueOf(txtChar[j]);
					try {
						txt = txt.replace(targetText, URLEncoder.encode(targetText, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
			response.setHeader("Content-disposition", "attachment; filename=" + txt);
			IOUtils.copy(in, response.getOutputStream());
			response.flushBuffer();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
