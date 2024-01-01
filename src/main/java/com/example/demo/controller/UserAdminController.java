package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Rooms;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;
import com.example.demo.repositroy.EmployeeRepository;
import com.example.demo.repositroy.RoomRepository;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.RoomService;
import com.example.demo.service.UserService;

@Controller
public class UserAdminController {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private RoomService roomService;

	@RequestMapping(value = { "/admin" })
	public String adminMain(Model model) {
		List<User> listEmployees = userService.getUserByRole("ROLE_ADMIN");
		model.addAttribute("listEmployees", listEmployees);
		return "admin1";
	}

	@RequestMapping(value = { "/admin/list" })
	public String admin(Model model) {
		List<Rooms> listOfRooms = roomService.getAllRooms();
		model.addAttribute("listOfRooms", listOfRooms);
		return "listOfUser";
	}

	@RequestMapping(value = { "/admin/manage" })
	public String adminManage(Model model) {
		List<Rooms> listOfRooms = roomService.getAllRooms();
		model.addAttribute("listOfRooms", listOfRooms);
		return "edit_user";
	}

	@RequestMapping(value = { "/admin/manage/edit/{roomId}" }, method = RequestMethod.GET)
	public String editRoom(@PathVariable("roomId") Integer roomId, Model model) {
		Optional<Rooms> optionalRoom = roomService.findRoomById(roomId);
		if (optionalRoom.isPresent()) {
			Rooms room = optionalRoom.get();
			model.addAttribute("room", room);
			return "update";
		} else {
			return "redirect:/admin/list";
		}
	}

	/*
	 * @RequestMapping(value = { "/admin/manage/update" }, method =
	 * RequestMethod.POST) public String updateRoom(@ModelAttribute("room") Rooms
	 * room, HttpSession session) { roomService.saveRoom(room);
	 * session.setAttribute("message", new
	 * Message("Contact updated Successfully...", "success")); return "update"; }
	 */
	@RequestMapping(value = { "/admin/manage/update" }, method = RequestMethod.POST)
	public String updateRoom(@ModelAttribute("room") Rooms updatedRoom, HttpSession session) {
		// Fetch the existing room from the database
		Optional<Rooms> optionalExistingRoom = roomService.findRoomById(updatedRoom.getrId());

		if (optionalExistingRoom.isPresent()) {
			Rooms existingRoom = optionalExistingRoom.get();

			// Update the existing room with the new data
			existingRoom.setFirst_name(updatedRoom.getFirst_name());
			existingRoom.setLast_name(updatedRoom.getLast_name());
			existingRoom.setEmail(updatedRoom.getEmail());
			// Set other properties accordingly...

			// Save the updated room
			roomService.saveRoom(existingRoom);

			session.setAttribute("message", new Message("Room updated successfully...", "success"));
			return "redirect:/admin/manage/edit/" + existingRoom.getrId();
		} else {
			session.setAttribute("message", new Message("Room not found...", "danger"));
			return  "redirect:/admin/manage";
		}
	}

	@RequestMapping(value = { "/admin/manage/delete/{roomId}" }, method = RequestMethod.GET)
	public String deleteRoom(@PathVariable("roomId") Integer roomId) {
		roomService.deleteRoom(roomId);
		return "redirect:/admin/manage";
	}

//	@RequestMapping(value = { "/employee" }, method = RequestMethod.GET)
//	public String commonEmployee(Model model, Principal principal) {
//		String name = principal.getName();
//		User user = userService.getUserByUserName(name);
//		model.addAttribute("employee", user);
//		return "hotel";
//	}

//	@RequestMapping(value = { "/admin/delete/{cid}" }, method = RequestMethod.GET)
//	public String deleteUser(@PathVariable("cid") Integer cId, Model model, HttpSession session) {
//		try {
//			userService.deleteUser(cId);
//			session.setAttribute("message", new Message("Contact deleted Successfully...", "success"));
//		} catch (Exception e) {
//			// Log the exception or handle it as needed
//			e.printStackTrace();
//			session.setAttribute("message", new Message("Error deleting contact.", "error"));
//		}
//		return "redirect:/admin";
//	}
//
//	@RequestMapping(value = { "/admin/update/{cid}" })
//	public String updateUser(@PathVariable("cid") Integer cId, Model model, HttpSession session) {
//		User user = userService.getUserById(cId);
//		model.addAttribute("user", user);
//		model.addAttribute("userRooms", user.getRooms());
//		return "admin_userinfo";
//	}
//
//	@RequestMapping(value = { "/admin/do_update" }, method = RequestMethod.POST)
//	public String processUpdate(@ModelAttribute User user, HttpSession session) {
//		if (user.getRole().equals("ROLE_ADMIN")) {
//			employeeRepository.save(user);
//		}
//		userService.saveUser(user);
//		session.setAttribute("message", new Message("Contact updated Successfully...", "success"));
//		return "admin_userinfo";
//	}
//
	@RequestMapping(value = { "/billing" })
	public String userBillinginfo(@ModelAttribute("rooms") Rooms rooms, Model model, HttpSession session,
			Principal principal) {
		String name = principal.getName();
		User user = userService.getUserByUserName(name);
		int cId = user.getId();
		List<Rooms> roomReservations = roomService.getRoomsByCId(cId);
		model.addAttribute("user", user);
		model.addAttribute("userRooms", roomReservations);
		return "billing";
	}

}
