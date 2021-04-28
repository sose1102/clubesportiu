package es.uji.ei1027.clubesportiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.uji.ei1027.clubesportiu.dao.ProvaDao;
import es.uji.ei1027.clubesportiu.model.Prova;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/prova")
public class ProvaController {

    private ProvaDao provaDao;

    @Autowired
    public void setProvaDao(ProvaDao provaDao) {
        this.provaDao=provaDao;
    }

    @RequestMapping("/list")
    public String listProva(Model model) {
        model.addAttribute("proves", provaDao.getProves());
        return "prova/list";
    }

    @RequestMapping(value="/update/{nom}", method = RequestMethod.GET)
    public String editNadador(Model model, @PathVariable String nom) {
        model.addAttribute("prova", provaDao.getProva(nom));
        List<String> typeList = Arrays.asList("Individual", "Grupal");
        model.addAttribute("typeList", typeList);
        return "prova/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("prova") Prova prova,
                                      BindingResult bindingResult) {
        // Ací hauriem de incloure la validació
        ProvaValidator provaValidator = new ProvaValidator();
        provaValidator.validate(prova, bindingResult);
        if (bindingResult.hasErrors())
            return "prova/update";
        provaDao.updateProva(prova);
        return "redirect:list";
    }

}
