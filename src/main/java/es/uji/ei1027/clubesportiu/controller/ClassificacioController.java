package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.ClassificacioDao;
import es.uji.ei1027.clubesportiu.model.Classificacio;
import es.uji.ei1027.clubesportiu.services.ClassificacioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;

@Controller
@RequestMapping("/classificacio")
public class ClassificacioController {
    private ClassificacioService classificacioService;
    private ClassificacioDao classificacioDao;

    @Autowired
    public void setClassificacioController(ClassificacioService classificacioService, ClassificacioDao classificacioDao) {
        this.classificacioService = classificacioService;
        this.classificacioDao = classificacioDao;
    }

    @RequestMapping("/list")
    public String listClassificacio(Model model) {
        model.addAttribute("classificacions", classificacioDao.getClassificacions());
        return "classificacio/list";
    }

    /*@RequestMapping(value="/add")
    public String addClassificacio(Model model) {
        model.addAttribute("classificacio", new Classificacio());
        return "classificacio/add";
    }*/

    /*@RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddClassif(@ModelAttribute("classificacio") Classificacio classificacio, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "classificacio/add";
        try {
            classificacioDao.addClassificacio(classificacio);
        } catch (DuplicateKeyException e) {
            throw new ClubesportiuException(
                    "Ja existeix una classificacio del nadador "
                            + classificacio.getNomNadador() + " per a la prova "
                            + classificacio.getNomProva(), "CPduplicada");
        }
        return "redirect:list";
    }*/
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddClassif(
            @ModelAttribute("classificacio") Classificacio classificacio,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "classificacio/add";
        try {
            classificacioDao.addClassificacio(classificacio);
        } catch (DuplicateKeyException e) {
            throw new ClubesportiuException(
                    "Ja existeix una classificacio d'aquest nadador en "
                            +classificacio.getNomProva(), "CPduplicada");
        } catch (DataAccessException e) {
            throw new ClubesportiuException(
                    "Error en l'accés a la base de dades", "ErrorAccedintDades");
        }
        return "redirect:list";
    }

    /*@RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("classificacio") Classificacio classificacio,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "classificacio/add";
        classificacioDao.addClassificacio(classificacio);
        return "redirect:list";
    }*/

    @RequestMapping(value="/update/{nom}/{nomProva}", method = RequestMethod.GET)
    public String editClassificacio(Model model, @PathVariable String nom, @PathVariable String nomProva) {
        model.addAttribute("classificacio", classificacioDao.getClassificacio(nom,nomProva));
        return "classificacio/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("classificacio") Classificacio classificacio,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "classificacio/update";
        classificacioDao.updateClassificacio(classificacio);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{nNadador}/{nProva}")
    public String processDeleteClassificacio(@PathVariable String nNadador,
                                       @PathVariable String nProva) {
        classificacioDao.deleteClassificacio(nNadador, nProva);
        return "redirect:../../list";
    }

    @RequestMapping("/perpais")
    public String listClsfPerPais(Model model) {
        model.addAttribute("classificacions",
                classificacioService.getClassificationByCountry("Duos Sincro"));
        return "classificacio/perpais";
    }

    @RequestMapping("/perpais/{nomProva}")
    public String nadadorsPerPais(Model model, @PathVariable String nomProva){
        model.addAttribute("classificacions", classificacioService.getClassificationByCountry(nomProva));
        model.addAttribute("nomProva", nomProva);
        return "classificacio/perpais";
    }

    @RequestMapping(value="/addPerProva/{nom}")
    public String addClassifPerProva(Model model,
                                     @PathVariable String nom) {
        Classificacio classificacio = new Classificacio();
        classificacio.setNomProva(nom);
        model.addAttribute("novaclassificacio", classificacio);
        model.addAttribute("classificacions",
                classificacioDao.getClassificacioProva(nom));
        model.addAttribute("nadadors",
                classificacioService.getNadadorsElegiblesPerProva(nom));
        return "classificacio/addPerProva";
    }

    @RequestMapping(value="/addPerProva", method=RequestMethod.POST)
    public String processAddSubmitPerProva(
            @ModelAttribute("classificacio") Classificacio classificacio,
            BindingResult bindingResult) {
        // Here we should include the validation
        // ...
        if (bindingResult.hasErrors())
            return "classificacio/addPerProva";
        classificacioDao.addClassificacio(classificacio);
        String nameUri="redirect:addPerProva/" + classificacio.getNomProva();
        nameUri = UriUtils.encodePath(nameUri, "UTF-8");
        return nameUri;
    }

}