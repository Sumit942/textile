package com.example.textile.action;

import com.example.textile.command.ProductDetailCommand;
import com.example.textile.entity.ProductDetail;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.ProductDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProductDetailSubmitAction extends ActionExecutor<ProductDetailCommand> {

    private final ProductDetailService productDetailService;

    public ProductDetailSubmitAction(ProductDetailService productDetailRepository) {
        this.productDetailService = productDetailRepository;
    }

    @Override
    protected ActionResponse onSuccess(ProductDetailCommand productDetailCommand, Map<String, Object> parameterMap, ModelMap model) {
        log.info("onSuccess() Entry");

        List<ProductDetail> productDetails = productDetailService.saveAll(productDetailCommand.getProductDetails());
        productDetailCommand.setProductDetails(productDetails);

        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        log.info("onSuccess() Exit[Save=Success]");
        return actionResponse;
    }

    @Override
    protected void doValidation(ProductDetailCommand command, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {
        log.info("doValidation() Entry");
        String logSuffix = "";
        List<ProductDetail> productDetails = command.getProductDetails();
        Map<String, String> errMap = new HashMap<>();
        if (productDetails == null || productDetails.isEmpty()) {
            errMap.put("productDetails","NotNull.productDetailsCommand");
        } else {
            for (int i = 0; i < productDetails.size(); i++) {
                ProductDetail prod = productDetails.get(i);
                if (prod.getProduct() == null) {
                    errMap.put("productDetails.product","NotNull.invoiceCommand.product.product");
                } else {

                    if (prod.getProduct().getName() == null)
                        errMap.put("productDetails["+i+"].product.name","NotNull.invoiceCommand.product.product.name");
                    if (prod.getProduct().getHsn() == null)
                        errMap.put("productDetails["+i+"].product.hsn","NotNull.invoiceCommand.product.hsn");
                }
                if (prod.getChNo() == null)
                    errMap.put("productDetails["+i+"].chNo","NotNull.invoiceCommand.product.chNo");
                if (prod.getQuantity() == null || prod.getQuantity() <= 0)
                    errMap.put("productDetails["+i+"].quantity","NotNull.invoiceCommand.product.quantity");
                /*if (prod.getRate() == null || prod.getRate() <= 0)
                    errMap.put("product["+i+"].rate","NotNull.invoiceCommand.product.rate");
                if (prod.getTotalPrice() == null || prod.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0)
                    errMap.put("product["+i+"].totalPrice","NotNull.invoiceCommand.product.totalPrice");*/
                if (prod.getUnitOfMeasure() == null || prod.getUnitOfMeasure().getUnitOfMeasure() == null)
                    errMap.put("productDetails["+i+"].unitOfMeasure","NotNull.invoiceCommand.product.unitOfMeasure.unitOfMeasure");
                if (prod.getParty() == null || prod.getParty().getId() == null || prod.getParty().getId().compareTo(0L) < 0) {
                    errMap.put("productDetails["+i+"].party","NotNull.invoiceCommand.product.party");
                }
            }

            if (errMap.isEmpty()) {
                //check for duplicate chNo
                List<Long> uniqChNo = new ArrayList<>();
                for (int i = 0; i < productDetails.size(); i++) {
                    if (productDetails.get(i).getChNo() != null) {
                        if (!uniqChNo.contains(productDetails.get(i).getChNo())) {
                            List<ProductDetail> byChNo = productDetailService.findByChNo(productDetails.get(i).getChNo());
                            if (!byChNo.isEmpty() && (productDetails.get(i).getId() == null
                                    || byChNo.get(0).getId().compareTo(productDetails.get(i).getId()) != 0)) {
                                if (byChNo.get(0).getInvoice() != null)
                                    result.rejectValue("productDetails[" + i + "].chNo", "alreadyExist.invoiceCommand.product.chNo",
                                        new Object[]{byChNo.get(0).getInvoice().getInvoiceNo()}, "Challan No Already Used");
                                else
                                    errMap.put("productDetails[" + i + "].chNo","alreadyAdded.invoiceCommand.product.chNo");
                            } else
                                uniqChNo.add(productDetails.get(i).getChNo());
                        } else {
                            int chNoIndex = uniqChNo.indexOf(productDetails.get(i).getChNo());
                            errMap.put("productDetails["+chNoIndex+"].chNo","duplicate.invoiceCommand.product.chNo");
                        }
                    }
                }
            }
        }

        errMap.forEach(result::rejectValue);
        log.info("doValidation() Exit[{}]",logSuffix);
    }

    @Override
    protected void doPreSaveOperation(ProductDetailCommand productDetailCommand, BindingResult result) {

    }

    @Override
    public void prePopulateOptionsAndFields(ProductDetailCommand productDetailCommand, Object model) throws InvalidObjectPopulationException {
        String logPrefix = "populateOptionsAndFields() |";
        log.info("{} Entry", logPrefix);
        if(!(model instanceof Model)) {
            throw new InvalidObjectPopulationException(productDetailCommand,model);
        }
        Model models = (Model) model;
        models.addAttribute("unitOfMeasures", productDetailService.findAllUnit());
        log.info("{} Exit", logPrefix);
    }
}
