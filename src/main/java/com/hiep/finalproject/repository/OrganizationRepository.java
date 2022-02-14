package com.hiep.finalproject.repository;

import com.hiep.finalproject.command.OrganizationCommand;
import com.hiep.finalproject.dto.IOrganizationDto;
import com.hiep.finalproject.model.Organization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrganizationRepository extends CrudRepository<Organization,Long> {
    @Query("select o.id as id, o.name as name, o.description as description, " +
            "sum(c.id) as countCampaign, sum(d.amount) as totalMoneyDonation " +
            "from Organization o left join Campaign c on o.id = c.organization.id " +
            "left join Donation d on c.id = d.campaign.id " +
            "group by  o.id, o.name, o.description")
    List<IOrganizationDto> findAllOrganization();

}
