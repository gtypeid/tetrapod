package com.kosta.databaseserver.spring.repo.jpa;

import com.kosta.common.spring.data.vo.User;
import com.kosta.databaseserver.spring.repo.abs.sync.UserRepository;
import com.kosta.databaseserver.spring.repo.abs.jpa.JPAUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class JpaUserRepository implements UserRepository {

    @Autowired
    private JPAUser jpaUser;

    @PersistenceContext
    private EntityManager entityManager;

    public Long getNextSequenceValue() {
        //createSequenceIfNotExists();
        Query query = entityManager.createNativeQuery("SELECT USER_SEQ.NEXTVAL FROM DUAL");
        return ((Number) query.getSingleResult()).longValue();
    }


    @Transactional
    public void createSequenceIfNotExists() {
        // SQL 쿼리로 시퀀스가 존재하는지 확인
        try {
            entityManager.createNativeQuery("SELECT USER_SEQ.NEXTVAL FROM DUAL").getSingleResult();
        } catch (Exception e) {
            // 시퀀스가 존재하지 않으면 예외 발생, 시퀀스 생성
            entityManager.createNativeQuery(
                    "CREATE SEQUENCE USER_SEQ START WITH 1 INCREMENT BY 1"
            ).executeUpdate();
        }
    }

    @Override
    public User create(User data) {
        //data.setId(getNextSequenceValue());
        User newUser = new User().copy(data);
        return jpaUser.save( newUser );
    }

    @Override
    public User read(Long id) {
        return jpaUser.findById(id).get();
    }

    @Override
    public User update(Long id, User data) {
        User user = jpaUser.findById(id).get().copy(data);
        return jpaUser.save(user);
    }

    @Override
    public Boolean delete(Long id) {
        jpaUser.deleteById(id);
        return true;
    }
}
