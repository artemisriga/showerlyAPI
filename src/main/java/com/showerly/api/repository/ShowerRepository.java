package com.showerly.api.repository;

import com.showerly.api.model.Shower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowerRepository extends JpaRepository<Shower, String> {
}
