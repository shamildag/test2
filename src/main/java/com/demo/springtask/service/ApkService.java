package com.demo.springtask.service;

import com.demo.springtask.model.Apk;
import com.demo.springtask.model.ApkView;
import com.demo.springtask.repo.ApkRepository;
import com.demo.springtask.repo.UserRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ApkService {

	private ApkRepository apkRepository;
	private UserRepository userRepository;

	private static final String path2image512 = "images/android512.jpg";
	private static final String path2image128 = "images/android128.jpg";
	private byte[] DEFAULT_128_IMAGE;
	private byte[] DEFAULT_512_IMAGE;
	private Logger log = LoggerFactory.getLogger(ApkRepository.class);


	@Autowired
	public ApkService(ApkRepository apkRepository, UserRepository userRepository) {
		this.apkRepository = apkRepository;
		this.userRepository = userRepository;
		this.uploadDefaultImages();
	}

	public Stream<ApkView> allForCategoryAsStream(String name) {
		log.info( "size is " + apkRepository.findAllByCategory(name).size()) ;
		Stream<ApkView> result = apkRepository.findAllByCategory(name).stream().map((Apk apk)-> {
			ApkView element = new ApkView(apk);
			userRepository.findById(apk.getDev_id())
					.ifPresent(x -> element.setDev_name(x.getName()));
			log.info(element.toString());
			return element;
		});
		return result;
	}


	public List<Apk> top10apk() {
//		Flux<Apk> top10Apk  = Flux.fromArray(apkRepository.findTop10ByDownloadsOrderByDownloadsDesc());
		List<Apk> top10Apk = (List<Apk>) apkRepository.findAll();
		return top10Apk;
	}

	public byte[] getImage128(Long id) {
		return apkRepository.findById(id).map(Apk::getImage128).orElse(DEFAULT_128_IMAGE);
	}

	public byte[] getImage512(Long id) {
		return apkRepository.findById(id).map(Apk::getImage512).orElse(DEFAULT_512_IMAGE);
	}

	private void uploadDefaultImages() {
		//todo move initialization to another place
		InputStream img128in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path2image128);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (img128in == null) {
			log.error("128 image not exists ");
		} else {
			try {
				IOUtils.copy(img128in, baos);
				DEFAULT_128_IMAGE = baos.toByteArray();
			} catch (IOException e) {
				DEFAULT_128_IMAGE = null;
				log.error("issue in copying  128 image");
			}
		}

		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
		InputStream img512in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path2image512);
		if (img512in == null) {
			log.error("512 image not exists ");
		} else {
			try {
				IOUtils.copy(img512in, baos2);
				DEFAULT_512_IMAGE = baos2.toByteArray();
			} catch (IOException e) {
				DEFAULT_512_IMAGE = null;
				log.error("issue in copying  512 image found");
			}
		}
	}
}
