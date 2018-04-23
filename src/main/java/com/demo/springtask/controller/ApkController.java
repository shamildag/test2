package com.demo.springtask.controller;

import com.demo.springtask.model.Apk;
import com.demo.springtask.model.ApkView;
import com.demo.springtask.service.ApkService;
import com.demo.springtask.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


@RestController
@RequestMapping("/apk")
public class ApkController {
	@Autowired
	ApkService apkService;
	@Autowired
	StorageService storageService;

	private Logger log = LoggerFactory.getLogger(ApkController.class);

	@PreAuthorize("hasAuthority('DEVELOPER') or hasAuthority('USER')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Resource> byId(@PathVariable Long id) throws IOException {
		File file = storageService.byId(id);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Cache-Control", "no-cache,  no-store, must-revalidate");
		responseHeaders.add("Pragma", "no-cache");
		responseHeaders.add("Expires", "0");

		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		responseHeaders.setAccessControlExposeHeaders(Collections.singletonList("Content-Disposition"));
		responseHeaders.set("Content-Disposition", String.format("attachment; filename=%s", file.getName()));

		log.info("returning file " + file.getName());

		ByteArrayResource bar = new ByteArrayResource(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
		return ResponseEntity.ok()
				.contentLength(file.length())
				.headers(responseHeaders)
				.contentType(MediaType.valueOf(mimeType))
				.body(bar);
	}

	@GetMapping(value = "/category/{name}")
	public Stream<ApkView> allForCategory(@PathVariable String name) {
		log.info("name is " + name);
		return apkService.allForCategoryAsStream(name);

	}

	@GetMapping(value = "/top10apk")
	public List<Apk> top10apk() {
		return apkService.top10apk();
	}

	@GetMapping(value = "/{id}/image128")
	public ResponseEntity<Resource> image128(@PathVariable Long id) {
		byte[] image = apkService.getImage128(id);
		ByteArrayResource bar = new ByteArrayResource(apkService.getImage128(id));
		return ResponseEntity.ok()
				.contentLength(image.length)
//				.headers(responseHeaders)
				.contentType(MediaType.IMAGE_JPEG)
				.body(bar);
	}

	@GetMapping(value = "/{id}/image512")
	public ResponseEntity<Resource> image512(@PathVariable Long id) {
		byte[] image = apkService.getImage512(id);
		ByteArrayResource bar = new ByteArrayResource(image);
		return ResponseEntity.ok()
				.contentLength(image.length)
//				.headers(responseHeaders)
				.contentType(MediaType.IMAGE_JPEG)
				.body(bar);
	}

	@PreAuthorize("hasAuthority('DEVELOPER')")
	@RequestMapping(value = "/upload",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public @ResponseBody
	ResponseEntity uploadFile(@RequestParam("myfile") MultipartFile file,
							  @RequestParam("name") String apkName,
							  @RequestParam("description") String apkDescription,
							  @RequestParam("category") String apkCategory
	)
			throws IllegalStateException, IOException {
		//use token to get name of developer
		Long devId = 2L;
		Apk apk = new Apk(apkName, apkDescription, apkCategory, devId);
		//store into db new package
		storageService.store(apk, file);

		//return correct response
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("MyResponseHeader", "MyValue");
		return new ResponseEntity<>("Successfully created", responseHeaders, HttpStatus.CREATED);
	}

}
