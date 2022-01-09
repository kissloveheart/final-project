package com.hiep.finalproject.dao;

import com.hiep.finalproject.entity.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class AccountDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public AccountDAO() {
    }

    public Account findAccount(String email){
        Session session = sessionFactory.getCurrentSession();
        String sql = "from Account  where email =:email";
        Query<Account> query = session.createQuery(sql);
        query.setParameter("email",email);
        return query.uniqueResult();
    }

}
