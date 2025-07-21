package com.example.catgameweb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.catgameweb.model.Role;
import com.example.catgameweb.model.Usuario;
import com.example.catgameweb.repository.RoleRepository;
import com.example.catgameweb.repository.UsuarioRepository;

@SpringBootApplication
public class CatgamewebApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatgamewebApplication.class, args);

	

		
	}


	@Bean
public CommandLineRunner initData(RoleRepository roleRepository, UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
    return args -> {
        if (roleRepository.findByNombre("ADMIN").isEmpty()) {
            Role admin = new Role(); admin.setNombre("ADMIN"); roleRepository.save(admin);
            Role usuario = new Role(); usuario.setNombre("USUARIO"); roleRepository.save(usuario);

            // Admin inicial
            Usuario adminUser = new Usuario();
            adminUser.setNombre("admin");
            adminUser.setEmail("admin@tienda.com");
            adminUser.setPassword(encoder.encode("admin123"));
            adminUser.setRol(admin);
            usuarioRepository.save(adminUser);
        }
    };
}

}
