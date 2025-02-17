import React from "react";
import { useForm, useFieldArray } from "react-hook-form";

const OrderFormVal = () => {
  const {
    control,
    handleSubmit,
    register,
    formState: { errors },
  } = useForm({
    defaultValues: {
      company: "",
      orderStatusType: "",
      remarks: "",
      version: 0,
      companyYarnOrders: [
        {
          yarnOrderItems: [],
          yarnBuilties: [
            {
              id: 0,
              updateDt: "",
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

  const onSubmit = (data) => {
    console.log(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className='mx-auto mt-16 max-w-xl sm:mt-20'>
      <div>
        <input
          {...register("company", { required: "Company is required" })}
          placeholder="Company"
          className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.company ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
        />
        {errors.company && <p className="text-red-500 text-sm mt-1">{errors.company.message}</p>}

        <input
          {...register("orderStatusType", {
            required: "Order status type is required",
          })}
          placeholder="Order Status Type"
          className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.orderStatusType ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
        />

        {errors.orderStatusType && <p className="text-red-500 text-sm mt-1">{errors.orderStatusType.message}</p>}

        <input
          {...register("remarks", { required: "Remarks are required" })}
          placeholder="Remarks"
          className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.remarks ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
        />
        {errors.remarks && <p className="text-red-500 text-sm mt-1">{errors.remarks.message}</p>}

        <input
          type="hidden"
          {...register("version", { required: false })}
          placeholder="Version"
        />
      </div>

      {orderFields.map((order, orderIndex) => (
        <div key={order.id}>
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
          {order.yarnBuilties.map((builtie, builtieIndex) => (
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
                  `companyYarnOrders.${orderIndex}.yarnBuilties.${builtieIndex}.tranportCompany`
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
            </div>
          ))}
        </div>
      ))}

      <button
        type="button"
        onClick={() =>
          appendOrder({
            yarnOrderItems: [],
            yarnBuilties: [
              {
                id: 0,
                updateDt: "",
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
