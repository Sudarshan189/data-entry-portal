package com.sudarshan.portal.domain.repository;

import com.sudarshan.portal.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created By Sudarshan Shanbhag
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
