package com.example.demo.repositroy;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Rooms;
import com.example.demo.entities.User;

@Repository
public interface RoomRepository extends JpaRepository<Rooms, Integer> {

	@Query("select u from Rooms u where u.cId = :cId")
	public List<Rooms> getUserByCId(@Param("cId") int cId);

	@Query("select u from Rooms u where u.cId = :cId")
	List<Rooms> findByCId(int cId);
	
	@Query("select u from Rooms u where u.roomId = :roomId")
	Optional<Rooms> findRoomById(Integer roomId);

	void deleteById(int id);
}
