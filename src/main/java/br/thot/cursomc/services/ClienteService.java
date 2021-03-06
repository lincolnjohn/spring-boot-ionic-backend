package br.thot.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.thot.cursomc.domain.Cidade;
import br.thot.cursomc.domain.Cliente;
import br.thot.cursomc.domain.Endereco;
import br.thot.cursomc.domain.enums.Perfil;
import br.thot.cursomc.domain.enums.TipoCliente;
import br.thot.cursomc.dto.ClienteDTO;
import br.thot.cursomc.dto.ClienteNewDTO;
import br.thot.cursomc.repositories.ClienteRepository;
import br.thot.cursomc.repositories.EnderecoRepository;
import br.thot.cursomc.secutrity.UserSS;
import br.thot.cursomc.services.exceptions.AutorizationException;
import br.thot.cursomc.services.exceptions.DataIntegrityException;
import br.thot.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AutorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: "+id+", Tipo: "+Cliente.class.getName()));
		
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
		
	}
	
	public Cliente update(Cliente obj) {
		
		Cliente newObj =  find(obj.getId());
		updateData(newObj,obj);
		return repo.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}


	public void delete(Integer id) {
		find(id);
		try {
			
			repo.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionadas");
		}
		
		
		
	}

	public List<Cliente> findAll() {
		
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(),objDTO.getNome(),objDTO.getEmail(),null,null,null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		
		Cliente cli = new Cliente(null,objDTO.getNome(),objDTO.getEmail(),objDTO.getCpfOuCnpj(),TipoCliente.toEnum(objDTO.getTipo()),pe.encode(objDTO.getSenha()));
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		if(objDTO.getTelefone2()!=null) {
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		 
		if(objDTO.getTelefone3()!=null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		
		return cli;
	}


}
