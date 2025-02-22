import React from 'react';
import { useForm, useFieldArray } from 'react-hook-form';
import { Button, TextField, IconButton } from '@mui/material';
import { Add, Remove } from '@mui/icons-material';

const OrderItem = ({ register, index, remove }) => (
    <div className="mb-2 border p-4 rounded-md grid grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-2">
        <TextField {...register(`order_items.${index}.item_name`)} label="Item Name" variant="outlined" />
        <TextField {...register(`order_items.${index}.hsn_code`)} label="HSN Code" variant="outlined" />
        <TextField {...register(`order_items.${index}.lot_no`)} label="Lot No" variant="outlined" />
        <TextField {...register(`order_items.${index}.quantity`)} label="Quantity" variant="outlined" />
        <TextField {...register(`order_items.${index}.rate`)} label="Rate" variant="outlined" />
        <TextField {...register(`order_items.${index}.discount`)} label="Discount" variant="outlined" />
        <TextField {...register(`order_items.${index}.boxes`)} label="Boxes" variant="outlined" />
        <TextField {...register(`order_items.${index}.amount`)} label="Amount" variant="outlined" />
        <IconButton onClick={() => remove(index)}><Remove /></IconButton>
    </div>
);

const CompanyYarnOrder = () => {
    const methods = useForm();
    const { control, register, handleSubmit } = methods;
    const { fields, append, remove } = useFieldArray({
        control,
        name: 'order_items'
    });

    const onSubmit = (data) => {
        console.log(data);
    };

    return (
        <div className="isolate bg-white px-6 py-5 sm:py-10 lg:px-8">
        <div className="mx-auto max-w-2xl text-center">
            <h2 className="text-balance text-4xl font-semibold tracking-tight text-gray-900 sm:text-5xl">
            Save Company Yarn Order
            </h2>
        </div> 
            <form onSubmit={handleSubmit(onSubmit)} className="mx-auto mt-16 max-w-xl sm:mt-20 space-y-4">
            <div className="grid grid-cols-1 gap-x-8 gap-y-6 sm:grid-cols-2">
                <TextField {...register('order_date')} label="Order Date" variant="outlined" type='date' 
                    slotProps={{ 
                        inputLabel: { 
                            shrink: true ,
                        } 
                    }}
                />
                <TextField {...register('order_no')} label="Order No" variant="outlined" />
                <TextField {...register('total_quantity')} label="Total Quantity" variant="outlined" />
                <TextField {...register('total_amount')} label="Total Amount" variant="outlined" />
                <TextField {...register('c_gst')} label="C GST" variant="outlined" />
                <TextField {...register('s_gst')} label="S GST" variant="outlined" />
                <TextField {...register('i_gst')} label="I GST" variant="outlined" />

                <div className="sm:col-span-2 grid grid-cols-1 space-y-4">
                    {fields.map((item, index) => (
                        <OrderItem key={item.id} control={control} register={register} index={index} remove={remove} />
                    ))}
                </div>
                </div>
                <div className="mt-4 space-x-4">
                    <Button variant="contained" color="primary" onClick={() => append({})} startIcon={<Add />}>
                        Add Item
                    </Button>

                    <Button type="submit" variant="contained" color="secondary">
                        Save Order
                    </Button>
                </div>
            </form>
        </div>
    );
};

export default CompanyYarnOrder;