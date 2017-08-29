package com.algaworks.brewer.controller;

import com.algaworks.brewer.dto.PeriodoRelatorio;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.*;
import java.util.*;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {

    @GetMapping("/vendasEmitidas")
    public ModelAndView relatorioForm() {
        ModelAndView mv = new ModelAndView("relatorio/RelatorioVendasEmitidas");
        mv.addObject(new PeriodoRelatorio());
        return mv;
    }

    @PostMapping("/vendasEmitidas")
    public ModelAndView relatorio(PeriodoRelatorio periodoRelatorio) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("format", "pdf");
        parameters.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        parameters.put("data_inicio", Date.from(LocalDateTime.of(periodoRelatorio.getDataInicio(), LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant()));
        parameters.put("data_fim", Date.from(LocalDateTime.of(periodoRelatorio.getDataFim(), LocalTime.of(23, 59, 59)).atZone(ZoneId.systemDefault()).toInstant()));
        return new ModelAndView("relatorio_vendas_emitidas", parameters);
    }
}
