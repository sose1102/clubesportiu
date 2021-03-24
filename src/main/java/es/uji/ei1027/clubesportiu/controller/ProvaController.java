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

    
}
