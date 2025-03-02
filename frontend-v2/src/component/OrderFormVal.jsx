import { useForm, useFieldArray } from "react-hook-form";
import React, { useEffect, useState } from "react";
import { getCompanyList } from "../service/company";
import { fetchOrderByID, saveOrder } from "../service/orderApi";
import { useLocation, useNavigate } from "react-router-dom";

const OrderFormVal = () => {
  const location = useLocation();
  const data = location.state;
  const orderId = data?.id;
  const [isUpdate, setIsUpdate] = useState(false);
  const [isFetchingOrder, setIsFetchingOrder] = useState(false);
  const [companyOptions, setCompanyOptions] = useState([]);
  const [selectedCompany, setSelectedCompany] = useState(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const [highlightedIndex, setHighlightedIndex] = useState(-1);
  const navigation = useNavigate();

  useEffect(() => {
    console.log('data', data)
    if (orderId) {
      fetchOrder(data.id);
      setIsUpdate(true);
    }
    if (highlightedIndex >= 0 && highlightedIndex < companyOptions.length) {
      const selectedOption = document.getElementById(`company-option-${highlightedIndex}`);
      if (selectedOption) {
        selectedOption.scrollIntoView({ block: "nearest" });
      }
    }
  },[orderId, highlightedIndex]);

  const fetchOrder = async (orderId) => {
    setIsFetchingOrder(true);
    const response = await fetchOrderByID(orderId);
    console.log('response', response)
    if (response && response.status === 200) {
      setValue("id", response.data.id);
      setValue("version", response.data.version);
      setValue("company.id", response.data.company.id);
      setValue("company.name", response.data.company.name);
      setValue("orderStatusType", response.data.orderStatusType);
      setValue("remarks", response.data.remarks);
      setValue("companyYarnOrders", response.data.companyYarnOrders);
      setValue("orderNo", response.data.orderNo)
      setIsFetchingOrder(false);
    } else {
      alert('Error fetching order by id', orderId);
    }
  }


  const {
    control,
    handleSubmit,
    register,
    formState: { errors, isValid, isSubmitting },
    setValue,
    watch,
    clearErrors,
    setError,
    reset,
  } = useForm();

  const {
    fields: orderFields,
    append: appendOrder,
    remove: removeOrder,
  } = useFieldArray({
    control,
    name: "companyYarnOrders",
  });

  const resetForm = () => {
    setIsUpdate(false);
    setSelectedCompany(null);
    reset();
  }

  const onSubmit = async (data) => {

    if (!isValid) {
      return;
    }
    try {
      const response = await saveOrder(data);
      if (response && response.status === 201) {
        navigation('/orders/view', {
          state: {
            id: response.data.id,
            message: `Order ${isUpdate ? 'Updated' : 'Saved'} successfully`,
          },
        });
      } else if (response && response.status === 400) {
        const apiErrors = response.response.data.errorMessages;
        for (const key in apiErrors) {
          setError(key, { type: "manual", message: apiErrors[key] });
        }
      } else {
        alert("Error saving order", response);
      }
    } catch (error) {
      console.error("Error saving order:", error);
      alert("Error saving order");
    }
  };

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
    setValue("company.id", "");
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
    setValue("company.code", company.code)
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

  if (isFetchingOrder) {
    return <div>Loading...</div>;
  }

  return (
    <div className="isolate bg-white px-6 py-5 sm:py-10 lg:px-8">
     <div className="mx-auto max-w-2xl text-center">
        <h2 className="text-balance text-3xl font-semibold tracking-tight text-gray-900 sm:text-5xl">
          {isUpdate ? `Update Order No :${watch('orderNo')}` : 'Save Order'}
        </h2>
     </div> 
    <form onSubmit={handleSubmit(onSubmit)} className='mx-auto mt-16 max-w-xl sm:mt-20'>
      <div className="grid grid-cols-1 gap-x-8 gap-y-6 sm:grid-cols-2">
        <input
          type="hidden"
          defaultValue={""}
          {...register("version", { required: false })}
        />
        <input
          type="hidden"
          {...register("id", { required: false })}
          defaultValue={""}
        />
        <div className="sm:col-span-1">
        <input
          type="hidden"
          {...register("company.id", { required: "Please search and select company" })}
          value={selectedCompany ? selectedCompany.id : watch("company.id") ? watch("company.id") : ""}
        />
        <input
          type="hidden"
          {...register("company.code", { required: "Please search and select company"})}
          value={selectedCompany?.code || ''}
        />
        <input
          {...register("company.name", { required: "Company name is required" })}
          placeholder="Enter Company Name"
          className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${errors.company?.name || errors.company?.id ? 'outline-red-300' : 'outline-gray-300'} placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
          onChange={handleCompanyChange}
          onKeyDown={handleKeyDown}
          onBlur={() => setShowDropdown(false)}
          value={selectedCompany ? selectedCompany.name : watch("company.name") ? watch("company.name") : ""}
          autoComplete="off"
        />
        {showDropdown && companyOptions.length > 0 && (
          <ul className="border border-gray-300 rounded-md mt-1 max-h-60 overflow-y-auto">
            {companyOptions.map((company, index) => (
              <li
                key={company.id}
                id={`company-option-${index}`}
                className={`px-3 py-2 cursor-pointer hover:bg-gray-200 ${highlightedIndex === index ? 'bg-gray-200' : ''}`}
                onClick={() => handleCompanySelect(company)}
              >
                {company.name} ({company.code}) - <b><i>{company.gst}</i></b>
              </li>
            ))}
          </ul>
        )}
        {errors.company?.id && <p className="text-red-500 text-sm mt-1">{errors.company.id.message}</p>}
        {errors.company?.code && <p className="text-red-500 text-sm mt-1">{errors.company.code.message}</p>}
        {errors.company?.name && <p className="text-red-500 text-sm mt-1">{errors.company.name.message}</p>}
        </div>

        <div className="sm:col-span-1">
          <div className="grid grid-cols-1">
          <select
            {...register("orderStatusType", {
              required: "Order status type is required",
            })}
            placeholder="Order Status Type"
            className={`col-start-1 row-start-1 w-full appearance-none rounded-md bg-white py-1.5 pl-3 pr-8 text-base text-gray-900 outline outline-1 -outline-offset-1 ${errors.orderStatusType ? 'outline-red-300' : 'outline-gray-300'} focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6`}            
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
          <svg
            className="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-gray-500 sm:size-4"
            viewBox="0 0 16 16"
            fill="currentColor"
            aria-hidden="true"
            data-slot="icon"
          >
            <path
              fillRule="evenodd"
              d="M4.22 6.22a.75.75 0 0 1 1.06 0L8 8.94l2.72-2.72a.75.75 0 1 1 1.06 1.06l-3.25 3.25a.75.75 0 0 1-1.06 0L4.22 7.28a.75.75 0 0 1 0-1.06Z"
              clipRule="evenodd"
            />
          </svg>
          </div>
          {errors.orderStatusType && <p className="text-red-500 text-sm mt-1">{errors.orderStatusType.message}</p>}
        </div>

        <div className="sm:col-span-2">
        <textarea
          {...register("remarks", { required: "Remarks are required" })}
          placeholder="Remarks"
          className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${errors.remarks ? 'outline-red-300' : 'outline-gray-300'} placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
        />
        {errors.remarks && <p className="text-red-500 text-sm mt-1">{errors.remarks.message}</p>}
        </div>

       
        <div className="sm:col-span-2 grid grid-cols-1">
          {orderFields.length > 0 && <h3 className="text-lg font-semibold mb-2">Company Yarn Orders</h3>}
          {orderFields.map((order, orderIndex) => (
            <OrderFormCompanyYarnOrder key={order.id} orderIndex={orderIndex} order={order} register={register} removeOrder={removeOrder} errors={errors} control={control} />
          ))}
        </div>
      </div>

      <div className="mt-2.5">
      <button
        type="button"
        onClick={() =>
          appendOrder({
            yarnOrderItems: [],
            yarnBuilties: [],
            orderDt: null,
            yarnInvoiceNo: null,
            remark: null,
            totalQuantity: null,
            totalAmount: null,
            version: null,
          })
        }
        className="mb-3 w-full rounded-md bg-gray-50 px-3.5 py-3.5 text-center text-sm font-semibold text-indigo-500 shadow-sm hover:bg-gray-200 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-600"
      >
        Add Order
      </button>
      <button type="submit"
        className="block w-full rounded-md bg-indigo-600 px-3.5 py-2.5 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
      >{isSubmitting ? (isUpdate ? 'Updating Order...' : 'Saving Order...') : (isUpdate ? 'Update Order' : 'Save Order')}</button>
      <button
        type="button"
        onClick={resetForm}
        className="mt-3 w-full rounded-md bg-red-600 px-3.5 py-2.5 text-center text-sm font-semibold text-white shadow-sm hover:bg-red-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-red-600"
      >
        Reset
      </button>
      </div>
    </form>
    </div>
  );
};
export default OrderFormVal;
//TODO: add remark field in OrderFormCompanyYarnOrder
const OrderFormCompanyYarnOrder = ({ orderIndex, register, errors, removeOrder, control }) => {
  const { fields: builtieFields, append: appendBuiltie, remove: removeBuiltie } = useFieldArray({
    control,
    name: `companyYarnOrders.${orderIndex}.yarnBuilties`,
  });
  return (
    <div className="mb-2 border p-4 rounded-md grid grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-2">
      <div>
      <input
        type="hidden"
        {...register(`companyYarnOrders.${orderIndex}.id`, { required: false })}
        defaultValue={""}
      />
      <input 
        type="hidden"
        {...register(`companyYarnOrders.${orderIndex}.version`, { required: false })}
        defaultValue={""}
      />
      <input
        {...register(`companyYarnOrders.${orderIndex}.orderDt`, {
          required: "Order date is required",
        })}
        type="date"
        placeholder="Order Date"
        className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.orderDt ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
      />
      {errors.companyYarnOrders?.[orderIndex]?.orderDt && (
        <p className="text-red-500 text-sm mt-1">{errors.companyYarnOrders[orderIndex].orderDt.message}</p>
      )}
    </div>
      <div>
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
      </div>

      <div>
      <input
        type="number"
        {...register(`companyYarnOrders.${orderIndex}.totalQuantity`, {
          required: "Total quantity is required",
        })}
        placeholder="Total Quantity"
        step="0.01"
        className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.companyYarnOrders?.[orderIndex]?.totalQuantity ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
      />
      {errors.companyYarnOrders?.[orderIndex]?.totalQuantity && (
        <p className="text-red-500 text-sm mt-1">{errors.companyYarnOrders[orderIndex].totalQuantity.message}</p>
      )}
      </div>

      <div>
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
      </div>

      <div className="sm:col-span-2 grid grid-cols-1">
        {builtieFields.length > 0 && <h3 className="text-lg font-semibold mb-1">Yarn Builties</h3>}
        {builtieFields.map((builtie, builtieIndex) => (
          <div key={builtie.id} className="border p-4 rounded-md grid grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-2 mb-2">
            <div>
            <input
              type="hidden"
              {...register(
                `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.id`,
                { required: false }
              )}
              defaultValue={""}
            />
            <input
              type="hidden"
              {...register(
                `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.version`,
                { required: false }
              )}
              defaultValue={""}
            />
            <input
              {...register(
                `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.receivedDt`,
                { required: "Received date is required" }
              )}
              type="date"
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
            </div>

            <div>
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
            </div>

            <div>
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
            </div>

            <div>
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
            </div>

            <div>
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
            </div>

            <div>
            <input
              type="number"
              {...register(
                `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.quantity`,
                { required: "Quantity is required" }
              )}
              placeholder="Quantity"
              step="0.01"
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
            </div>

        <button
          type="button"
          onClick={() => removeBuiltie(builtieIndex)}
          className="text-red-500 hover:underline"
        >
          Remove Yarn Builtie
        </button>
          </div>
        ))}
      </div>
      <button
        type="button"
        onClick={() =>
          appendBuiltie({
            id: null,
            version: null,
            receivedDt: null,
            loadUnloadCharges: null,
            boxes: null,
            tranportCompany: "",
            vehicleNo: "",
            quantity: null,
          })
        }
        className="sm:col-span-2 mb-3 w-full rounded-md bg-gray-50 px-3.5 py-3.5 text-center text-sm font-semibold text-indigo-500 shadow-sm hover:bg-gray-200 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-600"
      >
        Add Yarn Builtie
      </button>
      <button 
        className="sm:col-span-2 text-red-500 hover:underline"
        type="button" onClick={() => removeOrder(orderIndex)}>
        Remove Company Yarn Order
      </button>
    </div>
  )
}