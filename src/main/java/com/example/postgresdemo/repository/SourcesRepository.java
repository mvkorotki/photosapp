package com.example.postgresdemo.repository;

import com.example.postgresdemo.model.PhotoFileSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourcesRepository extends JpaRepository<PhotoFileSource, Long>  {
}
