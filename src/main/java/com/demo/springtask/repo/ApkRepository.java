package com.demo.springtask.repo;

import com.demo.springtask.model.Apk;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApkRepository extends CrudRepository<Apk, Long> {
	@Query("SELECT  apk  FROM  Apk apk WHERE apk.downloads > 0 ")
	List<Apk> findTop10();

	@Query("SELECT apk FROM Apk apk WHERE apk.category = :catName")
	List<Apk> findAllByCategory(String catName);
}
