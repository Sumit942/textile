import { useForm, useFieldArray } from "react-hook-form";
import React, { useEffect, useState } from "react";
import { getCompanyList } from "../service/company";
import { saveOrder } from "../service/orderApi";

const OrderFormVal = () => {
  const {
    control,
    handleSubmit,
    register,
    formState: { errors },
    setValue,
    watch,
    clearErrors,
  } = useForm({
    defaultValues: {
      company: {
        id: null,
        name: ""
      },
      orderStatusType: "",
      remarks: "",
      version: 0,
      companyYarnOrders: [
        {
          yarnOrderItems: [],
          yarnBuilties: [
            {
              id: null,
              version: 0,
              receivedDt: "",
              loadUnloadCharges: 0,
              boxes: 0,
              tranportCompany: {
                id: 0,
                name: "",
              },
              vehicleNo: "",
              quantity: 0,
            },
          ],
          orderDt: "",
          yarnInvoiceNo: "",
          remark: "",
          totalQuantity: 0,
          totalAmount: 0,
          version: 0,
        },
      ],
    },
  });

  const {
    fields: orderFields,
    append: appendOrder,
    remove: removeOrder,
  } = useFieldArray({
    control,
    name: "companyYarnOrders",
  });

  const onSubmit = async (data) => {
    const response = await saveOrder(data);
    if (response && response.status === 201) {
      alert("Order saved successfully");
    } else {
      if (response && response.status === 400) {
        alert("Error saving order: " + response.data.message);
      } else {
        alert("Error saving order", response);
      }
    }
  };

  const [companyOptions, setCompanyOptions] = useState([]);
  const [selectedCompany, setSelectedCompany] = useState(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const [highlightedIndex, setHighlightedIndex] = useState(-1);

  const fetchCompanies = async (query) => {
    try {
      const response = await getCompanyList(query);
      setCompanyOptions(response.data);
      setShowDropdown(true);
    } catch (error) {
      console.error("Error fetching companies:", error);
    }
  };
  
  const handleCompanyChange = (event) => {
    const query = event.target.value;
    setSelectedCompany(null);
    setValue("company.name", query);
    if (query.length >= 3) {
      fetchCompanies(query);
    } else {
      setCompanyOptions([]);
      setShowDropdown(false);
    }
  };

  const handleCompanySelect = (company) => {
    setSelectedCompany(company);
    setValue("company.name", company.name);
    setValue("company.id", company.id);
    clearErrors("company.name");
    clearErrors("company.id");
    setShowDropdown(false);
  };

  const handleKeyDown = (event) => {
    if (event.key === "ArrowDown") {
      setHighlightedIndex((prevIndex) => Math.min(prevIndex + 1, companyOptions.length - 1));
    } else if (event.key === "ArrowUp") {
      setHighlightedIndex((prevIndex) => Math.max(prevIndex - 1, 0));
    } else if (event.key === "Enter" && highlightedIndex >= 0) {
      event.preventDefault();
      handleCompanySelect(companyOptions[highlightedIndex]);
    }
  };

  useEffect(() => {
    if (highlightedIndex >= 0 && highlightedIndex < companyOptions.length) {
      const selectedOption = document.getElementById(`company-option-${highlightedIndex}`);
      if (selectedOption) {
        selectedOption.scrollIntoView({ block: "nearest" });
      }
    }
  }, [highlightedIndex]);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className='mx-auto mt-16 max-w-xl sm:mt-20'>
      <div>
        <input
          type="hidden"
          {...register("company.id", { required: "Please search and select company" })}
          value={selectedCompany ? selectedCompany.id : ""}
        />
        <input
          {...register("company.name", { required: "Company name is required" })}
          placeholder="Enter Company Name"
          className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${errors.company?.name || errors.company?.id ? 'outline-red-300' : 'outline-gray-300'} placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
          onChange={handleCompanyChange}
          onKeyDown={handleKeyDown}
          onBlur={() => setShowDropdown(false)}
          value={selectedCompany ? selectedCompany.name : watch("company.name")}
          autoComplete="off"
        />
        {showDropdown && companyOptions.length > 0 && (
          <ul className="border border-gray-300 rounded-md mt-2 max-h-60 overflow-y-auto">
            {companyOptions.map((company, index) => (
              <li
                key={company.id}
                id={`company-option-${index}`}
                className={`px-3 py-2 cursor-pointer hover:bg-gray-200 ${highlightedIndex === index ? 'bg-gray-200' : ''}`}
                onClick={() => handleCompanySelect(company)}
              >
                {company.name} - <b><i>{company.gst}</i></b>
              </li>
            ))}
          </ul>
        )}
        {errors.company?.id && <p className="text-red-500 text-sm mt-1">{errors.company.id.message}</p>}
        {errors.company?.name && <p className="text-red-500 text-sm mt-1">{errors.company.name.message}</p>}

        <select
          {...register("orderStatusType", {
            required: "Order status type is required",
          })}
          placeholder="Order Status Type"
          className={`col-start-1 row-start-1 w-full rounded-md px-3.5 py-2 text-base text-gray-500 placeholder:text-gray-400 outline outline-1 -outline-offset-1 ${ errors.orderStatusType ? 'outline-red-300' : 'outline-gray-300' } focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6`}
          // className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${errors.orderStatusType ? 'outline-red-300' : 'outline-gray-300'} placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
        >
          <option value="" disabled>Select Order Status</option>
          <option value="CREATED">Created</option>
          <option value="RECEIVED">Received</option>
          <option value="IN_PROCESS">In Process</option>
          <option value="HALF_PROCESSED">Half Processed</option>
          <option value="PROCESSED">Processed</option>
          <option value="HALF_DELIVERED">Half Delivered</option>
          <option value="PENDING">Pending</option>
          <option value="DELIVERED">Delivered</option>
          <option value="HOLD">Hold</option>
          <option value="CANCELLED">Cancelled</option>
        </select>
        {errors.orderStatusType && <p className="text-red-500 text-sm mt-1">{errors.orderStatusType.message}</p>}

        <input
          {...register("remarks", { required: "Remarks are required" })}
          placeholder="Remarks"
          className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${errors.remarks ? 'outline-red-300' : 'outline-gray-300'} placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
        />
        {errors.remarks && <p className="text-red-500 text-sm mt-1">{errors.remarks.message}</p>}

        <input
          type="hidden"
          {...register("version", { required: false })}
          placeholder="Version"
        />
      </div>

      {orderFields.map((order, orderIndex) => (
        <OrderFormCompanyYarnOrder key={order.id} orderIndex={orderIndex} order={order} register={register} removeOrder={removeOrder} errors={errors} control={control} />
      ))}

      <button
        type="button"
        onClick={() =>
          appendOrder({
            yarnOrderItems: [],
            yarnBuilties: [
              {
                id: 0,
                version: 0,
                receivedDt: "",
                loadUnloadCharges: 0,
                boxes: 0,
                tranportCompany: "",
                vehicleNo: "",
                quantity: 0,
              },
            ],
            orderDt: "",
            yarnInvoiceNo: "",
            remark: "",
            totalQuantity: 0,
            totalAmount: 0,
            version: 0,
          })
        }
        className="mb-3 w-full rounded-md bg-gray-50 px-3.5 py-3.5 text-center text-sm font-semibold text-indigo-500 shadow-sm hover:bg-gray-200 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-600"
      >
        Add Order
      </button>
      <button type="submit"
        className='mt-5 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500'
      >Submit</button>
    </form>
  );
};
export default OrderFormVal;

const OrderFormCompanyYarnOrder = ({ orderIndex, register, errors, removeOrder, control }) => {
  const { fields: builtieFields, append: appendBuiltie, remove: removeBuiltie } = useFieldArray({
    control,
    name: `companyYarnOrders.${orderIndex}.yarnBuilties`,
  });
  return (
    <div>
          <h3>Order {orderIndex + 1}</h3>
          <input
            {...register(`companyYarnOrders.${orderIndex}.orderDt`, {
              required: "Order date is required",
            })}
            placeholder="Order Date"
            className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.orderDt ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
          />

          {errors.companyYarnOrders?.[orderIndex]?.orderDt && (
            <p className="text-red-500 text-sm mt-1">{errors.companyYarnOrders[orderIndex].orderDt.message}</p>
          )}

          <input
            {...register(`companyYarnOrders.${orderIndex}.yarnInvoiceNo`, {
              required: "Invoice number is required",
            })}
            placeholder="Yarn Invoice No"
            className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.yarnInvoiceNo ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
          />

          {errors.companyYarnOrders?.[orderIndex]?.yarnInvoiceNo && (
            <p className="text-red-500 text-sm mt-1">{errors.companyYarnOrders[orderIndex].yarnInvoiceNo.message}</p>
          )}

          <input
            type="number"
            {...register(`companyYarnOrders.${orderIndex}.totalQuantity`, {
              required: "Total quantity is required",
            })}
            placeholder="Total Quantity"
            className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.totalQuantity ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
          />

          {errors.companyYarnOrders?.[orderIndex]?.totalQuantity && (
            <p className="text-red-500 text-sm mt-1">{errors.companyYarnOrders[orderIndex].totalQuantity.message}</p>
          )}

          <input
            type="number"
            {...register(`companyYarnOrders.${orderIndex}.totalAmount`, {
              required: "Total amount is required",
            })}
            placeholder="Total Amount"
            className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.totalAmount ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
          />

          {errors.companyYarnOrders?.[orderIndex]?.totalAmount && (
            <p className="text-red-500 text-sm mt-1">{errors.companyYarnOrders[orderIndex].totalAmount.message}</p>
          )}

          <button 
            className="mb-3 w-full rounded-md bg-gray-50 px-3.5 py-3.5 text-center text-sm font-semibold text-red-500 shadow-sm hover:bg-gray-200 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-600"
            type="button" onClick={() => removeOrder(orderIndex)}>
            Remove Order
          </button>
          <h4>Yarn Builties</h4>
          {builtieFields.map((builtie, builtieIndex) => (
            <div key={builtie.id}>
              <input
                {...register(
                  `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.receivedDt`,
                  { required: "Received date is required" }
                )}
                placeholder="Received Date"
                className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[builtieIndex]?.receivedDt ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
              />

              {errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[
                builtieIndex
              ]?.receivedDt && (
                <p className="text-red-500 text-sm mt-1">
                  {
                    errors.companyYarnOrders[orderIndex].yarnBuilties[
                      builtieIndex
                    ].receivedDt.message
                  }
                </p>
              )}

              <input
                type="number"
                {...register(
                  `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.loadUnloadCharges`,
                  { required: "Load/Unload charges are required" }
                )}
                placeholder="Load/Unload Charges"
                className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[builtieIndex]?.loadUnloadCharges ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
              />

              {errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[
                builtieIndex
              ]?.loadUnloadCharges && (
                <p className="text-red-500 text-sm mt-1">
                  {
                    errors.companyYarnOrders[orderIndex].yarnBuilties[
                      builtieIndex
                    ].loadUnloadCharges.message
                  }
                </p>
              )}

              <input
                type="number"
                {...register(
                  `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.boxes`,
                  { required: "Boxes are required" }
                )}
                placeholder="Boxes"
                className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[builtieIndex]?.boxes ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
              />

              {errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[
                builtieIndex
              ]?.boxes && (
                <p className="text-red-500 text-sm mt-1">
                  {
                    errors.companyYarnOrders[orderIndex].yarnBuilties[
                      builtieIndex
                    ].boxes.message
                  }
                </p>
              )}

              <input
                {...register(
                  `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.tranportCompany.name`
                )}
                placeholder="Transport Company"
                className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[builtieIndex]?.tranportCompany ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
              />
              {errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.tranportCompany && (
                <p className="text-red-500 text-sm mt-1">{errors.companyYarnOrders[orderIndex].yarnBuilties.tranportCompany.message}</p>
              )}

              <input
                {...register(
                  `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.vehicleNo`,
                  { required: "Vehicle number is required" }
                )}
                placeholder="Vehicle No"
                className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[builtieIndex]?.vehicleNo ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
              />

              {errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[
                builtieIndex
              ]?.vehicleNo && (
                <p className="text-red-500 text-sm mt-1">
                  {
                    errors.companyYarnOrders[orderIndex].yarnBuilties[
                      builtieIndex
                    ].vehicleNo.message
                  }
                </p>
              )}

              <input
                type="number"
                {...register(
                  `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.quantity`,
                  { required: "Quantity is required" }
                )}
                placeholder="Quantity"
                className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[builtieIndex]?.quantity ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
              />

              {errors.companyYarnOrders?.[orderIndex]?.yarnBuilties?.[
                builtieIndex
              ]?.quantity && (
                <p className="text-red-500 text-sm mt-1">
                  {
                    errors.companyYarnOrders[orderIndex].yarnBuilties[
                      builtieIndex
                    ].quantity.message
                  }
                </p>
              )}

          <button
            type="button"
            onClick={() => removeBuiltie(builtieIndex)}
            className="mb-3 w-full rounded-md bg-gray-50 px-3.5 py-3.5 text-center text-sm font-semibold text-red-500 shadow-sm hover:bg-gray-200 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-600"
          >
            Remove Yarn Builtie
          </button>
            </div>
          ))}
          <button
            type="button"
            onClick={() =>
              appendBuiltie({
                id: 0,
                version: 0,
                receivedDt: "",
                loadUnloadCharges: 0,
                boxes: 0,
                tranportCompany: "",
                vehicleNo: "",
                quantity: 0,
              })
            }
            className="mb-3 w-full rounded-md bg-gray-50 px-3.5 py-3.5 text-center text-sm font-semibold text-indigo-500 shadow-sm hover:bg-gray-200 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-600"
          >
            Add Yarn Builtie
          </button>
        </div>
  )
}