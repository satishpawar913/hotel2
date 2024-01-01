package com.example.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Rooms;
import com.example.demo.entities.User;
import com.example.demo.repositroy.RoomRepository;
import com.example.demo.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	public List<Rooms> getRoomsByCId(int cId) {
		return roomRepository.findByCId(cId);
	}

	@Override
	public Rooms saveRoom(Rooms rooms) {
		return roomRepository.save(rooms);
	}

	@Override
	public List<Rooms> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	public Optional<Rooms> findRoomById(Integer roomId) {
		return roomRepository.findById(roomId);
	}

	@Override
	public void deleteRoom(int roomId) {
		roomRepository.deleteById(roomId);
	}

}
