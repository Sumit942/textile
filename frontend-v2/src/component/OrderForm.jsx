import React, { useState } from 'react'
import { useForm } from 'react-hook-form';

const OrderForm = () => {

  const [orders, setOrders] = useState();
  const [editIndex, setEditIndex] = useState(null);

  const {
      register,
      handleSubmit,
      formState: { errors },
  } = useForm();

  const onSubmit = (data) => alert(JSON.stringify(data));

  return (
    <div className="isolate bg-white px-6 py-5 sm:py-10 lg:px-8">
    <div className="mx-auto max-w-2xl text-center">
      <h2 className="text-balance text-4xl font-semibold tracking-tight text-gray-900 sm:text-5xl">
        {editIndex ? 'Update' : 'Save'} Order
      </h2>
    </div>
    <form onSubmit={handleSubmit(onSubmit)} className='mx-auto mt-16 max-w-xl sm:mt-20'>
      <div className='sm:col-span-2'>
        <label htmlFor="company.name" className='block text-sm/6 font-semibold text-gray-900'>Party Name</label>
        <div className="mt-2.5">
          <input
            type="text"
            id="company.id"
            {...register('company.id', { required: true })}
            className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.company?.id ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
            />
        <input type="hidden" {...register('company.id')} value={orders.company?.id}/>
        {errors.company?.id && (
          <p className="text-red-500 text-sm mt-1">{errors.company.id}</p>
        )}
        </div>
      </div>

      <div className='sm:col-span-2 mt-2.5'>
      <label htmlFor="orderStatusType" className='block text-sm font-medium text-gray-700'>Order Status</label>
      <div className='mt-2.5 grid shrink-0 grid-cols-1 focus-within:relative'>
        <select
          type="text"
          id="orderStatusType"
          {...register('orderStatusType', { required: true })}
          className={`col-start-1 row-start-1 w-full appearance-none rounded-md py-1.5 pl-3 pr-7 text-base text-gray-500 placeholder:text-gray-400 outline outline-1 -outline-offset-1 ${ errors.orderStatusType ? 'outline-red-300' : 'outline-gray-300' } focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6`}
        >
          <option value="">Select Order Status</option>
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
        <svg className="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-gray-500 sm:size-4" viewBox="0 0 16 16" fill="currentColor" aria-hidden="true" data-slot="icon">
          <path fillRule="evenodd" d="M4.22 6.22a.75.75 0 0 1 1.06 0L8 8.94l2.72-2.72a.75.75 0 1 1 1.06 1.06l-3.25 3.25a.75.75 0 0 1-1.06 0L4.22 7.28a.75.75 0 0 1 0-1.06Z" clipRule="evenodd" />
        </svg>
      </div>
        {errors.orderStatusType && (
          <p className="text-red-500 text-sm mt-1">{errors.orderStatusType}</p>
        )}
      </div>

      <div className='sm:col-span-2 mt-2.5'>
        <label htmlFor="remark" className='block text-sm font-medium text-gray-700'>Remark</label>
        <textarea
          id="remark"
          {...register('remark')}
          className='mt-1 block w-full px-3 py-2 border border-gray-300 shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md'
        />
      </div>
      <input type="hidden" {...register('id')} value={orders.id}/>
      <input type="hidden" {...register('version')} value={orders.version} />
      <button
        type="submit"
        className='mt-5 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500'
      >
        Save Order
      </button>
    </form>
    </div>
  )
}

export default OrderForm;