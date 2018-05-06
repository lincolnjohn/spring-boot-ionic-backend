package br.thot.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.thot.cursomc.domain.Categoria;
import br.thot.cursomc.domain.Cidade;
import br.thot.cursomc.domain.Cliente;
import br.thot.cursomc.domain.Endereco;
import br.thot.cursomc.domain.Estado;
import br.thot.cursomc.domain.Produto;
import br.thot.cursomc.domain.enums.TipoCliente;
import br.thot.cursomc.repositories.CategoriaRepository;
import br.thot.cursomc.repositories.CidadeRepository;
import br.thot.cursomc.repositories.ClienteRepository;
import br.thot.cursomc.repositories.EnderecoRepository;
import br.thot.cursomc.repositories.EstadoRepository;
import br.thot.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	
	

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null,"Informática");
		Categoria cat2 = new Categoria(null,"Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 20.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		
		categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		Estado est1 = new Estado(null, "Para");
		Estado est2 = new Estado(null, "Rio de Janeiro");
		
		Cidade cid1= new Cidade(null, "Belém", est1);
		Cidade cid2= new Cidade(null, "Santarém", est1);
		Cidade cid3= new Cidade(null, "Rio de Janeiro", est2);
		Cidade cid4= new Cidade(null, "Parati", est2);
		
		est1.getCidades().addAll(Arrays.asList(cid1,cid2));
		est2.getCidades().addAll(Arrays.asList(cid3,cid4));
		
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(cid1,cid2,cid3,cid4));
		
		Cliente cli1 = new Cliente(null, "Incrivel Hulk", "hulk@email.com", "1223334343", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("234454555","5544666"));
		
		Endereco end1 = new Endereco(null, "Rua verde forte", "344", "perto do beco", "Vingadores", "665544", cli1, cid2);
		Endereco end2 = new Endereco(null, "Avenida Verde gama", "444", "próximo posto", "Radiação Gama", "55666", cli1, cid1);
		
		cli1.getEnderecos().addAll(Arrays.asList(end1,end2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(end1,end2));
		
	}
}












