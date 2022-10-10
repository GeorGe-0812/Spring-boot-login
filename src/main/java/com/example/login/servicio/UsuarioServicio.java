package com.example.login.servicio;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.login.dto.UsuarioRegistroDTO;
import com.example.login.modelo.Usuario;


public interface UsuarioServicio extends UserDetailsService{

	public Usuario guardar(UsuarioRegistroDTO registroDTO);
	
	public Usuario duplicado(UsuarioRegistroDTO registroDTO);
	
	public boolean validar (UsuarioRegistroDTO registroDTO);
	
	public List<Usuario> listarUsuarios();

}
