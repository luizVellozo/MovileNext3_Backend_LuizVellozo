package com.luiz.next.control;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
	
	public <T> T getHistory(final ValueEntity<?> value) {

		final StringBuilder jpql = new StringBuilder("SELECT v FROM ");
		jpql.append(value.getClass().getCanonicalName()).append(" v JOIN FETCH v.history WHERE v.id = :id");
		
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) value.getClass();
		
		final TypedQuery<T> query = em.createQuery(jpql.toString(), type).setParameter("id", value.getId());
		List<T> resultList = query.getResultList();
		if(resultList.isEmpty())
			return null;
		return resultList.get(0);
	}
}
