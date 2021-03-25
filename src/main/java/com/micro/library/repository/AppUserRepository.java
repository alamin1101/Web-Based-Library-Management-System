package com.micro.library.repository;

import com.micro.library.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser,String>
{
    int countAppUsersByUsername(String username);

    List<AppUser> findAllByRole(String role);

    @Query("select new AppUser(b.username,b.email,b.phone) from AppUser b WHERE b.role='ROLE_USER' ")
    List<AppUser> findAllUsers();


}
