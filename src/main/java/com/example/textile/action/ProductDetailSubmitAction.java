package com.example.textile.action;

import com.example.textile.command.ProductDetailCommand;
import com.example.textile.entity.ProductDetail;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.repo.ProductDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProductDetailSubmitAction extends ActionExecutor<ProductDetailCommand> {

    private final ProductDetailRepository productDetailRepo;

    public ProductDetailSubmitAction(ProductDetailRepository productDetailRepository) {
        this.productDetailRepo = productDetailRepository;
    }

    @Override
    protected ActionResponse onSuccess(ProductDetailCommand productDetailCommand, Map<String, Object> parameterMap, ModelMap model) {
        log.info("onSuccess() Entry");

        List<ProductDetail> productDetails = productDetailRepo.saveAll(productDetailCommand.getProductDetails());
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
        if (productDetails == null || !productDetails.isEmpty()) {
            errMap.put("productDetailsCommand","NotNull.productDetailsCommand");
        } else {
            for (int i = 0; i < productDetails.size(); i++) {
                ProductDetail prod = productDetails.get(i);
                if (prod.getProduct() == null) {
                    errMap.put("product.product","NotNull.invoiceCommand.product.product");
                } else {

                    if (prod.getProduct().getName() == null)
                        errMap.put("product["+i+"].product.name","NotNull.invoiceCommand.product.product.name");
                    if (prod.getProduct().getHsn() == null)
                        errMap.put("product["+i+"].product.hsn","NotNull.invoiceCommand.product.hsn");
                }
                if (prod.getChNo() == null)
                    errMap.put("product["+i+"].chNo","NotNull.invoiceCommand.product.chNo");
                if (prod.getQuantity() == null || prod.getQuantity() <= 0)
                    errMap.put("product["+i+"].quantity","NotNull.invoiceCommand.product.quantity");
                if (prod.getRate() == null || prod.getRate() <= 0)
                    errMap.put("product["+i+"].rate","NotNull.invoiceCommand.product.rate");
                if (prod.getTotalPrice() == null || prod.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0)
                    errMap.put("product["+i+"].totalPrice","NotNull.invoiceCommand.product.totalPrice");
                if (prod.getUnitOfMeasure() == null || prod.getUnitOfMeasure().getUnitOfMeasure() == null)
                    errMap.put("product["+i+"].unitOfMeasure","NotNull.invoiceCommand.product.unitOfMeasure.unitOfMeasure");
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

    }
}
