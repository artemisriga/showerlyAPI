package com.showerly.api.repository;

import com.showerly.api.model.DataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataModelRepository extends JpaRepository<DataModel, String> {
}
