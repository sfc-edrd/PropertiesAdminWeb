package local.lab.learning.creative.properties_admin_web.controller;

import local.lab.learning.creative.properties_admin_web.configuration.PropertiesConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

@Controller
@Slf4j
public class PropertiesAdminResource
{
    @Autowired
    private PropertiesConfiguration properties;

    @GetMapping(path = "properties-admin.html")
    public ModelAndView getPropertiesAdminPage(Model model,
                                               HttpServletRequest request,
                                               @ModelAttribute("error") String error,
                                               @ModelAttribute("success") String success)
    {
        Properties  propertiesLoaded;


        log.info("New Get request received from " + request.getRemoteAddr());
        propertiesLoaded = properties.getProperties();
        log.debug(String.format("Properties loaded: %s", propertiesLoaded));
        model.addAttribute("propertyList", propertiesLoaded.entrySet());

        return (new ModelAndView("properties_admin.html"));
    }

    @PostMapping(path = "properties-admin.html", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public RedirectView postPropertiesAdminPage(@RequestBody MultiValueMap<String, Object> entries,
                                                HttpServletRequest request,
                                                RedirectAttributes redirectAttributes)
    {
        log.info("New Post request received from " + request.getRemoteAddr());
        if (properties.setProperties(entries))
            redirectAttributes.addAttribute("success", "Properties saved successfully. An application restart is required for changes to take effect");
        else redirectAttributes.addAttribute("error", "An issue occurred while the properties were being saved. Please contact the administrator");
        log.debug(String.format("Redirect attributes after Post: %s", redirectAttributes));
        return (new RedirectView("/properties-admin.html"));
    }
}
