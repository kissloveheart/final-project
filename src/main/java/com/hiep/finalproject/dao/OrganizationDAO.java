package com.hiep.finalproject.dao;

import com.hiep.finalproject.entity.Campaign;
import com.hiep.finalproject.entity.Organization;
import com.hiep.finalproject.form.CampaignForm;
import com.hiep.finalproject.form.OrganizationForm;
import com.hiep.finalproject.model.CampaignInfo;
import com.hiep.finalproject.model.OrganizationInfo;
import com.hiep.finalproject.pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;



@Repository
@Transactional
public class OrganizationDAO {

    private final SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(OrganizationDAO.class);

    public OrganizationDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Organization findOrganization(Long organizationId){
        Session session = sessionFactory.getCurrentSession();
        return session.find(Organization.class,organizationId);
    }

    public List<Organization> getOrganizationList(){
        Session session = sessionFactory.getCurrentSession();
        String sql = "from Organization";
        return session.createQuery(sql).getResultList();
    }

    public OrganizationInfo getOrganizationInfo(Long organizationId){
        Session session = sessionFactory.getCurrentSession();
        String sql = "Select new "+OrganizationInfo.class.getName() +
                "(o.Id,o.name,o.description,count(c.Id),sum(d.amount))"+
                " from Organization o left join Campaign c on o.Id = c.organization.Id" +
                " left join Donation d on c.Id = d.campaign.Id"
                +" where o.Id=:organizationId"
                +" group by o.Id,o.name,o.description";
        Query<OrganizationInfo> query = session.createQuery(sql);
        query.setParameter("organizationId", organizationId);
        return query.uniqueResult();
    }

    public PaginationResult<OrganizationInfo> getOrganizationInfoList(String orderBy, int page, int maxResult, int maxNavigationPage){
        Session session = sessionFactory.getCurrentSession();
        String sql = "Select new "+OrganizationInfo.class.getName() +
                "(o.Id,o.name,o.description,count(c.Id),sum(d.amount))"+
                " from Organization o left join Campaign c on o.Id = c.organization.Id" +
                " left join Donation d on c.Id = d.campaign.Id"
                +" group by o.Id,o.name,o.description";

        if( orderBy != null && !orderBy.equals("")){
            sql+=" order by "+orderBy;
        }

        Query<OrganizationInfo> query = session.createQuery(sql);
        return new PaginationResult<OrganizationInfo>(query, page, maxResult, maxNavigationPage);

    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteOrganization(List<Long> organizationIdListDelete){
        Session session = sessionFactory.getCurrentSession();
        String sql = "delete from Organization o where o.Id in (:organizationIdListDelete)";
        Query query = session.createQuery(sql);
        query.setParameterList("organizationIdListDelete",organizationIdListDelete);
        return query.executeUpdate();
    }

    public List<Long> getOrganizationIdList(){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select o.Id from Organization o";
        return session.createQuery(sql).getResultList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Long saveOrganization(OrganizationForm organizationForm) {
        Session session = sessionFactory.getCurrentSession();
        Organization organization;


        if (organizationForm.getId() == null) {
            organization = new Organization();
        } else {
            organization = findOrganization(organizationForm.getId());
        }

        organization.setName(organizationForm.getName());
        organization.setDescription(organizationForm.getDescription());

        if(organizationForm.getFileData() != null){
            byte[] image = null;

            try {
                image = organizationForm.getFileData().getBytes();
            } catch (IOException e) {
                log.warn("The image is wrong");
                e.printStackTrace();

            }

            if(image != null && image.length > 0){
                organization.setImage(image);
            }
        }
        Long organizationId;
        if (organization.getId() == null){
            organizationId = (Long) session.save(organization);
        } else {
            organizationId = organization.getId();
        }
        session.flush();
        return  organizationId;
    }
}
