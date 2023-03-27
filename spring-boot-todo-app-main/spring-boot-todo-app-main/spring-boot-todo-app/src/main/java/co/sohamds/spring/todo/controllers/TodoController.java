package co.sohamds.spring.todo.controllers;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.sohamds.spring.todo.domain.Todo;
import co.sohamds.spring.todo.repository.TodoRepository;
import io.opentelemetry.*;

import static co.sohamds.spring.todo.SpringBootTodoAppApplication.openTelemetry;
import static co.sohamds.spring.todo.SpringBootTodoAppApplication.tracer;

@Controller
public class TodoController {
	@Autowired
	TodoRepository todoRepository;
	
	@GetMapping
	public String index() {
	return "index.html";
}

@GetMapping("/todos")
public String todos(Model model) {
		Span span = tracer.spanBuilder("Get todos").setAttribute("HTTP","GET").startSpan();
model.addAttribute("todos", todoRepository.findAll());
span.end();
return "todos";
}

@PostMapping("/todoNew")
public String add(@RequestParam String todoItem, @RequestParam String status, Model model) {
	Span span = tracer.spanBuilder("Todo_New").setAttribute("HTTP","POST").startSpan();
	Todo todo = new Todo(todoItem, status);
	todo.setTodoItem(todoItem);
	todo.setCompleted(status);
	span.addEvent("Adding to model");
	todoRepository.save(todo);
	model.addAttribute("todos", todoRepository.findAll());

	span.addEvent("Done adding to model");
	span.end();
	return "redirect:/todos";
}

@PostMapping("/todoDelete/{id}")
public String delete(@PathVariable long id, Model model) {
		Span span = tracer.spanBuilder("Todo_Delete").setAttribute("HTTP","POST").startSpan();;
	Span inner = tracer.spanBuilder("Delete By ID").addLink(span.getSpanContext()).startSpan();
		todoRepository.deleteById(id);
		inner.end();
		model.addAttribute("todos", todoRepository.findAll());
		span.end();
		return "redirect:/todos";
}

@PostMapping("/todoUpdate/{id}")
public String update(@PathVariable long id, Model model) {
		Span span = tracer.spanBuilder("Todo Update").setAttribute("HTTP","POST").startSpan();
		Span inner = tracer.spanBuilder("Find By ID").addLink(span.getSpanContext()).startSpan();

	Todo todo = todoRepository.findById(id).get();
	inner.end();
	if("Yes".equals(todo.getCompleted())) {
		span.addEvent("Swapping Yes to No");
	todo.setCompleted("No");
	}
	else {
		span.addEvent("Swapping No to Yes");
	todo.setCompleted("Yes");

	}
	span.addEvent("Done Swapping");
	todoRepository.save(todo);
	model.addAttribute("todos", todoRepository.findAll());
	span.end();
	return "redirect:/todos";
}
}