package com.luiz.next.control;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luiz.next.entity.Asset;

public interface AssetReporitory extends JpaRepository<Asset, Long> {

	@Query(value = "SELECT a FROM Asset a JOIN FETCH a.value")
	public List<Asset> findAllFetch();

	@Query(value = "SELECT a FROM Asset a JOIN FETCH a.value LEFT JOIN FETCH a.dependencies d LEFT JOIN FETCH d.value WHERE a.name LIKE :name")
	public Optional<Asset> findByNameWithDependencies(@Param("name") final String name);
	
	@Query(value = "SELECT a FROM Asset a JOIN FETCH a.value WHERE a.name = :name")
	public Optional<Asset> findByName(@Param("name") final String name);

	@Query(value = "SELECT a FROM Asset a JOIN FETCH a.value WHERE a.name NOT LIKE :asset AND a.name IN (:dependencies)")
	public List<Asset> loadAllDependenciesIn(@Param("asset") final String name,
			@Param("dependencies") final Collection<String> assets);

	@Query(value = "SELECT COUNT(a) > 0 FROM Asset a WHERE a.name LIKE :asset")
	public boolean exists(@Param("asset") final String asset);

	@Query(value = "SELECT COUNT(a) > 0 FROM Asset a WHERE a.name IN (:assets)")
	public boolean exists(@Param("assets") final Collection<String> assets);
}
