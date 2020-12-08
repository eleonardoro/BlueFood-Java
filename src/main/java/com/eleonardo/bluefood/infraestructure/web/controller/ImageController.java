package com.eleonardo.bluefood.infraestructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eleonardo.bluefood.application.service.ImageService;
import org.springframework.http.MediaType;

@Controller
public class ImageController {

  @Autowired
  private ImageService ImageService;
  
  @GetMapping(path = "/images/{type}/{imgName}", produces = MediaType.IMAGE_PNG_VALUE)
  @ResponseBody
  public byte[] getBytes(
      @PathVariable("type") String type,
      @PathVariable("imgName") String imgName ) {
    return ImageService.getByte(type, imgName);
  }
}
