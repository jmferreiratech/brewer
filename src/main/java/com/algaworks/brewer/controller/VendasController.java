package com.algaworks.brewer.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.model.ItemVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.controller.validator.VendaValidator;
import com.algaworks.brewer.mail.Mailer;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.security.UsuarioSistema;
import com.algaworks.brewer.service.CadastroVendaService;
import com.algaworks.brewer.session.TabelasItensSession;

@Controller
@RequestMapping("/vendas")
public class VendasController {

	@Autowired
	private Cervejas cervejas;
	@Autowired
	private TabelasItensSession tabelasItens;
	@Autowired
	private CadastroVendaService cadastroVendaService;
	@Autowired
	private VendaValidator vendaValidator;
	@Autowired
	private Vendas vendas;
	@Autowired
	private Mailer mailer;

	@InitBinder("venda")
	public void incializarValidador(WebDataBinder binder) {
		binder.setValidator(vendaValidator);
	}

	@GetMapping("/nova")
	public ModelAndView nova(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		setUuid(venda);
		mv.addObject("itens", venda.getItens());
		mv.addObject("valorFrete", venda.getValorFrete());
		mv.addObject("valorDesconto", venda.getValorDesconto());
		mv.addObject("valorTotalItens", tabelasItens.getValorTotal(venda.getUuid()));
		return mv;
	}

	@PostMapping(value = "/nova", params = "salvar")
	public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes,
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		venda.setUsuario(usuarioSistema.getUsuario());

		cadastroVendaService.salvar(venda);

		attributes.addFlashAttribute("mensagem", "Venda salva com sucesso!");
		return new ModelAndView("redirect:/vendas/nova");
	}

	@PostMapping(value = "/nova", params = "emitir")
	public ModelAndView emitir(Venda venda, BindingResult result, RedirectAttributes attributes,
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		venda.setUsuario(usuarioSistema.getUsuario());

		cadastroVendaService.emitir(venda);

		attributes.addFlashAttribute("mensagem", "Venda emitida com sucesso!");
		return new ModelAndView("redirect:/vendas/nova");
	}

	@PostMapping(value = "/nova", params = "enviarEmail")
	public ModelAndView enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes,
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		venda.setUsuario(usuarioSistema.getUsuario());

		Venda vendaSalva = cadastroVendaService.salvar(venda);
		mailer.enviar(vendaSalva);

		attributes.addFlashAttribute("mensagem",
				String.format("Venda nº %d salva e e-mail enviado!", venda.getCodigo()));
		return new ModelAndView("redirect:/vendas/nova");
	}

	@PostMapping("/item")
	public ModelAndView adicionarItem(Long codigoCerveja, String uuid) {
		Cerveja cerveja = cervejas.findOne(codigoCerveja);
		tabelasItens.adicionarItem(uuid, cerveja, 1);
		return mvTabelaItensVenda(uuid);
	}

	@PutMapping("/item/{codigoCerveja}")
	public ModelAndView alterarQuantidadeItem(@PathVariable("codigoCerveja") Cerveja cerveja, Integer quantidade,
			String uuid) {
		tabelasItens.alterarQuantidadeItens(uuid, cerveja, quantidade);
		return mvTabelaItensVenda(uuid);
	}

	@DeleteMapping("/item/{uuid}/{codigoCerveja}")
	public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja, @PathVariable String uuid) {
		tabelasItens.excluirItem(uuid, cerveja);
		return mvTabelaItensVenda(uuid);
	}

	@GetMapping
	public ModelAndView pesquisar(VendaFilter vendaFilter, BindingResult result,
			@PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("venda/PesquisaVendas");
		mv.addObject("statuses", StatusVenda.values());
		PageWrapper<Venda> paginaWrapper = new PageWrapper<>(vendas.filtrar(vendaFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Venda venda = vendas.buscarComItens(codigo);
		setUuid(venda);
		for (ItemVenda item: venda.getItens()) {
			tabelasItens.adicionarItem(venda.getUuid(), item.getCerveja(), item.getQuantidade());
		}

		ModelAndView mv = nova(venda);
		mv.addObject(venda);
		return mv;
	}

	@PostMapping(value = "/nova", params = "cancelar")
	public ModelAndView cancelar(Venda venda, BindingResult result, RedirectAttributes attributes,
									@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		try {
			cadastroVendaService.cancelar(venda);
		} catch (AccessDeniedException e) {
			return new ModelAndView("/403");
		}
		attributes.addFlashAttribute("mensagem", "Venda cancelada com sucesso");
		return new ModelAndView("redirect:/vendas/" + venda.getCodigo());
	}
		private void setUuid(Venda venda) {
		if (StringUtils.isEmpty(venda.getUuid())) {
			venda.setUuid(UUID.randomUUID().toString());
		}
	}

	@GetMapping("/totalPorMes")
	public @ResponseBody
	List<VendaMes> totalVendaPorMes() {
		return vendas.totalPorMes();
	}

	private ModelAndView mvTabelaItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelasItens.getItens(uuid));
		mv.addObject("valorTotal", tabelasItens.getValorTotal(uuid));
		return mv;
	}

	private void validarVenda(Venda venda, BindingResult result) {
		venda.adicionarItens(tabelasItens.getItens(venda.getUuid()));
		venda.calcularValorTotal();

		vendaValidator.validate(venda, result);
	}
}
