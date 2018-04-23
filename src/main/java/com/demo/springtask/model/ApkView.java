package com.demo.springtask.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class ApkView {
	private Long id;
	private String name;
	private String description;
	private String category;
	private String dev_name;
	private Integer downloads;
	private Float rating;
	private LocalDate date_uploads;

	public ApkView(Apk apk) {
		this.id = apk.getId();
		this.name = apk.getName();
		this.description = apk.getDescription();
		this.category = apk.getCategory();
		this.downloads = apk.getDownloads();
		this.rating = apk.getRating();
		this.date_uploads = apk.getUpload_date();
	}

	public void setDev_name(String dev_name) {
		this.dev_name = dev_name;
	}
}

