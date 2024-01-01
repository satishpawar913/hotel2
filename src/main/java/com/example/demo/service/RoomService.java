package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Rooms;
import com.example.demo.entities.User;

public interface RoomService {

	List<Rooms> getRoomsByCId(int cId);

	Rooms saveRoom(Rooms rooms);

	List<Rooms> getAllRooms();

	void deleteRoom(int roomId);

	Optional<Rooms> findRoomById(Integer roomId);

}
