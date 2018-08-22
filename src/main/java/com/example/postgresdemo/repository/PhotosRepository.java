package com.example.postgresdemo.repository;

import com.example.postgresdemo.model.PhotoFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotosRepository extends JpaRepository<PhotoFile, Long> {
}
