package com.hiep.finalproject.dao;

import com.hiep.finalproject.entity.Campaign;
import com.hiep.finalproject.entity.Donation;
import com.hiep.finalproject.entity.Organization;
import com.hiep.finalproject.form.CampaignForm;
import com.hiep.finalproject.model.CampaignInfo;
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Transactional
@Repository
public class CampaignDAO {
    private final static Logger log = LoggerFactory.getLogger(CampaignDAO.class);

    private final SessionFactory sessionFactory;
    private final OrganizationDAO organizationDAO;
    private final DonationDAO donationDAO;


    public CampaignDAO(SessionFactory sessionFactory, OrganizationDAO organizationDAO, DonationDAO donationDAO) {
        this.sessionFactory = sessionFactory;
        this.organizationDAO = organizationDAO;
        this.donationDAO = donationDAO;
    }

    public Campaign findCampaign(Long campaignId){
        Session session = sessionFactory.getCurrentSession();
        return session.find(Campaign.class,campaignId);
    }

    public List<Long> getCampaignIdList(){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select e.Id from Campaign e";
        return session.createQuery(sql).getResultList();
    }
    @Transactional(rollbackFor = Exception.class)
    public int deleteCampaign(List<Long> campaignIdListDelete){
        Session session = sessionFactory.getCurrentSession();
        String sql = "delete from Campaign c where c.Id in (:campaignIdListDelete)";
        Query query = session.createQuery(sql);
        query.setParameterList("campaignIdListDelete",campaignIdListDelete);
        return query.executeUpdate();
    }

    public int getMaxCampaign(){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select max(Id) from Campaign";
        Integer result = (Integer) session.createQuery(sql).getSingleResult();
        if( result == null){
            return 0;
        }
        return result;
    }


    public CampaignInfo getCampaignInfo(Long campaignId){
        Session session = sessionFactory.getCurrentSession();
        String sql = "Select new "+CampaignInfo.class.getName() +
                "(c.Id,c.name,c.description,c.organization,c.startDate,c.endDate, count(d.Id),sum(d.amount),c.targetBudget,c.status)"+
                " from Campaign c left join Donation d on c.Id = d.campaign.Id"
                +" where c.Id=:campaignId"
                +" group by c.Id,c.name,c.description,c.organization,c.startDate,c.endDate,c.targetBudget,c.status";
        Query<CampaignInfo> query = session.createQuery(sql);
        query.setParameter("campaignId", campaignId);
        return query.uniqueResult();
    }

    public PaginationResult<CampaignInfo> getCampaignInfoList(String orderBy,String status,int page, int maxResult, int maxNavigationPage){
        Session session = sessionFactory.getCurrentSession();
        String sql = "Select new "+CampaignInfo.class.getName() +
        "(c.Id,c.name,c.description,c.organization,c.startDate,c.endDate, count(d.Id),sum(d.amount),c.targetBudget,c.status)"+
                " from Campaign c left join Donation d on c.Id = d.campaign.Id";
        if(status != null && !status.equals("")){
            sql+= " where "+status;
        }
        sql += " group by c.Id,c.name,c.description,c.organization,c.startDate,c.endDate,c.targetBudget,c.status";
        if( orderBy != null && !orderBy.equals("")){
            sql+=" order by "+orderBy;
        }
        Query<CampaignInfo> query = session.createQuery(sql,CampaignInfo.class);

        return new PaginationResult<CampaignInfo>(query, page, maxResult, maxNavigationPage);

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Long saveCampaign(CampaignForm campaignForm) {
        Session session = sessionFactory.getCurrentSession();
        Campaign campaign;
        Organization organization = organizationDAO.findOrganization(campaignForm.getOrganization().getId());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        double targetBudget;

        try {
            targetBudget = numberFormat.parse(campaignForm.getTargetBudget()).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
            log.warn("Can not parse target budget");
            return null;
        }

        if (campaignForm.getId() == null) {
            campaign = new Campaign();
        } else {
            campaign = findCampaign(campaignForm.getId());
        }

        campaign.setName(campaignForm.getName());
        campaign.setDescription(campaignForm.getDescription());
        campaign.setStartDate(campaignForm.getStartDate());
        campaign.setEndDate(campaignForm.getEndDate());
        campaign.setStatus(campaignForm.isStatus());
        campaign.setTargetBudget(targetBudget);
        campaign.setOrganization(organization);

        if(campaignForm.getFileData() != null){
            byte[] image = null;

            try {
                image = campaignForm.getFileData().getBytes();
            } catch (IOException e) {
                log.warn("The image is wrong");
                e.printStackTrace();

            }

            if(image != null && image.length > 0){
                campaign.setImage(image);
            }
        }
        Long campaignId;
        if (campaign.getId() == null){
            campaignId = (Long) session.save(campaign);
        } else {
            campaignId = campaign.getId();
        }
        session.flush();
        return  campaignId;
    }


}
