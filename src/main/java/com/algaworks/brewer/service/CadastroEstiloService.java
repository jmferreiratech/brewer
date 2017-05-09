package com.algaworks.brewer.service;

import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;

import javax.persistence.PersistenceException;

@Service
public class CadastroEstiloService {

	@Autowired
	private Estilos estilos;

	@Transactional
	public Estilo salvar(Estilo estilo) {
		if (estilos.findByNomeIgnoreCase(estilo.getNome()).isPresent()) {
			throw new NomeEstiloJaCadastradoException("Nome do estilo já cadastrado");
		}
		return estilos.saveAndFlush(estilo);
	}

	@Transactional
    public void excluir(Long codigo) {
		try {
			estilos.delete(codigo);
			estilos.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar estilo. Existe alguma cerveja desse tipo.");
		}
    }
}
