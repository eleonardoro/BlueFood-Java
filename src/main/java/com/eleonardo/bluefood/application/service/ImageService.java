package com.eleonardo.bluefood.application.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eleonardo.bluefood.util.IOUtils;

@Service
public class ImageService {

	@Value("${bluefood.files.restaurante}")
	private String restaurantesDir;

	@Value("${bluefood.files.categoria}")
	private String categoriasDir;

	@Value("${bluefood.files.comida}")
	private String comidasDir;

	public void uploadLogotipo(MultipartFile multipartFile, String fileName) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, restaurantesDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}

	public void uploadComida(MultipartFile multipartFile, String fileName) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, comidasDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}

	public byte[] getByte(String type, String imgName) {

		try {
			String dir;

			if ("comida".equals(type))
				dir = comidasDir;
			else if ("restaurante".equals(type))
				dir = restaurantesDir;
			else if ("categoria".equals(type))
				dir = categoriasDir;
			else
				throw new Exception(type + " não é um tipo de imagem válido!");

			return IOUtils.getByte(Paths.get(dir, imgName));
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
	}

}
