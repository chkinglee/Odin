package org.chkinglee.norn.odin.mapper;

import org.chkinglee.norn.odin.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/31
 **/
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    List<Tenant> findByTenantAndModule(String tenant, String module);
}
