package com.micro.library.repository;

import com.micro.library.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser,String>
{
    int countAppUsersByUsername(String username);
    @Query("select user from AppUser user where user.role='ROLE_ADMIN'")
    List<AppUser>findByRole();


    @Query("select b from AppUser b where b.role=?2 and b.bookBorrowList.size > 0 and (?1 is null or b.username like %?1% or b.email like %?1% or b.phone like %?1%)")
    List<AppUser> findAllByRoleAndSearchValue(String s, String role);

    @Query("select new AppUser(b.username,b.email,b.phone) from AppUser b WHERE b.role='ROLE_USER' and (?1 is null or b.username like %?1% or b.email like %?1% or b.phone like %?1%)  ")
    List<AppUser> findAllUsers(String s);

}
