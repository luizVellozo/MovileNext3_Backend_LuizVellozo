package com.luiz.next.control;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.luiz.next.entity.value.ValueEntity;

@Repository
public class ValueRepository {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void incrementHistory(final ValueEntity<?> value) {

		final StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(value.getType().getSimpleName()).append("_history").append(" VALUES (:id,:d,:v)");

		final Query query = em.createNativeQuery(sql.toString());
		query.setParameter("id", value.getId()).setParameter("v", value.get()).setParameter("d", LocalDateTime.now());
		
		query.executeUpdate();
	}
}
