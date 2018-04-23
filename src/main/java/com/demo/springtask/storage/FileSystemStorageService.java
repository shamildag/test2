package com.demo.springtask.storage;

import com.demo.springtask.model.Apk;
import com.demo.springtask.repo.ApkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;

@Service
public class FileSystemStorageService implements StorageService {

	private static final Logger log = LoggerFactory.getLogger(FileSystemStorageService.class);
	private static final int SIZE_512 = 512;
	private static final int SIZE_128 = 128;

	private final Path rootLocation;

	@Autowired
	private ApkRepository repository;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(System.getProperty("user.dir")  , properties.getLocation());
		log.info("shama1" + rootLocation);
		log.info("shama2 == " + System.getProperty("user.dir") );
		log.info("shama3" + Paths.get(".").toAbsolutePath());
	}

	@Override
	public void store(Apk apk, MultipartFile file) {
		String filename = StringUtils.cleanPath(
				(file.getOriginalFilename() != null) ?
						file.getOriginalFilename()
						: generateFileName());
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			//read from the file and save zip file
			Path path2zip = this.rootLocation.resolve(filename);
			InputStream fileInStream = file.getInputStream();
			Files.copy(fileInStream, this.rootLocation.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);
			fileInStream.close();

			// extract 128 and 512 images directly from zip file
			apk = fillApkViewFromZip(path2zip, apk);
			// set file name for downloading

		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
		log.info("path is  " + this.rootLocation.resolve(filename));
		repository.save(apk);
	}

	private Apk fillApkViewFromZip(Path zipFile, Apk apk) throws IOException {
		apk.setDownloads(0);
		apk.setRating(0f);
		apk.setUpload_date(LocalDate.now());
		apk.setFilename(zipFile.getFileName().toString());
		//		extract images
		ZipInputStream zpStream = new ZipInputStream(new FileInputStream(zipFile.toFile()));
		ZipEntry entry = zpStream.getNextEntry();
		while (entry != null) {
			if (entry.getName().matches(".+.jpg|.+.png")) {
//				log.info("entry is " + entry.getName());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				final byte[] buffer = new byte[1024];
				int length;
				while ((length = zpStream.read(buffer)) > 0) {
					baos.write(buffer, 0, length);
				}

				ByteArrayInputStream baos2 = new ByteArrayInputStream(baos.toByteArray());
				BufferedImage image = ImageIO.read(baos2);
				if (image.getData().getHeight() == SIZE_512 && image.getData().getWidth() == SIZE_512) {
					apk.setImage512(baos.toByteArray());
				} else if (image.getData().getHeight() == SIZE_128 && image.getData().getWidth() == SIZE_128) {
					apk.setImage128(baos.toByteArray());
				}
			}
			entry = zpStream.getNextEntry();
		}

		zpStream.close();
		return apk;
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
					.filter(path -> !path.equals(this.rootLocation))
					.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			log.info("rootLocation is " + rootLocation + " this = " +  this.rootLocation );
			if (Files.notExists(rootLocation)) {
				throw new FileNotFoundException("Directory with packages not exists by path " + rootLocation);
			}
			for  ( Apk pack : fillPredefinedList()) {
				log.info("package is  " + pack.toString());
				log.info("package is  " + load(pack.getFilename()).toString());
				repository.save(fillApkViewFromZip(load(pack.getFilename()), pack));
			}


		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	// todo
	private String generateFileName() {
		return "random";
	}

	public File byId(Long id) {
		Apk apk = repository.findById(id).orElseThrow(StorageFileNotFoundException::new);
		apk.setDownloads(1 + apk.getDownloads());
		repository.save(apk);
		return rootLocation.resolve(apk.getFilename()).toFile();
	}

	private List<Apk> fillPredefinedList () {
		return Arrays.asList(
				new Apk("Ski in Matlas", "Dagestan Ski Resort Matlas", "Lifestyle", 1L, "ski.zip"),
				new Apk("Dating", "Make dating close to your place", "Lifestyle", 1L, "dating.zip"),
				new Apk("Dubai Info", "All info about Dubai in one place", "Lifestyle", 1L, "dubai_info.zip"),
				new Apk("Death Star", "Star wars death star", "Games", 1L, "DeathStar.zip"),
				new Apk("Best flights", "Find chip tickets", "Travel", 1L, "flights.zip"),
				new Apk("Funny cats", "Funny cats game", "Lifestyle", 1L, "funny_cats.zip"),
				new Apk("Hunday inside AVR", "Augmented reality application for Hunday Cars", "Cars", 2L, "hunday.zip"),
				new Apk("Mi28 in Syria", "Mi28 Night Hunter in the sky of Sirya", "Games", 2L, "Mi28_Syria.zip"),
				new Apk("Vet Clinics SPb", "All veterinary clinics for your pet in one place", "Games", 1L, "vetclinics.zip"),
				new Apk("VBoxing training", "Several program for training from world champion", "Sport", 1L, "trennings.zip"),
				new Apk("SPB Zoo", "Guide for Saint-Petersburg zoo", "Lifestyle", 1L, "spb_zoo.zip"),
				new Apk("Find cars near to you", "Find cars in your region", "Cars", 2L, "cars_finder.zip"));
	}
}
