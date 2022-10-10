package com.example.login.servicio;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.login.dto.UsuarioRegistroDTO;
import com.example.login.modelo.Rol;
import com.example.login.modelo.Usuario;
import com.example.login.repositorio.UsuarioRepositorio;

import ch.qos.logback.core.boolex.Matcher;

@Service
public class UsuarioServicioImpl implements UsuarioServicio{

	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio) {
		super();
		this.usuarioRepositorio = usuarioRepositorio;
	}
	
	public boolean validar (UsuarioRegistroDTO registroDTO){
		String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
		Pattern p = Pattern.compile(regex);
		if (registroDTO.getPassword() == null) {
            return false;
        }
		java.util.regex.Matcher m = p.matcher(registroDTO.getPassword());
		return m.matches();
	}

	@Override
	public Usuario guardar(UsuarioRegistroDTO registroDTO) {
			Usuario usuario = new Usuario(registroDTO.getNombre(), 
					registroDTO.getApellido(),registroDTO.getEmail(),
					passwordEncoder.encode(registroDTO.getPassword()),Arrays.asList(new Rol("ROLE_USER")));
	
			return usuarioRepositorio.save(usuario);
	}

	
	@Override
	public Usuario duplicado(UsuarioRegistroDTO registroDTO) {
		Usuario usuarioloc = usuarioRepositorio.findByEmail(registroDTO.getEmail());
		if(usuarioloc != null) {
			System.out.println("Usuario ya existe");
		}else {
			
		}
		return usuarioloc;

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorio.findByEmail(username);
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuario o password inválidos");
		}
		return new User(usuario.getEmail(),usuario.getPassword(), mapearAutoridadesRoles(usuario.getRoles()));
	}
	
	
	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
	}
	
	@Override
	public List<Usuario> listarUsuarios() {
		return usuarioRepositorio.findAll();
	}

}
