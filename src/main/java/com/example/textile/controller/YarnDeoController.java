package com.example.textile.controller;

import com.example.textile.action.YarnDeoSubmitAction;
import com.example.textile.command.YarnDeoCommand;
import com.example.textile.constants.CommandConstants;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.Company;
import com.example.textile.entity.YarnDeo;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.CompanyService;
import com.example.textile.service.YarnDeoService;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/yarnDeo")
public class YarnDeoController extends BaseController {

    @Autowired
    YarnDeoService yarnDeoService;

    @Autowired
    CompanyService companyService;

    private Map<String, ActionExecutor> actionExecutorMap;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(InvoiceController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new YarnDeoSubmitAction(yarnDeoService));
    }

    @GetMapping("/")
    public ModelAndView showYarnDeoForm(
            @ModelAttribute(CommandConstants.YARN_DEO_COMMAND) YarnDeoCommand deoCommand,
            ModelMap model) {

        ModelAndView modelAndView = new ModelAndView("/yarnDemoList");
        List<Company> companyList = companyService.findAll();
        modelAndView.addObject("companyList",companyList);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView addYarnDeoForm(@ModelAttribute(CommandConstants.YARN_DEO_COMMAND) YarnDeo yarnDeo) {

        return new ModelAndView("addYarnDeo");
    }

    @PostMapping("/add")
    public ModelAndView saveYarnDeo(@Valid @ModelAttribute(CommandConstants.YARN_DEO_COMMAND) YarnDeo yarnDeo,
                                    BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) {

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(TextileConstants.USER,getLoggedInUser());

        ActionExecutor<YarnDeo> actExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        ActionResponse actionResponse = null;
        try {
            actionResponse = actExecutor.execute(yarnDeo, parameterMap, result, model);
            if (ResponseType.SUCCESS.equals(actionResponse.getResponseType())) {
                return new ModelAndView("redirect:/");
            }
        } catch (DataAccessException e) {
            log.error("DB_Error: in saving YarnDeo -"+e.getLocalizedMessage(),e);
        } catch (ServiceActionException e) {
            log.error("ServiceError: in saving YarnDeo -"+e.getLocalizedMessage(),e);
        } catch (Throwable e) {
            log.error("Error: in saving YarnDeo -"+e.getLocalizedMessage(),e);
        }

        return new ModelAndView("addYarnDeo");
    }
}
