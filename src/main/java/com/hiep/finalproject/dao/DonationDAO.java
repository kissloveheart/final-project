package com.hiep.finalproject.dao;

import com.hiep.finalproject.entity.Donation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Repository
@Transactional
public class DonationDAO {
    private SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(DonationDAO.class);

    public DonationDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Donation> getDonationList(Long campaignId){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select d from Donation d where d.campaign.Id=:campaignId";
        Query<Donation> query = session.createQuery(sql);
        query.setParameter("campaignId",campaignId);
        List<Donation> donationList = query.getResultList();
        if(donationList == null){
            log.info("Can not get data");
            return Collections.emptyList();
        }
        return donationList;
    }
}
