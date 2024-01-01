package com.example.demo.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.entities.Rooms;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;
import com.example.demo.repositroy.EmployeeRepository;
import com.example.demo.repositroy.RoomRepository;
import com.example.demo.repositroy.UserRepository;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.RoomService;
import com.example.demo.service.UserService;

@Controller
public class TestController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoomRepository roomRepository;


	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String start() {
		return "login";
	}

	@RequestMapping(value = { "/signup" })
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@RequestMapping(value = { "/hotel" }, method = RequestMethod.GET)
	public String hotel() {
		return "hotel";
	}

	@RequestMapping(value = { "/room_booking" })
	public String room_booking(Model model) {
		model.addAttribute("rooms", new Rooms());
		return "room_booking";
	}

	@RequestMapping(value = { "/facilities" })
	public String facilities() {
		return "facilities";
	}

	@RequestMapping(value = { "/restaurant" })
	public String restaurant() {
		return "restaurant";
	}

	@RequestMapping(value = { "/user/data" })
	public String userdashboard() {
		return "userdashboard";
	}

	@RequestMapping(value = { "/register" }, method = RequestMethod.POST)
	public RedirectView registerUser(@ModelAttribute("user") User user, Model model, HttpSession session,
			RedirectAttributes redir) {

		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println("USER " + user);

		User result = this.userRepository.save(user);

		RedirectView redirectView = new RedirectView("/login", true);
		redir.addFlashAttribute("message", "You successfully registered! You can now login");
		return redirectView;

	}

	@RequestMapping(value = { "/process_booking" }, method = RequestMethod.POST)
	public String reserveRoom(@ModelAttribute("rooms") Rooms rooms, Model model, HttpSession session,
			Principal principal) {
		if(principal!=null) {
		String name = principal.getName();
		User user = userService.getUserByUserName(name);
		int cId = user.getId();
		rooms.setcId(cId);
		user.getRooms().add(rooms);
		}else {
			
		}

		if (rooms.getCategory().equals("Luxury Room")) {
			rooms.setPrice(15999);
		} else if (rooms.getCategory().equals("Luxury Grande Room City View")) {
			rooms.setPrice(21999);
		} else if (rooms.getCategory().equals("Luxury Grande Room Sea View")) {
			rooms.setPrice(30999);
		} else {
			rooms.setPrice(0);
		}

		// Calculate total days and total price...
		System.out.println("Rooms " + rooms);
		String checkIn = rooms.getCheckinDate();
		String checkOut = rooms.getCheckoutDate();
		try {
			Date checkInDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkIn);
			System.out.println("The check in date in date format: " + checkInDate);
			Date checkOutDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkOut);
			System.out.println("The check out date in date format: " + checkOutDate);
			long diff = checkOutDate.getTime() - checkInDate.getTime();
//		System.out.println("The difference between the two dates is: "+diff);
			TimeUnit time = TimeUnit.DAYS;
			long difference = time.convert(diff, TimeUnit.MILLISECONDS);
			System.out.println("The difference in days is : " + difference);
			rooms.setTotal_days(difference);
//		System.out.println(checkOutDate-checkInDate);
			long a = rooms.getPrice();
			long b = a * difference;
			rooms.setTotal_price(b);

		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Rooms reservation = roomService.saveRoom(rooms);

		model.addAttribute("rooms", new Rooms());
		session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
		return "room_booking";
	}

	@RequestMapping(value = { "/userdashboard" })
	public String userDashboardInfo(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}
		// Assuming the userRepository is autowired in your controller
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);

		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("user", user);
		System.out.println("User " + user);

		return "userdashboard";
	}

}
