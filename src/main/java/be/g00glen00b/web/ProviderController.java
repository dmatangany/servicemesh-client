package be.g00glen00b.web;

import java.io.IOException;
import java.util.HashMap;
import javax.validation.Valid;
import be.g00glen00b.dto.MessageDTO;
import be.g00glen00b.dto.ProviderDTO;
import be.g00glen00b.service.ProviderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class ProviderController {
    @Autowired
    private ProviderServiceImpl service;
    @Autowired
    private ObjectMapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public String findAll(Model model) {
        model.addAttribute("providers", service.findAll());
        model.addAttribute("newProvider", new ProviderDTO());
        return "providers";
    }

    @RequestMapping(value = "/alert", method = RequestMethod.GET)
    public String alertAll(Model model) {
        ProviderDTO providerDTO = service.alertAll();
        model.addAttribute("newProvider", providerDTO);
        return "alert";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(@RequestParam Long id, ProviderDTO provider) {
        service.update(id, provider);
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(@RequestParam Long id) {
        service.delete(id);
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("newProvider") ProviderDTO provider) {
        service.create(provider);
        if(provider.getDescription().toLowerCase().equals("consent")){
            return "redirect:/orders";
        }else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String send(@Valid @ModelAttribute("newProvider") ProviderDTO provider) {
        service.send(provider);
        System.out.println("sending: " + provider.getId());
        return "redirect:/alert";
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleClientError(HttpClientErrorException ex, Model model) throws IOException {
        MessageDTO dto = mapper.readValue(ex.getResponseBodyAsByteArray(), MessageDTO.class);
        model.addAttribute("error", dto.getMessage());
        return findAll(model);
    }

}
