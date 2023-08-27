package com.example.textile.action;

import com.example.textile.command.ProductCommand;
import com.example.textile.entity.Product;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Slf4j
public class ProductSubmitAction extends ActionExecutor<ProductCommand> {

    private final ProductService productService;

    public ProductSubmitAction(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected ActionResponse onSuccess(ProductCommand command, Map<String, Object> parameterMap, ModelMap model) {
        String logPrefix = "onSuccess()";
        log.info("{} Entry", logPrefix);

        Product saved = productService.save(command.getProduct());

        command.setProduct(saved);

        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        log.info("{} Exit",logPrefix);
        return actionResponse;
    }

    @Override
    protected void doValidation(ProductCommand command, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {
        String logPrefix = "doValidation()";
        log.info("{} Entry", logPrefix);

        Product product = command.getProduct();

        if (product.getName() == null || product.getName().isEmpty())
            result.rejectValue("product.name","NotNull.invoiceCommand.product.product.name");
        if (product.getHsn() == null || product.getHsn()<=0)
            result.rejectValue("product.hsn","Please enter HSN code");
        if (product.getActive() == null) {
            result.rejectValue("product.active","productCommand.product.active");
        }

        log.info("{} Exit",logPrefix);
    }

    @Override
    protected void doPreSaveOperation(ProductCommand command, BindingResult result) {

    }

    @Override
    public void prePopulateOptionsAndFields(ProductCommand command, Object model) throws InvalidObjectPopulationException {

    }
}
