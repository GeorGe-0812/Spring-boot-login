package com.example.login.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.login.dto.UsuarioRegistroDTO;
import com.example.login.servicio.UsuarioServicio;

@Controller
@RequestMapping("/registro")
public class RegistroUsuarioControlador {
	
	private UsuarioServicio usuarioServicio;

	public RegistroUsuarioControlador(UsuarioServicio usuarioServicio) {
		super();
		this.usuarioServicio = usuarioServicio;
	}
	
	@ModelAttribute("usuario")
	public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
		return new UsuarioRegistroDTO();
	}

	@GetMapping
	public String mostrarFormularioDeRegistro() {
		return "registro";
	}

	@PostMapping
	public String registrarCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO) {
		boolean validacion =
		usuarioServicio.validar(registroDTO);
		if (!validacion) {
			return "redirect:/registro?pass_invalido";
		}
		if (usuarioServicio.duplicado(registroDTO) != null) {
			return "redirect:/registro?duplicado";
		}else {
			usuarioServicio.guardar(registroDTO);
			return "redirect:/registro?exito";
		}
		
	}
	
	

}
