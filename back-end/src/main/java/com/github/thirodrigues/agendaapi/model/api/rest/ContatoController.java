package com.github.thirodrigues.agendaapi.model.api.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.thirodrigues.agendaapi.model.entity.Contato;
import com.github.thirodrigues.agendaapi.model.repository.ContatoRepository;

@RestController
@RequestMapping("/api/contatos")
@CrossOrigin("*")
public class ContatoController {

	@Autowired
	private ContatoRepository contatoRepository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Contato save( @RequestBody Contato contato ) {
		return contatoRepository.save(contato);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete( @PathVariable Integer id ) {
		contatoRepository.deleteById(id);
	}
	
	@GetMapping
	public Page<Contato> contatos( 
		@RequestParam(value= "page", defaultValue = "0") Integer pagina,
		@RequestParam(value= "size", defaultValue = "10") Integer tamanhoPagina
	){
		Sort sort = Sort.by(Sort.Direction.ASC, "nome");
		PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina, sort);
		return contatoRepository.findAll(pageRequest);
	}
	
	@PatchMapping("/{id}/favorito")
	public void favorite( @PathVariable Integer id ) {
		Optional<Contato> contatoId = contatoRepository.findById(id);
		contatoId.ifPresent( c -> {
			boolean favorito = c.getFavorito() == Boolean.TRUE;
			c.setFavorito(!favorito);
			contatoRepository.save(c);
		});
	}
	
	@PutMapping("/{id}/foto")
	public byte[] addPhoto( @PathVariable Integer id, @RequestParam("foto") Part arquivo ) {
		Optional<Contato> contatoId = contatoRepository.findById(id);
		return contatoId.map( c -> {
			try {
				InputStream inputStream = arquivo.getInputStream();
				byte[] bytes = new byte[(int) arquivo.getSize()];
				IOUtils.readFully(inputStream, bytes);
				c.setFoto(bytes);
				contatoRepository.save(c);
				inputStream.close();
				return bytes;
			} catch (IOException e) {
				return null;
			}
		}).orElse(null);
	}
}






