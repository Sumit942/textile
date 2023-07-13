package com.example.textile.controller;

import com.example.textile.action.InvoiceSubmitAction;
import com.example.textile.command.InvoiceCommand;
import com.example.textile.constants.CommandConstants;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.*;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.InvoiceService;
import com.example.textile.service.InvoiceViewService;
import com.example.textile.service.ProductDetailService;
import com.example.textile.utility.PdfUtility;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.ThymeleafTemplateUtility;
import com.example.textile.utility.factory.ActionExecutorFactory;
import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/invoices")
public class InvoiceController extends BaseController {

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    InvoiceViewService viewService;
    @Autowired
    ProductDetailService productDetailService;

    private Map<String, ActionExecutor> actionExecutorMap;

    @Autowired
    ThymeleafTemplateUtility templateUtility;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(InvoiceController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new InvoiceSubmitAction(invoiceService));
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(dateFormat,true);
        dataBinder.registerCustomEditor(Date.class, dateEditor);
    }

    @GetMapping
    public ModelAndView findAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                @RequestParam(required = false, defaultValue = "invoiceNo") String fieldName,
                                ModelMap model) {
        ModelAndView modelAndView = new ModelAndView("/invoiceList");
//        List<InvoiceView> invoices = viewService.findAllOrderByAndLimit(fieldName,pageNumber,pageSize);
        String isRedirect = (String) model.getAttribute("isRedirect");
        if ("YES".equals(isRedirect)) {
            log.info("findAll() | Entry [isRedirect=YES]");
        } else {
            log.info("findAll() | Entry [isRedirect=NO]");
            Page<InvoiceView> invoices = viewService.findAllByPageNumberAndPageSizeOrderByField(pageNumber, pageSize, fieldName);
            modelAndView.addObject("invoices", invoices);
            modelAndView.addObject("printInvoice", true);
        }
        return modelAndView;
    }

    @PostMapping
    public ModelAndView invoiceReport(@RequestParam(value = "fromDate", required = false) Date fromDate,
                                       @RequestParam(value="toDate", required = false) Date toDate,
                                       @RequestParam(value = "invoiceNo", required = false) String invoiceNo,
                                       @RequestParam(value = "companyId", required = false) Long companyId,
                                       @RequestParam(value = "companyId", required = false) String chNo,
                                      RedirectAttributes redirectAttributes) {
        String logPrefix = "invoiceReport() ";
        log.info("{} Entry",logPrefix);
        ModelAndView modelAndView = new ModelAndView("redirect:/invoices");

        List<InvoiceView> invoiceReport = null;
        if (null != chNo && !chNo.isEmpty()) {
            log.info("{} findByChNo", logPrefix);
            List<Long> invoiceId = new ArrayList<>();
            List<ProductDetail> productDetails = productDetailService.findByChNo(chNo);
            if (productDetails != null)
                productDetails.stream().forEach(e -> invoiceId.add(e.getInvoice().getId()));
            if (!invoiceId.isEmpty())
                invoiceReport = viewService.findByInvoiceId(invoiceId);

        } else {
            invoiceReport = viewService.getInvoiceReport(fromDate, toDate, invoiceNo, companyId);
        }
        Page<InvoiceView> invoiceViewsReportPage = new PageImpl<InvoiceView>(invoiceReport);
        redirectAttributes.addFlashAttribute("invoices",invoiceViewsReportPage);
        redirectAttributes.addFlashAttribute("isRedirect","YES");
        log.info("{} Exit",logPrefix);

        return modelAndView;
    }

    @GetMapping("/invoice/{id}")
    public ModelAndView getInvoiceById(@PathVariable("id") Long id,ModelMap model) {
        log.info("show invoice");
        ModelAndView modelView = new ModelAndView("/invoice");
        try {
            Invoice invoice = invoiceService.finById(id);
            actionExecutorMap.get(ActionType.SUBMIT.getActionType()).prePopulateOptionsAndFields(invoice, model);
            model.addAttribute(CommandConstants.INVOICE_COMMAND,invoice);
            model.addAttribute("printInvoice",true);
        } catch (Throwable e) {
            log.error("Exception: {} prePopulation","getInvoiceById()",e);
        }
        return modelView;
    }

    @GetMapping("/submit")
    public ModelAndView getInvoice(@ModelAttribute(CommandConstants.INVOICE_COMMAND) Invoice invoice,
                                   BindingResult result,ModelMap model, RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) throws ServiceActionException {
        log.info("show invoice");

        String action = request.getParameter(TextileConstants.ACTION);
        if (action != null) {
            //getting from session if redirected from demoInvoicePrint
            Object demoInvoicePrint = request.getSession().getAttribute(TextileConstants.DEMO_INVOICE_PRINT);
            if (demoInvoicePrint != null) {
                log.info("getting invoice from session");
                request.getSession().removeAttribute(TextileConstants.DEMO_INVOICE_PRINT);
                if ("edit".equals(action)) {
                    //if user wants to edit from print view
                    copyInvoice(demoInvoicePrint, invoice);
                } else if ("save".equals(action)) {
                    if (demoInvoicePrint instanceof Invoice)
                        return saveInvoice((Invoice) demoInvoicePrint, result,request,model,redirectAttributes);
                }
            }
        }
        ModelAndView modelView = new ModelAndView("invoice");
        try {
            actionExecutorMap.get(ActionType.SUBMIT.getActionType()).prePopulateOptionsAndFields(invoice, model);
        } catch (InvalidObjectPopulationException e) {
            log.error("Exception: {} prePopulation","getInvoice()",e);
        }
        return modelView;
    }

    private void copyInvoice(Object from, Invoice to) {
        if (from instanceof Invoice){
            Invoice temp = (Invoice) from;
            to.setInvoiceNo(temp.getInvoiceNo());
            to.setUser(temp.getUser());
            to.setTransportMode(temp.getTransportMode());
            to.setInvoiceDate(temp.getInvoiceDate());
            to.setVehicleNo(temp.getVehicleNo());
            to.setReverseCharge(temp.getReverseCharge());
            to.setDateOfSupply(temp.getDateOfSupply());
            to.setPlaceOfSupply(temp.getPlaceOfSupply());
            to.setBillToParty(temp.getBillToParty());
            to.setShipToParty(temp.getShipToParty());
            to.setSaleType(temp.getSaleType());
            to.setPnfCharge(temp.getPnfCharge());
            to.setTotalAmount(temp.getTotalAmount());
            to.setcGst(temp.getcGst());
            to.setsGst(temp.getsGst());
            to.setiGst(temp.getiGst());
            to.setGstPerc(temp.getGstPerc());
            to.setTotalTaxAmount(temp.getTotalTaxAmount());
            to.setRoundOff(temp.getRoundOff());
            to.setTotalAmountAfterTax(temp.getTotalAmountAfterTax());
            to.setTotalInvoiceAmountInWords(temp.getTotalInvoiceAmountInWords());
            if (to.getProduct() != null) {
                to.getProduct().clear();
                to.getProduct().addAll(temp.getProduct());
            } else {
                to.setProduct(temp.getProduct());
            }
            to.setSelectedBank(temp.getSelectedBank());
        }
    }

    @PostMapping("/submit")
    public ModelAndView saveInvoice(@Valid @ModelAttribute(CommandConstants.INVOICE_COMMAND) Invoice invoice,
                                    BindingResult result, HttpServletRequest request,
                                    ModelMap model, RedirectAttributes redirectAttr) throws ServiceActionException {
        ModelAndView modelAndView =  new ModelAndView("invoice");
        String logPrefix = "saveInvoice() |";
        log.info("{} Entry -> {}",logPrefix, invoice);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
        parameterMap.put(TextileConstants.USER,getLoggedInUser());

        ActionExecutor actExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        ActionResponse response;

        Invoice oldInvoiceBackUp = getNewInstanceOfInvoice(invoice);

        try {
            response = actExecutor.execute(invoice, parameterMap, result,model);
            if (ResponseType.SUCCESS.equals(response.getResponseType())) {
                modelAndView.setViewName("redirect:/invoices");
                log.info("{} saved Successfully!!", logPrefix);
                redirectAttr.addFlashAttribute(CommandConstants.INVOICE_COMMAND,invoice);
                redirectAttr.addFlashAttribute("printInvoice",true);
                redirectAttr.addFlashAttribute("actionResponse",response);
                redirectAttr.addFlashAttribute("successMessage",
                        messageSource.getMessage("ActionResponse.Success.Submit",
                                                    new Object[]{invoice.getInvoiceNo(),invoice.getId()},
                                                    request.getLocale()));
            } else {
                log.error("result has doValidation Errors");
                result.getAllErrors().forEach(System.out::println);
                log.info("{} save Unsuccessfull", logPrefix);
            }
        } catch (DataAccessException e) {
            log.error("DB_Error: in saving invoice: ",e);
            response = new ActionResponse(ResponseType.FAILURE);
            if (e instanceof ObjectOptimisticLockingFailureException) {
                response.addErrorMessage(messageSource.getMessage("System.Exception.Optimistic",new Object[]{invoice.getInvoiceNo(),invoice.getId()},request.getLocale()));
            } else {
                response.addErrorMessage(messageSource.getMessage("System.Exception.DB",null,request.getLocale()));
            }
            model.addAttribute("actionResponse",response);
            actExecutor.prePopulateOptionsAndFields(oldInvoiceBackUp, model);
            model.addAttribute(CommandConstants.INVOICE_COMMAND,oldInvoiceBackUp);
        } catch (Throwable e) {
            log.error("SystemError: in saving invoice", e);
            response = new ActionResponse(ResponseType.FAILURE);
            String message = messageSource.getMessage("System.Error",null,request.getLocale());
            response.addErrorMessage(message);
            model.addAttribute("actionResponse",response);
            actExecutor.prePopulateOptionsAndFields(oldInvoiceBackUp, model);
            model.addAttribute(CommandConstants.INVOICE_COMMAND,oldInvoiceBackUp);
        }
        log.info("{} Exit",logPrefix);
        return modelAndView;
    }

    private Invoice getNewInstanceOfInvoice(Invoice invoice) {

        Invoice invoiceBackUp = new Invoice();
        invoiceBackUp.setId(invoice.getId());
        invoiceBackUp.setInvoiceNo(invoice.getInvoiceNo());
        invoiceBackUp.setUser(invoice.getUser());
        invoiceBackUp.setTransportMode(invoice.getTransportMode());
        invoiceBackUp.setInvoiceDate(invoice.getInvoiceDate());
        invoiceBackUp.setVehicleNo(invoice.getVehicleNo());
        invoiceBackUp.setReverseCharge(invoice.getReverseCharge());
        invoiceBackUp.setDateOfSupply(invoice.getDateOfSupply());
        invoiceBackUp.setPlaceOfSupply(invoice.getPlaceOfSupply());
        invoiceBackUp.setBillToParty(invoice.getBillToParty());
        invoiceBackUp.setShipToParty(invoice.getShipToParty());
        invoiceBackUp.setSaleType(invoice.getSaleType());
        invoiceBackUp.setPnfCharge(invoice.getPnfCharge());
        invoiceBackUp.setTotalAmount(invoice.getTotalAmount());
        invoiceBackUp.setcGst(invoice.getcGst());
        invoiceBackUp.setsGst(invoice.getsGst());
        invoiceBackUp.setiGst(invoice.getiGst());
        invoiceBackUp.setGstPerc(invoice.getGstPerc());
        invoiceBackUp.setTotalTaxAmount(invoice.getTotalTaxAmount());
        invoiceBackUp.setRoundOff(invoice.getRoundOff());
        invoiceBackUp.setTotalAmountAfterTax(invoice.getTotalAmountAfterTax());
        invoiceBackUp.setTotalInvoiceAmountInWords(invoice.getTotalInvoiceAmountInWords());
        invoiceBackUp.setProduct(invoice.getProduct());
        // sorting to show ProductList in UI with ascending order
        invoiceBackUp.getProduct().sort(Comparator.comparing(ProductDetail::getChNo));
        invoiceBackUp.setSelectedBank(invoice.getSelectedBank());

        return invoiceBackUp;
    }

    @GetMapping("/deleteByInvoiceNo")
    public ModelAndView deleteInvoiceByBy(@RequestParam String invoiceNo,HttpServletRequest request, RedirectAttributes redirectAttributes) {
        invoiceService.deleteByInvoiceNo(invoiceNo);
        String msg = messageSource.getMessage("invoice.delete.SUCCESS",new Object[]{invoiceNo},request.getLocale());
        redirectAttributes.addFlashAttribute("deleted",msg);
        return new ModelAndView("redirect:/invoices");
    }

    @GetMapping("/bankInvoice")
    public @ResponseBody Invoice showNewInvoice() {
        return new Invoice();
    }
    @GetMapping("/blankCompany")
    public @ResponseBody Company showNewCompany() {
        return new Company();
    }
    @GetMapping("/bankState")
    public @ResponseBody State showState() {
        return new State();
    }

    @GetMapping("/printById/{id}")
    public ModelAndView showPrintInvoice(@PathVariable("id") Long id) {
        log.info("showPrintInvoice() | id-"+id);
        ModelAndView modelAndView = new ModelAndView("emailTemplates/SRTI_Invoice.html");
        Invoice invoice = invoiceService.finById(id);
        log.info("showPrintInvoice() | {}",invoice);
        modelAndView.addObject("invoice",invoice);
        return modelAndView;
    }

    @PostMapping("/demoInvoicePrint")
    public ModelAndView showDemoPrintView(@ModelAttribute Invoice invoice,HttpServletRequest request) {
        log.info("showDemoPrintView() | Entry");
        //saving the invoice in session if user wants to save after seeing the print view
        request.getSession().setAttribute(TextileConstants.DEMO_INVOICE_PRINT,invoice);

        ModelAndView modelAndView = new ModelAndView("emailTemplates/SRTI_Invoice.html");
        modelAndView.addObject("invoice",invoice);
        return modelAndView;
    }

    //@GetMapping("/downloadPdf")
    public void downloadToPdf(@RequestParam(name = "invoiceNo") String invoiceNo,
                              HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {

        String logPrefix = "downloadToPdf() |";
        String logSuffix = "";
        log.info("{} Entry",logPrefix);
        List<Invoice> invoices = invoiceService.findByInvoiceNo(invoiceNo);
        if (invoices != null && !invoices.isEmpty()) {
            Invoice invoice = invoices.get(0);
            logSuffix += "id="+invoice.getId()+";InvoiceNo="+invoice.getInvoiceNo()+";";
            String process = templateUtility
                    .getProcessedTemplate("emailTemplates/SRTI_Invoice.html","invoice",invoice);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfUtility.createPdf(os,process);

            byte[] content = os.toByteArray();

            response.setHeader("Content-Disposition","attachment; filename=Invtesting.pdf");
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
        }

        log.info("{} Exit {}", logPrefix, logSuffix);
    }


    @PostMapping("/report")
    public ModelAndView showReport(@ModelAttribute Invoice invoice, RedirectAttributes redirectAttr) {

        return new ModelAndView("redirect:/report");
    }
}
