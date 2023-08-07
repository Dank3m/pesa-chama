package com.kinduberre.chama.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kinduberre.chama.models.auth.Role;


@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>{
	public Role findByRole(String role);
}
