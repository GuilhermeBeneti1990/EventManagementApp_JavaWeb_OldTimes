package com.eventapp.eventapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventapp.eventapp.entities.Event;
import com.eventapp.eventapp.entities.Guest;
import com.eventapp.eventapp.repository.EventRepository;
import com.eventapp.eventapp.repository.GuestRepository;

@Controller
public class EventController {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private GuestRepository guestRepository;
	
	@RequestMapping(value="/eventRegister", method=RequestMethod.GET)
	public String form() {
		return "event/formEvent";
	}
	
	@RequestMapping(value="/eventRegister", method=RequestMethod.POST)
	public String form(@Validated Event event, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("message", "Check the fields again");
			return "redirect:/eventRegister";
		}
		eventRepository.save(event);
		attributes.addFlashAttribute("message", "Success");
		return "redirect:/eventRegister";
	}
	
	@RequestMapping("/eventList")
	public ModelAndView eventList() {
		ModelAndView mv = new ModelAndView("index.html");
		Iterable<Event> events = eventRepository.findAll();
		mv.addObject("events", events);
		return mv;
	}
	
	@RequestMapping(value="/event/{id}", method=RequestMethod.GET)
	public ModelAndView eventDetail(@PathVariable("id") Long id) {
		Event event = eventRepository.findById(id);
		ModelAndView mv = new ModelAndView("event/eventDetail");
		mv.addObject("event", event);
		
		Iterable<Guest> guests = guestRepository.findByEvent(event);
		mv.addObject("guests", guests);
		
		return mv;
	}
	
	@RequestMapping(value="/event/delete")
	public String eventDelete(Long id) {
		Event event = eventRepository.findById(id);
		eventRepository.delete(event);
		return "redirect:/eventList";
	}
	
	@RequestMapping(value="/guest/delete")
	public String guestDelete(String documentNumber) {
		Guest guest = guestRepository.findByDocumentNumber(documentNumber);
		guestRepository.delete(guest);
		Event event = guest.getEvent();
		Long id = event.getId();
		return "redirect:/event/" + id;
	}
	
	@RequestMapping(value="/event/{id}", method=RequestMethod.POST)
	public String eventDetailPost(@PathVariable("id") Long id, @Validated Guest guest, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("message", "Check the fields again before submit the form again");
			return "redirect:/event/{id}";
		}
		Event event = eventRepository.findById(id);
		guest.setEvent(event);
		guestRepository.save(guest);
		attributes.addFlashAttribute("message", "Success");
		return "redirect:/event/{id}";
	}

}
